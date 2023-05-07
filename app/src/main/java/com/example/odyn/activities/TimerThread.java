package com.example.odyn.activities;

import android.app.Activity;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Jest to wątek odpowiedzialny za liczenie czasu nagrywania.
 */
public class TimerThread extends Thread {
	private boolean stopTimer = false;
	private int count = 0;

	private TextView counterText, timerText;

	public TimerThread(TextView counterText, TextView timerText) {
		this.timerText = timerText;
		this.counterText = counterText;
	}
	/**
	 * Jest to metoda służąca do uruchamiania licznika czasu nagrywania.
	 */
	@Override
	public void run() {
		while (!stopTimer) {
			try {
				Thread.sleep(1000); // Pause for 1 second
				count++;

				// Get the current time
				Date currentTime = new Date();

				// Format the time using a SimpleDateFormat
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
				String formattedTime = dateFormat.format(currentTime);

				// Update the TextView with the formatted time and count
				((Activity)timerText.getContext()).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						timerText.setText(formattedTime);
					}
				});
				((Activity)counterText.getContext()).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						counterText.setText(Integer.toString(count));
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Jest to metoda służąca do zatrzymania licznika czasu nagrywania.
	 */
	public void stopTimer() {
		stopTimer = true;
	}
}