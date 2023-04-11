package com.example.odyn.main_service;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.example.odyn.cam.RecType;
import com.example.odyn.main_service.types.IconType;
import com.example.odyn.main_service.types.IconTypeInterface;

public class ServiceConnector {
	private static IconTypeInterface handler;
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

	// w argumencie podać, jak obsłużyć przyciski
	public static void setOnClickHandle(IconTypeInterface handler) {
		ServiceConnector.handler = handler;
	}
	// wywoływane, gdy jakiś przycisk kliknięto
	public void onClick(IconType it) {
		handler.onIconClick(it);
	}
}
