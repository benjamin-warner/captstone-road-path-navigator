package com.ksucapstone.gasandgo.Helpers;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.util.Timer;
import java.util.TimerTask;

public class GpsWrapper {

    private Activity mActivity;
    private Timer mTimer;
    private LocationManager mLocationManager;
    private LocationReceiver mLocationReceiver;
    private boolean mGpsEnabled = false;
    private boolean mNetworkEnabled = false;

    public GpsWrapper(Activity context, LocationReceiver receiver) {
        mActivity = context;
        mLocationReceiver = receiver;

        setupLocationManager(context);
    }

    private void setupLocationManager(Context context) {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        //Todo Catch exception
        try {
            mGpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            mNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
    }

    public void updateLocation(long homingAllowance) {
        if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (mGpsEnabled)
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, homingAllowance, 0, locationListenerGps);
            if (mNetworkEnabled)
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, homingAllowance, 0, locationListenerNetwork);
        }

        mTimer = new Timer();
        mTimer.schedule(new GetLastLocation(), homingAllowance);
    }

    private void processLocation(Location location) {
        mLocationReceiver.onLocationReceived(location);
    }

    private LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (mTimer != null)
                mTimer.cancel();
            processLocation(location);
            mLocationManager.removeUpdates(this);
            mLocationManager.removeUpdates(locationListenerNetwork);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (mTimer != null)
                mTimer.cancel();
            processLocation(location);
            mLocationManager.removeUpdates(this);
            mLocationManager.removeUpdates(locationListenerGps);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    private class GetLastLocation extends TimerTask {
        @Override
        public void run() {
            mLocationManager.removeUpdates(locationListenerGps);
            mLocationManager.removeUpdates(locationListenerNetwork);

            Location net_loc = null, gps_loc = null;

            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (mGpsEnabled)
                    gps_loc = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (mNetworkEnabled)
                    net_loc = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            //if there are both values use the latest one
            if (gps_loc != null && net_loc != null) {
                if (gps_loc.getTime() > net_loc.getTime()) {
                    processLocation(gps_loc);
                } else {
                    processLocation(net_loc);
                }
                return;
            }

            if (gps_loc != null) {
                processLocation(gps_loc);
                return;
            }
            if (net_loc != null) {
                processLocation(net_loc);
                return;
            }
            processLocation(null);
        }
    }

    public interface LocationReceiver {
        void onLocationReceived(Location location);
    }
}
