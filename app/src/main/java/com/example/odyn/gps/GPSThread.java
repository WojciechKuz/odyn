/*
    BSD 3-Clause License
    Copyright (c) Viacheslav Kushinir <kushnir@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.gps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.odyn.main_service.ServiceConnector;
import com.example.odyn.main_service.types.IconType;

/**
 * GPS thread, gathers information about location, speed, acceleration. Additionally implements simplified method to detect collisions.
 */
public class GPSThread extends Thread implements SensorEventListener {
	private LocationManager locationManager;
	private LocationListener locationListener;
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private Context context;
	private Handler handler;

	private float acceleration;
	private float currentAcceleration;
	private float lastAcceleration;
	private double speed;

	public GPSThread(Context context) {
		this.context = context;
		this.handler = new Handler(Looper.getMainLooper());
	}

	/**
	 * Method starting the thread
	 */
	@Override
	public void run() {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			/**
			 * Method that updates values in DataHolder when locations is changed.
			 */
			@Override
			public void onLocationChanged(Location location) {
				DataHolder dataHolder = DataHolder.getInstance();
				dataHolder.setLatitude("Lat: " + location.getLatitude());
				dataHolder.setLongitude("Long: " + location.getLongitude());
				speed = location.getSpeed() * 3.6;
				dataHolder.setSpeed("Speed: " + String.format("%.1f", speed));
				//Log.d("GPSThread", "Latitude: " + dataHolder.getLatitude() + ", Longitude: " + dataHolder.getLongitude() + ", Speed: " + dataHolder.getSpeed() + "km/h");
			}

			/**
			 * Method responsible for handling status change
			 * @param provider
			 * @param status
			 * @param extras
			 */
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			/**
			 * Method responsible for handling event of provider being enabled
			 * @param provider
			 */
			@Override
			public void onProviderEnabled(String provider) {
			}

			/**
			 * Method responsible for handling event of provider being disabled
			 * @param provider
			 */
			@Override
			public void onProviderDisabled(String provider) {
			}
		};

		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		acceleration = 0.0f;
		currentAcceleration = SensorManager.GRAVITY_EARTH;
		lastAcceleration = SensorManager.GRAVITY_EARTH;

		handler.post(new Runnable() {
			@SuppressLint("MissingPermission") // already requested on app startup
			@Override
			public void run() {
				locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 1000, 0, locationListener);
				sensorManager.registerListener(GPSThread.this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
			}
		});
		locationManager.removeUpdates(locationListener);
		sensorManager.unregisterListener(this);
	}

	/**
	 * Method responsible for stopping the thread
	 */
	public void stopGPS() {
		locationManager.removeUpdates(locationListener);
		sensorManager.unregisterListener(this);
		interrupt();
	}

	/**
	 * Method to detect phone shake using acceleration sensor
	 */
	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		lastAcceleration = currentAcceleration;
		currentAcceleration = (float) Math.sqrt(x * x + y * y + z * z);

		float delta = currentAcceleration - lastAcceleration;
		acceleration = acceleration * 0.9f + delta;
		if (acceleration > 1 && speed >= 20) { // TODO: Speed setting
			startEmergency();
		}
	}

	/**
	 * Method responsible for handling accuracy change
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	/**
	 * Method to start emergency recording
	 */
	private void startEmergency() {
		Log.d("GPSThread", ">>> nagrywanie awaryjne");
		ServiceConnector.onClickIcon(IconType.emergency);
	}
}
