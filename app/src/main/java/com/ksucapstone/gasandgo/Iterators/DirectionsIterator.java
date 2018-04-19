package com.ksucapstone.gasandgo.Iterators;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.Helpers.DistanceHelper;
import com.ksucapstone.gasandgo.Helpers.PolylineDecoder;
import com.ksucapstone.gasandgo.Models.CarModel;
import com.ksucapstone.gasandgo.Models.Directions.DirectionsModel;
import com.ksucapstone.gasandgo.Models.RefillModel;

import java.util.ArrayList;
import java.util.List;

public class DirectionsIterator {

    private List<LatLng> pathCoords;

    public DirectionsIterator(DirectionsModel directionsModel){
        pathCoords = PolylineDecoder.decode(directionsModel.polyline);
    }

    public List<RefillModel> FindRefillPointsForCar(CarModel car){

        double currentTankCapacity = car.TankCapacity;

        List<RefillModel> refillPoints = new ArrayList<>();
        if(pathCoords.size() <= 1){
            return refillPoints;
        }
        for(int i = 1; i < pathCoords.size(); i++){
            double distance = DistanceHelper.MilesBetween(pathCoords.get(i), pathCoords.get(i-1));
            Log.d(this.getClass().getSimpleName(), "Distance " + distance);
            currentTankCapacity = currentTankCapacity - (distance / car.Mpg);
            Log.d(this.getClass().getSimpleName(), "Tank Cap " + currentTankCapacity);
            if(currentTankCapacity < (car.TankCapacity / 4)){
                RefillModel newRefill = new RefillModel();
                newRefill.location = pathCoords.get(i);
                newRefill.gallonsFilled = car.TankCapacity - currentTankCapacity;
                refillPoints.add(newRefill);
                currentTankCapacity = car.TankCapacity;
            }
        }
        return refillPoints;
    }
}
