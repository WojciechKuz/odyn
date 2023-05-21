/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, Viacheslav Kushinir <kushnir@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.gps;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Build;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimerThread extends Thread {
	private boolean stopTimer = false;
	private int count = 0;

	private TextFieldChanger changer;
	private Context context;

	public TimerThread(Context activityContext, TextFieldChanger changer) {
		this.context = activityContext;
		this.changer = changer;
	}

	// FIXME probably better solution would be scheduled execution. Here, sleep() blocks thread.
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
				((Activity) context).runOnUiThread(new Runnable() {
					@Override
					public void run() {
						changer.changeTextField(formattedTime, GPSValues.timer);
						changer.changeTextField(Integer.toString(count), GPSValues.counter);
					}
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void stopTimer() {
		stopTimer = true;
	}
}