package com.newwords.app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.newwords.database.ChoiceItem;
import com.newwords.database.Word;
import com.newwords.database.WordsManager;
import com.newwords.utils.RandomUtils;
import com.newwords.utils.Settings;
import com.newwords.utils.SettingsManager;

public class TestChoiceActivity extends FragmentActivity {
	
	private static List<ChoiceItem> mQuestions = new ArrayList<ChoiceItem>();

	private static ViewPager mPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_choice_pager);
		
		// ���ؿؼ�
		mPager = (ViewPager) findViewById (R.id.test_choice_viewpager);
		
		// ����
		int testnum = SettingsManager.getInteger(this, Settings.PREF_TEST_NUM);
		if (testnum == Settings.PREF_TEST_NUM_ALL) {
			testnum = WordsManager.getSize();
		}
		createQuestions(testnum);

		mPager.setAdapter(new TestChoiceAdapter(getSupportFragmentManager()));
	}
	
	
	private void createQuestions(int count) {
		mQuestions.clear();
		// �ڵ��ʱ������ѡ��count�������浽questions��
		int total = WordsManager.getSize();
		
		// ����count�����ظ��������
		int[] questionId = Arrays.copyOf(RandomUtils.randomCommon(0, total-1, count), count);
		// ��Ŀ����
		int testorder = SettingsManager.getInteger(this, Settings.PREF_TEST_ORDER);
		if (testorder == Settings.PREF_TEST_ORDER_ALPHABET) {
			// ˳��
			Arrays.sort(questionId);
		} else if (testorder == Settings.PREF_TEST_ORDER_RANDOM) {
			// ����
		} else {
			Log.e("TestChoiceActivity", "δ֪�Ĳ���˳��");
		}
		
		// �������
		Word word = new Word();
		for (int i = 0; i < count; i++) {
			ChoiceItem item = new ChoiceItem();
			word = WordsManager.getWord(questionId[i]);
			item.mId = i;
			item.mName = word.mName;
			item.mMeaning = word.mMeaning;
			item.mCorrectAnswer = (int) (Math.random() * 4);
			item.mChoiceItemId = Arrays.copyOf(createChoiceItems(count, item.mCorrectAnswer, i), 4);
			mQuestions.add(item);
		}
	}
	
	private int[] createChoiceItems(int count, int correct, int correctId) {
		int[] items = new int[4];
		// ��ȷѡ��
		items[correct] = correctId;
		for (int i = 0; i < 4; i++) {
			if (i == correct) {
				continue;
			}
			// �Ҵ���ѡ�Ҫ��ѡ��ظ�
			while (true) {
				items[i] = (int) (Math.random() * count);
				if (items[i] == correctId)
					continue;
				boolean flag = true;
				for (int j = 0; j < i; j++) {
					if (items[j] == items[i]) {
						flag = false;
						break;
					}
				}
				if (flag) {
					break;
				}
			}
		}
		return items;
	}
	
	public static class TestChoiceAdapter extends FragmentStatePagerAdapter {
		
		public TestChoiceAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public int getCount() {
			return mQuestions.size();
		}
		
		@Override
		public Fragment getItem(int position) {
			return TestChoiceFragment.newinstance(position);
		}
		
	}
	
	public static class TestChoiceFragment extends Fragment implements OnClickListener {
		
		int mNum;
		ChoiceItem mChoiceItem;

    	TextView mChoiceQuestionText;
    	TextView[] mChoiceText;
    	TextView mChoicePageText;
    	Button mShowAnswerButton;
    	Button mEndTestButton;
		
		static TestChoiceFragment newinstance(int num) {
			TestChoiceFragment f = new TestChoiceFragment();
			// ����num
            Bundle args =new Bundle();
            args.putInt("num", num);
            f.setArguments(args);
			return f;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mNum = getArguments() != null ? getArguments().getInt("num") : 0;
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.test_choice, container, false);
            
            // ���ؿؼ�
    		mChoiceQuestionText = (TextView) view.findViewById (R.id.test_choice_question);
    		mChoiceText = new TextView[4];
    		mChoiceText[0] = (TextView) view.findViewById (R.id.test_choice_a);
    		mChoiceText[1] = (TextView) view.findViewById (R.id.test_choice_b);
    		mChoiceText[2] = (TextView) view.findViewById (R.id.test_choice_c);
    		mChoiceText[3] = (TextView) view.findViewById (R.id.test_choice_d);
    		mChoicePageText = (TextView) view.findViewById (R.id.test_choice_page);
    		mShowAnswerButton = (Button) view.findViewById (R.id.test_choice_btn_show_answer);
    		mEndTestButton = (Button) view.findViewById (R.id.test_choice_btn_end_test);
    		
    		// ������
    		for (int i = 0; i < 4; i++) {
    			mChoiceText[i].setOnClickListener(this);
    		}
    		mShowAnswerButton.setOnClickListener(this);
    		mEndTestButton.setOnClickListener(this);
    		mChoicePageText.setOnClickListener(this);
    		
    		// ������Ŀѡ��
    		mChoiceItem = mQuestions.get(mNum);
            // ���ÿؼ���ֵ
    		loadQuestion();
    		
            return view;
		}

		@Override
		public void onClick(View v) {
			Drawable drawableCorrect = getResources().getDrawable(R.drawable.test_choice_correct);
			drawableCorrect.setBounds(0, 0, drawableCorrect.getMinimumWidth(), drawableCorrect.getMinimumHeight());
			Drawable drawableWrong = getResources().getDrawable(R.drawable.test_choice_wrong);
			drawableWrong.setBounds(0, 0, drawableWrong.getMinimumWidth(), drawableWrong.getMinimumHeight());
						
			switch (v.getId()) {
			case R.id.test_choice_a:
				if (mChoiceItem.mbAnswered) {
					return;
				}
				mChoiceItem.mbAnswered = true;
				mChoiceItem.mChoiceAnswer = 0;
				// drawable
				mChoiceText[mChoiceItem.mChoiceAnswer].setCompoundDrawables(drawableWrong, null, null, null);
				mChoiceText[mChoiceItem.mCorrectAnswer].setCompoundDrawables(drawableCorrect, null, null, null);
				// textcolor
				mChoiceText[mChoiceItem.mChoiceAnswer].setTextColor(getResources().getColor(R.color.color_light_red));
				mChoiceText[mChoiceItem.mCorrectAnswer].setTextColor(getResources().getColor(R.color.color_light_green));
				// clickable
				for (int i = 0; i < 4; i++) {
					mChoiceText[i].setClickable(false);
				}
				mShowAnswerButton.setClickable(false);
				break;
			case R.id.test_choice_b:
				if (mChoiceItem.mbAnswered) {
					return;
				}
				mChoiceItem.mbAnswered = true;
				mChoiceItem.mChoiceAnswer = 1;
				// drawable
				mChoiceText[mChoiceItem.mChoiceAnswer].setCompoundDrawables(drawableWrong, null, null, null);
				mChoiceText[mChoiceItem.mCorrectAnswer].setCompoundDrawables(drawableCorrect, null, null, null);
				// textcolor
				mChoiceText[mChoiceItem.mChoiceAnswer].setTextColor(getResources().getColor(R.color.color_light_red));
				mChoiceText[mChoiceItem.mCorrectAnswer].setTextColor(getResources().getColor(R.color.color_light_green));
				// clickable
				for (int i = 0; i < 4; i++) {
					mChoiceText[i].setClickable(false);
				}
				mShowAnswerButton.setClickable(false);
				break;
			case R.id.test_choice_c:
				if (mChoiceItem.mbAnswered) {
					return;
				}
				mChoiceItem.mbAnswered = true;
				mChoiceItem.mChoiceAnswer = 2;
				// drawable
				mChoiceText[mChoiceItem.mChoiceAnswer].setCompoundDrawables(drawableWrong, null, null, null);
				mChoiceText[mChoiceItem.mCorrectAnswer].setCompoundDrawables(drawableCorrect, null, null, null);
				// textcolor
				mChoiceText[mChoiceItem.mChoiceAnswer].setTextColor(getResources().getColor(R.color.color_light_red));
				mChoiceText[mChoiceItem.mCorrectAnswer].setTextColor(getResources().getColor(R.color.color_light_green));
				// clickable
				for (int i = 0; i < 4; i++) {
					mChoiceText[i].setClickable(false);
				}
				mShowAnswerButton.setClickable(false);
				break;
			case R.id.test_choice_d:
				if (mChoiceItem.mbAnswered) {
					return;
				}
				mChoiceItem.mbAnswered = true;
				mChoiceItem.mChoiceAnswer = 3;
				// drawable
				mChoiceText[mChoiceItem.mChoiceAnswer].setCompoundDrawables(drawableWrong, null, null, null);
				mChoiceText[mChoiceItem.mCorrectAnswer].setCompoundDrawables(drawableCorrect, null, null, null);
				// textcolor
				mChoiceText[mChoiceItem.mChoiceAnswer].setTextColor(getResources().getColor(R.color.color_light_red));
				mChoiceText[mChoiceItem.mCorrectAnswer].setTextColor(getResources().getColor(R.color.color_light_green));
				// clickable
				for (int i = 0; i < 4; i++) {
					mChoiceText[i].setClickable(false);
				}
				mShowAnswerButton.setClickable(false);
				break;
			case R.id.test_choice_btn_show_answer:
				if (mChoiceItem.mbAnswered) {
					return;
				}
				mChoiceItem.mbAnswered = true;
				mChoiceItem.mChoiceAnswer = mChoiceItem.mCorrectAnswer;
				mChoiceText[mChoiceItem.mCorrectAnswer].setCompoundDrawables(drawableCorrect, null, null, null);
				mChoiceText[mChoiceItem.mCorrectAnswer].setTextColor(getResources().getColor(R.color.color_light_green));
				// clickable
				for (int i = 0; i < 4; i++) {
					mChoiceText[i].setClickable(false);
				}
				mShowAnswerButton.setClickable(false);
				break;
			case R.id.test_choice_btn_end_test:
				// ͳ�ƽ��
				int answered = 0;
				int noanswer = 0;
				int correctAnswer = 0;
				for (ChoiceItem question : mQuestions) {
					if (question.mbAnswered) {
						answered++;
					} else {
						noanswer++;
					}
					if (question.mCorrectAnswer == question.mChoiceAnswer) {
						correctAnswer++;
					}
				}
				// dialog
				String testMsg = "�Ѵ�" + answered + "��"
						+ "\nδ��" + noanswer + "��"
						+ "\n��ȷ�ʣ�" + String.format("%.2f", (float) correctAnswer * 100 / mQuestions.size()) + "%";
				new AlertDialog.Builder(getActivity())
				.setTitle("���Խ��")
				.setMessage(testMsg)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// �˳�����
						getActivity().finish();
					}
				})
				.create().show();
				break;
			case R.id.test_choice_page:
				// ��ҳ�Ի���
				final EditText inputEdit = new EditText(getActivity());
				inputEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
				inputEdit.setText("" + (mNum + 1));
				inputEdit.setSelection(inputEdit.getText().toString().length());
				
				new AlertDialog.Builder(getActivity())
				.setTitle(R.string.dialog_jump_title)
				.setView(inputEdit)
				.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						String editVal = inputEdit.getText().toString();
						if (editVal == null || editVal.length() == 0) {
							// ����Ϊ��
							return;
						} else {
							int page = 0;
							try {
								page = Integer.parseInt(editVal);
							} catch (NumberFormatException e) {
								Toast.makeText(getActivity(), R.string.dialog_jump_err, Toast.LENGTH_SHORT).show();
								return;
							}
							if (page >= 1 && page <= mQuestions.size()) {
								// ��ҳ
								mPager.setCurrentItem(page - 1);
							} else {
								Toast.makeText(getActivity(), R.string.dialog_jump_err, Toast.LENGTH_SHORT).show();
							}
						}
					}
				})
				.setNegativeButton(R.string.cancel, null)
				.create().show();
				break;
			}
		}
		
		private void loadQuestion() {
			String question = null;
			String[] answer = new String[4];
			// ������Ŀ
			int testway = SettingsManager.getInteger(getActivity(), Settings.PREF_TEST_WAY);
			if (testway == Settings.PREF_TEST_WAY_E2C) {
				question = mChoiceItem.mName;
				for (int i = 0; i < 4; i++) {
					answer[i] = mQuestions.get(mChoiceItem.mChoiceItemId[i]).mMeaning;
				}
			} else if (testway == Settings.PREF_TEST_WAY_C2E) {
				question = mChoiceItem.mMeaning;
				for (int i = 0; i < 4; i++) {
					answer[i] = mQuestions.get(mChoiceItem.mChoiceItemId[i]).mName;
				}
			} else {
				Log.e("TestChoiceActivity", "δ֪�Ĳ��Է�ʽ");
			}
			// ������Ŀ���ؼ�
			mChoiceQuestionText.setText(question);
			for (int i = 0; i < 4; i++) {
				mChoiceText[i].setText(answer[i]);
			}
			// page
			int testnum = SettingsManager.getInteger(getActivity(), Settings.PREF_TEST_NUM);
			if (testnum == Settings.PREF_TEST_NUM_ALL) {
				testnum = WordsManager.getSize();
			}
			mChoicePageText.setText((mNum + 1) + "/" + testnum);
			
			// �������Ŀ�Ѿ��ش��
			if (mChoiceItem.mbAnswered) {
				// clickable
				for (int i = 0; i < 4; i++) {
					mChoiceText[i].setClickable(false);
				}
				mShowAnswerButton.setClickable(false);
				// drawable
				Drawable drawableCorrect = getResources().getDrawable(R.drawable.test_choice_correct);
				drawableCorrect.setBounds(0, 0, drawableCorrect.getMinimumWidth(), drawableCorrect.getMinimumHeight());
				Drawable drawableWrong = getResources().getDrawable(R.drawable.test_choice_wrong);
				drawableWrong.setBounds(0, 0, drawableWrong.getMinimumWidth(), drawableWrong.getMinimumHeight());
				mChoiceText[mChoiceItem.mChoiceAnswer].setCompoundDrawables(drawableWrong, null, null, null);
				mChoiceText[mChoiceItem.mCorrectAnswer].setCompoundDrawables(drawableCorrect, null, null, null);
				// textcolor
				mChoiceText[mChoiceItem.mChoiceAnswer].setTextColor(getResources().getColor(R.color.color_light_red));
				mChoiceText[mChoiceItem.mCorrectAnswer].setTextColor(getResources().getColor(R.color.color_light_green));
			}
		}
	}
	
} 