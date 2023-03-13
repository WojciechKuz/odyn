package com.example.odyn;

import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import java.io.File;

// ekran główny aplikacji
public class MainScreen extends AppCompatActivity {

    public PreviewView previewView;
    private CamAccess camAccess; // dostęp do kamery
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

        // tu znajdują się rzeczy związane z inicjalizacją kamery
        camAccess = new CamAccess(this, previewView);
    }


    // Otwórz panel menu
    public void onClickMenu(View view) {
        //
    }
    // Zrób zdjęcie
    public void onClickPhoto(View view) {
        // Create a file to store the image
        File file = new File(getExternalMediaDirs()[0], "image.jpg");
        camAccess.takePicture(file);
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
