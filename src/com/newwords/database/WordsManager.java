package com.newwords.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordsManager {

	private static List<Word> mWords = new ArrayList<Word>();
	
	public static List<Word> getWords() {
		return mWords;
	}
	
	public static int getSize() {
		return mWords.size();
	}
	
	public static Word getWord(int row) {
		return mWords.get(row);
	}

	public static void sortWords() {
		Collections.sort(mWords);		
	}
}