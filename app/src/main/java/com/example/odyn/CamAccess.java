package com.example.odyn;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CamAccess extends AppCompatActivity {
    private ImageCapture imageCapture;

    private Activity main; // póki co spełnia dwie role: wątek (Context) i aktywność (wyświetlanie), później warto rozważyć rozdzielenie

    // konstruktor. PreviewView służy do wyświetlenia w nim obrazu z kamery
    public CamAccess(Activity main, PreviewView prView) {
        this.main = main;
        cameraProviderSetup(prView);
    }

    // te dwie poniższe funkcje służą do przygotowania kamery do przekazywania obrazu do <PreviewView> i robienia zdjęć
    @SuppressLint("RestrictedApi")
    private void cameraProviderSetup(PreviewView prView) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(main);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider, prView);
            } catch (ExecutionException | InterruptedException e) {
                // gdzie przechwycenie ???
            }
        }, ContextCompat.getMainExecutor(main));

    }
    private void bindPreview(ProcessCameraProvider cameraProvider, PreviewView prView) {

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        // Set up the preview use case
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(prView.getSurfaceProvider());

        ImageCapture.Builder builder = new ImageCapture.Builder();
        imageCapture = builder
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetRotation(main.getWindowManager().getDefaultDisplay().getRotation())
                .build();

        // użyj kamery do wyświetlania w mainActivity (preview) i do robienia zdjęć (imageCapture)
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)main, cameraSelector, preview, imageCapture);
    }

    // robi zdjęcie
    public void takePicture(File file) {
        // Set up the output file and capture the image
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(main), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                // The image has been saved to the file
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                // Handle any errors here
            }
        });
    }

    boolean isRecording = false;
    public VideoCapture videoCapture;
    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(this);
    }
    @SuppressLint("RestrictedApi")
    public void recordVideo(File file) {

        if (isRecording == false) {
            isRecording = true;
            if (videoCapture != null) {
                long timeStamp = System.currentTimeMillis();
                ContentValues contentValues = new ContentValues();
                contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, timeStamp);
                contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                videoCapture.startRecording(
                    new VideoCapture.OutputFileOptions.Builder(
                        getContentResolver(),
                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    ).build(),
                    getExecutor(),
                    new VideoCapture.OnVideoSavedCallback() {
                        @Override
                        public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                            Toast.makeText(CamAccess.this, "Zapisywanie...", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                            Toast.makeText(CamAccess.this, "Blad... : " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                );
            }
        }
        else {
            isRecording = false;
            if (videoCapture != null) {
                videoCapture.stopRecording();
            }
        }

    } // end of recordVideo()

}
