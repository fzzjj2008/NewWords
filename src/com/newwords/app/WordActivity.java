package com.newwords.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.newwords.database.Word;
import com.newwords.database.WordsManager;
import com.newwords.utils.Settings;
import com.newwords.utils.SettingsManager;
import com.newwords.utils.TTSUtils;

public class WordActivity extends FragmentActivity {
	
	private static ViewPager mPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_pager);
	
		// 加载控件
		mPager = (ViewPager) findViewById (R.id.word_viewpager);	
		mPager.setAdapter(new WordAdapter(getSupportFragmentManager()));	
		
		// 读取单词
		int currentPage = SettingsManager.getInteger(this, Settings.PREF_CURRENT_WORD);
		if (currentPage < 0 || currentPage >= WordsManager.getSize()) {
			currentPage = 0;
		}
		mPager.setCurrentItem(currentPage);
		
		// TTS
		TTSUtils.initTTS(this);
	}
	
	@Override
	public void onBackPressed() {
		// 保存一下当前页数
		SettingsManager.putString(this, Settings.PREF_CURRENT_WORD, mPager.getCurrentItem());
		super.onBackPressed();
	}
	
	@Override
	protected void onStop() {
		// TTS
		TTSUtils.stop();
		super.onStop();
	}
	
	public static class WordAdapter extends FragmentStatePagerAdapter {
		
		public WordAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public int getCount() {
			return WordsManager.getSize();
		}
		
		@Override
		public Fragment getItem(int position) {
			return WordFragment.newInstance(position);
		}
		
	}
	
	public static class WordFragment extends Fragment implements OnClickListener {
		
		int mNum;
		
		TextView mWordNameText;
		TextView mWordPronunciationText;
		TextView mWordMeaningText;
		TextView mWordSentenceText;
		TextView mWordPageText;
		
		static WordFragment newInstance(int num) {
			WordFragment f = new WordFragment();
			// 传参num
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
            View view = inflater.inflate(R.layout.word, container, false);
            
            // 加载控件    		
    		mWordNameText = (TextView) view.findViewById (R.id.word_name);
    		mWordPronunciationText = (TextView) view.findViewById (R.id.word_pronunciation);
    		mWordMeaningText = (TextView) view.findViewById (R.id.word_meaning);
    		mWordSentenceText = (TextView) view.findViewById (R.id.word_sentence);
    		mWordPageText = (TextView) view.findViewById (R.id.word_page);
    		
    		// 监听器
    		mWordPronunciationText.setOnClickListener(this);
    		mWordPageText.setOnClickListener(this);
    		
    		// 设置控件的值
    		Word word = WordsManager.getWord(mNum);
    		mWordNameText.setText(word.mName);
    		mWordPronunciationText.setText(word.mPronunciation);
    		mWordMeaningText.setText(word.mMeaning);
    		mWordSentenceText.setText(word.mSentence);
    		mWordPageText.setText((mNum + 1) + "/" + WordsManager.getSize());
			
            return view;
		}
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.word_pronunciation:
				TTSUtils.speak(mWordNameText.getText().toString());
				break;
			case R.id.word_page:
				// 跳页对话框
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
							// 输入为空
							return;
						} else {
							int page = 0;
							try {
								page = Integer.parseInt(editVal);
							} catch (NumberFormatException e) {
								Toast.makeText(getActivity(), R.string.dialog_jump_err, Toast.LENGTH_SHORT).show();
								return;
							}
							if (page >= 1 && page <= WordsManager.getSize()) {
								// 跳页
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
		
	}
	
}