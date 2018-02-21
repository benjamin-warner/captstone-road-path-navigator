package com.ksucapstone.gasandgo.Helpers;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.ksucapstone.gasandgo.Repositories.SharedPreferencesWrapper;

public class PermissionHelper {

    private static final int GPS_PERMISSION_REQUEST_CODE = 9002;
    private final static String CAN_USE_GPS = "CAN_USE_GPS";

    public static void RequestGpsPermission(Activity activity){
        if (ContextCompat.checkSelfPermission(activity,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            }
            else {
                ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        GPS_PERMISSION_REQUEST_CODE);
            }
        }
    }

    public static void SetPermissionIfAllowed(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults, Activity activity){
        switch (requestCode) {
            case PermissionHelper.GPS_PERMISSION_REQUEST_CODE: {
                boolean permissionSuccess = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
                SetPermissionPreference(activity, permissionSuccess);
            }
            default:
                break;
        }
    }

    private static void SetPermissionPreference(Activity activity, boolean permissionGranted) {

        SharedPreferencesWrapper prefsWrapper = new SharedPreferencesWrapper(activity);
        if(permissionGranted){
            prefsWrapper.writeBoolean(CAN_USE_GPS,true);
        }
        else {
            prefsWrapper.writeBoolean(CAN_USE_GPS,false);
        }
    }

}
