package com.example.odyn;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class CamAccess {
    private ImageCapture imageCapture;
    private Activity main; // póki co spełnia dwie role: wątek (Context) i aktywność (wyświetlanie), później warto rozważyć rozdzielenie

    // konstruktor. PreviewView służy do wyświetlenia w nim obrazu z kamery
    public CamAccess(Activity main, PreviewView prView) {
        this.main = main;
        cameraProviderSetup(prView);
    }

    // te dwie poniższe funkcje służą do przygotowania kamery do przekazywania obrazu do <PreviewView> i robienia zdjęć
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


}
