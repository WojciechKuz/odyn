package com.example.odyn.settings;

public class SettingNames {
	public static final String[] switches = {
			"no 0", "switch1", "switch2", "switch3","switch4", "switch5", "switch6"
	};
	public static final String[] spinners = {
			"no 0", "storage_option", "Left_Right", "Length_record", "Size_video", "Size_emergency"
	};
}
	/*
	public static Map<String, Integer> settingIndex = createMap();
	private static Map<String, Integer> createMap() {
		//
		Map<String, Integer> map = new HashMap<>(switches.length - 1 + spinners.length - 1); // odejmuję po 1, bo nie będzie pierwszego elementu
		for(int i = 1; i < switches.length; i++) {
			map.put(switches[i], i);
		}
		for(int i = 1; i < spinners.length; i++) {
			map.put(spinners[i], i);
		}
		return null;
	}
	public static int getSettingIndex(String settingName) {
		Integer index = settingIndex.get(settingName);
		if(index == null) {
			return 0;
		}
		return index;
	}
	*/

