package com.example.odyn.gps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

public class GPSThread extends Thread {
	private LocationManager locationManager;
	private LocationListener locationListener;
	private Context context;

	private TextFieldChanger changer;

	public GPSThread(Context context, TextFieldChanger changer) {
		this.context = context;
		this.changer = changer;
	}

	@Override
	public void run() {

		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			@Override
			public void onLocationChanged(Location location) {
				//Log.d("GPSThread", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude() + ", Speed: " + location.getSpeed() * 3.6 + "km/h");
				changer.changeTextField("Lat: " + location.getLatitude(), GPSValues.latitude);
				changer.changeTextField("Long: " + location.getLongitude(), GPSValues.longitude);
				changer.changeTextField("Speed: " + String.format("%.1f", location.getSpeed() * 3.6), GPSValues.speed);
			}

			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {

			}

			@Override
			public void onProviderEnabled(String provider) {

			}

			@Override
			public void onProviderDisabled(String provider) {

			}
		};

		// The following line needs to run on the main thread because it creates a handler internally
		((Activity) context).runOnUiThread(new Runnable() {
			@SuppressLint("MissingPermission")
			@Override
			public void run() {
				locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 1000, 0, locationListener);
			}
		});

		locationManager.removeUpdates(locationListener);
	}

	public void requestGPSPermissions(){
		// Request permissions
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
			return;
		}
	}

	public void stopGPS() {
		locationManager.removeUpdates(locationListener);
		interrupt();
	}
}