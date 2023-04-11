package com.example.odyn.main_service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Icon;

import com.example.odyn.activities.MainScreen;
import com.example.odyn.R;
import com.example.odyn.cam.Cam;
import com.example.odyn.main_service.types.IconType;

// ta klasa Service będzie służyć do zapisu/odczytu obrazu oraz zajęć pobocznych, jak powiadomienia pływające
public class MainService extends IntentService {

	private Notification notif;

	private Cam cam; // dostęp do kamery

	public MainService() {
		super("MainService");
	}
	// TODO przenieść nagrywanie tutaj. gdy potrzeba zdjęcia MainScreen (Activity) może wołać tę klasę

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
		Intent startMainScreen = new Intent(this, MainScreen.class);
		// TODO put extra MainService > MainScreen
		startActivity(startMainScreen);
		ServiceConnector.setOnClickHandle(this::buttonHandler);

		// utwórz Cam, trzeba dostarczyć do konstruktora MainScreen Activity
		cam = new Cam(this, ServiceConnector.getActivity());
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
}