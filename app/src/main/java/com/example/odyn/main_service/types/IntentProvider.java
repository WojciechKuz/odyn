package com.example.odyn.main_service.types;

import android.content.Context;
import android.content.Intent;

import com.example.odyn.cam.ActionType;
import com.example.odyn.cam.Cam;
import com.example.odyn.cam.RecType;

// IntentProvider to klasa, która dla każdej ikony dostarcza (wykonuje) powiązaną z nią akcję
public class IntentProvider {
	public static void iconClicked(Context context, IconType iconType) {
		Intent intent;
		switch(iconType) {
			case photo:
				// send request to CamAccess
				intent = new Intent(context, Cam.class);
				intent.putExtra("RecType", RecType.picture); // enum casted to Serializable
				intent.putExtra("ActionType", ActionType.toggle);
				break;
			case recording:
				// send request to CamAccess
				intent = new Intent(context, Cam.class);
				intent.putExtra("RecType", RecType.video); // enum casted to Serializable
				intent.putExtra("ActionType", ActionType.toggle);
				break;
			case emergency:
				// send request to CamAccess
				intent = new Intent(context, Cam.class);
				intent.putExtra("RecType", RecType.emergency); // enum casted to Serializable
				intent.putExtra("ActionType", ActionType.toggle);
				break;
			case close:
				// close app
				intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				break;
			case menu:
				// start menuActivity ??? does not exist yet
				// TODO first, menu Activity is needed

				return; // temporary, FIXME
				//break;
			case back_to_app:
				// start MainScreen Activity
				// hmmm, a samo się nie wystartuje po powrocie na ekran?
				// tzn. jeśli zamkniemy service w MainScreen.onRestart(),
				// to ten Intent do MainScreen jest zbędny.
				// TODO close notification, open MainScreen

				return; // temporary, FIXME
				//break;
			default:
				return; // zabezpieczenie
		}
		context.startActivity(intent);
	}
}
