package com.ksucapstone.gasandgo.Wrappers;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.GasBuddyWrapper;

import org.junit.Test;

import static org.junit.Assert.*;

public class GetListOfGasPricesNearLatitudeLongitudeShould {

    @Test
    public void ReturnAtLeastOncePriceForKentOhio() throws Exception {
        LatLng kentLatLng = new LatLng(41.1500897, -81.334272);
        GasBuddyWrapper.GetListOfGasPricesNearLatitudeLongitude(kentLatLng);
    }
}
