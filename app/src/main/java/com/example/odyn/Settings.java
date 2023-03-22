package com.example.odyn;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class Settings extends AppCompatActivity {

    private SharedPreferences sharedPrefs;
    private Switch switch1;
    private Switch switch2;
    private Switch switch3;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);
        switch3 = findViewById(R.id.switch3);

        // Inicjalizacja pliku SharedPreferences
        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE);

        // Ustawienie wartości domyślnych
        switch1.setChecked(sharedPrefs.getBoolean("switch1", false));
        switch2.setChecked(sharedPrefs.getBoolean("switch2", false));
        switch3.setChecked(sharedPrefs.getBoolean("switch3", false));

        // Dodanie obsługi zdarzeń dla przełączników
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPrefs.edit().putBoolean("switch1", isChecked).apply();
                if (isChecked) {
                    mode = AppCompatDelegate.MODE_NIGHT_YES;
                } else {
                    mode = AppCompatDelegate.MODE_NIGHT_NO;
                }
                AppCompatDelegate.setDefaultNightMode(mode);
                saveSettingsToFile();
            }
        });

        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPrefs.edit().putBoolean("switch2", isChecked).apply();
                saveSettingsToFile();
            }
        });

        switch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPrefs.edit().putBoolean("switch3", isChecked).apply();
                saveSettingsToFile();
            }
        });

        loadSettingsFromFile();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettingsToFile();
    }

    private void saveSettingsToFile() {
        try {
            JSONObject settings = new JSONObject();
            settings.put("switch1", switch1.isChecked());
            settings.put("switch2", switch2.isChecked());
            settings.put("switch3", switch3.isChecked());
            settings.put("mode", mode);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(openFileOutput("settings.json", MODE_PRIVATE)));
            writer.write(settings.toString());
            writer.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }


    private void loadSettingsFromFile() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(openFileInput("settings.json")));
            StringBuilder stringBuilder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = reader.readLine();
            }
            reader.close();
            JSONObject settings = new JSONObject(stringBuilder.toString());
            switch1.setChecked(settings.getBoolean("switch1"));
            switch2.setChecked(settings.getBoolean("switch2"));
            switch3.setChecked(settings.getBoolean("switch3"));
            mode = settings.getInt("mode");
            AppCompatDelegate.setDefaultNightMode(mode);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}