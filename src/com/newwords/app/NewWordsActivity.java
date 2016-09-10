package com.newwords.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.newwords.database.WordsManager;
import com.newwords.utils.ExcelManager;
import com.newwords.utils.Settings;
import com.newwords.utils.SettingsManager;

public class NewWordsActivity extends Activity implements OnClickListener {

	private Button mWordsViewButton;
	private Button mWordsInputButton;
	private Button mWordsTestButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newwords);
		
		// ��ȡ�ؼ�
		mWordsViewButton = (Button) findViewById (R.id.btn_newwords_view);
		mWordsInputButton = (Button) findViewById (R.id.btn_newwords_input);
		mWordsTestButton = (Button) findViewById (R.id.btn_newwords_test);
		
		// ��ť�¼�
		mWordsViewButton.setOnClickListener(this);
		mWordsInputButton.setOnClickListener(this);
		mWordsTestButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_newwords_view: {
			String fileName = SettingsManager.getString(this, Settings.PREF_FILE_PATH);
			// ����Xls�ļ�
			if (!ExcelManager.loadXlsFile(this, fileName)) {
				// �����ļ�ʧ��
				return;
			}
			// mWords��û����
			if (WordsManager.getSize() == 0) {
				Toast.makeText(this, R.string.file_err_empty_file, Toast.LENGTH_SHORT).show();
				return;
			}
			// ����WordActivity
			Intent intent = new Intent(NewWordsActivity.this, WordActivity.class);
			startActivity(intent);
			// toast
			String toastText = getResources().getString(R.string.file_success) + fileName;
			Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
			break;
		}
		case R.id.btn_newwords_input: {
			Intent intent = new Intent(NewWordsActivity.this, FileBrowserActivity.class);
			startActivityForResult(intent, 0);
			break;
		}
		case R.id.btn_newwords_test: {
			String fileName = SettingsManager.getString(this, Settings.PREF_FILE_PATH);
			// ����Xls�ļ�
			if (!ExcelManager.loadXlsFile(this, fileName)) {
				// �����ļ�ʧ��
				return;
			}
			// mWords��û����
			if (WordsManager.getSize() == 0) {
				Toast.makeText(this, R.string.file_err_empty_file, Toast.LENGTH_SHORT).show();
				return;
			}
			// ����TestActivity
			Intent intent = new Intent(NewWordsActivity.this, TestActivity.class);
			startActivity(intent);
			break;
		}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0 && resultCode == 0) {
			// ��ȡxls�ļ�
			String fileName = data.getStringExtra("filename");
			if (!ExcelManager.loadXlsFile(this, fileName)) {
				// �����ļ�ʧ��
				return;
			}
			// mWords��û����
			if (WordsManager.getSize() == 0) {
				Toast.makeText(this, R.string.file_err_empty_file, Toast.LENGTH_SHORT).show();
				return;
			}
			// �ļ����¶�ȡ����Ҫ�ӵ�һҳ��ʼ���
			SettingsManager.putString(this, Settings.PREF_CURRENT_WORD, 0);
			// ����WordActivity
			Intent intent = new Intent(NewWordsActivity.this, WordActivity.class);
			startActivity(intent);
			// toast
			String toastText = getResources().getString(R.string.file_success) + fileName;
			Toast.makeText(this, toastText, Toast.LENGTH_LONG).show();
		}
//		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
