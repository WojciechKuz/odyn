package com.example.odyn.main_service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.odyn.activities.MainScreen;
import com.example.odyn.cam.Cam;
import com.example.odyn.main_service.types.IconType;

// ta klasa Service będzie służyć do zapisu/odczytu obrazu oraz zajęć pobocznych, jak powiadomienia pływające

// TODO przerób na Foreground Service
public class MainService extends Service {
	private Notification notif = null;
	private Cam cam; // dostęp do kamery

	// nagrywanie przeniesione tutaj. gdy potrzeba zdjęcia, MainScreen (Activity) może wołać tę klasę

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	} // ZWRACA NULL. Za pomocą zwracanego IBinder powinno dać się komunikować z Service'm, póki co ZWRACA NULL

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

		// utwórz Notification
		createNotif();

		ServiceConnector.setOnClickHandle(this::buttonHandler);
		ServiceConnector.setCamReceiver(this::receiveCam); // MainScreen dostarczy Cam

		// Z Logcat'a: Skipped 36 frames!  The application may be doing too much work on its main thread.
		// TODO utworzyć wątek, na kamerę
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

	// uruchamia ekran aplikacji
	private void startMainScreen() {
		// DrawerActivity zawiera MainScreen, więc ok.
		Intent startMainScreen = new Intent(this, MainScreen.class);
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
				startMainScreen(); // ? należy otworzyć MainScreen, a to tworzy.
				break;
			case display_notif:
				// nieaktywne, Service wersja 1
				break;
			case hide_notif:
				// nieaktywne, Service wersja 1
				break;
		}
	}

	public void createNotif() {
		if(notif == null) {
			notif = new NotificationCreator(this).create();
		}
	}

	private void receiveCam(Cam cam) {
		this.cam = cam;
	}

	// obsłuż intent'y. żądania nagrywania, itp.
	public void onHandleIntent(Intent intent) { // ???
		// odczytaj, co zrobić RecType i ActionType
		if(intent != null) {
			if(intent.hasExtra("RecType") && intent.hasExtra("ActionType")) {
				Log.w("MainService", ">>> odbieranie Intent'ów z polem \"RecType\" nie jest już wspierane");
			}
			if(intent.hasExtra("IconType")) {
				IconType iconType = (IconType) intent.getSerializableExtra("IconType");
				buttonHandler(iconType);
			}
		}
	}

	// w wersji service 1 te metody poniżej nieaktywne:

	// użytkownik wyszedł z aplikacji nie zamykając jej. wyświetl powiadomienie, że aplikacja nadal nagrywa. wywołać w MainScreen.onStop()
	public void appNotOnScreen() {
		notif = new NotificationCreator(this).create();
	}

	// użytkownik wszedł z powrotem do aplikacji. zamknij powiadomienie. wywołać w MainScreen.onRestart()
	public void appBackOnScreen() {
		//
		// zamknij powiadomienie ???
	}

}