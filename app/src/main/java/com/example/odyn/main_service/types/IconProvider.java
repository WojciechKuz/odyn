package com.example.odyn.main_service.types;

import com.example.odyn.R;

/**
 * Jest to klasa służąca do otrzymania ID zasobów dla podanej ikony.
 */
// klasa służąca do otrzymania id zasobów dla podanej ikony. Później, jak będzie potrzeba, może zwracać Drawable albo Icon
public class IconProvider {
	public static int getIconId(IconType it) {
		return getIconId(it, false);
	}
	// zwróci id ikony, drugi argument: false - nasze ikony, true - domyślne ikony android "material design"
	/**
	 * Jest to metoda uzyskująca ID ikony.
	 */
	public static int getIconId(IconType it, boolean material) {
		if(material) {
			return resIdMaterial(it);
		}
		switch(it) {
			case photo:
				return R.drawable.photo;
			case recording:
				return R.drawable.record;
			case emergency:
				return R.drawable.emergency3ungroup;
			case menu:
				return R.drawable.menubutton;
			default:
				return resIdMaterial(it);
		}
	}

	/**
	 * Jest to metoda która pozwala na użycie domyślnych ikon Androida.
	 */
	// użyj domyślnych ikon androida
	private static int resIdMaterial(IconType it) {
		switch(it) {
			case photo:
				return android.R.drawable.ic_menu_camera;
			case recording:
				return android.R.drawable.presence_video_online;
			case emergency:
				return android.R.drawable.ic_dialog_alert;
			case close:
				return android.R.drawable.ic_delete;
			case menu:
				return android.R.drawable.ic_menu_sort_by_size;
			case back_to_app:
				return android.R.drawable.ic_menu_set_as;
		}
		return 0;
	}
}
