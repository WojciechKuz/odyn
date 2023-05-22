/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.main_service.types;

import android.content.Context;
import android.content.Intent;

import com.example.odyn.cam.ActionType;
import com.example.odyn.cam.Cam;
import com.example.odyn.cam.RecType;
import com.example.odyn.main_service.MainService;

// IntentProvider to klasa, która dla każdej ikony dostarcza (wykonuje) powiązaną z nią akcję
/**
 * Jest to klasa, która dla każdej ikony wykonuje powoiązaną z nią akcję.
 */
public class IntentProvider {

	public static final String extraName = "iconType";

	/**
	 * Jest to metoda, która dla każdej ikony dotarcza powoiązaną z nią akcję.
	 */
	public static Intent iconClicked(Context context, IconType iconType) {
		Class<?> actionReciever = MainService.class;
		Intent intent;
		switch(iconType) {
			case photo:
			case recording:
			case emergency:
			case back_to_app:
				intent = new Intent(context, actionReciever);
				intent.putExtra(extraName, iconType); // enum casted to Serializable
				return intent;
			case close:
				// close app
				intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				return intent;
			case menu:
				// start menuActivity ???
				// TODO

				return null; // temporary
			default:
				return null; // zabezpieczenie
		}
	}
}
