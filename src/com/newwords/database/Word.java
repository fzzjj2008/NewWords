package com.newwords.database;

public class Word implements Comparable<Word> {

	public int mId = 0;
	public String mName = null;
	public String mPronunciation = null;
	public String mMeaning = null;
	public String mSentence = null;
	
	@Override
	public int compareTo(Word word) {
		return this.mName.compareTo(word.mName);
	}
	
}