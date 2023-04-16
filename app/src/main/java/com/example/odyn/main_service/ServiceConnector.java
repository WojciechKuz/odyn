package com.example.odyn.main_service;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.example.odyn.cam.Cam;
import com.example.odyn.main_service.types.IconType;
import com.example.odyn.main_service.types.IconTypeInterface;

// służy do komunikacji aktywność <-> MainService
// póki co nieprofesjonalnie, bo przez klasę statyczną. Czytam o alternatywach
// TODO Later: zamienić na komunikację za pomocą intent'ów
public class ServiceConnector {

	// Aktywność:
	@SuppressLint("StaticFieldLeak") // ostrożnie używać. w aktywności w onDestroy() użyć ServiceConnector.removeActivity()
	private static Activity activity;

	public static Activity getActivity() {
		return activity;
	}
	public static void setActivity(Activity activity) {
		ServiceConnector.activity = activity;
	}
	public static void removeActivity() {
		activity = null;
	}

	public static boolean activityExists() {
		return activity != null;
	}

	// onClickHandler:
	private static IconTypeInterface handler;
	// w argumencie podać, jak obsłużyć przyciski: Cam.camAction()
	public static void setOnClickHandle(IconTypeInterface handler) {
		ServiceConnector.handler = handler;
	}
	// wywoływane, gdy jakiś przycisk kliknięto
	public static void onClickIcon(IconType it) {
		handler.onIconClick(it);
	}


	// przekazywanie Cam z MainScreen do MainActivity:
	public static RecieveCamInterface camReciever;
	public static void setCamReciever(RecieveCamInterface camReciever) {
		ServiceConnector.camReciever = camReciever;
	}
	public static void sendCam(Cam cam) {
		camReciever.recieveCam(cam);
	}
}
