package com.ksucapstone.gasandgo.Helpers;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class DistanceHelper {

    private static final double MilesInMeter = 0.000621371;
    private static final double MetersInMile = 1609.344;

    public static double MetersToMiles(double meters){
        return meters * MilesInMeter;
    }

    public static double MilesToMeters(double miles){
        return miles * MetersInMile;
    }

    public static double MilesBetween(LatLng a, LatLng b){
        double distanceInMeters = GetDistance(a.latitude, a.longitude, b.latitude, b.longitude);
        return MetersToMiles(distanceInMeters);
    }

    private static double GetDistance(double lat_a, double lng_a, double lat_b, double lng_b )
    {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b-lat_a);
        double lngDiff = Math.toRadians(lng_b-lng_a);
        double a = Math.sin(latDiff /2) * Math.sin(latDiff /2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff /2) * Math.sin(lngDiff /2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return distance * meterConversion;
    }
}
