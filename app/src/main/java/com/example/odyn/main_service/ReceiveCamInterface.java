package com.example.odyn.main_service;

import com.example.odyn.cam.Cam;

/**
 Jest to interfejs odpowiedzialny za przekazywanie kamery z klasy Cam z MainScreen do MainActivity.
 */
public interface ReceiveCamInterface {
	void receiveCam(Cam cam);
}
