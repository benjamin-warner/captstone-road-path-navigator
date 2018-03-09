package com.ksucapstone.gasandgo.Models.Directions;

import com.ksucapstone.gasandgo.Models.GoogleApiLocation;

import java.util.ArrayList;

public class DirectionsModel {
    ArrayList<Step> steps;
    NumberWithLabel distance;
    NumberWithLabel duration;
    GoogleApiLocation start_location;
    GoogleApiLocation end_location;
    String overview_polyline;
}
