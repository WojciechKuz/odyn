package com.example.odyn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.odyn.FileHandler;
import com.example.odyn.R;
import com.example.odyn.cam.Cam;
import com.example.odyn.gps.GPSThread;
import com.example.odyn.main_service.ServiceConnector;
import com.example.odyn.main_service.types.IconType;
import com.example.odyn.tools.SRTWriter;
import com.example.odyn.tools.TimerThread;

import java.io.File;

public class MainScreen extends AppCompatActivity {

	private TimerThread timerThread;
	private GPSThread gpsThread;
	private SRTWriter srtWriter;

	private Handler emergencyHandler = new Handler();

	private boolean isEmergencyActive = false, isVideoActive = false;

	private TextView counterText, timerText, latitudeText, longitudeText, srtText, speedText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);

		Log.d("MainScreen", ">>> onCreate DrawerActivity");

		setupGPS();
		// połączenie przycisku otwarcia menu
		setupMainScreen();
	}

	private void setupMainScreen() {
		ServiceConnector.setActivity(this); // static, usunięcie w onDestroy()
		ServiceConnector.sendCam(createCam()); // jak się da to tworzenie z powrotem przenieść do MainService

		// obsługa przycisków
		View mainScreenLayout = findViewById(R.id.layout_incepcja);
		mainScreenLayout.findViewById(R.id.MenuButton).setOnClickListener(this::onClickMenu); // ok
		ImageButton EmergencyButton = findViewById(R.id.EmergencyButton);
		EmergencyButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isEmergencyActive == false) {
					EmergencyButton.setImageResource(R.drawable.emergency_active);
					isEmergencyActive = true;
					emergencyHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							EmergencyButton.setImageResource(R.drawable.emergency3ungroup);
							isEmergencyActive = false;
						}
					}, 5000);
				}
				onClickEmergency(mainScreenLayout);
			}
		});

		ImageButton PhotoButton = findViewById(R.id.PhotoButton);
		PhotoButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isEmergencyActive == false) {
					PhotoButton.setImageResource(R.drawable.photo_active);
					isEmergencyActive = true;
					emergencyHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							PhotoButton.setImageResource(R.drawable.photo);
							isEmergencyActive = false;
						}
					}, 300);
				}
				onClickPhoto(mainScreenLayout);
			}
		});

		ImageButton RecordButton = findViewById(R.id.RecordButton);
		RecordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isEmergencyActive == false) {
					RecordButton.setImageResource(R.drawable.record_active);
					isEmergencyActive = true;
				} else {
					RecordButton.setImageResource(R.drawable.record);
					isEmergencyActive = true;
				}
				onClickRecord(mainScreenLayout);
			}
		});
	}
	// zwraca do MainService
	public Cam createCam() {
		return new Cam(this);
	}

	private void setupGPS() {
		timerText = findViewById(R.id.timerText);
		counterText = findViewById(R.id.counterText);
		timerThread = new TimerThread(counterText, timerText);
		timerThread.start();

		latitudeText = findViewById(R.id.latitudeText);
		longitudeText = findViewById(R.id.longitudeText);
		speedText = findViewById(R.id.speedText);
		gpsThread = new GPSThread(this, latitudeText, longitudeText, speedText);
		gpsThread.requestGPSPermissions();
		gpsThread.start();


		File file = new FileHandler(this).createDataFile("srt");
		srtText = findViewById(R.id.srtText);
		srtWriter = new SRTWriter(this, file, counterText, timerText, latitudeText, longitudeText, srtText);
		srtWriter.requestWritePermissions();
		srtWriter.start();
	}


	// Zamknij/otwórz powiadomienie (nieaktywne w wersji service 1)
	@Override
	protected void onStop() {
		// nieaktywne, ponieważ powiadomienie widoczne cały czas. Service wersja 1
		ServiceConnector.onClickIcon(IconType.display_notif); // utwórz pływające powiadomienie
		super.onStop();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		// nieaktywne, ponieważ powiadomienie widoczne cały czas. Service wersja 1
		ServiceConnector.onClickIcon(IconType.hide_notif); // zamknij pływające powiadomienie
	}


	// Przyciski ekranu:

	// Otwórz panel menu
	public void onClickMenu(View view) {
		Log.d("MainScreen", ">>> otwórz menu");
		DrawerLayout drawer = findViewById(R.id.drawer_layout);

		int grawitacja = darkSideOfMenu();
		drawer.openDrawer(grawitacja);
	}

	// Zrób zdjęcie
	public void onClickPhoto(View view) {
		Log.d("MainScreen", ">>> zrób zdjęcie");
		ServiceConnector.onClickIcon(IconType.photo);
	}

	// Nagrywanie awaryjne
	public void onClickEmergency(View view) {
		Log.d("MainScreen", ">>> nagrywanie awaryjne");
		ServiceConnector.onClickIcon(IconType.emergency);
	}

	// Nagraj wideo
	public void onClickRecord(View view) {
		Log.d("MainScreen", ">>> nagraj");
		ServiceConnector.onClickIcon(IconType.recording);
	}

	@Override
	protected void onDestroy() {
		ServiceConnector.removeActivity(); // trzeba się pozbyć referencji, aby poprawnie usunąć Aktywność
		timerThread.stopTimer();
		gpsThread.stopGPS();
		super.onDestroy();
	}



	// Z menu:

	// Zamknij menu
	public void onClickCloseMenu(MenuItem item) {
		Log.d("MainScreen", ">>> zamknij menu");
		DrawerLayout drawer = findViewById(R.id.drawer_layout);

		int grawitacja = darkSideOfMenu();
		drawer.closeDrawer(grawitacja);
	}

	// Przejdź do listy nagrań
	public void onClickRecordingsList(MenuItem item) {
		// Początkowo może przekierowywać do innej aplikacji

		Intent doListy = new Intent(this, RecordingList.class);
		// tu można dołączyć dodatkowe informacje dla listy nagrań
		startActivity(doListy);
	}

	// Przejdź do ustawień
	public void onClickSettings(MenuItem item) {
		Intent doUstaw = new Intent(this, Settings.class);
		doUstaw.putExtra("settings_file", "settings.xml");
		// tu można dołączyć dodatkowe informacje dla ustawień
		startActivity(doUstaw);
	}

	// Wyjdź i nagrywaj w tle
	public void onClickBackground(MenuItem item) {
		// ???
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}



	// podaj, po której stronie menu. Gravity .LEFT / .RIGHT
	@SuppressLint("RtlHardcoded")
	private int darkSideOfMenu() {
		if(true/* todo put setting here */) {
			return Gravity.LEFT;
		}
		else {
			return Gravity.RIGHT;
		}
	}
}