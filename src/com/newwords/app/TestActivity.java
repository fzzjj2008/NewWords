package com.newwords.app;

import com.newwords.database.WordsManager;
import com.newwords.utils.Settings;
import com.newwords.utils.SettingsManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class TestActivity extends Activity implements OnClickListener, OnTouchListener {
	
	private RelativeLayout mTestLayout;
	private RadioButton mTestWayE2CRadio;
	private RadioButton mTestWayC2ERadio;
	private RadioButton mTestOrderAlphaRadio;
	private RadioButton mTestOrderRandomRadio;
	private RadioButton mTestNumAllRadio;
	private RadioButton mTestNumGivenRadio;
	private EditText mTestNumEdit;
	private Button mTestStartButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test);
		
		// ���ؿؼ�
		mTestLayout = (RelativeLayout) findViewById (R.id.test_layout);
		mTestWayE2CRadio = (RadioButton) findViewById (R.id.test_way_choice_e2c);
		mTestWayC2ERadio = (RadioButton) findViewById (R.id.test_way_choice_c2e);
		mTestOrderAlphaRadio = (RadioButton) findViewById (R.id.test_order_alphabet);
		mTestOrderRandomRadio = (RadioButton) findViewById (R.id.test_order_random);
		mTestNumAllRadio = (RadioButton) findViewById (R.id.test_number_all);
		mTestNumGivenRadio = (RadioButton) findViewById (R.id.test_number_given);
		mTestNumEdit = (EditText) findViewById (R.id.test_number_edit);
		mTestStartButton = (Button) findViewById (R.id.btn_test_start);
		
		// �Ӽ�����
		mTestWayE2CRadio.setOnClickListener(this);
		mTestWayC2ERadio.setOnClickListener(this);
		mTestOrderAlphaRadio.setOnClickListener(this);
		mTestOrderRandomRadio.setOnClickListener(this);
		mTestNumAllRadio.setOnClickListener(this);
		mTestNumGivenRadio.setOnClickListener(this);
		mTestNumEdit.setOnTouchListener(this);
		mTestStartButton.setOnClickListener(this);
		// ����focusableInTouchMode���ƣ���������ѹر�������¼�д��onClick�����С����
		mTestLayout.setOnTouchListener(this);
		
		// Settings
		int testway = SettingsManager.getInteger(this, Settings.PREF_TEST_WAY);
		int testorder = SettingsManager.getInteger(this, Settings.PREF_TEST_ORDER);
		int testnum = SettingsManager.getInteger(this, Settings.PREF_TEST_NUM);
		
		if (testway == Settings.PREF_TEST_WAY_E2C) {
			mTestWayE2CRadio.setChecked(true);
			mTestWayC2ERadio.setChecked(false);
		} else if (testway == Settings.PREF_TEST_WAY_C2E){
			mTestWayE2CRadio.setChecked(false);
			mTestWayC2ERadio.setChecked(true);
		}
		if (testorder == Settings.PREF_TEST_ORDER_ALPHABET) {
			mTestOrderAlphaRadio.setChecked(true);
			mTestOrderRandomRadio.setChecked(false);
		} else if (testorder == Settings.PREF_TEST_ORDER_RANDOM){
			mTestOrderAlphaRadio.setChecked(false);
			mTestOrderRandomRadio.setChecked(true);
		}
		if (testnum == Settings.PREF_TEST_NUM_ALL) {
			mTestNumAllRadio.setChecked(true);
			mTestNumGivenRadio.setChecked(false);
			mTestNumEdit.setText("");
		} else {
			mTestNumAllRadio.setChecked(false);
			mTestNumGivenRadio.setChecked(true);
			mTestNumEdit.setText("" + testnum);
		}
		
	}
	
	@Override
	protected void onPause() {
		// Settings
		int testway = mTestWayE2CRadio.isChecked() ? 0 : 1;
		int testorder = mTestOrderAlphaRadio.isChecked() ? 0 : 1;
		String editval = mTestNumEdit.getText().toString();
		int testnum = Settings.PREF_TEST_NUM_ALL;
		if (editval != null && editval.length() > 0) {
			testnum = Integer.parseInt(editval);
		}
		SettingsManager.putString(this, Settings.PREF_TEST_WAY, testway);
		SettingsManager.putString(this, Settings.PREF_TEST_ORDER, testorder);
		SettingsManager.putString(this, Settings.PREF_TEST_NUM, testnum);
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		
		switch (v.getId()) {
		case R.id.test_way_choice_e2c:
		case R.id.test_way_choice_c2e:
		case R.id.test_order_alphabet:
		case R.id.test_order_random:
			// ʹ��Layout��ý���
			mTestLayout.requestFocus();
			// �ر������
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}
			break;
		case R.id.test_number_all:
			mTestNumEdit.setText("");
			// ʹ��Layout��ý���
			mTestLayout.requestFocus();
			// �ر������
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}
			break;
		case R.id.test_number_given:
			// ʹ��EditText��ý���
			mTestNumEdit.requestFocus();
			mTestNumEdit.setSelection(mTestNumEdit.getText().toString().length());
			// �������
			imm.showSoftInput(getCurrentFocus(), 0);
			// ������̣���ʱ
//			Timer timer = new Timer();
//			timer.schedule(new TimerTask() {
//				@Override
//				public void run() {
//					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//					imm.showSoftInput(getCurrentFocus(), InputMethodManager.RESULT_SHOWN);
//				}
//				
//			}, 100);
			break;
		case R.id.btn_test_start:
			// �ر������
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
			}
			// ȫ������
			if (mTestNumAllRadio.isChecked()) {
				// ��ʼTest
				Intent intent = new Intent(TestActivity.this, TestChoiceActivity.class);
				startActivity(intent);
				return;
			}
			// ������Ŀ����
			String editval = mTestNumEdit.getText().toString();
			int testnum = -1;
			if (editval != null && editval.length() > 0) {
				testnum = Integer.parseInt(editval);
			} else {
				Toast.makeText(this, R.string.test_number_err_empty, Toast.LENGTH_SHORT).show();
				return;
			}
			if (testnum >= 5 && testnum <= WordsManager.getSize()) {
				// ��ʼTest
				Intent intent = new Intent(TestActivity.this, TestChoiceActivity.class);
				startActivity(intent);
			} else if (testnum < 5) {
				Toast.makeText(this, R.string.test_number_err_too_few, Toast.LENGTH_SHORT).show();
				return;
			} else if (testnum > WordsManager.getSize()) {
				Toast.makeText(this, R.string.test_number_err_too_many, Toast.LENGTH_SHORT).show();
				return;
			}
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (v.getId()) {
		case R.id.test_layout: {
			switch (event.getAction()) {
			// ��������ΪACTION_UP��Ϊ�˱���Scroll�¼�
			case MotionEvent.ACTION_UP:
				// ʹ��Layout��ý���
				mTestLayout.requestFocus();
				// �ر������
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				if (imm.isActive()) {
					imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				}
				break;
			}
			// �����������Ϊfalse����Ҫ��ACTION_DOWN��ACTION_UP�ﶼreturn true
			return true;
		}
		case R.id.test_number_edit: {
			mTestNumAllRadio.setChecked(false);
			mTestNumGivenRadio.setChecked(true);
			mTestNumEdit.requestFocus();
			mTestNumEdit.setSelection(mTestNumEdit.getText().toString().length());
			// �������
			InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			imm.showSoftInput(getCurrentFocus(), 0);
			return true;
		}
		}
		return false;
	}

}
