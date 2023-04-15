package com.example.odyn.activities;

import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import com.example.odyn.cam.Cam;
import com.example.odyn.cam.CamAccess;
import com.example.odyn.main_service.FileHandler;
import com.example.odyn.R;
import com.example.odyn.main_service.MainService;
import com.example.odyn.main_service.ServiceConnector;
import com.example.odyn.main_service.types.IconType;

import com.example.odyn.cam.CamAccess;
//import com.example.odyn.FileHandler;
import com.example.odyn.R;

import java.io.File;

// ekran główny aplikacji
public class MainScreen extends AppCompatActivity {

    public PreviewView previewView;

    private MainService service;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_WRITE_EXTERNAL_STORAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        // sprawdzenie uprawnień do aparatu i pamięci wewn.
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_EXTERNAL_STORAGE);
        }

        previewView = findViewById(R.id.previewView);

        ServiceConnector.setActivity(this); // static, usunięcie w onDestroy()
        service = ServiceConnector.getService();
        service.setCam(createCam()); // wyślij Cam do service
    }

    @Override
    protected void onStop() {
        service.appNotOnScreen(); // utwórz pływające powiadomienie
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        service.appBackOnScreen(); // zamknij pływające powiadomienie
    }

    // Otwórz panel menu
    public void onClickMenu(View view) {
        //
    }
    // Zrób zdjęcie
    public void onClickPhoto(View view) {
        // nowe
        ServiceConnector.onClick(IconType.photo);
    }

    // Nagrywanie awaryjne
    public void onClickEmergency(View view) {
        // nowe
        ServiceConnector.onClick(IconType.emergency);
    }

    // Nagraj wideo
    public void onClickRecord(View view) {
        // nowe
        ServiceConnector.onClick(IconType.recording);
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
        doUstaw.putExtra("settings_file", "settings.xml");
        // tu można dołączyć dodatkowe informacje dla ustawień
        startActivity(doUstaw);
    }

    // Wyjdź i nagrywaj w tle
    public void onClickBackground(View view) {
        //
    }

    @Override
    protected void onDestroy() {
        ServiceConnector.removeActivity(); // trzeba się pozbyć referencji, aby poprawnie usunąć Aktywność
        super.onDestroy();
    }

    public Cam createCam() {
        return new Cam(ServiceConnector.getActivity());
    }
}
