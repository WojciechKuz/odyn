/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.main_service.types;

import com.example.odyn.R;

// klasa służąca do otrzymania id zasobów dla podanej ikony. Później, jak będzie potrzeba, może zwracać Drawable albo Icon
/**
 * Jest to klasa służąca do otrzymania ID zasobów dla podanej ikony.
 */
public class IconProvider {
	/**
	 * Jest to metoda uzyskująca ID ikony.
	 */
	public static int getIconId(IconType it) {
		return getIconId(it, false);
	}
	// zwróci id ikony, drugi argument: false - nasze ikony, true - domyślne ikony android "material design"
	/**
	 * Jest to metoda uzyskująca ID ikony, która sprawdza dodatkowo czy ikona nie jest domyślną ikoną Androida.
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

	// użyj domyślnych ikon androida
	/**
	 * Jest to metoda która pozwala na użycie domyślnych ikon Androida.
	 */
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
