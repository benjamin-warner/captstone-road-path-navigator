package com.ksucapstone.gasandgo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
import com.ksucapstone.gasandgo.Models.RefillModel;
import com.ksucapstone.gasandgo.Wrappers.DirectionsWrapper;
import com.ksucapstone.gasandgo.Wrappers.GasBuddyWrapper;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, DirectionsWrapper.Callback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng mUserLocation;
    private SupportMapFragment mMapFragment;
    private PopupProgressMessage mLoadingMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mLoadingMessage = new PopupProgressMessage(this, false, ProgressDialog.STYLE_SPINNER);
        mLoadingMessage.showWithMessage("Crunching numbers");

        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);

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
        getSupportFragmentManager().beginTransaction().hide(mMapFragment).commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        finish();
    }

    @Override
    public void onPathComputed(DirectionsModel direction, List<RefillModel> refillPoints) {
        List<LatLng> routePoints = PolylineDecoder.decode(direction.polyline);
        mMap.addPolyline(new PolylineOptions().addAll(routePoints).color(0xff0000ff).width(20));
        for(RefillModel refillPoint : refillPoints){
            mMap.addMarker(new MarkerOptions().position(refillPoint.location));
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : routePoints) {
            builder.include(point);
        }
        LatLngBounds bounds = builder.build();

        int padding = 50; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);

        populateDirections(direction, refillPoints);
        mLoadingMessage.dismiss();
        getSupportFragmentManager().beginTransaction().show(mMapFragment).commit();
    }

    public void populateDirections(DirectionsModel directions, List<RefillModel> refillPoints){
        ArrayList<Step> steps = new ArrayList<>();
        for(Leg leg : directions.legs)
            steps.addAll(leg.steps);

        DirectionsAdapter mAdapter = new DirectionsAdapter(this, R.layout.leg_info, steps);
        ListView directionsListView = findViewById(R.id.directions_listview);

        View header = View.inflate(this, R.layout.drive_stats, null);

        directionsListView.addHeaderView(header);
        directionsListView.setAdapter(mAdapter);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.setAlpha(0.25f);
        final LatLng refillPoint = marker.getPosition();
        final Activity thisActivity = this;
        GetGasStationsAsync getGasStationsAsync = new GetGasStationsAsync(new GasBuddyWrapper(), new GetGasStationsAsync.GetGasStationsCallback() {
            @Override
            public void onGasStationsReceived(ArrayList<GasStationModel> gasStations) {
                ArrayList<GasStationModel> stationsAvailable = new ArrayList<>();
                for(GasStationModel station : gasStations){
                    LatLng stationLoc = GeoHelper.getLocationFromAddress(thisActivity, station.address);
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
