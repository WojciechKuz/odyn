package com.example.odyn.main_service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.util.Log;

import com.example.odyn.activities.MainScreen;
import com.example.odyn.R;
import com.example.odyn.cam.Cam;
import com.example.odyn.main_service.types.IconType;

// ta klasa Service będzie służyć do zapisu/odczytu obrazu oraz zajęć pobocznych, jak powiadomienia pływające
public class MainService extends IntentService {

	private Notification notif;

	// nagrywanie przeniesione tutaj. gdy potrzeba zdjęcia MainScreen (Activity) może wołać tę klasę
	private Cam cam; // dostęp do kamery

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

	private void buttonHandler(IconType it) {
		// tu obsłuż przyciski, te powiązane z wideo przekaż do Cam
		switch(it) {
			// bloki switch case działają jak goto label, więc dozwolone.
			case photo:
			case recording:
			case emergency:
				cam.camAction(it);
				break;
			case close:
				// todo
				break;
			case menu:
				// todo
				break;
			case back_to_app:
				// todo
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
		// TODO zamknij powiadomienie
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
}