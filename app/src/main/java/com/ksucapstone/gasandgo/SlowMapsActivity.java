package com.ksucapstone.gasandgo;

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
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.ksucapstone.gasandgo.ArrayAdapters.DirectionsAdapter;
import com.ksucapstone.gasandgo.Helpers.DistanceHelper;
import com.ksucapstone.gasandgo.Helpers.PolylineDecoder;
import com.ksucapstone.gasandgo.Models.CarModel;
import com.ksucapstone.gasandgo.Models.Directions.DirectionsModel;
import com.ksucapstone.gasandgo.Models.Directions.Leg;
import com.ksucapstone.gasandgo.Models.Directions.Step;
import com.ksucapstone.gasandgo.Models.RefillModel;
import com.ksucapstone.gasandgo.Wrappers.SlowDirectionsWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SlowMapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, SlowDirectionsWrapper.Callback{

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

        CarModel userCar = (CarModel)getIntent().getSerializableExtra("car");
        String origin = getIntent().getStringExtra("origin");
        String destination = getIntent().getStringExtra("destination");

        SlowDirectionsWrapper directionsWrapper = new SlowDirectionsWrapper(this, this)
                .setRoute(origin, destination).setCar(userCar);
        directionsWrapper.getDirections();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        getSupportFragmentManager().beginTransaction().hide(mMapFragment).commit();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        finish();
    }

    @Override
    public void onDirectionsComputed(DirectionsModel directions, List<RefillModel> refillPoints) {
        List<LatLng> routePoints = PolylineDecoder.decode(directions.polyline);
        mMap.addPolyline(new PolylineOptions().addAll(routePoints).color(0xff0000ff).width(20));
        LatLng start = new LatLng(directions.legs.get(0).start_location.lat,directions.legs.get(0).start_location.lng);
        mMap.addMarker(new MarkerOptions().position(start));
        for(Leg leg : directions.legs){
            mMap.addMarker(new MarkerOptions().position(new LatLng(leg.end_location.lat, leg.end_location.lng)));
        }

        ArrayList<Step> steps = new ArrayList<>();
        int seconds = 0;
        int meters = 0;
        for(Leg leg : directions.legs) {
            meters += leg.distance.value;
            seconds += leg.duration.value;
            steps.addAll(leg.steps);
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : routePoints) {
            builder.include(point);
        }
        LatLngBounds bounds = builder.build();

        int padding = 50; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.moveCamera(cu);

        DirectionsAdapter mAdapter = new DirectionsAdapter(this, R.layout.leg_info, steps);
        ListView directionsListView = findViewById(R.id.directions_listview);

        View header = View.inflate(this, R.layout.drive_stats, null);

        double cost = 0;
        double gallons = 0;
        for(RefillModel refill : refillPoints){
            cost += refill.cost;
            gallons += refill.gallonsFilled;
        }
        String gallonStr = String.valueOf(gallons);
        if(gallonStr.length() >= 5)
            gallonStr = gallonStr.substring(0, 5);

        String costStr = String.valueOf(cost);
        if(costStr.length() >= 5)
            costStr = costStr.substring(0, 5);

        ((TextView)header.findViewById(R.id.trip_cost)).append(costStr);
        ((TextView)header.findViewById(R.id.trip_gallons)).append(gallonStr);

        String miles = String.valueOf(DistanceHelper.MetersToMiles(meters));
        int decimalIndex = miles.indexOf('.');
        miles = miles.substring(0,decimalIndex) + "mi";
        ((TextView)header.findViewById(R.id.trip_distance)).append(miles);
        ((TextView)header.findViewById(R.id.trip_time)).append(timeStringFromSeconds(seconds));

        directionsListView.addHeaderView(header);
        directionsListView.setAdapter(mAdapter);

        mLoadingMessage.dismiss();
        getSupportFragmentManager().beginTransaction().show(mMapFragment).commit();
    }

    String timeStringFromSeconds(long seconds){
        long days = TimeUnit.SECONDS.toDays(seconds);
        seconds -= TimeUnit.DAYS.toSeconds(days);

        long hours = TimeUnit.SECONDS.toHours(seconds);
        seconds -= TimeUnit.HOURS.toSeconds(hours);
        long minutes = TimeUnit.SECONDS.toMinutes(seconds);

        String time = "";
        if(days > 0)
            time += String.valueOf(days) + "d";
        if(hours > 0)
            time += String.valueOf(hours) + "h";
        if(minutes > 0)
            time += String.valueOf(minutes) + "m";
        return time;
    }
}
