package com.example.odyn.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;

import com.example.odyn.cam.Cam;
import com.example.odyn.R;
import com.example.odyn.main_service.ServiceConnector;
import com.example.odyn.main_service.types.IconType;


// ekran główny aplikacji
public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // sprawdzenie uprawnień do aparatu i pamięci wewn. przeniesione do StartActivity

        ServiceConnector.setActivity(this); // static, usunięcie w onDestroy()
        ServiceConnector.sendCam(createCam()); // jak się da to tworzenie z powrotem przenieść do MainService

        // obsługa przycisków
        findViewById(R.id.MenuButton).setOnClickListener(this::onClickMenu);
        findViewById(R.id.EmergencyButton).setOnClickListener(this::onClickEmergency);
        findViewById(R.id.PhotoButton).setOnClickListener(this::onClickPhoto);
        findViewById(R.id.RecordButton).setOnClickListener(this::onClickRecord);
    }
    // zwraca do MainService
    public Cam createCam() {
        return new Cam(this);
    }


    // Zamknij/otwórz powiadomienie (nieaktywne w wersji service 1)
    @Override
    protected void onStop() {
        // nieaktywne, ponieważ powiadomienie widoczne cały czas. Service wersja 1
        ServiceConnector.onClickIcon(IconType.display_notif); // utwórz pływające powiadomienie
        super.onStop();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        // nieaktywne, ponieważ powiadomienie widoczne cały czas. Service wersja 1
        ServiceConnector.onClickIcon(IconType.hide_notif); // zamknij pływające powiadomienie
    }


    // Przyciski ekranu:

    // Otwórz panel menu
    public void onClickMenu(View view) {
        Log.d("MainScreen", ">>> otwórz menu");
        //todo
    }
    // Zrób zdjęcie
    public void onClickPhoto(View view) {
        Log.d("MainScreen", ">>> zrób zdjęcie");
        ServiceConnector.onClickIcon(IconType.photo);
    }

    // Nagrywanie awaryjne
    public void onClickEmergency(View view) {
        Log.d("MainScreen", ">>> nagrywanie awaryjne");
        ServiceConnector.onClickIcon(IconType.emergency);
    }

    // Nagraj wideo
    public void onClickRecord(View view) {
        Log.d("MainScreen", ">>> nagraj");
        ServiceConnector.onClickIcon(IconType.recording);
    }


    // Z menu:

    // Zamknij menu
    public void onClickCloseMenu(View view) {
        Log.d("MainScreen", ">>> zamknij menu");
        //todo
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
        doUstaw.putExtra("settings_file", "settings.xml");
        // tu można dołączyć dodatkowe informacje dla ustawień
        startActivity(doUstaw);
    }

    // Wyjdź i nagrywaj w tle
    public void onClickBackground(View view) {
        //todo
    }

    @Override
    protected void onDestroy() {
        ServiceConnector.removeActivity(); // trzeba się pozbyć referencji, aby poprawnie usunąć Aktywność
        super.onDestroy();
    }
}
