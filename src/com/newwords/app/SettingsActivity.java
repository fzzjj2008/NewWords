package com.newwords.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.Toast;

import com.newwords.database.WordsManager;
import com.newwords.utils.ExcelManager;
import com.newwords.utils.Settings;
import com.newwords.utils.SettingsManager;

public class SettingsActivity extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getFragmentManager().beginTransaction().replace(android.R.id.content,
				new SettingsFragment()).commit();
	}
	
	public static class SettingsFragment extends PreferenceFragment
			implements OnPreferenceChangeListener, OnPreferenceClickListener, OnSharedPreferenceChangeListener {
		
		private SwitchPreference mPrefAutoSave;
		private ListPreference mPrefTTS;
		private Preference mPrefPath;
		private Preference mPrefReset;
		private Preference mPrefAbout;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_fragment);

			// autosave
			mPrefAutoSave = (SwitchPreference) findPreference ("pref_autosave");
			
			// tts
			mPrefTTS = (ListPreference) findPreference("pref_tts");
			mPrefTTS.setSummary(mPrefTTS.getEntry());
			// 动态改变summary的值，要进行注册
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());  
	        prefs.registerOnSharedPreferenceChangeListener(this);
	        
	        // path
			mPrefPath = findPreference("pref_filepath");
			mPrefPath.setOnPreferenceClickListener(this);
			String filePath = SettingsManager.getString(getActivity(), Settings.PREF_FILE_PATH);
			if (filePath.length() == 0) {
				mPrefPath.setSummary(R.string.settings_filepath_nofile);
			} else {
				mPrefPath.setSummary(filePath);
			}
			
			// reset
			mPrefReset = findPreference("pref_reset");
			mPrefReset.setOnPreferenceClickListener(this);
			
			// about
			mPrefAbout = findPreference("pref_about");
			mPrefAbout.setOnPreferenceClickListener(this);
		}

		@Override
		public boolean onPreferenceClick(Preference preference) {
			if (preference.getKey().equals("pref_about")) {
				new AlertDialog.Builder(getActivity())
				.setTitle(R.string.dialog_about_title)
				.setIcon(R.drawable.ic_launcher)
				.setMessage(R.string.dialog_about_msg)
				.setPositiveButton(R.string.ok, null)
				.create().show();
			} else if (preference.getKey().equals("pref_reset")) {
				new AlertDialog.Builder(getActivity())
				.setTitle(R.string.dialog_reset_title)
				.setIcon(R.drawable.ic_launcher)
				.setMessage(R.string.dialog_reset_msg)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// reset
						SettingsManager.removeAll(getActivity());
						// 恢复Preference界面的控件
						mPrefAutoSave.setChecked(SettingsManager.getBoolean(getActivity(), Settings.PREF_AUTO_SAVE));
						mPrefTTS.setValue(SettingsManager.getString(getActivity(), Settings.PREF_TTS));
						mPrefPath.setSummary(R.string.settings_filepath_nofile);
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create().show();
			} else if (preference.getKey().equals("pref_filepath")) {
				Intent intent = new Intent(getActivity(), FileBrowserActivity.class);
				startActivityForResult(intent, 0);
			}
			return true;
		}

		@Override
		public boolean onPreferenceChange(Preference preference, Object obj) {
			return true;
		}

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (key.equals("pref_tts")) {
				mPrefTTS.setSummary(mPrefTTS.getEntry());
			}
		}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (requestCode == 0 && resultCode == 0) {
				// 读取xls文件
				String fileName = data.getStringExtra("filename");
				if (!ExcelManager.loadXlsFile(getActivity(), fileName)) {
					// 导入文件失败
					return;
				}
				// summary
				mPrefPath.setSummary(fileName);
				// mWords中没单词
				if (WordsManager.getSize() == 0) {
					Toast.makeText(getActivity(), R.string.file_err_empty_file, Toast.LENGTH_SHORT).show();
					return;
				}
				// 文件重新读取，需要从第一页开始浏览
				SettingsManager.putString(getActivity(), Settings.PREF_CURRENT_WORD, 0);
				// toast
				String toastText = getResources().getString(R.string.file_success) + fileName;
				Toast.makeText(getActivity(), toastText, Toast.LENGTH_LONG).show();
			}
//			super.onActivityResult(requestCode, resultCode, data);
		}
		
	}
	
}
