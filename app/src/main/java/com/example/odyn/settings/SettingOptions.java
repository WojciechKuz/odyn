/*
    BSD 3-Clause License
    Copyright (c) Wojciech Kuźbiński <wojkuzb@mat.umk.pl>, Damian Gałkowski <galdam@mat.umk.pl>, 2023

    See https://aleks-2.mat.umk.pl/pz2022/zesp10/#/project-info for see license text.
*/

package com.example.odyn.settings;

public class SettingOptions {
	public static final String[] storageOptions = {"Karta SD", "Pamięć wewnętrzna"};
	public static final String[] LeftOrRight = {"Lewo", "Prawo"};
	public static final String[] LengthRecords = {"30s", "1 min", "2 min", "3 min", "4 min", "5 min"};
	public static final String[] SizeVideo = {"512MB", "1GB", "2GB", "4GB", "8GB", "12GB", "16GB", "32GB", "64GB", "128GB"};
	public static final String[] SizeEmergency = SizeVideo; // to to samo

	// kolejność
	public static final String[][] optionsOrder = {null, storageOptions, LeftOrRight, LengthRecords, SizeVideo, SizeEmergency}; // to tylko referencje

	// te nie będą w Layou'cie. Lista wartości wykorzystywana przy logice ustawień
	public static final int[] lengthValuesSeconds = {30, 60, 120, 180, 240, 300};  // musi odpowiadać wartościom w LengthRecords tylko że w sekundach
	public static final int[] sizeValuesMB = {512, 1024, 2048, 4096, 8192, 12288, 16384, 32768, 65536, 131072}; // musi odpowiadać wartościom w SizeVideo tylko że w MegaBajtach
}
