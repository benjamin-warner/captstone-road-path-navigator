package com.ksucapstone.gasandgo;

import android.app.Activity;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ksucapstone.gasandgo.ArrayAdapters.DirectionsAdapter;
import com.ksucapstone.gasandgo.AsyncTasks.GetGasStationsAsync;
import com.ksucapstone.gasandgo.Helpers.DistanceHelper;
import com.ksucapstone.gasandgo.Helpers.GeoHelper;
import com.ksucapstone.gasandgo.Helpers.PolylineDecoder;
import com.ksucapstone.gasandgo.Models.CarModel;
import com.ksucapstone.gasandgo.Models.Directions.DirectionsModel;
import com.ksucapstone.gasandgo.Models.Directions.Leg;
import com.ksucapstone.gasandgo.Models.Directions.Step;
import com.ksucapstone.gasandgo.Models.GasStationModel;
import com.ksucapstone.gasandgo.Wrappers.DirectionsWrapper;
import com.ksucapstone.gasandgo.Wrappers.GasBuddyWrapper;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, DirectionsWrapper.Callback, GoogleMap.OnMarkerClickListener {

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
        testCar.Mpg = 32;
        testCar.TankCapacity = 8;
        String origin = getIntent().getStringExtra("origin");
        String destination = getIntent().getStringExtra("destination");

        DirectionsWrapper directionsWrapper = new DirectionsWrapper(this, this)
                .setRoute(origin, destination).setCar(testCar);
        directionsWrapper.getDirections();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.5301914,-106.8468267), 4.f));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        finish();
    }

    @Override
    public void onPathComputed(DirectionsModel direction, List<LatLng> refillPoints) {
        List<LatLng> routePoints = PolylineDecoder.decode(direction.polyline);
        mMap.addPolyline(new PolylineOptions().addAll(routePoints).color(0xff0000ff).width(20));
        for(LatLng refillPoint : refillPoints){
            mMap.addMarker(new MarkerOptions().position(refillPoint));
        }
        populateDirections(direction.legs);
    }

    public void populateDirections(ArrayList<Leg> routeLegs){
        ArrayList<Step> steps = new ArrayList<>();
        for(Leg leg : routeLegs)
            steps.addAll(leg.steps);

        DirectionsAdapter mAdapter = new DirectionsAdapter(this, R.layout.leg_info, steps);
        ListView directionsListView = findViewById(R.id.directions_listview);
        directionsListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setAlpha(0.25f);
        final LatLng refillPoint = marker.getPosition();
        final Activity thiz = this;
        GetGasStationsAsync getGasStationsAsync = new GetGasStationsAsync(new GasBuddyWrapper(), new GetGasStationsAsync.GetGasStationsCallback() {
            @Override
            public void onGasStationsReceived(ArrayList<GasStationModel> gasStations) {
                ArrayList<GasStationModel> stationsAvailable = new ArrayList<>();
                for(GasStationModel station : gasStations){
                    LatLng stationLoc = GeoHelper.getLocationFromAddress(thiz, station.address);
                    if(stationLoc != null){
                        GasStationModel availableStation = station;
                        availableStation.distance = DistanceHelper.MilesBetween(refillPoint, stationLoc);
                        stationsAvailable.add(availableStation);
                    }
                }
                openGasStationDialog(stationsAvailable);
            }
        });
        getGasStationsAsync.execute(refillPoint);
        return false;
    }

    public void openGasStationDialog(ArrayList<GasStationModel> gasStations){
        StationPickerFragment pickerFragment = new StationPickerFragment();
        Bundle stations = new Bundle();
        stations.putSerializable("GAS", gasStations);
        pickerFragment.setArguments(stations);
        pickerFragment.show(getFragmentManager(), StationPickerFragment.TAG);
    }
}
