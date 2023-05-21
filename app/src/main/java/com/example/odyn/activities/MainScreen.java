/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, Viacheslav Kushinir <kushnir@mat.umk.pl>, Jakub Orłowski <orljak@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
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
import com.example.odyn.cam.CamAccess;
import com.example.odyn.cam.CamInfo;
import com.example.odyn.gps.GPSThread;
import com.example.odyn.gps.GPSValues;
import com.example.odyn.gps.TextFieldChanger;
import com.example.odyn.main_service.ServiceConnector;
import com.example.odyn.main_service.types.IconType;
import com.example.odyn.gps.SRTWriter;
import com.example.odyn.gps.TimerThread;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/*  Kolejność metod:
onCreate
setupMainScreen
setupGPS

changeTextField

onStop
onRestart
onDestroy

onClickMenu
onClickPhoto
onClickEmergency
onClickRecord

onClickCloseMenu
onClickRecordinglist
onClickSettings
onClickBackground

darkSideOfMenu
*/

/**
 * Jest to aktywność odpowiadająca za główny ekran aplikacji.
 */
public class MainScreen extends AppCompatActivity {

	private TimerThread timerThread;
	private GPSThread gpsThread;
	private SRTWriter srtWriter;
	private Handler delayHandler = new Handler();

	private boolean isEmergencyActive = false;
	private boolean isVideoActive = false;

	/**
	 * Jest to metoda tworząca główny ekran aplikacji.
	 * @param savedInstanceState Wiązka argumentów
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);

		Log.d("MainScreen", ">>> onCreate DrawerActivity");

		setupMainScreen();
		setupGPS();
	}

	/**
	 * Jest to metoda odpowiadająca za ustawienie prawidłowego działania ekranu głównego.
	 */
	private void setupMainScreen() {
		ServiceConnector.setActivity(this); // static, usunięcie w onDestroy()
		Cam cam = new Cam(this);
		ServiceConnector.sendCam(cam); // zwraca do MainService, jak się da to tworzenie z powrotem przenieść do MainService

		CamInfo camInfo = cam.getCamInfo();	// TODO use bitmap and info to analyze image

		// obsługa przycisków, metody do obsługi (np. this::onClickPhoto) znajdują się poniżej
		View mainScreenLayout = findViewById(R.id.layout_incepcja);
		mainScreenLayout.findViewById(R.id.MenuButton).setOnClickListener(this::onClickMenu); // połączenie przycisku otwarcia menu
		findViewById(R.id.PhotoButton).setOnClickListener(this::onClickPhoto);
		findViewById(R.id.RecordButton).setOnClickListener(this::onClickRecord);
		findViewById(R.id.EmergencyButton).setOnClickListener(this::onClickEmergency);
	}

	/**
	 * Jest to metoda odpowiadająca za ustawienie i zainicjalizowanie działania GPS.
	 */
	// ustawia i inicjalizuje rzeczy związane z GPSem
	private void setupGPS() {
		timerThread = new TimerThread(this, this::changeTextField);
		timerThread.start();

		gpsThread = new GPSThread(this, this::changeTextField);
		gpsThread.requestGPSPermissions(); // TODO check permissions in StartActivity instead
		gpsThread.start();

		File file = new FileHandler(this).createDataFile("srt");
		srtWriter = new SRTWriter(this, this, file);
		srtWriter.requestWritePermissions();
		srtWriter.start();
	}

	/**
	 * Jest to metoda odpowiadająca za dostarczanie informacji o lokalizacji i prędkości do zapisania.
	 */
	public Map<String, String> textProvider() {
		TextView timerText = findViewById(R.id.timerText);
		TextView counterText = findViewById(R.id.counterText);
		TextView latitudeText = findViewById(R.id.latitudeText);
		TextView longitudeText = findViewById(R.id.longitudeText);
		TextView speedText = findViewById(R.id.speedText);
		TextView srtText = findViewById(R.id.srtText);

		Map<String, String> textMap = new HashMap<>();
		textMap.put("counterText", counterText.getText().toString());
		textMap.put("timerText", timerText.getText().toString());
		textMap.put("latitudeText", latitudeText.getText().toString());
		textMap.put("longitudeText", longitudeText.getText().toString());
		textMap.put("speedText", speedText.getText().toString());
		textMap.put("srtText", srtText.getText().toString());
		return textMap;
	}

