/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.gps;

/**
 * Jest to interfejs odpowiedzialny za zmianę tekstu.
 */
public interface TextFieldChanger {
	void changeTextField(String text, GPSValues whatValue);
}
