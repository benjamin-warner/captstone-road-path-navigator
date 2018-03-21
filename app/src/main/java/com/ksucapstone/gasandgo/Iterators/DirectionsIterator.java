package com.ksucapstone.gasandgo.Iterators;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.Helpers.DistanceHelper;
import com.ksucapstone.gasandgo.Helpers.PolylineDecoder;
import com.ksucapstone.gasandgo.Models.CarModel;
import com.ksucapstone.gasandgo.Models.Directions.DirectionsModel;
import com.ksucapstone.gasandgo.Models.Directions.Step;

import java.util.ArrayList;
import java.util.List;

public class DirectionsIterator {

    private ArrayList<Step> steps;
    private List<LatLng> pathCoords;

    public DirectionsIterator(DirectionsModel directionsModel){
        pathCoords = PolylineDecoder.decode(directionsModel.polyline);
    }

    public List<LatLng> FindRefillPointsForCar(CarModel car){

        double currentTankCapacity = car.TankCapacity;

        List<LatLng> refillPoints = new ArrayList<>();
        if(pathCoords.size() <= 1){
            return refillPoints;
        }

        for(int i = 1; i < pathCoords.size(); i++){
            double distance = DistanceHelper.MilesBetween(pathCoords.get(i), pathCoords.get(i-1));
            Log.d(this.getClass().getSimpleName(), "Distance " + distance);
            currentTankCapacity = currentTankCapacity - (distance / car.Mpg);
            Log.d(this.getClass().getSimpleName(), "Tank Cap " + currentTankCapacity);
            if(currentTankCapacity < (car.TankCapacity / 4)){
                LatLng refillLocation = pathCoords.get(i);
                refillPoints.add(refillLocation);
                currentTankCapacity = car.TankCapacity;
            }
        }
        return refillPoints;
    }
}
