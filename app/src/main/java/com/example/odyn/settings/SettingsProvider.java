package com.example.odyn.settings;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class SettingsProvider {
	private static JSONObject settings;


	// podaj nazwę ustawienia, zwróci wartość
	public synchronized boolean getSettingBool(String settingName) throws JSONException {
		return settings.getBoolean(settingName);
	}
	public synchronized int getSettingInt(String settingName) throws JSONException {
		return settings.getInt(settingName);
	}


	// jeżeli tylko jedna rzecz się zmieniła. z założenia pisać będzie tylko Settings.java
	public synchronized void setSetting(String settingName, boolean value) throws JSONException {
		settings.put(settingName, value);
	}
	public synchronized void setSetting(String settingName, int value) throws JSONException {
		settings.put(settingName, value);
	}


	// zapisuje / nadpisuje plik ustawień. z założenia pisać będzie tylko Settings.java
	public synchronized void writeSettings(Context context, JSONObject settings) {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(context.openFileOutput("settings.json", MODE_PRIVATE)));
			writer.write(settings.toString());
			writer.close();
		} catch (IOException e) {
			Log.e("SettingsProvider", ">>> Nie udało się zapisać ustawień");
		}
	}

	// odczytuje ustawienia z pliku. wywołać na początku działania Aplikacji
	public synchronized void loadSettings(Context context) {
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(context.openFileInput("settings.json")));
			StringBuilder stringBuilder = new StringBuilder();
			while(reader.ready()) {
				stringBuilder.append(reader.readLine()).append('\n');
			}
			reader.close();
			settings = new JSONObject(stringBuilder.toString());

			// akcje związane z ustawieniami, np. włącz ciemny motyw.
			settingActions();
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}
	}

	// akcje związane z ustawieniami, np. włącz ciemny motyw. Jak będzie dużo akcji to przenieść do nowej klasy
	private void settingActions() {
		try {
			boolean isChecked = this.getSettingBool("mode");
			int mode;
			if (isChecked) {
				mode = AppCompatDelegate.MODE_NIGHT_YES;
			} else {
				mode = AppCompatDelegate.MODE_NIGHT_NO;
			}
			AppCompatDelegate.setDefaultNightMode(mode);
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}

}
