package com.ksucapstone.gasandgo.Iterators;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.Helpers.DistanceHelper;
import com.ksucapstone.gasandgo.Models.CarModel;
import com.ksucapstone.gasandgo.Models.Directions.DirectionsModel;
import com.ksucapstone.gasandgo.Models.Directions.Step;

import java.util.ArrayList;

public class DirectionsIterator {

    private ArrayList<Step> steps;

    public DirectionsIterator(DirectionsModel directionsModel){
        steps = directionsModel.legs.get(0).steps;
    }

    public ArrayList<LatLng> FindRefillPointsForCar(CarModel car){
        double currentTakeCapacity = car.TankCapacity;

        ArrayList<LatLng> refillPoints = new ArrayList<>();
        for(Step step : steps){
            double stepDistanceInMiles = DistanceHelper.MetersToMiles(step.distance.value);
            currentTakeCapacity = currentTakeCapacity - (stepDistanceInMiles / car.Mpg);
            if(currentTakeCapacity < (car.TankCapacity / 4)){
                LatLng refillLocation = new LatLng(step.start_location.lat, step.start_location.lng);
                refillPoints.add(refillLocation);
                currentTakeCapacity = car.TankCapacity;
            }
        }

        return  refillPoints;
    }
}
