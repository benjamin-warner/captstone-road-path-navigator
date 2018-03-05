package com.ksucapstone.gasandgo.Helpers;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;

public class GeoHelper {

    public static LatLng getLocationFromAddress(Activity activity, String addressString) {
        Geocoder coder = new Geocoder(activity);
        LatLng location = null;
        try {
            Address address = coder.getFromLocationName(addressString, 1).get(0);
            location = new LatLng(address.getLatitude(), address.getLongitude() );
        }
        catch (IOException ex) {

            ex.printStackTrace();
        }
        return location;
    }
}
