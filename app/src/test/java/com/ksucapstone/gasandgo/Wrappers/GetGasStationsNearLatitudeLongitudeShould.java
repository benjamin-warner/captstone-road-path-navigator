package com.ksucapstone.gasandgo.Wrappers;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.Interfaces.IGasStationGetter;
import com.ksucapstone.gasandgo.Models.GasStationModel;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class GetGasStationsNearLatitudeLongitudeShould {

    @Test
    public void ReturnAtLeastOncePriceForKentOhio() throws Exception {
        LatLng kentLatLng = new LatLng(41.1500897, -81.334272);
        IGasStationGetter gasStationGetter = new GasBuddyWrapper();
        ArrayList<GasStationModel> gasStations = gasStationGetter.GetGasStationsNearLatitudeLongitude(kentLatLng);
        assertFalse( gasStations.isEmpty() );
    }
}
