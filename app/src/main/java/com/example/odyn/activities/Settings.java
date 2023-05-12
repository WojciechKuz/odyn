package com.example.odyn.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.odyn.R;

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
    private Switch switch4;
    private Switch switch5;
    private Switch switch6;
    private Switch switch7;
    private int mode;
    private Spinner Spinner;
    private Spinner Spinner2;
    private Spinner Spinner3;
    private Spinner Spinner4;
    private Spinner Spinner5;
    private Spinner Spinner6;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);
        switch3 = findViewById(R.id.switch3);
        switch4 = findViewById(R.id.switch4);
        switch5 = findViewById(R.id.switch5);
        switch6 = findViewById(R.id.switch6);
        switch7 = findViewById(R.id.switch7);
        Spinner = findViewById(R.id.spinner1);
        Spinner2 = findViewById(R.id.spinner2);
        Spinner3 = findViewById(R.id.spinner3);
        Spinner4 = findViewById(R.id.spinner4);
        Spinner5 = findViewById(R.id.spinner5);
        Spinner6 = findViewById(R.id.spinner6);

        // Inicjalizacja pliku SharedPreferences
        sharedPrefs = getSharedPreferences("settings", Context.MODE_PRIVATE);

        // Ustawienie wartości domyślnych
        switch1.setChecked(sharedPrefs.getBoolean("switch1", false));
        switch2.setChecked(sharedPrefs.getBoolean("switch2", false));
        switch3.setChecked(sharedPrefs.getBoolean("switch3", false));
        switch4.setChecked(sharedPrefs.getBoolean("switch4", false));
        switch5.setChecked(sharedPrefs.getBoolean("switch5", false));
        switch6.setChecked(sharedPrefs.getBoolean("switch6", false));
        switch7.setChecked(sharedPrefs.getBoolean("switch7", false));

        // Ustawienie opcji spinnera
        String[] storageOptions = {"Karta SD", "Pamięć wewnętrzna"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, storageOptions);
        Spinner.setAdapter(adapter);

        String[] LeftOrRight = {"Lewo", "Prawo"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, LeftOrRight);
        Spinner2.setAdapter(adapter2);

        String[] LengthRecords = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, LengthRecords);
        Spinner3.setAdapter(adapter3);

        String[] SizeVideo = {"512MB", "1024MB", "2048MB", "4096MB", "8192MB", "12288MB", "16384MB", "32768MB"};
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, SizeVideo);
        Spinner4.setAdapter(adapter4);

        String[] EmergencyBefore= {"1", "2", "3", "4", "5"};
        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, EmergencyBefore);
        Spinner5.setAdapter(adapter5);

        String[] EmergencyAfter = {"1", "2", "3", "4", "5"};
        ArrayAdapter<String> adapter6 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, EmergencyAfter);
        Spinner6.setAdapter(adapter6);




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

        switch4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPrefs.edit().putBoolean("switch4", isChecked).apply();
                saveSettingsToFile();
            }
        });

        switch5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPrefs.edit().putBoolean("switch5", isChecked).apply();
                saveSettingsToFile();
            }
        });

        switch6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPrefs.edit().putBoolean("switch6", isChecked).apply();
                saveSettingsToFile();
            }
        });

        switch7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPrefs.edit().putBoolean("switch7", isChecked).apply();
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
            settings.put("switch4", switch4.isChecked());
            settings.put("switch5", switch5.isChecked());
            settings.put("switch6", switch6.isChecked());
            settings.put("switch7", switch7.isChecked());
            settings.put("mode", mode);
            settings.put("storage_option", Spinner.getSelectedItemPosition());
            settings.put("Left_Right", Spinner2.getSelectedItemPosition());
            settings.put("Length_record", Spinner3.getSelectedItemPosition());
            settings.put("Size_video", Spinner4.getSelectedItemPosition());
            settings.put("Emergency_before", Spinner5.getSelectedItemPosition());
            settings.put("Emergency_after", Spinner6.getSelectedItemPosition());
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
            switch4.setChecked(settings.getBoolean("switch4"));
            switch5.setChecked(settings.getBoolean("switch5"));
            switch6.setChecked(settings.getBoolean("switch6"));
            switch7.setChecked(settings.getBoolean("switch7"));
            mode = settings.getInt("mode");
            AppCompatDelegate.setDefaultNightMode(mode);

            // ładowanie opcji spinnera
            int spinnerPosition = settings.getInt("storage_option");
            Spinner.setSelection(spinnerPosition);

            int spinnerPosition2 = settings.getInt("Left_Right");
            Spinner2.setSelection(spinnerPosition2);

            int spinnerPosition3 = settings.getInt("Length_record");
            Spinner3.setSelection(spinnerPosition3);

            int spinnerPosition4 = settings.getInt("Size_video");
            Spinner4.setSelection(spinnerPosition4);

            int spinnerPosition5 = settings.getInt("Emergency_before");
            Spinner5.setSelection(spinnerPosition5);

            int spinnerPosition6 = settings.getInt("Emergency_after");
            Spinner6.setSelection(spinnerPosition6);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
}