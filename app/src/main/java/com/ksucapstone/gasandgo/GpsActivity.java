package com.ksucapstone.gasandgo;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import com.ksucapstone.gasandgo.Helpers.GpsHelper;
import com.ksucapstone.gasandgo.Helpers.GpsWrapper;
import com.ksucapstone.gasandgo.Repositories.SharedPreferencesWrapper;

public abstract class GpsActivity extends FragmentActivity implements GpsWrapper.LocationReceiver {

    public static final int GPS_PERMISSION_REQUEST_CODE = 9002;

    private GpsHelper mGpsHandler;
    private Location mCurrentLocation;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mGpsHandler = new GpsHelper(this, this, 5000, 750);
        requestGpsPermission();
    }

    @Override
    public void onPause(){
        super.onPause();
        mGpsHandler.stopGpsUpdates();
    }

    @Override
    public void onResume(){
        super.onResume();
        mGpsHandler.resumeGpsUpdates();
    }

    public void requestGpsPermission(){
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                //todo implement gps permission request explanation
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        GPS_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case GPS_PERMISSION_REQUEST_CODE: {
                boolean permissionSuccess = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                setGpsAllowed(permissionSuccess);
            }
            default:
                break;
        }
    }

    private void setGpsAllowed(boolean permissionGranted) {
        if(permissionGranted){
            SharedPreferencesWrapper prefsWrapper = new SharedPreferencesWrapper(this);
            prefsWrapper.writeBoolean(SharedPreferencesWrapper.CAN_USE_GPS,true);
        } else {
            SharedPreferencesWrapper prefsWrapper = new SharedPreferencesWrapper(this);
            prefsWrapper.writeBoolean(SharedPreferencesWrapper.CAN_USE_GPS,false);
        }
    }

    public Location getLocation(){
        return mCurrentLocation;
    }

    @Override
    public void onLocationReceived(Location location){
        mCurrentLocation = location;
    };
}
