package com.example.odyn.main_service;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.drawable.Icon;

import com.example.odyn.activities.MainScreen;
import com.example.odyn.R;
import com.example.odyn.cam.RecType;
import com.example.odyn.main_service.types.IconProvider;
import com.example.odyn.main_service.types.IconType;

// ta klasa Service będzie służyć do zapisu/odczytu obrazu oraz zajęć pobocznych, jak powiadomienia pływające
public class MainService extends IntentService {

	private Notification notif;

	public MainService() {
		super("MainService");
	}
	// TODO przenieść nagrywanie tutaj. gdy potrzeba zdjęcia MainScreen (Activity) może wołać tę klasę

	@Override
	protected void onHandleIntent(Intent intent) {
		// obsługa Intent'ów
		// po uruchomieniu service też tutaj.

	}

	// użytkownik wyszedł z aplikacji nie zamykając jej. wyświetl powiadomienie, że aplikacja nadal nagrywa. wywołać w MainScreen.onStop()
	public void appNotOnScreen() {
		Notification.Builder builder = new Notification.Builder(this);
		builder
				.setSmallIcon(android.R.drawable.sym_def_app_icon)
				//.setLargeIcon(bitmap) // TODO, potrzebna ikonka
				.setContentTitle(getString(R.string.notif_title))
				.setContentText(getString(R.string.notif_text))
				.setAutoCancel(false);

		// TODO ikonki akcji

		builder.setContentIntent(contentIntent()); // naciśniesz > otworzy się apka
		/* ✔ utworzone
		*  ✔ ikonka (domyślna)
		*  ✔ tekst
		*  ✔ akcja po naciśnięciu powiadomienia: otwórz aplikację
		* ❌ przyciski akcji: zdjęcie, nagraj, emergency, zamknij
		* ❌ nagrywanie
		* */
		notif = builder.build();
	}
	private PendingIntent contentIntent() {
		// po naciśnięciu tła powiadomienia otworzy się ekran główny
		Intent openApp = new Intent(this, MainScreen.class);
		return PendingIntent.getActivity(this, 0, openApp, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	private Notification.Action getAction(IconType iconType) {
		Notification.Action.Builder builder = new Notification.Action.Builder(
				Icon.createWithResource(this, IconProvider.getIconId(iconType, false)),
				iconType.toString(),
				(PendingIntent) null	// TODO zamień null'a
		);
		// FIXME jeśli ikony nie wyświetlają się poprawnie, to zmień 4 linijki wyżej getIcon(..., false) na getIcon(..., true)

		return null; // temporary, TODO
	}
	private String recTypeProvider(RecType type) {
		//
		return null; // temporary, TODO
	}
	private PendingIntent pendingIntentProvider(RecType type) {
		//
		return null; // temporary, TODO
	}

	// użytkownik wszedł z powrotem aplikacji. zamknij powiadomienie. wywołać w MainScreen.onRestart()
	public void appBackOnScreen() {
		//
		// TODO zamknij powiadomienie
	}
}