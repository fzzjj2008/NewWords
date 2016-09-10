package com.newwords.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import android.content.Context;
import android.widget.Toast;

import com.newwords.app.R;
import com.newwords.database.Word;
import com.newwords.database.WordsManager;

public class ExcelManager {
	
	/**
	 * ��ȡexcel�ļ�
	 * @param filename
	 * @param words
	 * @throws BiffException
	 * @throws IOException
	 */
	public static void readExcel(File filename, List<Word> words) throws BiffException, IOException {
		// ��ȡExcel�ļ�����
		Workbook wb = Workbook.getWorkbook(filename);
		// ��ȡ��һ��������
		Sheet sheet = wb.getSheet(0);
		final int ROWS = sheet.getRows();
		
		// ��ȡExcel�ļ�
		words.clear();
		for (int i = 0; i < ROWS; i++) {
			Word word = new Word();
			word.mId = i;
			word.mName = sheet.getCell(0, i).getContents();
			word.mPronunciation = sheet.getCell(1, i).getContents();
			word.mMeaning = sheet.getCell(2, i).getContents();
			word.mSentence = sheet.getCell(3, i).getContents();
			words.add(word);
		}
		
		if (wb != null) {
			wb.close();
		}
	}


	public static boolean loadXlsFile(Context context, String fileName) {
		// ��ǰ·��Ϊ��
		if (fileName == null || fileName.length() == 0) {
			Toast.makeText(context, R.string.file_err_empty_path, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		File file = new File(fileName);
		// �ļ�������
		if (!file.exists()) {
			Toast.makeText(context, R.string.file_err_not_exist, Toast.LENGTH_SHORT).show();
			return false;
		}
		// ��ȡXls�ļ�
		try {
			ExcelManager.readExcel(file, WordsManager.getWords());
			// ����
			WordsManager.sortWords();
		} catch (BiffException | IOException e) {
			Toast.makeText(context, R.string.file_err_read_file, Toast.LENGTH_SHORT).show();
			return false;
		}

		// �ļ��Ѿ���ȡ�ɹ������浱ǰ��filepath
		SettingsManager.putString(context, Settings.PREF_FILE_PATH, fileName);
		
		return true;
	}
	
}
