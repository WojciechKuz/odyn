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
import com.example.odyn.gps.GPSValues;
import com.example.odyn.main_service.ServiceConnector;
import com.example.odyn.main_service.types.IconType;
import com.example.odyn.gps.SRTWriter;
import com.example.odyn.gps.TimerThread;

import java.io.File;

public class MainScreen extends AppCompatActivity {

	private TimerThread timerThread;
	private GPSThread gpsThread;
	private SRTWriter srtWriter;

	private Handler delayHandler = new Handler();

	private boolean isEmergencyActive = false;
	private boolean isVideoActive = false;

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

		// obsługa przycisków, metody do obsługi (np. this::onClickPhoto) znajdują się poniżej
		View mainScreenLayout = findViewById(R.id.layout_incepcja);
		mainScreenLayout.findViewById(R.id.MenuButton).setOnClickListener(this::onClickMenu); // ok
		findViewById(R.id.PhotoButton).setOnClickListener(this::onClickPhoto);
		findViewById(R.id.RecordButton).setOnClickListener(this::onClickRecord);
		findViewById(R.id.EmergencyButton).setOnClickListener(this::onClickEmergency);
	}
	// zwraca do MainService
	public Cam createCam() {
		return new Cam(this);
	}

	// ustawia i inicjalizuje rzeczy związane z GPSem
	private void setupGPS() {
		TextView timerText = findViewById(R.id.timerText);
		TextView counterText = findViewById(R.id.counterText);
		timerThread = new TimerThread(this::changeTextField);
		timerThread.start();

		TextView latitudeText = findViewById(R.id.latitudeText);
		TextView longitudeText = findViewById(R.id.longitudeText);
		TextView speedText = findViewById(R.id.speedText);
		gpsThread = new GPSThread(this, this::changeTextField);
		gpsThread.requestGPSPermissions(); // TODO check permissions in StartActivity instead
		gpsThread.start();


		File file = new FileHandler(this).createDataFile("srt");
		TextView srtText = findViewById(R.id.srtText);
		srtWriter = new SRTWriter(this, file, counterText, timerText, latitudeText, longitudeText, srtText);
		srtWriter.requestWritePermissions();
		srtWriter.start();
	}
	private void changeTextField(String text, GPSValues whatValue) {
		switch(whatValue) {
			case timer:
				TextView timerText = findViewById(R.id.timerText);
				timerText.setText(text);
				break;
			case counter:
				TextView counterText = findViewById(R.id.counterText);
				counterText.setText(text);
				break;
			case latitude:
				TextView latitudeText = findViewById(R.id.latitudeText);
				latitudeText.setText(text);
				break;
			case longitude:
				TextView longitudeText = findViewById(R.id.longitudeText);
				longitudeText.setText(text);
				break;
			case speed:
				TextView speedText = findViewById(R.id.speedText);
				speedText.setText(text);
		}
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

		// na 0,6s zmień ikonkę, aby było widać, że kliknięto
		ImageButton PhotoButton = (ImageButton) view;
		PhotoButton.setImageResource(R.drawable.photo_active);
		delayHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				PhotoButton.setImageResource(R.drawable.photo);
				isEmergencyActive = false;
			}
		}, 600);
	}

	// Nagrywanie awaryjne
	public void onClickEmergency(View view) {
		Log.d("MainScreen", ">>> nagrywanie awaryjne");
		ServiceConnector.onClickIcon(IconType.emergency);

		// FIXME this is Temporary code, because there's no emergency recording feature yet
		ImageButton emergencyButton = (ImageButton) view;
		if (!isEmergencyActive) {
			emergencyButton.setImageResource(R.drawable.emergency_active);
			isEmergencyActive = true;
			delayHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					emergencyButton.setImageResource(R.drawable.emergency3ungroup);
					isEmergencyActive = false;
				}
			}, 5000); // What??? Po 5 sekundach wyłącz? Jaki to ma związek z czasem nagrywania? Jeśli tymczasowe, to powinno być oznaczone
		}
	}

	// Nagraj wideo
	public void onClickRecord(View view) {
		Log.d("MainScreen", ">>> nagraj");
		ServiceConnector.onClickIcon(IconType.recording);

		// zmiana koloru ikony, aby zasygnalizować nagrywanie
		ImageButton recordButton = findViewById(R.id.RecordButton);
		if (!isVideoActive) {
			recordButton.setImageResource(R.drawable.record_active);
			isVideoActive = true;
		} else {
			recordButton.setImageResource(R.drawable.record);
			isVideoActive = false;
		}
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