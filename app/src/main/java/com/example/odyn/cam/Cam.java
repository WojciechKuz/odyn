package com.example.odyn.cam;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.odyn.FileHandler;
import com.example.odyn.main_service.types.IconType;

import java.io.File;

// przykrywa CamAccess (inherit or field?)
public class Cam extends CamAccess {

	private boolean isRecording = false; // nagranie zapisywane (blokowanie usuwania)
	private boolean isEmergency = false; // nagranie awaryjne zapisywane (blokowanie usuwania)
	public Cam(Activity mainActivity) {
		super(mainActivity);
		Log.v("Cam", ">>> Class Cam is ready");
	}

	// w CamAccess publiczne tylko:
	// takePicture(File file)
	// takeVideo(File file, boolean opcja)
	//
	// tutaj zamiast ich:
	// photo()
	// record()
	// emergency()
	// uruchamiane poprzez camAction(IconType)

	// obsłuż intent'y. żądania nagrywania, itp.
	public void onHandleIntent(Intent intent) { // ???
		// odczytaj, co zrobić RecType i ActionType
		if(intent != null) {
			if(intent.hasExtra("RecType") && intent.hasExtra("ActionType")) {
				RecType recType = (RecType) intent.getSerializableExtra("RecType");
				ActionType actionType = (ActionType) intent.getSerializableExtra("ActionType");  // nie trzeba używać ActionType
				camAction(recType, actionType);
			}
			if(intent.hasExtra("IconType")) {
				IconType iconType = (IconType) intent.getSerializableExtra("IconType");
				camAction(iconType);
			}
		}
	}


	public void camAction(IconType iconType) {
		switch(iconType) {
			case photo:
				photo();
				break;
			case recording:
				record();
				break;
			case emergency:
				emergency();
				break;
			default:
				// nic nie rób, są inne typy ikon, których Cam nie obsłuży
				break;
		}
	}
	private void photo() {
		File file = new FileHandler(main).createPicture();
		takePicture(file);
	}
	private void record() {
		File file = new FileHandler(main).createVideo("mp4");
		if (!isRecording) {
			Log.v("Cam", ">>> rozpoczynam nagrywanie");
			isRecording = true;
			takeVideo(file,isRecording);
		} else {
			Log.v("Cam", ">>> kończę nagrywanie");
			isRecording = false;
			takeVideo(file,isRecording);
		}
	}
	// może jeszcze zostać zmieniony format, albo dodane jakieś dane jeszcze
	private void emergency() {
		File file = new FileHandler(main).createEmergencyVideo("mp4");
		if (!isEmergency) {
			isEmergency = true;
			takeVideo(file,isEmergency);
		} else {
			isEmergency = false;
			takeVideo(file,isEmergency);
		}
	}

	// śmieć:
	@Deprecated
	private void camAction(RecType rt, ActionType at) {
		switch(rt) {
			case picture:
				break;
			// notTODO
		}
	}
}