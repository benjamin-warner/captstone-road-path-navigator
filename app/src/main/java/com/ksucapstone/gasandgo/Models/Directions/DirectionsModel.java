package com.ksucapstone.gasandgo.Models.Directions;

import com.ksucapstone.gasandgo.Models.GoogleApiLocation;

import java.util.ArrayList;

public class DirectionsModel {
    public ArrayList<Step> steps;
    public NumberWithLabel distance;
    public NumberWithLabel duration;
    public GoogleApiLocation start_location;
    public GoogleApiLocation end_location;
    public String overview_polyline;
}
