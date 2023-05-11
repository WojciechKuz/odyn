package com.example.odyn.gps;

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

public class SRTWriter extends Thread {
    private boolean stopWriting = false;
    private File file;
	private Context context;
    private TextView counterText, timerText, latitudeText, longitudeText, srtText;
	private static final int REQUEST_CODE_WRITE_EXTERNAL_STORAGE = 1;

	private String srtLine;

	// FIXME instead of passing all TextViews here in constructor, define one method in MainScreen eg. 'textProvider', that returns all Strings from
	//  TextViews (in form of map or array or object holding Strings). Then define interface eg. 'GPSDataProvider'. Make field in this class,
	//  that would hold this interface and it will be passed in constructor instead of all this Textviews. Then pass textProvider in SRTWriter constructor call in MainScreen.
    public SRTWriter(Context context, File file, TextView counterText,TextView timerText, TextView latitudeText, TextView longitudeText, TextView srtText) {
		this.context = context;
		this.file = file;
		this.counterText = counterText;
        this.timerText = timerText;
        this.latitudeText = latitudeText;
        this.longitudeText = longitudeText;
		this.srtText = srtText;
    }

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
					writer.flush();
					Log.d("GPS","Wrote srt line");
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

