package com.newwords.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.newwords.app.R;

public class SettingsManager {
	
	private static SharedPreferences getPreferences(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static boolean getBoolean(Context context, String key) {
		// getBoolean有点特殊，是针对pref_autosave的
		// 但是这个在SharedPreference中直接存为boolean而不是String，需修改
		boolean defaultVal = Boolean.parseBoolean(Settings.defaultMap.get(key));
		return getPreferences(context).getBoolean(key, defaultVal);
	}
	
	public static void putBoolean(Context context, String key, boolean val) {
		// 特殊
		getPreferences(context).edit().putBoolean(key, val).commit();
	}
	
	public static int getInteger(Context context, String key) {
		return Integer.parseInt(getString(context, key));
	}
	
	public static String getString(Context context, String key) {
		String defaultVal = Settings.defaultMap.get(key);
		return getPreferences(context).getString(key, defaultVal);
	}
	
	public static void putString(Context context, String key, int val) {
		String sval = String.valueOf(val);
		putString(context, key, sval);
	}
	
	public static void putString(Context context, String key, String val) {
		getPreferences(context).edit().putString(key, val).commit();
	}
	
	public static void initDefaultMap(Context context) {
		Settings.defaultMap.put(Settings.PREF_AUTO_SAVE, context.getResources().getString(R.string.default_autosave));
		Settings.defaultMap.put(Settings.PREF_FILE_PATH, context.getResources().getString(R.string.default_filepath));
		Settings.defaultMap.put(Settings.PREF_CURRENT_WORD, context.getResources().getString(R.string.default_currentword));
		Settings.defaultMap.put(Settings.PREF_TTS, context.getResources().getString(R.string.default_tts));
		Settings.defaultMap.put(Settings.PREF_TEST_WAY, context.getResources().getString(R.string.default_testway));
		Settings.defaultMap.put(Settings.PREF_TEST_ORDER, context.getResources().getString(R.string.default_testorder));
		Settings.defaultMap.put(Settings.PREF_TEST_NUM, context.getResources().getString(R.string.default_testnumber));		
	}
	
//	public static void remove(Context context, String key) {
//		getPreferences(context).edit().remove(key).commit();
//	}
	
	public static void removeAll(Context context) {
//		// autosave永久储存
//		remove(context, Settings.PREF_FILE_PATH);
//		remove(context, Settings.PREF_CURRENT_WORD);
//		remove(context, Settings.PREF_TTS);
//		remove(context, Settings.PREF_TEST_WAY);
//		remove(context, Settings.PREF_TEST_ORDER);
//		remove(context, Settings.PREF_TEST_NUM);
		// 全部清除
		getPreferences(context).edit().clear().commit();
	}
	
}
