package com.ksucapstone.gasandgo.Interfaces;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.Models.GasStationModel;

import java.util.ArrayList;

public interface IGasStationGetter {
    ArrayList<GasStationModel> GetGasStationsNearLatitudeLongitude(LatLng latLng);
}
