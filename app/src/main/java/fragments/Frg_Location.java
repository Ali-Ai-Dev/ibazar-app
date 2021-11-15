package fragments;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tnt.ibazaar.Act_Main;
import com.tnt.ibazaar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import database.DataBase;
import models.Category;
import models.ServerResponse;
import network.serverinterface.ServerInterface;
import network.webconnection.WebConnection;
import sidebar.CenterZoomLayoutManager;
import sidebar.MyRecyclerView;
import sidebar.SidebarAdapter;
import tools.BTree;
import tools.ProgressGenerator;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_Location extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    SharedPreferences prefs;
    SharedPreferences.Editor mEditor;
    BroadcastReceiver disconnect = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getBooleanExtra("connected", false)) {
//                if (dialog != null && dialog.isShowing()) {
//                    dialog.dismiss();
                Toast.makeText(getContext(), R.string.error_connecting_to_server, Toast.LENGTH_SHORT).show();
//                }
            }
        }
    };
    //    MapView mapView;
    private GoogleMap mMap;
    private boolean location_selected = false;
    private Location mLocation;
    private ActionProcessButton btnAddress;
    private ProgressGenerator progressGenerator;
    //    private RecyclerView list_shop_cat_1;
//    private WheelView wheelView;
    private MyRecyclerView sidebar;
    private CenterZoomLayoutManager layoutManager;
    //    private CardView cat_level1;
    private int card_width;
    private int sidebar_height;
    private int spaceViewHeight;
    private int index = -1;
    private ArrayList<Marker> markers;
    private BTree nodes;
    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Do what you need in here
            if (intent.hasExtra("responseLocation")) {
                String data = intent.getStringExtra("responseLocation");
                Log.e("responseLocation", data + " ");
                try {
                    if (mMap == null || data == null)
                        return;

//                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = new JSONArray(data);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (!nodes.add(new BTree(object.optInt("ID_Shp")))) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(object.getDouble("Lat_Shp"),
                                    object.getDouble("Lng_Shp")))
                                    .zIndex(object.optInt("ID_Shp"))
                                    .title(object.getString("Title_Shp"));

                            Marker marker = mMap.addMarker(markerOptions);
                            markers.add(marker);
                        }
                    }

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            if (index == marker.getZIndex()) {
                                Act_Main mAct = (Act_Main) getActivity();
                                mAct.goToShop((int) marker.getZIndex(), marker.getTitle());
                            } else {
                                marker.showInfoWindow();
                                index = (int) marker.getZIndex();
                            }
                            return true;
                        }
                    });

                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Act_Main mAct = (Act_Main) getActivity();
                            mAct.goToShop((int) marker.getZIndex(), marker.getTitle());
                        }
                    });

                } catch (JSONException e) {
                    Log.e("error responseLocation", " " + e.getMessage());
                }
            }
        }

//        private boolean markerExists(int id) {
//            for (Marker marker : markers)
//                if (marker.getZIndex() == id)
//                    return true;
//
//            return false;
//        }

