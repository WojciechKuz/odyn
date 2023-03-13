package com.example.odyn;

import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

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
/*  WYCIĘTY KOD. wróć tu jeśli coś niedziała.

    POLE TEJ KLASY:
    private ImageCapture imageCapture;

    FRAGMENT ONCREATE():
    // to poniżej przeniesione do CamAccess. usunąć.
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) {

            }
        }, ContextCompat.getMainExecutor(this));
    KONIEC

    METODA BINDPREVIEW():
    private void bindPreview(ProcessCameraProvider cameraProvider) { // przeniesione do CamAccess. usunąć.

        // Set up the preview use case
        Preview preview = new Preview.Builder()
                .build();


        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();


        preview.setSurfaceProvider(previewView.getSurfaceProvider());


        ImageCapture.Builder builder = new ImageCapture.Builder();
        imageCapture = builder
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetRotation(getWindowManager().getDefaultDisplay().getRotation())
                .build();
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageCapture);
    }

    FRAGMENT ONCLICKPHOTO() ZA UTWORZENIEM PLIKU:
    // Set up the output file and capture the image
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(this), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                // The image has been saved to the file
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                // Handle any errors here
            }
        });
 */

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