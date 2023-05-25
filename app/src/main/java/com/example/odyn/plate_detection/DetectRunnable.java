package com.example.odyn.plate_detection;

import android.graphics.Bitmap;

public class DetectRunnable implements Runnable{
    private final Bitmap image;
    public DetectRunnable(Bitmap image){
        this.image = image;
    }

    @Override
    public void run() {

    }
}
