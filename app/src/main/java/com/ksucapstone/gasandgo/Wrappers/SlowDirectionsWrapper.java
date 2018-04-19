package com.ksucapstone.gasandgo.Wrappers;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

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
import com.ksucapstone.gasandgo.Repositories.MemoryCache;

import java.util.ArrayList;
import java.util.List;

public class SlowDirectionsWrapper implements GetDirectionsAsync.Callback, GetGasStationsAsync.GetGasStationsCallback {

    private List<LatLng> refillNeededLocations;
    private int locationIndex;
    private ArrayList<GasStationModel> stationsToStopAt;
    private Callback callback;
    private Activity activity;

    private long scrapeDelay = 10000;
    private String source;
    private String destination;
    private CarModel car;

    public SlowDirectionsWrapper(Activity activity, Callback callback){
        this.callback = callback;
        this.activity = activity;
    }

    public SlowDirectionsWrapper setRoute(String source, String destination){
        this.source = source;
        this.destination = destination;
        return this;
    }

    public SlowDirectionsWrapper setCar(CarModel car){
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
            getGasStations();
        }
        else{
            callback.onDirectionsComputed(directions);
        }

    }

    @Override
    public void onGasStationsReceived(ArrayList<GasStationModel> gasStations) {
        LatLng searchLocation = refillNeededLocations.get(locationIndex);
        MemoryCache.GetInstance().put(searchLocation.toString(), gasStations);
        handleStationResponse(gasStations);
    }

    private void getGasStations(){
        LatLng searchLocation = refillNeededLocations.get(locationIndex);
        if(MemoryCache.GetInstance().get(searchLocation.toString()) != null){
            ArrayList<GasStationModel> stations = (ArrayList<GasStationModel>)MemoryCache.GetInstance().get(searchLocation.toString());
            handleStationResponse(stations);
        }
        else{
            scrapeGasBuddy();
        }
    }

    private void handleStationResponse(ArrayList<GasStationModel> gasStations) {
        stationsToStopAt.add(gasStations.get(0));
        locationIndex++;
        if(locationIndex < refillNeededLocations.size()) {
            getGasStations();
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
        Log.d(this.getClass().getSimpleName(), "Scraping Gas Buddy");
        final GetGasStationsAsync.GetGasStationsCallback callback = this;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                IGasStationGetter stationGetter = new GasBuddyWrapper();
                GetGasStationsAsync getGasStationsAsync = new GetGasStationsAsync(stationGetter, callback);
                getGasStationsAsync.execute(refillNeededLocations.get(locationIndex));
            }
        },scrapeDelay);
    }

    public interface Callback{
        void onDirectionsComputed(DirectionsModel directions);
    }
}