package views;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.rey.material.app.Dialog;
import com.tnt.ibazaar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import network.serverinterface.ServerInterface;
import tools.CallBack;
import tools.ProgressGenerator;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by Omid on 5/14/2018.
 */

public class LoadMapDialog {
    private static View view;
    private static Dialog dialog;
    private static ProgressGenerator progressGenerator;
    private static ActionProcessButton btnAddress;
    private static AppCompatActivity mAct;

    public static void loadMap(
            final AppCompatActivity mAct, final CallBack callBack, LatLng latLng) {
        if (mAct == null)
            return;
        if (mAct != LoadMapDialog.mAct || dialog == null || view == null) {
            dialog = null;
            view = null;
            System.gc();
            LoadMapDialog.mAct = mAct;
            dialog = new Dialog(mAct);
            view = mAct.getLayoutInflater().inflate(R.layout.dialog_map, null);

            progressGenerator = new ProgressGenerator(new ProgressGenerator.OnCompleteListener() {
                @Override
                public void onComplete() {
                }
            });
            btnAddress = (ActionProcessButton) view.findViewById(R.id.btnAddress);
            SupportMapFragment mapFragment = (SupportMapFragment)
                    mAct.getSupportFragmentManager()
                            .findFragmentById(R.id.map);
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap mMap) {

                    View pin = view.findViewById(R.id.view);
                    pin.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TCPSocketInterface.nearest Shops to selected position
//                    showDialog();
//                    TCPSocketInterface.getShops(mAct, category, last_shop_id
//                            , latLng, distance);
                            try {
                                callBack.callback(mMap.getCameraPosition().target);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });

                    mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
                        @Override
                        public void onCameraMove() {
                            progressGenerator.startInfinite(btnAddress);
                        }
                    });
                    mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                        @Override
                        public void onCameraIdle() {
                            new GetAddress(
                                    mMap.getCameraPosition().target.latitude,
                                    mMap.getCameraPosition().target.longitude,
                                    btnAddress,
                                    progressGenerator, mAct).execute();
                        }
                    });

                    mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                        private Location getLastKnownLocation() {
                            LocationManager mLocationManager =
                                    (LocationManager) mAct.getSystemService(LOCATION_SERVICE);
                            List<String> providers = mLocationManager.getProviders(true);
                            Location bestLocation = null;
                            for (String provider : providers) {
                                Location l = null;
                                if (checkPermissions())
                                    l = mLocationManager.getLastKnownLocation(provider);

                                if (l == null) {
                                    continue;
                                }
                                if (bestLocation == null
                                        || l.getAccuracy() < bestLocation.getAccuracy()) {

                                    bestLocation = l;
                                }
                            }
                            if (bestLocation == null) {
                                return null;
                            }

//                            mEditor.putString("lat", bestLocation.getLatitude() + "");
//                            mEditor.putString("lng", bestLocation.getLongitude() + "");
//                            mEditor.commit();

                            return bestLocation;
                        }

                        private boolean checkGPS() {
                            LocationManager lm = (LocationManager) mAct.getSystemService(LOCATION_SERVICE);
                            boolean gps_enabled = false;
                            try {
                                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            } catch (Exception ex) {
                                gps_enabled = false;
                            }


                            if (!gps_enabled) {
                                // notify user
                                AlertDialog.Builder dialog = new AlertDialog.Builder(mAct);
                                dialog.setMessage(mAct.getResources().getString(R.string.gps_not_enabled));
                                dialog.setPositiveButton(mAct.getResources().getString(R.string.settings), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        // TODO Auto-generated method stub
                                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                        mAct.startActivity(myIntent);
                                        //get gps
                                    }
                                });
                                dialog.setNegativeButton(mAct.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                                        // TODO Auto-generated method stub

                                    }
                                });
                                dialog.show();

                            }
                            return gps_enabled;
                        }

                        Location mLastLocation;
                        private boolean use_gps = false;

                        @Override
                        public void onMapLoaded() {
                            {
                                Location myLocation;
                                myLocation = mMap.getMyLocation();
                                if (myLocation == null) {
                                    Location l = getLastKnownLocation();

                                    if (l == null && mLastLocation == null) {

                                        if (checkGPS() && !use_gps) {
                                            use_gps = true;
                                            onMapLoaded();
                                        } else {
                                            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mAct);
//                                Toast.makeText(getContext(), "Unknown location", Toast.LENGTH_SHORT).show();
                                            double lat = Double.parseDouble(prefs.getString("lat", "32.654627"));
                                            double lng = Double.parseDouble(prefs.getString("lng", "51.667983"));
                                            LatLng latLng = new LatLng(lat, lng);
                                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                                        }
                                        return;
                                    }
//                        init_map = false;
                                    if (l == null)
                                        myLocation = mLastLocation;
                                    else
                                        myLocation = l;
                                }
//                     Get latitude of the current location
                                double latitude = myLocation.getLatitude();

                                // Get longitude of the current location
                                double longitude = myLocation.getLongitude();

                                // Create a LatLng object for the current location
                                LatLng latLng = new LatLng(latitude, longitude);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));

                            }

                        }
                    });
                    mMap.getUiSettings().setCompassEnabled(true);
                    mMap.getUiSettings().setMyLocationButtonEnabled(true);
//                    mMap.getUiSettings().setZoomControlsEnabled(true);
                    if (checkPermissions())
                        mMap.setMyLocationEnabled(true);
                }
            });
            View mapView = mapFragment.getView();
            if (mapView != null) {
//            Log.e("relocate", "" + height);
//            mapOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {

//                     Get the button view
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1"))
                        .getParent())
                        .findViewById(Integer.parseInt("2"));

                LayoutParams layoutParams = (LayoutParams)
                        locationButton.getLayoutParams();

                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

                layoutParams.setMargins(0, 0, 10, 30);

//                    if (Build.VERSION.SDK_INT < 16)
//                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(mapOnGlobalLayoutListener);
//                    else
//                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(mapOnGlobalLayoutListener);
//
//                }
//
//            };
//            mapView.getViewTreeObserver().addOnGlobalLayoutListener(mapOnGlobalLayoutListener);
            }
            dialog.setContentView(view);
        }
        dialog.show();
    }

    private static boolean checkPermissions() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ArrayList<String> neededPermissions = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(mAct,
                    perm)
                    != PackageManager.PERMISSION_GRANTED)
                neededPermissions.add(perm);
        }
        return neededPermissions.size() == 0;

    }

    private static class GetAddress extends AsyncTask<Void, Void, Void> {
        private double lat, lng;
        private String result;
        private ActionProcessButton btnAddress;
        private ProgressGenerator progressGenerator;
        private Activity mAct;

        public GetAddress(double lat, double lng,
                          ActionProcessButton btnAddress,
                          ProgressGenerator progressGenerator, Activity mAct) {
            this.lat = lat;
            this.lng = lng;
            this.btnAddress = btnAddress;
            btnAddress.setMode(ActionProcessButton.Mode.ENDLESS);
            this.progressGenerator = progressGenerator;
            this.mAct = mAct;
        }

        @Override
        protected Void doInBackground(Void... params) {
            result = ServerInterface.getAddress(mAct, lat, lng);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (result != null)
                btnAddress.setText(result + "");
            progressGenerator.finish(btnAddress);
        }
    }

}
