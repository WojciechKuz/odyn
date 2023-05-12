package com.example.odyn.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.odyn.R;
import com.example.odyn.settings.SettingNames;
import com.example.odyn.settings.SettingOptions;
import com.example.odyn.settings.SettingsProvider;
import com.google.android.material.switchmaterial.SwitchMaterial;

import org.json.JSONException;
import org.json.JSONObject;

public class Settings extends AppCompatActivity {
    private SwitchMaterial switch1;
    private SwitchMaterial switch2;
    private SwitchMaterial switch3;
    private SwitchMaterial switch4;
    private SwitchMaterial switch5;
    private SwitchMaterial switch6;
    private SwitchMaterial switch7;
    private int mode;
    private Spinner spinner1;
    private Spinner spinner2;
    private Spinner spinner3;
    private Spinner spinner4;
    private Spinner spinner5;
    private Spinner spinner6;

    // elementy umieściłem w tablicy, dla ułatwienia użytkowania
    private SwitchMaterial[] mSwitch;
    private Spinner[] spinners;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        findUIElements();
        setSwitches();
        initSpinners();
        setSwitchListeners();

        loadSettingsFromFile();
    }
    private void findUIElements() {
        switch1 = findViewById(R.id.switch1);
        switch2 = findViewById(R.id.switch2);
        switch3 = findViewById(R.id.switch3);
        switch4 = findViewById(R.id.switch4);
        switch5 = findViewById(R.id.switch5);
        switch6 = findViewById(R.id.switch6);
        switch7 = findViewById(R.id.switch7);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        spinner3 = findViewById(R.id.spinner3);
        spinner4 = findViewById(R.id.spinner4);
        spinner5 = findViewById(R.id.spinner5);
        spinner6 = findViewById(R.id.spinner6);
        mSwitch = new SwitchMaterial[] {null, switch1, switch2, switch3, switch4, switch5, switch6, switch7};
        spinners = new Spinner[] {null, spinner1, spinner2, spinner3, spinner4, spinner5, spinner6};
    }
    private void setSwitches() {
        // Ustawienie wartości przełączników
        String[] sw = SettingNames.switches;
        SettingsProvider sprov = new SettingsProvider();
        for(int i = 1; i < mSwitch.length; i++) {
            try {
                mSwitch[i].setChecked(sprov.getSettingBool(sw[i]));
            } catch (JSONException e) {
                Log.e("Settings", ">>> nie odczytano stanu ustawień");
            }
        }
    }
    private void initSpinners() {
        // Ustawienie opcji spinnera
        int spinnerTypeCode = android.R.layout.simple_spinner_dropdown_item;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, spinnerTypeCode, SettingOptions.storageOptions);
        spinner1.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, spinnerTypeCode, SettingOptions.LeftOrRight);
        spinner2.setAdapter(adapter2);

        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, spinnerTypeCode, SettingOptions.LengthRecords);
        spinner3.setAdapter(adapter3);

        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, spinnerTypeCode, SettingOptions.SizeVideo);
        spinner4.setAdapter(adapter4);

        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(this, spinnerTypeCode, SettingOptions.EmergencyBefore);
        spinner5.setAdapter(adapter5);

        ArrayAdapter<String> adapter6 = new ArrayAdapter<>(this, spinnerTypeCode, SettingOptions.EmergencyAfter);
        spinner6.setAdapter(adapter6);
    }
    private void setSwitchListeners() {
        // Dodanie obsługi zdarzeń dla przełączników
        for(SwitchMaterial switchM : mSwitch) {
            switchM.setOnCheckedChangeListener(this::switchListener);
        }
        switch1.setOnCheckedChangeListener((buttonView, isChecked) -> {
            try {
                new SettingsProvider().setSetting(SettingNames.switches[1], isChecked);
            } catch (JSONException e) {
                Log.w("Settings", ">>> nie udało się zapisać ustawienia");
            }

            AppCompatDelegate.setDefaultNightMode(mode);
            // saveSettingsToFile(); // zbędne, nie potrzeba co chwilę nadpisywać pliku, ustawienia w aplikacji są dostępne przez SettingsProvider.
            //  zapisanie do pliku po wyjściu z tej aktywności
        });
    }
    private void switchListener(CompoundButton buttonView, boolean isChecked) {
        for(int i = 1; i < mSwitch.length; i++) {
            if(buttonView == mSwitch[i]) {
                try {
                    new SettingsProvider().setSetting(SettingNames.switches[i], isChecked);
                } catch (JSONException e) {
                    Log.w("Settings", ">>> nie udało się zapisać ustawienia");
                }
                saveSettingsToFile(); // ??? wydaje się niepotrzebne

                break; // jak już znaleźliśmy element, to nie iterujemy dalej
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveSettingsToFile();
    }

    private void saveSettingsToFile() {
        try {
            JSONObject settings = new JSONObject();

            for(int i = 1; i < mSwitch.length; i++)
                settings.put(SettingNames.switches[i], mSwitch[i].isChecked());
            for(int i = 1; i < spinners.length; i++)
                settings.put(SettingNames.spinners[i], spinners[i].getSelectedItemPosition());

            settings.put("mode", mode); // po co dodatkowo zapisujemy mode, skoro jest też w przełączniku?

            new SettingsProvider().writeSettings(this, settings); // zapisz ustawienia
        } catch (JSONException e) {
            Log.e("Settings", ">>> błąd podczas tworzenia obiektu JSON");
            e.printStackTrace();
        }
    }

    private void loadSettingsFromFile() {
        try {
            SettingsProvider sprov = new SettingsProvider();

            // ustawienie opcji
            for(int i = 1; i < mSwitch.length; i++)
                mSwitch[i].setChecked(sprov.getSettingBool(SettingNames.switches[i]));
            for(int i = 1; i < spinners.length; i++)
                spinners[i].setSelection(sprov.getSettingInt(SettingNames.spinners[i]));

            mode = sprov.getSettingInt("mode");
            AppCompatDelegate.setDefaultNightMode(mode);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}