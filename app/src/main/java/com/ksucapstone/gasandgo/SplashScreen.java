package com.ksucapstone.gasandgo;

import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.ksucapstone.gasandgo.Helpers.GpsHelper;
import com.ksucapstone.gasandgo.Helpers.GpsWrapper;
import com.ksucapstone.gasandgo.Helpers.PermissionHelper;

public class SplashScreen extends AppCompatActivity implements GpsWrapper.LocationReceiver {

    private FirebaseAuth firebaseAuth;
    private GpsHelper mGpsHelper;

    private final long LOCATION_REQUEST_INTERVAL = 20000;
    private final long ACCURACY_ACQUISITION_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        PermissionHelper.RequestGpsPermission(this);
        mGpsHelper = new GpsHelper(this, this, LOCATION_REQUEST_INTERVAL, ACCURACY_ACQUISITION_TIME);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() != null) {
            //profile activity here (user already logged in)
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        } else { //go to signup page
            finish();
            startActivity(new Intent(getApplicationContext(), SignupActivity.class));
        }
    }

    @Override
    public void onLocationReceived(final Location location) {
        mGpsHelper.stopGpsUpdates();
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("LATITUDE", latitude);
        intent.putExtra("LONGITUDE", longitude);
        startActivity(intent);
        finish();
    }

    @Override
    public void onPause(){
        super.onPause();
        mGpsHelper.stopGpsUpdates();
    }

    @Override
    public void onResume(){
        super.onResume();
        mGpsHelper.resumeGpsUpdates();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        PermissionHelper.SetPermissionIfAllowed(requestCode, permissions, grantResults, this);
    }
}
