package com.example.odyn.cam;

import android.content.Intent;

// przykrywa CamAccess (inherit or field?)
public class Cam {
	// TODO konstruktor

	// TODO metody robiące zdjęcia i nagrywające

	// obsłuż intent'y. żądania nagrywania, itp.
	public void onHandleIntent(Intent intent) {
		// odczytaj, co zrobić RecType i ActionType
		if(intent != null) {
			RecType recType = (RecType) intent.getSerializableExtra("RecType");
			ActionType actionType = (ActionType) intent.getSerializableExtra("ActionType");  // nie trzeba używać ActionType
			camAction(recType, actionType);
		}
	}

	// obsługa Intent'ów
	private void camAction(RecType rt, ActionType at) {
		switch(rt) {
			case picture:
				break;
				// TODO
		}
	}
}
