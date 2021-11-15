package fragments;


import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.tnt.ibazaar.Act_Shop;
import com.tnt.ibazaar.R;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class Frg_Shop_Coordinates extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Act_Shop mAct;

    private int h, w;

    public Frg_Shop_Coordinates() {
        // Required empty public constructor
    }

    private SupportMapFragment mapFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAct = (Act_Shop) getActivity();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frg__map, container, false);

        mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        w = mapFragment.getView().getWidth();
        h = mapFragment.getView().getHeight();
        mapFragment.getView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mapFragment.getView() != null) {
                    h = mapFragment.getView().getHeight(); //height is ready
                    w = mapFragment.getView().getWidth();
                }
            }
        });
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMyLocationEnabled(true);

        // Add a marker in Sydney and move the camera
        final LatLng shop = mAct.getShopCoordinates();
        mMap.addMarker(new MarkerOptions().position(shop)
                .title(mAct.getShopInfo().getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shop, 12));

        mMap.setOnMyLocationButtonClickListener(
                new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        PolylineOptions options = new PolylineOptions();
                        options.add(shop);
                        final Location location = getLastKnownLocation();
                        options.add(new LatLng(location.getLatitude(),
                                location.getLongitude()));
                        mMap.addPolyline(options);
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude(),
                                        location.getLongitude()))
                                .title(getString(R.string.you_are_here))
                                .icon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_CYAN)));

                        LatLngBounds.Builder builder;
                        LatLngBounds bounds;
                        builder = LatLngBounds.builder()
                                .include(new LatLng(location.getLatitude(),
                                        location.getLongitude()));
                        builder = builder.include(shop);
                        bounds = builder.build();
                        Log.e("width", w + "");
                        Log.e("height", h + "");
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(
                                bounds, w, h, 5);
                        mMap.setPadding(40, 45, 40, 5);
                        mMap.animateCamera(cu);
                        return true;
                    }

                    private Location getLastKnownLocation() {
                        LocationManager mLocationManager =
                                (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
                        List<String> providers = mLocationManager.getProviders(true);
                        Location bestLocation = null;
                        for (String provider : providers) {
                            Location l = null;
//                    if (checkPermissions())
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

//                mEditor.putString("lat", bestLocation.getLatitude() + "");
//                mEditor.putString("lng", bestLocation.getLongitude() + "");
//                mEditor.commit();

                        return bestLocation;
                    }
                });
    }
}
