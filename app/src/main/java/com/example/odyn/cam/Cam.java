package com.example.odyn.cam;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.odyn.main_service.types.IconType;

import java.io.File;

// przykrywa CamAccess (inherit or field?)
public class Cam extends CamAccess {

	public Cam(Context context, Activity mainActivity) {
		super(context, mainActivity);
	}

	// w CamAccess publiczne tylko:
	// takePicture(File file)
	// takeVideo(File file, boolean opcja)

	// TODO opakować metody robiące zdjęcia i nagrywające

	// obsłuż intent'y. żądania nagrywania, itp.
	public void onHandleIntent(Intent intent) { // ???
		// odczytaj, co zrobić RecType i ActionType
		if(intent != null) {
			RecType recType = (RecType) intent.getSerializableExtra("RecType");
			ActionType actionType = (ActionType) intent.getSerializableExtra("ActionType");  // nie trzeba używać ActionType
			camAction(recType, actionType);
		}
	}

	// obsługa Intent'ów ???
	private void camAction(RecType rt, ActionType at) {
		switch(rt) {
			case picture:
				break;
				// TODO
		}
	}
	public void camAction(IconType iconType) {
		switch(iconType) {
			case photo:
				// TODO
				break;
			case recording:
				// TODO
				break;
			case emergency:
				// TODO
				break;
			default:
				return;
		}
	}
}
