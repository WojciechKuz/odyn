package com.example.odyn.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.odyn.R;
import com.example.odyn.main_service.MainService;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

public class StartActivity extends AppCompatActivity {

	private static final int MY_CAMERA_REQUEST_CODE = 100;
	private static final int MY_WRITE_EXTERNAL_STORAGE = 100;

	private static final int MY_MICROPHONE_REQUEST = 100;

	private boolean cameraPermission = false;

	private boolean writeExternalStoragePermission = false;

	private boolean recordAudioPermission = false;


	@Override
	protected synchronized void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		// pytania o uprawnienia
		permissionCheck();

		// TODO nie uruchamiaj bez uprawnień, przycisk do ponownego zapytania o uprawn.
		// np. w pętli co 1s sprawdzać uprawnienia, jeśli są uruchom MainService

		// start MainService
		if(cameraPermission && writeExternalStoragePermission && recordAudioPermission) {
			startMainService();
		}  else {
			Log.w("StartActivity", ">>> nie uruchomiono mainService, ponieważ nie wyrażono zgody na uprawnienia");
		}
	}
	private void permissionCheck() {
		// sprawdzenie uprawnień do aparatu i pamięci wewn.
		if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
		} else {
			cameraPermission = true;
		}
		if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_EXTERNAL_STORAGE);
		} else {
			writeExternalStoragePermission = true;
		}
		if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, MY_MICROPHONE_REQUEST);
		} else {
			recordAudioPermission = true;
		}
	}

	private void startMainService() {
		Intent service = new Intent(this, MainService.class);
		service.putExtra("start", 1);
		startService(service);
	}
	private void startMainScreen() {
		Intent activity = new Intent(this, MainScreen.class);
		//activity.putExtra("start", 1);
		startActivity(activity);
	}
}