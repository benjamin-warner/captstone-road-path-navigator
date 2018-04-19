package com.ksucapstone.gasandgo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ksucapstone.gasandgo.ArrayAdapters.DirectionsAdapter;
import com.ksucapstone.gasandgo.Helpers.PolylineDecoder;
import com.ksucapstone.gasandgo.Models.CarModel;
import com.ksucapstone.gasandgo.Models.Directions.DirectionsModel;
import com.ksucapstone.gasandgo.Models.Directions.Leg;
import com.ksucapstone.gasandgo.Models.Directions.Step;
import com.ksucapstone.gasandgo.Wrappers.SlowDirectionsWrapper;

import java.util.ArrayList;
import java.util.List;

public class SlowMapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, SlowDirectionsWrapper.Callback{

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mUserLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        double latitude = getIntent().getDoubleExtra("LATITUDE", 0.0);
        double longitude = getIntent().getDoubleExtra("LONGITUDE", 0.0);
        mUserLocation = new LatLng(latitude, longitude);

        CarModel testCar = new CarModel();
        testCar.Mpg = 15;
        testCar.TankCapacity = 8;
        String origin = getIntent().getStringExtra("origin");
        String destination = getIntent().getStringExtra("destination");

        SlowDirectionsWrapper directionsWrapper = new SlowDirectionsWrapper(this, this)
                .setRoute(origin, destination).setCar(testCar);
        directionsWrapper.getDirections();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.5301914,-106.8468267), 4.f));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        finish();
    }

    @Override
    public void onDirectionsComputed(DirectionsModel directions) {
        List<LatLng> routePoints = PolylineDecoder.decode(directions.polyline);
        mMap.addPolyline(new PolylineOptions().addAll(routePoints).color(0xff0000ff).width(20));
        LatLng start = new LatLng(directions.legs.get(0).start_location.lat,directions.legs.get(0).start_location.lng);
        mMap.addMarker(new MarkerOptions().position(start));
        for(Leg leg : directions.legs){
            mMap.addMarker(new MarkerOptions().position(new LatLng(leg.end_location.lat, leg.end_location.lng)));
        }

        ArrayList<Step> steps = new ArrayList<>();
        for(Leg leg : directions.legs)
            steps.addAll(leg.steps);

        DirectionsAdapter mAdapter = new DirectionsAdapter(this, R.layout.leg_info, steps);
        ListView directionsListview = findViewById(R.id.directions_listview);
        directionsListview.setAdapter(mAdapter);
    }
}