	/**
	 * Jest to metoda odpowiadająca za wyświetlanie informacji o lokalizacji i prędkości do zapisania.
	 */
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
				speedText.setText(text + "km/h");
		}
	}



	// metody cyklu życia aktywności
	// Zamknij/otwórz powiadomienie (nieaktywne w wersji service 1)
	/**
	 * Jest to metoda wywoływana, gdy aplikacja przestaje być widoczna.
	 * Zadaniem tej metody jest wyświetlanie powiadomienia.
	 */
	@Override
	protected void onStop() {
		// nieaktywne, ponieważ powiadomienie widoczne cały czas. Service wersja 1
		ServiceConnector.onClickIcon(IconType.display_notif); // utwórz pływające powiadomienie
		super.onStop();
	}

	/**
	 * Jest to metoda wywoływana, gdy aplikacja jest przywracana do stanu widocznego.
	 * Zadaniem tej metody jest zamykanie powiadomienia.
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		// nieaktywne, ponieważ powiadomienie widoczne cały czas. Service wersja 1
		ServiceConnector.onClickIcon(IconType.hide_notif); // zamknij pływające powiadomienie
	}

	/**
	 * Jest to metoda wywoływana przez Android, gdy aktywność jest niszczona.
	 * Zamyka wątki, Usuwa referencje na siebie.
	 */
	@Override
	protected void onDestroy() {
		ServiceConnector.removeActivity(); // trzeba się pozbyć referencji, aby poprawnie usunąć Aktywność
		timerThread.stopTimer();
		gpsThread.stopGPS();
		srtWriter.stopWriting();
		super.onDestroy();
	}



	// Przyciski ekranu:

	// Otwórz panel menu
	/**
	 * Jest to metoda odpowiadająca za otwarcie menu aplikacji.
	 */
	public void onClickMenu(View view) {
		Log.d("MainScreen", ">>> otwórz menu");
		DrawerLayout drawer = findViewById(R.id.drawer_layout);

		int grawitacja = darkSideOfMenu();
		drawer.openDrawer(grawitacja);
	}

	// Zrób zdjęcie
	/**
	 * Jest to metoda odpowiadająca za obsługę przycisku do wykonywania zdjęcia.
	 */
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
	/**
	 * Jest to metoda odpowiadająca za obsługę przycisku do nagrywania awaryjnego.
	 */
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
			}, 5000); // What??? Po 5 sekundach wyłącz? Jaki to ma związek z czasem nagrywania? TEMPORARY
		}
	}

	// Nagraj wideo
	/**
	 * Jest to metoda odpowiadająca za obsługę przycisku do nagrywania video.
	 */
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



	// Z menu:

	// Zamknij menu
	/**
	 * Jest to metoda odpowiadająca za zamykanie menu aplikacji z poziomu menu.
	 */
	public void onClickCloseMenu(MenuItem item) {
		Log.d("MainScreen", ">>> zamknij menu");
		DrawerLayout drawer = findViewById(R.id.drawer_layout);

		int grawitacja = darkSideOfMenu();
		drawer.closeDrawer(grawitacja);
	}

	// Przejdź do listy nagrań
	/**
	 * Jest to metoda odpowiadająca za przejście do listy nagrań z poziomu menu.
	 */
	public void onClickRecordingsList(MenuItem item) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("content://media/internal/images/media"), "image/*");
		startActivity(intent);
	}

	// Przejdź do ustawień
	/**
	 * Jest to metoda odpowiadająca za przejście do ustawień z poziomu menu.
	 */
	public void onClickSettings(MenuItem item) {
		Intent doUstaw = new Intent(this, Settings.class);
		doUstaw.putExtra("settings_file", "settings.xml");
		// tu można dołączyć dodatkowe informacje dla ustawień
		startActivity(doUstaw);
	}

	// Wyjdź i nagrywaj w tle
	/**
	 * Jest to metoda odpowiadająca za wyjście z aplikacji i nagrywanie w tle.
	 */
	public void onClickBackground(MenuItem item) {
		// ???
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}



	// podaj, po której stronie menu. Gravity .LEFT / .RIGHT
	/**
	 * Jest to metoda odpowiadająca za ustawienie menu po lewej/prawej stronie.
	 */
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