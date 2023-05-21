/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.main_service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;

import com.example.odyn.R;
import com.example.odyn.activities.MainScreen;
import com.example.odyn.cam.RecType;
import com.example.odyn.main_service.types.IconProvider;
import com.example.odyn.main_service.types.IconType;

public class NotificationCreator {
	private Context context;
	public NotificationCreator(Context context) {
		this.context = context;
	}

	// użyj, aby utworzyć powiadomienie
	public Notification create() {
		Notification.Builder builder = new Notification.Builder(context);
		builder
				.setSmallIcon(android.R.drawable.sym_def_app_icon)
				//.setLargeIcon(bitmap) // TODO, potrzebna ikonka
				.setContentTitle(context.getString(R.string.notif_title))
				.setContentText(context.getString(R.string.notif_text))
				.setAutoCancel(false);

		// TODO ikonki akcji
		builder.addAction(getAction(IconType.emergency));
		builder.addAction(getAction(IconType.recording));
		builder.addAction(getAction(IconType.photo));
		builder.addAction(getAction(IconType.back_to_app));

		builder.setContentIntent(contentIntent()); // naciśniesz > otworzy się apka
		/* ✔ utworzone
		 *  ✔ ikonka (domyślna)
		 *  ✔ tekst
		 *  ✔ akcja po naciśnięciu powiadomienia: otwórz aplikację
		 * ❌ przyciski akcji: zdjęcie, nagraj, emergency, zamknij
		 * ❌ nagrywanie
		 * */
		return builder.build();
	}

	// akcja po naciśnięciu tła powiadomienia - powrót do aplikacji
	private PendingIntent contentIntent() {
		// po naciśnięciu tła powiadomienia otworzy się ekran główny
		Intent openApp = new Intent(context, MainScreen.class);
		return PendingIntent.getActivity(context, 0, openApp, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	// ustaw akcje dostępne pod przyciskami powiadomienia
	private Notification.Action getAction(IconType iconType) {
		Notification.Action.Builder builder = new Notification.Action.Builder(
				Icon.createWithResource(context, IconProvider.getIconId(iconType, false)),
				iconType.toString(),
				(PendingIntent) null	// TODO dodaj Intent'a, zamień null'a
		);
		// FIXME jeśli ikony nie wyświetlają się poprawnie, to zmień 4 linijki wyżej z getIcon(..., false) na getIcon(..., true)

		return builder.build();
	}
	private String recTypeProvider(RecType type) {
		//
		return null; // temporary, TODO
	}
	private PendingIntent pendingIntentProvider(RecType type) {
		//
		return null; // temporary, TODO
	}

	/*
	public void closeNotification() {
		//
	}
	*/
}
