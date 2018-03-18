package com.ksucapstone.gasandgo.Wrappers;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.AsyncTasks.GetDirectionsAsync;
import com.ksucapstone.gasandgo.Helpers.ManifestDataHelper;
import com.ksucapstone.gasandgo.Helpers.UrlBuilder;
import com.ksucapstone.gasandgo.Iterators.DirectionsIterator;
import com.ksucapstone.gasandgo.Models.CarModel;
import com.ksucapstone.gasandgo.Models.Directions.DirectionsModel;

import java.util.ArrayList;

public class DirectionsWrapper implements GetDirectionsAsync.Callback {

    private ArrayList<LatLng> refillNeededLocations;
    private int locationIndex;
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
        callback.onPathComputed(directions, refillNeededLocations);
    }

    public interface Callback{
        void onPathComputed(DirectionsModel direction, ArrayList<LatLng> refillPoints);
    }
}
