package com.ksucapstone.gasandgo.Helpers;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

public class GeoHelper {

    public static LatLng getLocationFromAddress(Activity activity, String addressString) {
        Geocoder coder = new Geocoder(activity);
        LatLng location = null;
        try {
            List<Address> addresses = coder.getFromLocationName(addressString, 1);

            if(addresses.size() > 0) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                location = new LatLng(latitude, longitude);
            }
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        return location;
    }
}
