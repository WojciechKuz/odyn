package com.example.odyn.cam;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
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

import com.example.odyn.R;
import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

public class CamAccess extends AppCompatActivity {
    private ImageCapture imageCapture;
    private VideoCapture videoCapture;
    private Context context; // póki co spełnia dwie role: wątek (Context) i aktywność (wyświetlanie), później warto rozważyć rozdzielenie
    private int rotation;

    // konstruktor. PreviewView służy do wyświetlenia w nim obrazu z kamery
    public CamAccess(Context context, Activity mainActivity) {
        this.context = context;
        PreviewView prView = mainActivity.findViewById(R.id.previewView);
        rotation =  mainActivity.getWindowManager().getDefaultDisplay().getRotation(); // inny sposób na otrzymanie orientacji ekranu ???
        cameraProviderSetup(prView);
    }

    // te dwie poniższe funkcje służą do przygotowania kamery do przekazywania obrazu do <PreviewView> i robienia zdjęć
    @SuppressLint("RestrictedApi")
    private void cameraProviderSetup(PreviewView prView) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(context);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider, prView);
            } catch (ExecutionException | InterruptedException e) {
                // gdzie przechwycenie ???
            }
        }, ContextCompat.getMainExecutor(context));

    }
    @SuppressLint("RestrictedApi")
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
                .setTargetRotation(rotation)
                .build();
        videoCapture = new VideoCapture.Builder()
                .setVideoFrameRate(60)
                .build();

        // użyj kamery do wyświetlania w mainActivity (preview) i do robienia zdjęć (imageCapture)
        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)context, cameraSelector, preview, imageCapture,videoCapture);
    }

    // robi zdjęcie TODO change to protected
    public void takePicture(File file) {
        // Set up the output file and capture the image
        ImageCapture.OutputFileOptions outputFileOptions = new ImageCapture.OutputFileOptions.Builder(file).build();
        imageCapture.takePicture(outputFileOptions, ContextCompat.getMainExecutor(context), new ImageCapture.OnImageSavedCallback() {
            @Override
            public void onImageSaved(@NonNull ImageCapture.OutputFileResults outputFileResults) {
                // The image has been saved to the file
                Log.v("CamAccess", "---------ZapisywanieIMG---------");
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                // Handle any errors here
            }
        });
    }

    // TODO change to protected
    @SuppressLint({"RestrictedApi", "MissingPermission"})
    public void takeVideo(File file, boolean opcja) {
        // Set up the output file and start recording video

        if(opcja) {
            VideoCapture.OutputFileOptions outputFileOptions = new VideoCapture.OutputFileOptions.Builder(file).build();
            videoCapture.startRecording(outputFileOptions, ContextCompat.getMainExecutor(context), new VideoCapture.OnVideoSavedCallback() {

                @Override
                public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {
                    // The video has been saved to the file
                    System.out.println("-----------------------.-------------------.---------ZapisywanieVID-----------------------.-------------------.---------");
                    Log.v("CamAccess", "---------ZapisywanieVID---------");
                }

                @Override
                public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                    // Handle any errors here
                    System.out.println("-----------------------.-------------------.---------GownoVID-----------------------.-------------------.---------");
                    Log.wtf("CamAccess", "---------GownoVID---------");
                }
            });
        }
        else
        {
             videoCapture.stopRecording();
        }
    } // end of takeVideo()

}
