package com.example.gpsv2;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {
	private TimerThread timerThread;
	private GPSThread gpsThread;
	private SRTWriter srtWriter;

	private TextView counterText, timerText, latitudeText, longitudeText, srtText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		timerText = findViewById(R.id.timerText);
		counterText = findViewById(R.id.counterText);
		timerThread = new TimerThread(counterText, timerText);
		timerThread.start();

		latitudeText = findViewById(R.id.latitudeText);
		longitudeText = findViewById(R.id.longitudeText);
		gpsThread = new GPSThread(this, latitudeText, longitudeText);
		gpsThread.requestGPSPermissions();
		gpsThread.start();


		File file = new File(getExternalFilesDir(null), "myfile.srt");
		srtText = findViewById(R.id.srtText);
		srtWriter = new SRTWriter(this, file, counterText, timerText, latitudeText, longitudeText, srtText);
		srtWriter.requestWritePermissions();
		srtWriter.start();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		timerThread.stopTimer();
		gpsThread.stopGPS();
	}
}