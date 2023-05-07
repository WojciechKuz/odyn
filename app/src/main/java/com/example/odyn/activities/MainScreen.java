package com.example.odyn.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.odyn.R;
import com.example.odyn.cam.Cam;
import com.example.odyn.main_service.ServiceConnector;
import com.example.odyn.main_service.types.IconType;

import java.io.File;

/**
 * Jest to klasa odpowiadająca za główny ekran aplikacji.
 */
public class MainScreen extends AppCompatActivity {

	private TimerThread timerThread;
	private GPSThread gpsThread;
	private SRTWriter srtWriter;

	private TextView counterText, timerText, latitudeText, longitudeText, srtText;

	/**
	 * Jest to metoda tworząca główny ekran aplikacji.
	 * @param savedInstanceState Wiązka argumentów
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer);

		Log.d("MainScreen", ">>> onCreate DrawerActivity");

		timerText = findViewById(R.id.timerText);
		counterText = findViewById(R.id.counterText);
		timerThread = new TimerThread(counterText, timerText);
		timerThread.start();

		latitudeText = findViewById(R.id.latitudeText);
		longitudeText = findViewById(R.id.longitudeText);
		gpsThread = new GPSThread(this, latitudeText, longitudeText);
		gpsThread.requestGPSPermissions();
		gpsThread.start();


		File file = new File(getExternalFilesDir(null), "myfile.srt");
		srtText = findViewById(R.id.srtText);
		srtWriter = new SRTWriter(this, file, counterText, timerText, latitudeText, longitudeText, srtText);
		srtWriter.requestWritePermissions();
		srtWriter.start();

		// połączenie przycisku otwarcia menu
		setupMainScreen();
	}

	/**
	 * Jest to metoda odpowiadająca za ustawienie prawidłowego działania ekranu głównego
	 */
	private void setupMainScreen() {
		ServiceConnector.setActivity(this); // static, usunięcie w onDestroy()
		ServiceConnector.sendCam(createCam()); // jak się da to tworzenie z powrotem przenieść do MainService

		// obsługa przycisków
		View mainScreenLayout = findViewById(R.id.layout_incepcja);
		mainScreenLayout.findViewById(R.id.MenuButton).setOnClickListener(this::onClickMenu); // ok
		findViewById(R.id.EmergencyButton).setOnClickListener(this::onClickEmergency);
		findViewById(R.id.PhotoButton).setOnClickListener(this::onClickPhoto);
		findViewById(R.id.RecordButton).setOnClickListener(this::onClickRecord);
	}
	// zwraca do MainService
	/**
	 * Jest to metoda odpowiadająca za utworzenie kamery.
	 */
	public Cam createCam() {
		return new Cam(this);
	}


	// Zamknij/otwórz powiadomienie (nieaktywne w wersji service 1)
	/**
	 * Jest to metoda odpowiadająca za utworzenie pływającego powiadomienia.
	 */
	@Override
	protected void onStop() {
		// nieaktywne, ponieważ powiadomienie widoczne cały czas. Service wersja 1
		ServiceConnector.onClickIcon(IconType.display_notif); // utwórz pływające powiadomienie
		super.onStop();
	}

	/**
	 * Jest to metoda odpowiadająca za zamknięcie pływającego powiadomienia.
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		// nieaktywne, ponieważ powiadomienie widoczne cały czas. Service wersja 1
		ServiceConnector.onClickIcon(IconType.hide_notif); // zamknij pływające powiadomienie
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
	 * Jest to metoda odpowiadająca za wykonanie zdjęcia.
	 */
	public void onClickPhoto(View view) {
		Log.d("MainScreen", ">>> zrób zdjęcie");
		ServiceConnector.onClickIcon(IconType.photo);
	}

	// Nagrywanie awaryjne
	/**
	 * Jest to metoda odpowiadająca za nagrywanie awaryjne.
	 */
	public void onClickEmergency(View view) {
		Log.d("MainScreen", ">>> nagrywanie awaryjne");
		ServiceConnector.onClickIcon(IconType.emergency);
	}

	// Nagraj wideo
	/**
	 * Jest to metoda odpowiadająca za nagrywanie video.
	 */
	public void onClickRecord(View view) {
		Log.d("MainScreen", ">>> nagraj");
		ServiceConnector.onClickIcon(IconType.recording);
	}

	/**
	 * Jest to metoda odpowiadająca za usunięcie klasy MainScreen i powstałych pozostałości po zakończeniu pracy programu.
	 */
	@Override
	protected void onDestroy() {
		ServiceConnector.removeActivity(); // trzeba się pozbyć referencji, aby poprawnie usunąć Aktywność
		timerThread.stopTimer();
		gpsThread.stopGPS();
		super.onDestroy();
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
		// Początkowo może przekierowywać do innej aplikacji

		Intent doListy = new Intent(this, RecordingList.class);
		// tu można dołączyć dodatkowe informacje dla listy nagrań
		startActivity(doListy);
	}

	/**
	 * Jest to metoda odpowiadająca za przejście do ustawień z poziomu menu.
	 */
	// Przejdź do ustawień
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