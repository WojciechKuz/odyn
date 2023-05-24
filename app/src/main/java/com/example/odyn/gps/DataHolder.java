/*
    BSD 3-Clause License
    Copyright (c) Viacheslav Kushinir <kushnir@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.gps;

/**
 * Class responsible for holding GPS-related data so it can be accessed by multiple other classes
 */
public class DataHolder {
	private static DataHolder instance;

	private String longitude;
	private String latitude;
	private String speed;
	private String counter;
	private String timer;

	/**
	 * Constructor
	 */
	private DataHolder() {	}

	/**
	 * Method responsible for returning instance of DataHolder, if it exists. If it doesn't, creates one.
	 * @return instance
	 */
	public static synchronized DataHolder getInstance() {
		if (instance == null) {
			instance = new DataHolder();
		}
		return instance;
	}

	/**
	 * Method returning stored longitude
	 * @return longitude
	 */
	public String getLongitude() {
		return longitude;
	}

	/**
	 * Method used to update longitude value
	 */
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	/**
	 * Method returning stored latitude
	 * @return latitude
	 */
	public String getLatitude() {
		return latitude;
	}

	/**
	 * Method used to update latitude value
	 */
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	/**
	 * Method returning stored speed
	 * @return speed
	 */
	public String getSpeed() {
		return speed;
	}

	/**
	 * Method used to update speed value
	 */
	public void setSpeed(String speed) {
		this.speed = speed;
	}

	/**
	 * Method returning stored counter
	 * @return counter
	 */
	public String getCounter() {
		return counter;
	}

	/**
	 * Method used to update counter value
	 */
	public void setCounter(String counter) {
		this.counter = counter;
	}

	/**
	 * Method returning stored timer
	 * @return counter
	 */
	public String getTimer() {
		return timer;
	}

	/**
	 * Method used to update timer value
	 */
	public void setTimer(String timer) {
		this.timer = timer;
	}
}
