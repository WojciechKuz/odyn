/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.main_service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.odyn.R;
import com.example.odyn.activities.MainScreen;
import com.example.odyn.cam.RecType;
import com.example.odyn.main_service.types.IconProvider;
import com.example.odyn.main_service.types.IconType;
import com.example.odyn.main_service.types.IntentProvider;

/**
 Jest to klasa odpowiedzialna za tworzenie powiadomień.
 */
public class NotificationCreator {
	private Context context;
	public NotificationCreator(Context context) {
		this.context = context;
	}

	// użyj, aby utworzyć powiadomienie
	/**
	 Jest to metoda służąca do tworzenia powiadomień.
	 */
	public Notification create() {
		Notification.Builder builder = new Notification.Builder(context);
		builder
				.setSmallIcon(android.R.drawable.sym_def_app_icon)
				//.setLargeIcon(bitmap) // TODO, potrzebna ikonka
				.setContentTitle(context.getString(R.string.notif_title))
				.setContentText(context.getString(R.string.notif_text))
				.setPriority(Notification.PRIORITY_DEFAULT)
				.setAutoCancel(false);
				//.setOngoing(true)

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			builder.setChannelId(setChannel());
		}

		// TODO ikonki akcji
		builder.addAction(getAction(IconType.emergency));
		builder.addAction(getAction(IconType.recording));
		builder.addAction(getAction(IconType.photo));
		builder.addAction(getAction(IconType.back_to_app));

		builder.setContentIntent(contentIntent()); // naciśniesz > otworzy się apka
		/*  ✔ utworzone
		 *  ✔ ikonka (domyślna)
		 *  ✔ tekst
		 *  ✔ akcja po naciśnięciu powiadomienia: otwórz aplikację
		 *  ✔ przyciski akcji: zdjęcie, nagraj, emergency, zamknij
		 *  ✔ nagrywanie
		 * */
		return builder.build();
	}

	// akcja po naciśnięciu tła powiadomienia - powrót do aplikacji
	/**
	 Jest to metoda służąca do powrotu do aplikacji po naciśnięciu tła powiadomienia.
	 */
	private PendingIntent contentIntent() {
		// po naciśnięciu tła powiadomienia otworzy się ekran główny
		Intent openApp = new Intent(context, MainScreen.class);
		return PendingIntent.getActivity(context, 0, openApp, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	// ustaw akcje dostępne pod przyciskami powiadomienia
	/**
	 Jest to metoda służąca do uzyskiwania akcji pod przyciskami powiadomienia.
	 @param iconType Typ ikony
	 */
	private Notification.Action getAction(IconType iconType) {
		Notification.Action.Builder builder = new Notification.Action.Builder(
				Icon.createWithResource(context, IconProvider.getIconId(iconType, true)),
				iconType.toString(),
				pendingIntentProvider(iconType)
		);
		// FIXME jeśli ikony nie wyświetlają się poprawnie, to zmień 4 linijki wyżej z getIcon(..., false) na getIcon(..., true)

		return builder.build();
	}

	/**
	 Jest to metoda służąca do tworzenia pending intentów, jest to intent nie wykonujący się od razu, tylko w chwili naciśnięcia przycisku w powiadomieniu.
	 @param type Typ ikony
	 */
	private PendingIntent pendingIntentProvider(IconType type) {
		Intent intent = IntentProvider.iconClicked(context, type);    // Tak, MainService wysyła do siebie te intenty futureTODO
		if(intent == null)
			Log.e("NotificationCreator", ">>> ERROR, intent nie istnieje, typ ikony: "+ type);
		return PendingIntent.getService(context, 7, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	}

	/**
	 Jest to metoda służąca do tworzenia kanału powiadomień.
	 */
	@RequiresApi(api = Build.VERSION_CODES.O)
	private String setChannel() {
		String channelid = "CHANNEL1";
		NotificationChannel channel = new NotificationChannel(channelid, "notifChannel", NotificationManager.IMPORTANCE_DEFAULT);
		NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
		notificationManager.createNotificationChannel(channel);
		return channelid;
	}

	/*
	public void closeNotification() {
		//
	}
	*/
}
