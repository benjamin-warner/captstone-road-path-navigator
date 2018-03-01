package com.ksucapstone.gasandgo.Wrappers;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.GasBuddyWrapper;

import org.junit.Test;
import static org.junit.Assert.*;

public class BuildGasBuddyUrlForLatitudeLongitudeShould {

    @Test
    public void returnValidUrlForKentOhio() throws Exception {
        String desiredUrl = "https://www.gasbuddy.com/home?search=41.1500897%2C-81.334272&fuel=1";
        LatLng kentLatLng = new LatLng(41.1500897,-81.334272);
        String actualUrl = GasBuddyWrapper.BuildGasBuddyUrlFromLatitudeLongitude(kentLatLng);
        assertEquals(desiredUrl, actualUrl);
    }
}
