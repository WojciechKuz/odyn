package com.example.odyn.activities;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Jest to wątek odpowiedzialny za zapis video w formacie SRT.
 */
public class SRTWriter extends Thread {
    private boolean stopWriting = false;
    private File file;
	private Context context;
    private TextView counterText, timerText, latitudeText, longitudeText, srtText;
	private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;

	private String srtLine;

    public SRTWriter(Context context, File file, TextView counterText,TextView timerText, TextView latitudeText, TextView longitudeText, TextView srtText) {
		this.context = context;
		this.file = file;
		this.counterText = counterText;
        this.timerText = timerText;
        this.latitudeText = latitudeText;
        this.longitudeText = longitudeText;
		this.srtText = srtText;
    }

	/**
	 * Jest to metoda służąca do uruchomienia wątku zapisywania plików SRT.
	 */
    @Override
    public void run() {
        try {
            FileWriter writer = new FileWriter(file, true); // true for appending

            while (!stopWriting) {
                if (timerText != null && latitudeText != null && longitudeText != null) {
                    // Write the data in SRT format
					srtLine = "\n";
					srtLine += secondsToTimestamp(Integer.parseInt(counterText.getText().toString()) - 1) + " --> " + secondsToTimestamp(Integer.parseInt(counterText.getText().toString()));
					srtLine += "\n" + timerText.getText() + " | ";
					srtLine += latitudeText.getText() + ", ";
					srtLine += longitudeText.getText() + "\n\n";
					Log.d("GPS", srtLine);
					((Activity) srtText.getContext()).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							srtText.setText(srtLine);
						}
					});
                    writer.write(srtLine);
                }

                // Sleep for a while before checking again
                Thread.sleep(1000);
            }

            writer.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

	/**
	 * Jest to metoda służąca do zatrzymania zapisu do pliku SRT.
	 */
    public void stopWriting() {
        stopWriting = true;
    }

	/**
	 * Jest to metoda służąca do zapisu czasu w godzinach, minutach, sekundach i milisekundach.
	 */
	public static String secondsToTimestamp(int seconds) {
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		int secs = seconds % 60;
		int millis = 0; // set milliseconds to 0

		return String.format("%02d:%02d:%02d,%03d", hours, minutes, secs, millis);
	}

	/**
	 * Jest to metoda odpowiadająca za wykonanie żądania o uprawnienia do zapisywania do pliku SRT.
	 */
	public void requestWritePermissions(){
		// Request permissions
		if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE);
			return;
		}
	}
}

