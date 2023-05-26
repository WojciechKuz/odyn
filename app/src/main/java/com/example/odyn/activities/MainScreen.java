/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, Viacheslav Kushinir <kushnir@mat.umk.pl>, Jakub Orłowski <orljak@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.activities;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.odyn.R;
import com.example.odyn.cam.Cam;
import com.example.odyn.cam.CamInfo;
import com.example.odyn.gps.DataHolder;
import com.example.odyn.main_service.MainService;
import com.example.odyn.main_service.ServiceConnector;
import com.example.odyn.main_service.types.IconType;
import com.example.odyn.main_service.types.IntentProvider;
import com.example.odyn.main_service.types.ServCounter;
import com.example.odyn.settings.SettingNames;
import com.example.odyn.settings.SettingOptions;
import com.example.odyn.settings.SettingsProvider;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

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

	private Handler delayHandler = new Handler();
	Timer gpsTimer = new Timer();
	private boolean isEmergencyActive = false;
	private boolean isVideoActive = true;

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
		setupVisibility();
		gpsInfoUpdate();

		NavigationView navigationView = findViewById(R.id.nav_view);
		Menu menu = navigationView.getMenu();
		for (int i = 0; i < menu.size(); i++) {
			MenuItem menuItem = menu.getItem(i);
			SpannableString spannableString = new SpannableString(menuItem.getTitle());
			spannableString.setSpan(new RelativeSizeSpan(1.7f), 0, spannableString.length(), 0);
			menuItem.setTitle(spannableString);
		}

		/*
		try {
			sleep(20);
		} catch (InterruptedException e) {
			Log.v("MainScreen", ">>> Interrupted sleep");
		}
		// domyślnie włącza nagrywanie od razu po wejściu do aplikacji
		onClickRecord(null);
		*/
	}

	/**
	 * Jest to odpowiadająca za przełączanie widoczności lokalizacji i prędkości na ekranie.
	 */
	private void setupVisibility() {
		Boolean showLocation, showSpeed;
		TextView latitudeText = findViewById(R.id.latitudeText);
		TextView longitudeText = findViewById(R.id.longitudeText);
		TextView speedText = findViewById(R.id.speedText);
		try {
			SettingsProvider settingsProvider = new SettingsProvider();
			showLocation = settingsProvider.getSettingBool(SettingNames.switches[2]);
			showSpeed = settingsProvider.getSettingBool(SettingNames.switches[4]);
			Log.e("MainScreen", ">>>" + showLocation + " " + showSpeed + "\n");
		} catch (JSONException e) {
			Log.e("MainScreen", ">>> nie załadowano ustawień, użyję wartości domyślnych\n" + e);
			showLocation = SettingOptions.defaultSwitches[2];
			showSpeed = SettingOptions.defaultSwitches[4];
		}
		if (showLocation) {
			latitudeText.setVisibility(View.VISIBLE);
			longitudeText.setVisibility(View.VISIBLE);
		} else {
			latitudeText.setVisibility(View.INVISIBLE);
			longitudeText.setVisibility(View.INVISIBLE);
		}
		if (showSpeed) {
			speedText.setVisibility(View.VISIBLE);
		} else {
			speedText.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Jest to metoda odpowiedzialna za aktualizację wartości GPS na ekranie
	 */
	private void gpsInfoUpdate() {
		final TextView latitudeText = findViewById(R.id.latitudeText);
		final TextView longitudeText = findViewById(R.id.longitudeText);
		final TextView speedText = findViewById(R.id.speedText);
		final TextView timeText = findViewById(R.id.timerText);
		TimerTask task = new TimerTask() {
			public void run() {
				runOnUiThread(new Runnable() {
					public void run() {
						DataHolder dataHolder = DataHolder.getInstance();
						latitudeText.setText(dataHolder.getLatitude());
						longitudeText.setText(dataHolder.getLongitude());
						speedText.setText(dataHolder.getSpeed());
						timeText.setText(dataHolder.getTimer());
						setupVisibility();
					}
				});
			}
		};
		gpsTimer.schedule(task, 0, 1000);
	}

	/**
	 * Jest to metoda odpowiadająca za ustawienie prawidłowego działania ekranu głównego.
	 */
	private void setupMainScreen() {
		ServiceConnector.setActivity(this); // static, usunięcie w onDestroy()
		Cam cam = new Cam(this);
		ServiceConnector.sendCam(cam); // zwraca do MainService, jak się da to tworzenie z powrotem przenieść do MainService

		CamInfo camInfo = null;
		if(cam.canIgetCamInfo()) {
			camInfo = cam.getCamInfo();	// TODO use bitmap and info to analyze image
			Log.d("MainScreen", ">>> Otrzymano CamInfo");
		}
		else {
			Log.d("MainScreen", ">>> Nie można tu jeszcze otrzymać CamInfo");
		}

		// obsługa przycisków, metody do obsługi (np. this::onClickPhoto) znajdują się poniżej
		View mainScreenLayout = findViewById(R.id.layout_incepcja);
		mainScreenLayout.findViewById(R.id.MenuButton).setOnClickListener(this::onClickMenu); // połączenie przycisku otwarcia menu
		findViewById(R.id.PhotoButton).setOnClickListener(this::onClickPhoto);
		findViewById(R.id.RecordButton).setOnClickListener(this::onClickRecord);
		findViewById(R.id.EmergencyButton).setOnClickListener(this::onClickEmergency);
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
		Log.d("MainScreen", ">>> onDestroy, ilość kamer: " + ServCounter.getCamCount());
		gpsTimer.cancel();
		super.onDestroy();
	}



	// Przyciski ekranu:

	// Otwórz panel menu
	/**
	 * Jest to metoda odpowiadająca za otwarcie menu aplikacji.
	 * @param view Widok
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
	 * @param view Widok
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
	 * @param view Widok
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
			}, 5000); // TODO: make this use value of emergency recording from settings
		}
	}

	// Nagraj wideo
	/**
	 * Jest to metoda odpowiadająca za obsługę przycisku do nagrywania video.
	 * @param view Widok
	 */
	public void onClickRecord(View view) {
		Log.d("MainScreen", ">>> nagraj");
		ServiceConnector.onClickIcon(IconType.recording);

		// zmiana koloru ikony, aby zasygnalizować nagrywanie
		ImageButton recordButton = findViewById(R.id.RecordButton);
		if (isVideoActive) {
			recordButton.setImageResource(R.drawable.record);
			isVideoActive = false;
		} else {
			recordButton.setImageResource(R.drawable.record_active);
			isVideoActive = true;
		}
	}



	// Z menu:

	// Zamknij menu
	/**
	 * Jest to metoda odpowiadająca za zamykanie menu aplikacji z poziomu menu.
	 * @param item Pozycja w menu
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
	 * @param item Pozycja w menu
	 */
	public void onClickRecordingsList(MenuItem item) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.parse("content://media/internal/images/media"), "image/*");
		startActivity(intent);
	}

	// Przejdź do ustawień
	/**
	 * Jest to metoda odpowiadająca za przejście do ustawień z poziomu menu.
	 * @param item Pozycja w menu
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
	 * @param item Pozycja w menu
	 */
	public void onClickBackground(MenuItem item) {
		// ???
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		startActivity(intent);
	}

	/**
	 * Wyłącz całą aplikację, zamknij powiadomienie.
	 * Najpierw wyłącza service, zakańczając pisanie do pliku wideo i srt.
	 * Potem zamyka samą aplikację. Aby całkowicie zamknąć aplikację należy usunąć ją z listy aktywnych aplikacji
	 * na androidzie (kwadrat na pasku na dole, albo przeciągnąć w prawo).
	 * @param item Pozycja w menu
	 */
	public synchronized void onClickTurnOff(MenuItem item) {
		stopService(new Intent(this, MainService.class));
		finishAffinity();
	}



	// podaj, po której stronie menu. Gravity .LEFT / .RIGHT
	/**
	 * Jest to metoda odpowiadająca za ustawienie menu po lewej/prawej stronie.
	 */
	@SuppressLint("RtlHardcoded")
	private int darkSideOfMenu() {
		int leftOrRight = -1;
		try {
			leftOrRight = new SettingsProvider().getSettingInt(SettingNames.spinners[2]);
		} catch (JSONException e) {
			Log.e("MainScreen", ">>> Nie udało się odczytać ustawień\n" + e);
			leftOrRight = 0;
		}
		if(leftOrRight == 0) {
			return Gravity.LEFT;
		}
		else if (leftOrRight == 1){
			return Gravity.RIGHT;
		}
		else {
			Log.wtf("MainScreen", ">>> niespotykana jak dotąd wartość leftOrRight: " + leftOrRight);
			return Gravity.LEFT;
		}
	}

	/**
	 * nadpisanie tej metody zapobiega cofaniu do StartActivity po naciśnięciu systemowego przycisku wstecz
	 */
	@Override
	public void onBackPressed() {
		DrawerLayout draw = findViewById(R.id.drawer_layout);
		if(draw.isOpen())
			onClickCloseMenu(null);
		setResult(RESULT_CANCELED);
		//super.onBackPressed();
	}
}