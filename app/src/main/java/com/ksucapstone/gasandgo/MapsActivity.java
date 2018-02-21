package com.ksucapstone.gasandgo;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ksucapstone.gasandgo.Helpers.GpsHelper;
import com.ksucapstone.gasandgo.Helpers.GpsWrapper;
import com.ksucapstone.gasandgo.Helpers.PermissionHelper;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GpsWrapper.LocationReceiver {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private GpsHelper mGpsHelper;
    private final long LOCATION_REQUEST_INTERVAL = 20000;
    private final long ACCURACY_ACQUISITION_TIME = 5000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Google API object. New APIs can be added here.
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mGpsHelper = new GpsHelper(this, this, LOCATION_REQUEST_INTERVAL, ACCURACY_ACQUISITION_TIME);
        PermissionHelper.RequestGpsPermission(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    @Override
    public void onLocationReceived(Location location) {
        this.runOnUiThread(new Runnable() {
            public void run() {
                LatLng currentLocation = new LatLng(32.705267, -117.070312);
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        mGpsHelper.resumeGpsUpdates();
    }

    @Override
    public void onResume(){
        super.onResume();
        mGpsHelper.resumeGpsUpdates();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        PermissionHelper.SetPermissionIfAllowed(requestCode, permissions, grantResults, this);
    }

}
