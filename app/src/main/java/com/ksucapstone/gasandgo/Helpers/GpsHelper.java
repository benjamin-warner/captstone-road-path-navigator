package com.ksucapstone.gasandgo.Helpers;


import android.app.Activity;
import android.os.Handler;

public class GpsHelper {

    private GpsRunnable mGpsRunnable;
    private Handler mGpsHandler;
    private GpsWrapper mGpsWrapper;
    private long mTimeOutDuration;
    private long mRefreshInterval;

    public GpsHelper(Activity activity, GpsWrapper.LocationReceiver receiver, long refresh, long timeOut){
        mGpsHandler = new Handler();
        mGpsWrapper = new GpsWrapper(activity, receiver);
        mGpsRunnable = new GpsRunnable();
        mRefreshInterval = refresh;
        mTimeOutDuration = timeOut;
    }

    public void stopGpsUpdates(){
        mGpsHandler.removeCallbacks(mGpsRunnable);
    }

    public void resumeGpsUpdates(){
        mGpsHandler.post(mGpsRunnable);
    }

    private class GpsRunnable implements Runnable {
        @Override
        public void run() {
            mGpsWrapper.updateLocation(mTimeOutDuration);
            mGpsHandler.postDelayed(this, mRefreshInterval);
        }
    }
}
