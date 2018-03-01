package com.ksucapstone.gasandgo;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.Models.GasStationModel;
import com.ksucapstone.gasandgo.Wrappers.GasBuddyWrapper;

import java.util.ArrayList;

public class GetGasStationsAsync extends AsyncTask<LatLng, Void, ArrayList<GasStationModel> > {

    private GetGasStationsCallback mCallback;

    public GetGasStationsAsync(GetGasStationsCallback callback){
        mCallback = callback;
    }

    @Override
    protected ArrayList<GasStationModel> doInBackground(LatLng... params) {
        return GasBuddyWrapper.GetGasStationsNearLatitudeLongitude(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<GasStationModel> gasStations){
        mCallback.onGasStationsReceived(gasStations);
    }

    public interface GetGasStationsCallback{
        void onGasStationsReceived(ArrayList<GasStationModel> gasStations);
    }
}
