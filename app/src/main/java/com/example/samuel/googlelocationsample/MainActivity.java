package com.example.samuel.googlelocationsample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_PERMISIONS_REQUEST_CODE = 111;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Location mLastLocation;
    private String mLatitudeLabel;
    private String mLongitudeLabel;
    private TextView mLatitudeText;
    private TextView mLongitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLatitudeLabel = getResources().getString(R.string.lat_label);
        mLongitudeLabel = getResources().getString(R.string.lng_label);
        mLatitudeText = findViewById(R.id.textViewLatitude);
        mLongitudeText = findViewById(R.id.textViewLongitude);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


    }
//Write functions to check if the the app has necessary permission to access location.
// If the permission is unavailable, request permission from user for the app.
// Write a function that will actually start the permission requesting dialog.

    private boolean checkPermissions(){
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        Log.i(TAG, "Inside requestPermissionRationale");
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION);
        //Log an additional rationale to the user. This would happen if the user denied the
        //request previously, but didn't check the "Don't ask again" checkbox.
        // In case you want, you can also show snackbar. Here, we used Log just to clear the concept.
        if (shouldProvideRationale) {
            Log.i(TAG, "****Inside requestPermissions function when shouldProvideRationale = true");
            startLocationPermissionRequest();
        } else {
            Log.i(TAG, "****Inside requestPermissions function when shouldProvideRationale = false");
            startLocationPermissionRequest();
        }

    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this, new String [] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISIONS_REQUEST_CODE);
    }



    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationProviderClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            mLatitudeText.setText(String.format(Locale.ENGLISH, "%s: %f", mLatitudeLabel, mLastLocation.getLatitude()));
                            mLongitudeText.setText(String.format(Locale.ENGLISH, "%s: %f",mLongitudeLabel, mLastLocation.getLongitude()));
                        } else {
                            Log.i(TAG, "Inside getLocation function. Error while getting location");
                            System.out.println(TAG+task.getException());
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            Log.i(TAG, "Inside onStart function; requesting permission when permission is not available");
            requestPermission();
        } else {
            Log.i(TAG, "Inside onStart function; getting location when permission is already available");
            getLastLocation();
        }
    }

}