//        private ArrayList<Category> getCategoryChildren(String categoriesJsonString) {
//            try {
//                JSONArray jsonArray = new JSONArray(categoriesJsonString);
//                ArrayList<Category> categories = new ArrayList<>();
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
////                    if (parent_id != jsonObject.getInt("ParnetID"))
////                        continue;
//
//                    Category category = new Category();
//                    category.setId(jsonObject.getInt("ID"));
//                    category.setTitle(jsonObject.getString("Name"));
//                    category.setParent(jsonObject.getInt("ParnetID"));
//                    if (jsonObject.has("ImageUrl"))
//                        category.setImage_name(jsonObject.getString("ImageUrl"));
//                    categories.add(category);
//                }
//                return categories;
//            } catch (Exception e) {
//                Log.e("Exception", " " + e.getMessage());
//            }
//            return null;
//        }
    };
    private int category = 0;
    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    //    private ViewTreeObserver.OnGlobalLayoutListener mapOnGlobalLayoutListener;
    private boolean loading_location = false;
    private int b_offset = -1;
    private GoogleApiClient googleApiClient;

    public Frg_Location() {
        // Required empty public constructor
    }

    public static Frg_Location newInstance(int sidebar_height, int spaceViewHeight) {
        Bundle bundle = new Bundle();
        bundle.putInt("height", sidebar_height);
        bundle.putInt("spaceViewHeight", spaceViewHeight);
        Frg_Location frg_location = new Frg_Location();
        frg_location.setArguments(bundle);
        return frg_location;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            getActivity().unregisterReceiver(mReceiver);
            getActivity().unregisterReceiver(disconnect);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        markers = new ArrayList<>();
        nodes = new BTree(-1);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        mEditor = prefs.edit();
        final View view = inflater.inflate(R.layout.frg_by_location, container, false);
        turnGPSOn();
        sidebar = (MyRecyclerView) view.findViewById(R.id.list_sidebar);


        if (getArguments() != null) {
            sidebar_height = getArguments().getInt("height");
            spaceViewHeight = getArguments().getInt("spaceViewHeight");

            onGlobalLayoutListener =
                    new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {

//                            int height = sidebar.getHeight();
//                            if (height == 0)
//                                return;
//                            int h2 = 5 * sidebar_height;
//
                            if (Build.VERSION.SDK_INT >= 16)
                                sidebar.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
                            else
                                sidebar.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
//                            sidebar.getLayoutParams().height = h2;
//                            Log.e("h2", h2 + "");
                            card_width = sidebar.getWidth();
                            TranslateAnimation animate2 = new TranslateAnimation(
                                    0,                  // fromXDelta
                                    -card_width,                  // toXDelta
                                    0,                // fromYDelta
                                    0);                 // toYDelta
                            animate2.setDuration(0);
                            animate2.setFillAfter(true);
                            sidebar.startAnimation(animate2);
                            sidebar.setVisibility(View.GONE);
                        }
                    };
            sidebar.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

//        SnapHelper snapHelper = new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(sidebar);
        layoutManager = new CenterZoomLayoutManager(getContext());
        sidebar.setLayoutManager(layoutManager);


        SidebarAdapter adapter = new SidebarAdapter((Act_Main) getActivity(),
                DataBase.selectCategories(0));

        sidebar.setAdapter(adapter);

        sidebar.enableViewScaling(true);

        sidebar.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);

        View pin = view.findViewById(R.id.view);
        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sidebar.getVisibility() == View.VISIBLE)
                    return;
                if (mMap == null)
                    return;

                ((Act_Main) getActivity()).setSelected_location(
                        mMap.getCameraPosition().target);

                LatLng farRight = mMap.getProjection().getVisibleRegion().farRight;
                Log.e("distance", "" +
                        CalculationByDistance(farRight, mMap.getCameraPosition().target));
                getShopsOnMap(mMap.getCameraPosition().target,
                        CalculationByDistance(farRight, mMap.getCameraPosition().target),
                        category, (int) mMap.getCameraPosition().zoom);


            }
        });

        btnAddress = (ActionProcessButton) view.findViewById(R.id.btnAddress);
        btnAddress.setMode(ActionProcessButton.Mode.ENDLESS);
        progressGenerator = new ProgressGenerator(new ProgressGenerator.OnCompleteListener() {
            @Override
            public void onComplete() {
                loading_location = false;
            }
        });

        return view;
    }

    private void getShopsOnMap(LatLng target, double distance, int category, int zoom) {
        Uri.Builder builder = new Uri.Builder()
                .appendQueryParameter("lat", "" + target.latitude)
                .appendQueryParameter("lng", "" + target.longitude)
                .appendQueryParameter("distance", "" + distance)
                .appendQueryParameter("categoryId", "" + category)
                .appendQueryParameter("zoom", "" + zoom);
        new WebConnection(new WebConnection.ConnectionResponse() {
            @Override
            public void connectionFinish(String result, String urlCanConnect) {
                ServerResponse response = null;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    response = new ServerResponse();
                    response.setStatus(jsonObject.getInt("Status"));
                    response.setMessage(jsonObject.getString("MSG"));
                    if (response.getStatus() == 100) {
                        response.setData(jsonObject.getString("Data"));
                    } else
                        response.setData("");
                } catch (Exception e) {
                    Log.e("getShopsOnMap error", " " + e.getMessage());
                }

                if (response == null) {
                    Toast.makeText(getContext(), R.string.not_connected_check_internet, Toast.LENGTH_SHORT).show();
                    return;
                }

                String data = (String) response.getData();
                Log.e("responseLocation", data + " ");
                try {
                    if (mMap == null || data == null)
                        return;

//                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray jsonArray = new JSONArray(data);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        if (!nodes.add(new BTree(object.optInt("ID_Shp")))) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(object.getDouble("Lat_Shp"),
                                    object.getDouble("Lng_Shp")))
                                    .zIndex(object.optInt("ID_Shp"))
                                    .title(object.getString("Title_Shp"));

                            Marker marker = mMap.addMarker(markerOptions);
                            markers.add(marker);
                        }
                    }

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {

                            if (index == marker.getZIndex()) {
                                Act_Main mAct = (Act_Main) getActivity();
                                mAct.goToShop((int) marker.getZIndex(), marker.getTitle());
                            } else {
                                marker.showInfoWindow();
                                index = (int) marker.getZIndex();
                            }
                            return true;
                        }
                    });

                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Act_Main mAct = (Act_Main) getActivity();
                            mAct.goToShop((int) marker.getZIndex(), marker.getTitle());
                        }
                    });

                } catch (JSONException e) {
                    Log.e("error responseLocation", " " + e.getMessage());
                }
            }
        }).connect(builder, "fetchShopListWithLatAndLng", "GET",0);
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if (b_offset != -1) {
            relocateMyLocationButton(b_offset);
        }
    }

    public void showSideBar(boolean show) {
        if (show) {
            sidebar.setVisibility(View.VISIBLE);
//                wheelView.setVisibility(View.VISIBLE);
            TranslateAnimation animate2 = new TranslateAnimation(
                    -card_width,                  // fromXDelta
                    0,                  // toXDelta
                    0,                // fromYDelta
                    0);                 // toYDelta
            animate2.setDuration(500);
            animate2.setFillAfter(true);
            sidebar.startAnimation(animate2);
        } else {
            TranslateAnimation animate2 = new TranslateAnimation(
                    0,                  // fromXDelta
                    -card_width,                  // toXDelta
                    0,                // fromYDelta
                    0);                 // toYDelta
            animate2.setDuration(500);
            animate2.setFillAfter(true);
            sidebar.startAnimation(animate2);
            sidebar.setVisibility(View.GONE);
        }
    }

    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT < 23)
            return true;
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ArrayList<String> neededPermissions = new ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(),
                    perm)
                    != PackageManager.PERMISSION_GRANTED)
                neededPermissions.add(perm);
        }
        return neededPermissions.size() == 0;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                boolean b = ((Act_Main) getActivity()).isShowingTopAndBottom();
                Log.e("showTopAndBottomTest", "isShowing sidebar? " + !b);
                ((Act_Main) getActivity()).showTopAndBottom(!b);
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(32.6311928, 51.6746596), 5f));
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            Location mLastLocation;
            private boolean use_gps = false;

            private Location getLastKnownLocation() {
                LocationManager mLocationManager =
                        (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
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

                mEditor.putString("lat", bestLocation.getLatitude() + "");
                mEditor.putString("lng", bestLocation.getLongitude() + "");
                mEditor.commit();

                return bestLocation;
            }

            private boolean checkGPS() {
                LocationManager lm = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                boolean gps_enabled = false;
                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch (Exception ex) {
                    gps_enabled = false;
                }
//                if (!gps_enabled) {
//                    // notify user
//                    AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
//                    dialog.setMessage(getResources().getString(R.string.gps_not_enabled));
//                    dialog.setPositiveButton(getResources().getString(R.string.settings), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                            // TODO Auto-generated method stub
//                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                            startActivity(myIntent);
//                            //get gps
//                        }
//                    });
//                    dialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface paramDialogInterface, int paramInt) {
//                            // TODO Auto-generated method stub
//
//                        }
//                    });
//                    dialog.show();
//
//                }
                return gps_enabled;
            }

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
//                                Toast.makeText(getContext(), "Unknown location", Toast.LENGTH_SHORT).show();
                                double lat = Double.parseDouble(prefs.getString("lat", "32.6311928  "));
                                double lng = Double.parseDouble(prefs.getString("lng", "51.6746596"));
                                LatLng latLng = new LatLng(lat, lng);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
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
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5.0f));

                }

            }


        });
        mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                requestShops();
                new GetAddress(mMap.getCameraPosition().target.latitude,
                        mMap.getCameraPosition().target.longitude).execute();
            }
        });
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if (loading_location)
                    return;
                if (!location_selected) {
                    loading_location = true;
                    progressGenerator.startInfinite(btnAddress);
                }
            }
        });
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        if (checkPermissions())
            mMap.setMyLocationEnabled(true);
    }

    private void requestShops() {
        LatLng farRight = mMap.getProjection().getVisibleRegion().farRight;
        LatLng farLeft = mMap.getProjection().getVisibleRegion().farLeft;
//        mMap.addMarker(new MarkerOptions().position(farRight).alpha(0.3f));
//        mMap.addMarker(new MarkerOptions().position(farLeft).alpha(0.3f));
        double distance = CalculationByDistance(farRight, farLeft);
        distance /= 2;
        Log.e("distance", "" + distance);


        getShopsOnMap(mMap.getCameraPosition().target,
                distance, category, (int) mMap.getCameraPosition().zoom);

    }

    public double CalculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return Radius * c;
    }

    public int getFirstPosition() {
        return layoutManager.findFirstCompletelyVisibleItemPosition();
    }

    public int getLastPosition() {
        return layoutManager.findLastCompletelyVisibleItemPosition();
    }

    public void sidebarSmoothScrollToPosition(int pos) {
        if (pos < 0)
            return;
//        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(getContext()) {
//            @Override protected int getVerticalSnapPreference() {
//                return LinearSmoothScroller.SNAP_TO_START;
//            }
//        };
//        smoothScroller.setTargetPosition(pos);
//        layoutManager.startSmoothScroll(smoothScroller);
        sidebar.smoothScrollToPosition(pos);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void relocateMyLocationButton(final int height) {
        try {

            SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                    .findFragmentById(R.id.map);

            final View mapView = mapFragment.getView();
            if (mapView != null) {
//            Log.e("relocate", "" + height);
//            mapOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {

//                     Get the button view
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1"))
                        .getParent())
                        .findViewById(Integer.parseInt("2"));

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
                        locationButton.getLayoutParams();

                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

                layoutParams.setMargins(0, 0, 10, height);

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
        } catch (IllegalStateException e) {
            b_offset = height;
        } catch (Exception e) {
        }
    }

    public void setCategory(int id) {
        category = id;
        Log.e("category", "" + id);
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
        nodes = new BTree(-1);
        requestShops();
    }

    public void setCategoryByPosition(int position) {
        ArrayList<Category> categories = DataBase.selectCategories(0);
        position %= categories.size();
        category = categories.get(position).getId();
        Log.e("pos", "" + position + " , category : " + category);
        for (Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
        nodes = new BTree(-1);
        requestShops();
    }

    private void turnGPSOn() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getContext())
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied. The client can
                            // initialize location
                            // requests here.
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be
                            // fixed by showing the user
                            // a dialog.
                            try {
                                // Show the dialog by calling
                                // startResolutionForResult(),
                                // and check the result in onActivityResult().
                                status.startResolutionForResult(getActivity(), 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have
                            // no way to fix the
                            // settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    private class GetAddress extends AsyncTask<Void, Void, Void> {
        private double lat, lng;
        private String result;

        public GetAddress(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        @Override
        protected Void doInBackground(Void... params) {
            result = ServerInterface.getAddress(getContext(), lat, lng);
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
