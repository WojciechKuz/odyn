/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.main_service;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.example.odyn.cam.Cam;
import com.example.odyn.main_service.types.IconType;
import com.example.odyn.main_service.types.IconTypeInterface;

// służy do komunikacji aktywność <-> MainService
// póki co nieprofesjonalnie, bo przez klasę statyczną. Czytam o alternatywach
// TODO Jeśli metody dostępu do pól (gettery, settery) będą synchronised, to klasa static jest akceptowalnym rozwiązaniem.
// Można by zamienić na komunikację za pomocą intent'ów

/**
 Jest to klasa odpowiedzialna za komunikację aktywności z MainService.
 */
public class ServiceConnector {

	// Aktywność:
	@SuppressLint("StaticFieldLeak") // ostrożnie używać. w aktywności w onDestroy() użyć ServiceConnector.removeActivity()
	private static Activity activity = null;

	/**
	 Jest to metoda służąca do uzyskiwania aktywności.
	 */
	public static Activity getActivity() {
		return activity;
	}

	/**
	 Jest to metoda służąca do tworzenia nowej aktywności.
	 */
	public static void setActivity(Activity activity) {
		ServiceConnector.activity = activity;
	}

	/**
	 Jest to metoda służąca do usuwania aktywności.
	 */
	public static void removeActivity() {
		activity = null;
	}

	/**
	 Jest to metoda służąca do sprawdzania czy aktywność istnieje.
	 */
	public static boolean activityExists() {
		return activity != null;
	}


	// onClickHandler:
	private static IconTypeInterface handler;
	// w argumencie podać, jak obsłużyć przyciski: Cam.camAction()
	/**
	 Jest to metoda służąca do ustawiania obsługi przycisku.
	 */
	public static void setOnClickHandle(IconTypeInterface handler) {
		ServiceConnector.handler = handler;
	}
	// wywoływane, gdy jakiś przycisk kliknięto
	/**
	 Jest to metoda wywoływana po naciśnięciu przycisku.
	 */
	public static void onClickIcon(IconType it) {
		handler.onIconClick(it);
	}


	// przekazywanie Cam z MainScreen do MainActivity:
	public static RecieveCamInterface camReceiver;

	/**
	 Jest to metoda służąca do utworzenia przekaźnika klasy Cam z MainScreen do MainActivity.
	 */
	public static void setCamReceiver(RecieveCamInterface camReceiver) {
		ServiceConnector.camReceiver = camReceiver;
	}

	/**
	 Jest to metoda służąca do przekazywania klasy Cam z MainScreen do MainActivity.
	 */
	public static void sendCam(Cam cam) {
		camReceiver.recieveCam(cam);
	}
}
