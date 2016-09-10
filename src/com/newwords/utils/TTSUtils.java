package com.newwords.utils;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.Toast;

import com.newwords.app.R;

public class TTSUtils {

	public static TextToSpeech mTTS = null;
	
	public static void initTTS(final Context context) {

		int lang = SettingsManager.getInteger(context, Settings.PREF_TTS);
		if (lang == Settings.PREF_TTS_NONE) {
			stop();
			return;
		}
		
		mTTS = new TextToSpeech(context, new OnInitListener() {
			@Override
			public void onInit(int status) {
				// 如果装载引擎TTS成功
				if (status == TextToSpeech.SUCCESS) {
					int lang = SettingsManager.getInteger(context, Settings.PREF_TTS);
					if (lang == Settings.PREF_TTS_UK) {
						// 英式发音
						int result = mTTS.setLanguage(Locale.UK);
						// 不支持
						if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE &&
								result != TextToSpeech.LANG_AVAILABLE) {
							Toast.makeText(context, R.string.tts_not_support, Toast.LENGTH_SHORT).show();
						}
					} else if (lang == Settings.PREF_TTS_US) {
						// 美式发音
						int result = mTTS.setLanguage(Locale.US);
						// 不支持
						if (result != TextToSpeech.LANG_COUNTRY_AVAILABLE &&
								result != TextToSpeech.LANG_AVAILABLE) {
							Toast.makeText(context, R.string.tts_not_support, Toast.LENGTH_SHORT).show();
						}
					} else {
						Log.e("TextToSpeech", "不支持这种语言的发音");
					}
				} else {
					Toast.makeText(context, R.string.tts_engine_err, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	public static void speak(String text) {
		if (mTTS == null) {
			return;
		}		
//		mTTS.speak(text, TextToSpeech.QUEUE_ADD, null);
		mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}
	
	public static void stop() {
		// 关闭，释放资源
		if (mTTS != null) {
			mTTS.stop();
			mTTS.shutdown();
		}
		mTTS = null;
	}
}
