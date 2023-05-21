/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, Viacheslav Kushinir <kushnir@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.gps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.odyn.activities.MainScreen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class SRTWriter extends Thread {
    private boolean stopWriting = false;
    private File file;
	private Context context;
	private MainScreen mainScreen;
	private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;

	private String srtLine;
	public SRTWriter(MainScreen mainScreen, Context context, File file) {
		this.mainScreen = mainScreen;
		this.context = context;
		this.file = file;
	}

    @Override
    public void run() {
        try {
            FileWriter writer = new FileWriter(file, true); // true for appending

            while (!stopWriting) {
				Map<String, String> textMap = mainScreen.textProvider();
				String timerText = textMap.get("timerText");
				String counterText = textMap.get("counterText");
				String latitudeText = textMap.get("latitudeText");
				String longitudeText = textMap.get("longitudeText");
				String speedText = textMap.get("speedText");

                if (timerText != null && latitudeText != null && longitudeText != null) {
                    // Write the data in SRT format
					srtLine = "\n";
					srtLine += secondsToTimestamp(Integer.parseInt(counterText) - 1) + " --> " + secondsToTimestamp(Integer.parseInt(counterText));
					srtLine += "\n" + timerText + " | " + speedText + " | ";
					srtLine += latitudeText + ", ";
					srtLine += longitudeText + "\n\n";
					Log.d("GPS", srtLine);
					writer.write(srtLine);
					writer.flush();
					//Log.d("GPSThread","Wrote srt line");
                }

                // Sleep for a while before checking again
                Thread.sleep(1000);
            }

            writer.close();
        } catch (IOException | InterruptedException e) {
			e.printStackTrace();
			Log.d("GPS","Exception/End of srt writing");
        }
    }

    public void stopWriting() {
        stopWriting = true;
    }

	@SuppressLint("DefaultLocale")
	public static String secondsToTimestamp(int seconds) {
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		int secs = seconds % 60;
		int millis = 0; // set milliseconds to 0

		return String.format("%02d:%02d:%02d,%03d", hours, minutes, secs, millis);
	}

	public void requestWritePermissions(){
		// Request permissions
		if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
			return;
		}
	}
}

