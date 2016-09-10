package com.newwords.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.newwords.utils.Settings;
import com.newwords.utils.SettingsManager;

public class MainActivity extends Activity implements OnClickListener {

	private Button mNewWordsButton;
	private Button mSettingsButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// 获取控件
		mNewWordsButton = (Button) findViewById (R.id.btn_newwords);
		mSettingsButton = (Button) findViewById (R.id.btn_settings);
		
		// 按钮事件
		mNewWordsButton.setOnClickListener(this);
		mSettingsButton.setOnClickListener(this);
		
		// 初始化Settings
		SettingsManager.initDefaultMap(this);
	}
	
	@Override
	public void onBackPressed() {
		new AlertDialog.Builder(this)
		.setTitle(R.string.dialog_exit_title)
		.setMessage(R.string.dialog_exit_msg)
		.setIcon(android.R.drawable.ic_dialog_info)
	    .setNegativeButton(R.string.cancel, null)
		.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				boolean autosave = SettingsManager.getBoolean(MainActivity.this, Settings.PREF_AUTO_SAVE);
				if (!autosave) {
					// 如果不保存，需要清空SharedPreferences
					SettingsManager.removeAll(MainActivity.this);
					SettingsManager.putBoolean(MainActivity.this, Settings.PREF_AUTO_SAVE, autosave);
				}
				MainActivity.this.finish();
			}
		})
	    .show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_newwords: {
			Intent intent = new Intent(MainActivity.this, NewWordsActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.btn_settings: {
			Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intent);
			break;
		}
		}
		
	}

}
