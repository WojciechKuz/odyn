package com.example.odyn.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
/**
 * Jest to wątek, który odpowiada za nawigację GPS w naszej aplikacji.
 */
public class GPSThread extends Thread {
	private boolean stopGPS = false;
	private LocationManager locationManager;
	private LocationListener locationListener;
	private Context context;

	private TextView latitudeText, longitudeText;

	public GPSThread(Context context, TextView latitudeText, TextView longitudeText) {
		this.context = context;
		this.latitudeText = latitudeText;
		this.longitudeText = longitudeText;
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
				Log.d("GPS", "Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());
				((Activity)latitudeText.getContext()).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						latitudeText.setText("Lat: " + location.getLatitude());
					}
				});
				((Activity)longitudeText.getContext()).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						longitudeText.setText("Long: " + location.getLongitude());
					}
				});
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
			 * Jest to metoda odpowiadająca za działania po uruchomieniu dostawcy.
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

		// The following line needs to run on the main thread because it creates a handler internally
		((Activity) context).runOnUiThread(new Runnable() {
			@SuppressLint("MissingPermission")
			@Override
			public void run() {
				locationManager.requestLocationUpdates(LocationManager.FUSED_PROVIDER, 1000, 0, locationListener);
			}
		});

		while (!stopGPS) {
			// Do nothing
		}

		locationManager.removeUpdates(locationListener);
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
		stopGPS = true;
	}
}