/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, Mateusz Szymczak <mszymczak710@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.odyn.R;
import com.example.odyn.main_service.MainService;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

/* Do podręcznika użytkownika:
	Należy zgodzić się na wszystkie uprawnienia.
	Jeżeli nie chce się, aby aplikacja nagrywała dźwięk,
	albo pokazywała lokalizację, można to wyłączyć poprzez ustawienia aplikacji.
*/

/**
 * Jest to aktywność pytająca użytkownika o uprawnienia przed uruchomieniem głównego ekranu aplikacji.
 */
public class StartActivity extends AppCompatActivity {

	//private static final int MY_CAMERA_REQUEST_CODE = 100;
	//private static final int MY_WRITE_EXTERNAL_STORAGE = 100;
	//private static final int MY_MICROPHONE_REQUEST = 100;

	//private boolean cameraPermission = false;
	//private boolean writeExternalStoragePermission = false;
	//private boolean recordAudioPermission = false;

	private static final int permCode = 100;

	// z tego zawsze potrzebne są na pewno CAMERA, WRITE_EXTERNAL_STORAGE.
	// od ustawień zależy RECORD_AUDIO, (ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
	private final String[] perms = {
			Manifest.permission.CAMERA,
			Manifest.permission.RECORD_AUDIO,
			Manifest.permission.WRITE_EXTERNAL_STORAGE,
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION
	};

	/**
	 * Jest to metoda tworząca główny widok aplikacji.
	 * @param savedInstanceState Wiązka argumentów
	 */
	@Override
	protected synchronized void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		// laterTODO sprawdź, które uprawnienia są potrzebne w ustawieniach

		// sprawdza uprawnienia, jeśli są, uruchamia MainService (start apki)
		permCheckNew();

	}

	// sprawdzenie odpowiedzi z okna proszącego o uprawnienia
	/**
	 * Jest to metoda odpowiadająca za sprawdzenie odpowiedzi z okna proszącego o uprawnienia.
	 * @param requestCode Kod odpowiedzi
	 * @param permissions Tablica uprawień
	 * @param grantResults Tablica zwróconych kodów zgód na uprawnienia
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		StringBuilder upr = new StringBuilder("upr: ");
		if(requestCode == permCode) {
			boolean permissionSum = true;
			for(int granty : grantResults) {
				permissionSum = permissionSum && (granty == PackageManager.PERMISSION_GRANTED);
				upr.append(granty == PackageManager.PERMISSION_GRANTED).append(", ");
			}
			if(permissionSum) {
				Log.d("StartActivity", ">>> Mam uprawnienia, uruchamiam MainService");
				startMainService();
			}
			else {
				upr.append('.');
				Log.w("StartActivity", ">>> Nie wyrażono zgody na wszystkie potrzebne uprawnienia" + upr);
			}
		}

	}


	// wstępne sprawdzenie
	/**
	 * Jest to metoda odpowiadająca za wstępne sprawdzenie nowych uprawnień aplikacji.
	 */
	private void permCheckNew() {
		if (allPerm()) {
			startMainService();
		}
		else {
			ActivityCompat.requestPermissions(this, perms, permCode);
		}
	}

	/**
	 * Jest to metoda odpowiadająca za wstępne sprawdzenie wszystkich uprawnień aplikacji.
	 */
	private boolean allPerm() {
		boolean permSum = true;
		for(String perm : perms)
			permSum = permSum && ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED;
		return permSum;
	}

	// uruchom aplikację
	/**
	 * Jest to metoda uruchamiająca główny serwis aplikacji.
	 */
	private void startMainService() {
		Intent service = new Intent(this, MainService.class);
		service.putExtra("start", 1);
		startService(service);
	}

	@Deprecated
	private void startMainScreen() {
		Intent activity = new Intent(this, MainScreen.class);
		//activity.putExtra("start", 1);
		startActivity(activity);
	}
}
