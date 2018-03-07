package com.ksucapstone.gasandgo;

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
import com.ksucapstone.gasandgo.AsyncTasks.GetGasStationsAsync;
import com.ksucapstone.gasandgo.Helpers.GeoHelper;
import com.ksucapstone.gasandgo.Models.GasStationModel;
import com.ksucapstone.gasandgo.Wrappers.GasBuddyWrapper;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GetGasStationsAsync.GetGasStationsCallback{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mUserLocation;

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

        double latitude = getIntent().getDoubleExtra("LATITUDE", 0.0);
        double longitude = getIntent().getDoubleExtra("LONGITUDE", 0.0);
        mUserLocation = new LatLng(latitude, longitude);

        GetGasStationsAsync getGasStationsAsync = new GetGasStationsAsync(this, new GasBuddyWrapper());
        getGasStationsAsync.execute(mUserLocation);
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
        mMap.addMarker(new MarkerOptions().position(mUserLocation).title("You are here"));

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        float zoomLevel = 11.0f; //zoom into level (2 to 21)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mUserLocation, zoomLevel)); //move camera to user's location
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        finish();
    }

    @Override
    public void onGasStationsReceived(ArrayList<GasStationModel> gasStations) {
        for(GasStationModel gasStation : gasStations){
            LatLng location = GeoHelper.getLocationFromAddress(this, gasStation.address);
            gasStation.latLng = location;
            if(gasStation.latLng != null) {
                mMap.addMarker(new MarkerOptions().position(gasStation.latLng).title("$" + String.valueOf(gasStation.price)));
            }

        }
    }
}
