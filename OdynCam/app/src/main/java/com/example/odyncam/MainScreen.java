package com.example.odyncam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.content.Intent;

// ekran główny aplikacji
public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
    }
    // Otwórz panel menu
    public void onClickMenu(View view) {
        //
    }
    // Zrób zdjęcie
    public void onClickPhoto(View view) {
        //
    }
    // Nagrywanie awaryjne
    public void onClickEmergency(View view) {
        //
    }
    // Nagraj wideo
    public void onClickRecord(View view) {
        //
    }

    // Z menu:
    // Zamknij menu
    public void onClickCloseMenu(View view) {
        //
    }
    // Przejdź do listy nagrań
    public void onClickRecordingsList(View view) {
        // Początkowo może przekierowywać do innej aplikacji

        Intent doListy = new Intent(this, RecordingList.class);
        // tu można dołączyć dodatkowe informacje dla listy nagrań
        startActivity(doListy);
    }
    // Przejdź do ustawień
    public void onClickSettings(View view) {
        Intent doUstaw = new Intent(this, Settings.class);
        // tu można dołączyć dodatkowe informacje dla ustawień
        startActivity(doUstaw);
    }
    // Wyjdź i nagrywaj w tle
    public void onClickBackground(View view) {
        //
    }
}