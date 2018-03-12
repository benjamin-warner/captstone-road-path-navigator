package com.ksucapstone.gasandgo.Helpers;

public class DistanceHelper {

    private static final double MilesInMeter = 0.000621371;
    private static final double MetersInMile = 1609.344;

    public static double MetersToMiles(double meters){
        return meters * MilesInMeter;
    }

    public static double MilesToMeters(double miles){
        return miles * MetersInMile;
    }
}
