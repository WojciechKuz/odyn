/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, Viacheslav Kushinir <kushnir@mat.umk.pl>, Jakub Orłowski <orljak@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.cam;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.example.odyn.FileHandler;
import com.example.odyn.main_service.types.IconType;

import java.io.File;

// przykrywa CamAccess (inherit or field?)
/**
 * Jest to wrapper na klasę CamAccess ułatwiający pracę z kamerą.
 */
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
	/**
	 * Jest to metoda służąca do obsługi intencji oraz żądań nagrywania.
	 * @param intent Intent
	 */
	public void onHandleIntent(Intent intent) { // ???
		// odczytaj, co zrobić RecType i ActionType
		if(intent != null) {
			if(intent.hasExtra("RecType") && intent.hasExtra("ActionType")) {
				Log.w("Cam", ">>> odbieranie Intent'ów z polem \"RecType\" przez Cam nie jest już wspierane");
			}
			if(intent.hasExtra("IconType")) {
				IconType iconType = (IconType) intent.getSerializableExtra("IconType");
				camAction(iconType);
			}
		}
	}

	/**
	 * Jest to metoda służąca do robienia zdjęć, nagrywania oraz nagrywania awaryjnego.
	 * @param iconType Typ ikony
	 */
	public void camAction(IconType iconType) {
		switch(iconType) {
			case photo:
				photo(1);
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

	/**
	 * Jest to metoda służąca do tworzenia zdjęcia.
	 * @param option Opcja
	 */
	private void photo(int option) {
			File file = new FileHandler(main).createPicture();
			takePicture(option);
	}

	/**
	 * Jest to metoda służąca do nagrywania.
	 */
	private void record() {
		if (!isRecording) {
			Log.v("Cam", ">>> rozpoczynam nagrywanie");
			isRecording = true;
			takeVideo(isRecording);
		} else {
			Log.v("Cam", ">>> kończę nagrywanie");
			isRecording = false;
			takeVideo(isRecording);
		}
	}

	// może jeszcze zostać zmieniony format, albo dodane jakieś dane jeszcze
	/**
	 * Jest to metoda służąca do nagrywania awaryjnego.
	 */
	private void emergency() {
		//File file = new FileHandler(main).createEmergencyVideo("mp4");
		if (!isEmergency) {
			isEmergency = true;
			takeVideo(isEmergency);
		} else {
			isEmergency = false;
			takeVideo(isEmergency);
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
