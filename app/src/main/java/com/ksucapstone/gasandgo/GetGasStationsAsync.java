package com.ksucapstone.gasandgo;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.Interfaces.IGasStationGetter;
import com.ksucapstone.gasandgo.Models.GasStationModel;

import java.util.ArrayList;

public class GetGasStationsAsync extends AsyncTask<LatLng, Void, ArrayList<GasStationModel> > {

    private GetGasStationsCallback mCallback;
    private IGasStationGetter mGasStationGetter;

    public GetGasStationsAsync(GetGasStationsCallback callback, IGasStationGetter gasBuddyWrapper){
        mCallback = callback;
        mGasStationGetter = gasBuddyWrapper;
    }

    @Override
    protected ArrayList<GasStationModel> doInBackground(LatLng... params) {
        return mGasStationGetter.GetGasStationsNearLatitudeLongitude(params[0]);
    }

    @Override
    protected void onPostExecute(ArrayList<GasStationModel> gasStations){
        mCallback.onGasStationsReceived(gasStations);
    }

    public interface GetGasStationsCallback{
        void onGasStationsReceived(ArrayList<GasStationModel> gasStations);
    }
}
