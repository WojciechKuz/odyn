package com.example.odyn.activities;

import androidx.appcompat.app.AppCompatActivity;
import com.example.odyn.R;
import com.example.odyn.main_service.MainService;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {

	private static final int MY_CAMERA_REQUEST_CODE = 100;
	private static final int MY_WRITE_EXTERNAL_STORAGE = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		// pytania o uprawnienia
		permissionCheck();

		// TODO nie uruchamiaj bez uprawnień, przycisk do ponownego zapytania o uprawn.
		// np. w pętli co 1s sprawdzać uprawnienia, jeśli są uruchom MainService

		// start MainService
		startMainService();
	}
	private void permissionCheck() {
		// sprawdzenie uprawnień do aparatu i pamięci wewn.
		if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
		}
		if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_EXTERNAL_STORAGE);
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