package com.newwords.utils;

import java.util.HashMap;
import java.util.Map;

public class Settings {

	public static Map<String, String> defaultMap = new HashMap<String, String>();
	
	public static final String PREF_AUTO_SAVE = "pref_autosave";
	public static final String PREF_FILE_PATH = "pref_filepath";
	public static final String PREF_CURRENT_WORD = "pref_currentword";
	public static final String PREF_TTS = "pref_tts";
	public static final String PREF_TEST_WAY = "pref_testway";
	public static final String PREF_TEST_ORDER = "pref_testorder";
	public static final String PREF_TEST_NUM = "pref_testnum";
	
	public static final int PREF_TTS_NONE = 0;
	public static final int PREF_TTS_UK = 1;
	public static final int PREF_TTS_US = 2;
	public static final int PREF_TEST_WAY_E2C = 0;
	public static final int PREF_TEST_WAY_C2E = 1;
	public static final int PREF_TEST_ORDER_ALPHABET = 0;
	public static final int PREF_TEST_ORDER_RANDOM = 1;
	public static final int PREF_TEST_NUM_ALL = -1;
	
}
