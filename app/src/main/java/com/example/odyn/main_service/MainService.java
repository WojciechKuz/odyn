package com.example.odyn.main_service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.odyn.activities.DrawerActivity;
import com.example.odyn.activities.MainScreen;
import com.example.odyn.cam.Cam;
import com.example.odyn.main_service.types.IconType;

// ta klasa Service będzie służyć do zapisu/odczytu obrazu oraz zajęć pobocznych, jak powiadomienia pływające

// TODO przerób na Foreground Service
public class MainService extends Service {

	// Musi pracować jakaś pętla, żeby MainService działało. gdy nic nie robi jest usuwane.

	private Notification notif;
	private Cam cam; // dostęp do kamery

	//private MainScreen mainScreen;


	// nagrywanie przeniesione tutaj. gdy potrzeba zdjęcia, MainScreen (Activity) może wołać tę klasę

	@Nullable
	@Override
	public IBinder onBind(Intent intent) { // TODO zbadaj co to
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		//return super.onStartCommand(intent, flags, startId);

		if(intent != null && intent.hasExtra("start"))
		{
			if(intent.getIntExtra("start", 0) != 1) {
				Log.w("MainService", ">>> MainService started with wrong value in intent!");
			}
		}
		mainServiceStart();

		Log.v("MainService", ">>> MainService started");
		return START_STICKY; // uruchomienie / wyłączenie serwisu, tylko gdy się tego zażąda
	}

	private synchronized void mainServiceStart() { // ma się wykonywać pokolei
		Log.v("MainService", ">>> setting up MainService");

		// KOLEJNOŚĆ TWORZENIA WERSJA 1

		// utwórz MainScreen
		startMainScreen();

		// TODO utwórz Notification

		ServiceConnector.setOnClickHandle(this::buttonHandler);
		ServiceConnector.setCamReciever(this::receiveCam); // MainScreen dostarczy Cam

		// utwórz Cam, trzeba dostarczyć do konstruktora MainScreen Activity
		//cam = new Cam(ServiceConnector.getActivity(), ServiceConnector.getActivity());
		// tutaj nie działa, cam musi być utworzony w głównym wątku
		// OD TERAZ: Cam tworzone w MainScreen.createCam() i przekazywane tu do setCam()
	}
	private void startMainScreen() {
		// DrawerActivity zawiera MainScreen
		Intent startMainScreen = new Intent(this, DrawerActivity.class);
		startMainScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMainScreen);
	}


	// tu obsłuż przyciski, te powiązane z wideo przekaż do Cam
	private void buttonHandler(IconType it) {
		// niestety, android jest oparty starym JDK i nie ma enchanced switch'a
		switch(it) {
			// bloki switch case działają jak go to label, więc dozwolone.
			case photo:
			case recording:
			case emergency:
				Log.d("MainService", ">>> naciśnięto jeden z przycisków");
				cam.camAction(it);
				break;
			case close:
				// todo - zamknij apkę
				break;
			case menu:
				// nottodo: MainScreen będzie otwierać menu
				break;
			case back_to_app:
				startMainScreen(); // ? należy otworzyć MainScreen, a to tworzy. Można tak???
				break;
			case display_notif:
				// nieaktywne, Service wersja 1
				break;
			case hide_notif:
				// nieaktywne, Service wersja 1
				break;
		}
	}

	// użytkownik wyszedł z aplikacji nie zamykając jej. wyświetl powiadomienie, że aplikacja nadal nagrywa. wywołać w MainScreen.onStop()
	public void appNotOnScreen() {
		notif = new NotificationCreator(this).create();
	}

	// użytkownik wszedł z powrotem do aplikacji. zamknij powiadomienie. wywołać w MainScreen.onRestart()
	public void appBackOnScreen() {
		//
		// zamknij powiadomienie ???
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.v("MainService", ">>> MainService created");
	}

	@Override
	public void onDestroy() {
		Log.v("MainService", ">>> MainService destroyed");
		super.onDestroy();
	}

	private void receiveCam(Cam cam) {
		this.cam = cam;
	}

	/*

	// Do niedawna to był IntentService, dlatego są tutaj te śmieci
	// Proszę, nie usuwaj :(	Szkoda mi kodu, który napisałem

	public MainService() {
		super("MainService");
	}
	@Override
	protected void onHandleIntent(Intent intent) {
		// obsługa Intent'ów
		// po uruchomieniu service też tutaj.
		if(intent == null) {
			return; // albo exception
		}
		if(intent.hasExtra("start")) {
			int code = intent.getIntExtra("start", 0);
			if(code == 1) {
				mainServiceStart();
			}
		}
	}

	private synchronized void mainServiceStart() { // ma się wykonywać pokolei
		// zamiast konstruktora
		Log.v("MainService", ">>> starting & setting up MainService");
		Intent startMainScreen = new Intent(this, MainScreen.class);
		startMainScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(startMainScreen);

		ServiceConnector.setOnClickHandle(this::buttonHandler);
		ServiceConnector.setService(this);

		// utwórz Cam, trzeba dostarczyć do konstruktora MainScreen Activity
		//cam = new Cam(ServiceConnector.getActivity(), ServiceConnector.getActivity());
		// tutaj nie działa, cam musi być utworzony w głównym wątku
		// OD TERAZ: Cam tworzone w MainScreen.createCam() i przekazywane tu do setCam()
	}

	public void setCam(Cam cam) {
		this.cam = cam;
	}
	*/

}