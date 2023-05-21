/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.main_service;

import com.example.odyn.cam.Cam;

/**
 Jest to interfejs odpowiedzialny za przekazywanie kamery z klasy Cam z MainScreen do MainActivity.
 */
public interface RecieveCamInterface {
	void recieveCam(Cam cam);
}
