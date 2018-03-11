package com.ksucapstone.gasandgo.Models.Directions;

import com.ksucapstone.gasandgo.Models.GoogleApiLocation;

public class Step {
    NumberWithLabel distance;
    NumberWithLabel duration;
    GoogleApiLocation start_location;
    GoogleApiLocation end_location;
    String html_instructions;
}
