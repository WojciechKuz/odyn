package com.example.odyn.main_service.types;

// IntentProvider to klasa, która dla każdej ikony wykonuje powiązaną z nią akcję
public class IntentProvider {
	public static void iconClicked(IconType iconType) {
		// TODO create Intents
		switch(iconType) {
			case photo:
				// send to CamAccess
			case recording:
				// send to CamAccess
			case emergency:
				// send to CamAccess
			case close:
				// close app
			case menu:
				// start menuActivity ??? does not exist yet
			case back_to_app:
				// start MainScreen Activity
		}
	}
}
