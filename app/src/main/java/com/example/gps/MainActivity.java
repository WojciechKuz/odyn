package com.example.gps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    private TextView latitudeDisplayField, longitudeDisplayField, linePreview;

    private ToggleButton writeToggle;
    private LocationRequest request;
    private LocationSettingsRequest.Builder builder;
    private final int REQUEST_CODE = 8990;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;

    long startTime = 0;
    String startTimeString = "", endTimeString = "";
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {

            startTimeString = endTimeString;

            long millis = System.currentTimeMillis() - startTime;
            int endMiliseconds = (int) (millis % 1000);
            int endSeconds = (int) (millis / 1000);
            int endMinutes = endSeconds / 60;
            endSeconds = endSeconds % 60;

            endTimeString = String.format("%d:%02d:%02d", endMinutes, endSeconds, endMiliseconds);

            //startTime = System.currentTimeMillis();

            timerHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latitudeDisplayField = findViewById(R.id.ltDF);
        longitudeDisplayField = findViewById(R.id.lgDF);
        writeToggle = findViewById(R.id.writeToggle);
        linePreview = findViewById(R.id.linePreview);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        request = new LocationRequest().setFastestInterval(100).setInterval(100).setPriority(100);

        builder = new LocationSettingsRequest.Builder().addLocationRequest(request);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());

        result.addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this, REQUEST_CODE);
                    } catch (IntentSender.SendIntentException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    longitudeDisplayField.setText("Null");
                    latitudeDisplayField.setText("Null");
                }
                for (Location location : locationResult.getLocations()) {
                    String longitude = String.valueOf(location.getLongitude());
                    String latitude = String.valueOf(location.getLatitude());
                    longitudeDisplayField.setText(longitude);
                    latitudeDisplayField.setText(latitude);
                    if (writeToggle.isChecked()) {
                        linePreview.setText(startTimeString + "->" + endTimeString + "\n" + latitude + " lat." + longitude + " long. ");
                    } else {
                        linePreview.setText("File Writing is off");
                    }
                }
            }
        };


    }

    private void startLocationUpdate() {
        if (fusedLocationProviderClient != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                //return;
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);
            fusedLocationProviderClient.requestLocationUpdates(request, locationCallback, getMainLooper());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdate();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
}