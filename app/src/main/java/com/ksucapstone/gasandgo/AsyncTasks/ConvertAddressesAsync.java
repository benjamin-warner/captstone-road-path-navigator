package com.ksucapstone.gasandgo.AsyncTasks;

import android.app.Activity;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.ksucapstone.gasandgo.Helpers.GeoHelper;

import java.util.ArrayList;

public class ConvertAddressesAsync extends AsyncTask<Void, Void, ArrayList<LatLng>> {

    private AddressConversionCallback mCallback;
    private Activity mActivity;
    private ArrayList<String> mAddresses;

    public ConvertAddressesAsync(ArrayList<String> address, Activity activity, AddressConversionCallback callback){
        mCallback = callback;
        mActivity = activity;
        mAddresses = address;
    }

    @Override
    protected ArrayList<LatLng> doInBackground(Void... params) {
        ArrayList<LatLng> coords = new ArrayList<>();
        for(String address : mAddresses)
            coords.add(GeoHelper.getLocationFromAddress(mActivity, address));
        return coords;
    }

    @Override
    protected void onPostExecute(ArrayList<LatLng> coord){
        mCallback.onConverted(coord);
    }

    public interface AddressConversionCallback{
        void onConverted(ArrayList<LatLng> gasStations);
    }
}