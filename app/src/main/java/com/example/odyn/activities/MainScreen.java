package com.example.odyn.activities;

import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import android.content.pm.PackageManager;
import android.os.Bundle;
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
    }
    // zwraca do MainService
    public Cam createCam() {
        return new Cam(this);
    }


    // Zamknij/otwórz powiadomienie (nieaktywne w wersji service 1)
    @Override
    protected void onStop() {
        // nieaktywne, ponieważ powiadomienie widoczne cały czas. Service wersja 1
        ServiceConnector.onClick(IconType.display_notif); // utwórz pływające powiadomienie
        super.onStop();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        // nieaktywne, ponieważ powiadomienie widoczne cały czas. Service wersja 1
        ServiceConnector.onClick(IconType.hide_notif); // zamknij pływające powiadomienie
    }


    // Przyciski ekranu:

    // Otwórz panel menu
    public void onClickMenu(View view) {
        //todo
    }
    // Zrób zdjęcie
    public void onClickPhoto(View view) {
        ServiceConnector.onClick(IconType.photo);
    }

    // Nagrywanie awaryjne
    public void onClickEmergency(View view) {
        ServiceConnector.onClick(IconType.emergency);
    }

    // Nagraj wideo
    public void onClickRecord(View view) {
        ServiceConnector.onClick(IconType.recording);
    }


    // Z menu:

    // Zamknij menu
    public void onClickCloseMenu(View view) {
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
