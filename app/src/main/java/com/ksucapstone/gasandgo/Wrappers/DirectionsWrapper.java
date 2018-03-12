package com.ksucapstone.gasandgo.Wrappers;

import android.app.Activity;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.AsyncTasks.GetDirectionsAsync;
import com.ksucapstone.gasandgo.AsyncTasks.GetGasStationsAsync;
import com.ksucapstone.gasandgo.Helpers.ManifestDataHelper;
import com.ksucapstone.gasandgo.Helpers.UrlBuilder;
import com.ksucapstone.gasandgo.Interfaces.IGasStationGetter;
import com.ksucapstone.gasandgo.Iterators.DirectionsIterator;
import com.ksucapstone.gasandgo.Models.CarModel;
import com.ksucapstone.gasandgo.Models.Directions.DirectionsModel;
import com.ksucapstone.gasandgo.Models.GasStationModel;

import java.util.ArrayList;

public class DirectionsWrapper implements GetDirectionsAsync.Callback, GetGasStationsAsync.GetGasStationsCallback {

    private ArrayList<LatLng> refillNeededLocations;
    private int locationIndex;
    private ArrayList<GasStationModel> stationsToStopAt;
    private Callback callback;
    private Activity activity;

    private String source;
    private String destination;
    private CarModel car;

    public DirectionsWrapper(Activity activity, Callback callback){
        this.callback = callback;
        this.activity = activity;
    }

    public DirectionsWrapper setRoute(String source, String destination){
        this.source = source;
        this.destination = destination;
        return this;
    }

    public DirectionsWrapper setCar(CarModel car){
        this.car = car;
        return this;
    }

    public void getDirections(){
        GetDirectionsAsync getDirectionsAsync = new GetDirectionsAsync(this);
        String directionUrl = UrlBuilder.BuildDirectionUrl(source, destination, ManifestDataHelper.GetApiKey(activity));
        getDirectionsAsync.execute(directionUrl);
    }

    @Override
    public void onResponseReceived(DirectionsModel directions) {
        DirectionsIterator directionsIterator = new DirectionsIterator(directions);
        refillNeededLocations = directionsIterator.FindRefillPointsForCar(car);
        stationsToStopAt = new ArrayList<>();
        locationIndex = 0;
        if(locationIndex < refillNeededLocations.size()) {
            scrapeGasBuddy();
        }
        else{
            callback.onDirectionsComputed(directions);
        }

    }

    @Override
    public void onGasStationsReceived(ArrayList<GasStationModel> gasStations) {
        stationsToStopAt.add(gasStations.get(0));
        locationIndex++;
        if(locationIndex < refillNeededLocations.size()) {
            scrapeGasBuddy();
        }
        else{
            GetDirectionsAsync getFinalRouteTask = new GetDirectionsAsync(new GetDirectionsAsync.Callback() {
                @Override
                public void onResponseReceived(DirectionsModel directions) {
                    callback.onDirectionsComputed(directions);
                }
            });
            String directionsUrl = UrlBuilder.BuildDirectionUrl(source, destination, ManifestDataHelper.GetApiKey(activity), stationsToStopAt);
            getFinalRouteTask.execute(directionsUrl);
        }
    }
    private void scrapeGasBuddy(){
        final GetGasStationsAsync.GetGasStationsCallback callback = this;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                IGasStationGetter stationGetter = new GasBuddyWrapper();
                GetGasStationsAsync getGasStationsAsync = new GetGasStationsAsync(stationGetter, callback);
                getGasStationsAsync.execute(refillNeededLocations.get(locationIndex));
            }
        },2500);
    }

    public interface Callback{
        void onDirectionsComputed(DirectionsModel directions);
    }
}
