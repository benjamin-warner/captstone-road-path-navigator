package com.ksucapstone.gasandgo.Models.Directions;

import com.ksucapstone.gasandgo.Models.GoogleApiLocation;

public class Step {
    public NumberWithLabel distance;
    public NumberWithLabel duration;
    public GoogleApiLocation start_location;
    public GoogleApiLocation end_location;
    public String html_instructions;
}
