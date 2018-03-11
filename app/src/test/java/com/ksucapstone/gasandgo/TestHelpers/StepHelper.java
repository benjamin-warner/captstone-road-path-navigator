package com.ksucapstone.gasandgo.TestHelpers;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.Models.Directions.NumberWithLabel;
import com.ksucapstone.gasandgo.Models.Directions.Step;
import com.ksucapstone.gasandgo.Models.GoogleApiLocation;

public class StepHelper {

    public static Step MakeStep(int meters, LatLng startCoord){
        Step step = new Step();
        NumberWithLabel distance = new NumberWithLabel();
        distance.value = meters;
        step.distance = distance;
        GoogleApiLocation location = new GoogleApiLocation();
        location.lat = startCoord.latitude;
        location.lng = startCoord.longitude;
        step.start_location = location;

        return step;
    }
}
