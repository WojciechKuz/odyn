package com.example.odyn.settings;

public class SettingOptions {
	public static final String DEFAULT_SIZE_VIDEO = "DefaultSizeVideo";
	public static final String[] storageOptions = {"Karta SD", "Pamięć wewnętrzna"};
	public static final String[] LeftOrRight = {"Lewo", "Prawo"};
	public static final String[] LengthRecords = {"30", "60", "120", "180", "240", "300"}; // what.
	public static final String[] SizeVideo = {"512MB", "1024MB", "2048MB", "4096MB", "8192MB", "12288MB", "16384MB", "32768MB", "65536MB", "131072MB"};
	public static final String[] SizeEmergency = {"512MB", "1024MB", "2048MB", "4096MB", "8192MB", "12288MB", "16384MB", "32768MB", "65536MB", "131072MB"};



	public static final String[][] optionsOrder = {null, storageOptions, LeftOrRight, LengthRecords, SizeVideo, SizeEmergency}; // to tylko referencje
}
