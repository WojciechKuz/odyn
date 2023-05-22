/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, Viacheslav Kushinir <kushnir@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.gps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.example.odyn.main_service.ServiceConnector;
import com.example.odyn.main_service.types.IconType;
import com.example.odyn.settings.SettingNames;
import com.example.odyn.settings.SettingsProvider;

import org.json.JSONException;

/**
 * Jest to wątek, który odpowiada za nawigację GPS w naszej aplikacji.
 */
public class GPSThread extends Thread implements SensorEventListener {
	private LocationManager locationManager;
	private LocationListener locationListener;
	private SensorManager sensorManager;
	private Sensor accelerometer;
	private float acceleration;
	private float currentAcceleration;
	private float lastAcceleration;
	private Context context;
	private double speed;

	private TextFieldChanger changer;

	public GPSThread(Context context, TextFieldChanger changer) {
		this.context = context;
		this.changer = changer;
	}

	/**
	 * Jest to metoda uruchamiająca wątek do obsługi GPS.
	 */
	@Override
	public void run() {
		locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			/**
			 * Jest to metoda odpowiadająca za zmianę lokalizacji.
			 */
			@Override
			public void onLocationChanged(Location location) {
				//Log.d("GPSThread", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude() + ", Speed: " + location.getSpeed() * 3.6 + "km/h");
				changer.changeTextField("Lat: " + location.getLatitude(), GPSValues.latitude);
				changer.changeTextField("Long: " + location.getLongitude(), GPSValues.longitude);
				changer.changeTextField("Speed: " + String.format("%.1f", location.getSpeed() * 3.6), GPSValues.speed);
				speed = location.getSpeed() * 3.6;
			}

			/**
			 * Jest to metoda odpowiadająca za zmianę statusu wątku nawigacji.
			 * @param provider Dostawca
			 * @param status Status
			 * @param extras Wiązka dodatkowych argumentów
			 */
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			}

			/**
			 * Jest to metoda odpowiadająca za działania po uruchomieniu pracy dostawcy.
			 * @param provider Dostawca
			 */
			@Override
			public void onProviderEnabled(String provider) {
			}

			/**
			 * Jest to metoda odpowiadająca za działania po zakończeniu pracy dostawcy.
			 * @param provider Dostawca
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

		((Activity) context).runOnUiThread(new Runnable() {
			@SuppressLint("MissingPermission")
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
	 * Jest to metoda odpowiadająca za wykonanie żądania o uprawnienia do GPS.
	 */
	public void requestGPSPermissions(){
		// Request permissions
		if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
			return;
		}
	}

	/**
	 * Jest to metoda odpowiadająca za zakończenie pracy wątku GPS.
	 */
	public void stopGPS() {
		locationManager.removeUpdates(locationListener);
		sensorManager.unregisterListener(this);
		interrupt();
	}

	/**
	 * Jest to metoda odpowiadająca za zmianę sensora.
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

		if (acceleration > 1 && speed >= 20) {
			// Phone was shaken
			startEmergency();
		}
	}

	/**
	 * Jest to metoda odpowiadająca za zmianę dokładności.
	 */
	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	/**
	 * Jest to metoda odpowiadająca za rozpoczęcie nagrywania awaryjnego.
	 */
	private void startEmergency() {
		Log.d("GPSThread", ">>> nagrywanie awaryjne");
		ServiceConnector.onClickIcon(IconType.emergency);
	}
}
