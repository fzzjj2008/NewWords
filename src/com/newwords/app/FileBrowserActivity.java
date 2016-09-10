package com.newwords.app;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FileBrowserActivity extends Activity implements OnClickListener {
	
	private static final String ROOT_PATH = Environment.getExternalStorageDirectory().toString();
	
	private TextView mBackText;
	private TextView mPathText;
	private ListView mListView;
	
	private static List<ListFile> mList = new ArrayList<ListFile>();
	private String mCurrentPath;
	private FileListAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.file_browser);
		
		// ��ȡ�ؼ�
		mBackText = (TextView) findViewById (R.id.file_back);
		mPathText = (TextView) findViewById (R.id.file_path);
		mListView = (ListView) findViewById (R.id.file_list);
		
		// �����¼�
		mBackText.setOnClickListener(this);
		
		// ��ʼ���ؼ�����
		mCurrentPath = ROOT_PATH;
		mBackText.setText(R.string.file_back_exit);
		updatePath(mCurrentPath);
		initList(mCurrentPath);
		mAdapter = new FileListAdapter(FileBrowserActivity.this);
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(mAdapter);
		
	}
	
	private void initList(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		mList.clear();
		for (File f : files) {
			if (f.isDirectory()) {
				// �ļ���
				mList.add(new ListFile(f.getName(), true));
			} else {
				// �ļ�
				String filename = f.getName();
				// �жϺ�׺
				int dot = filename.lastIndexOf('.');
				if (dot > -1 && dot < filename.length() - 1) {
					String extension = filename.substring(dot + 1);
					if (extension.equalsIgnoreCase("xls")) {
						mList.add(new ListFile(f.getName(), false));
					}
				}
			}
		}
		// ����
		Collections.sort(mList);
	}
	
	private void updatePath(String path) {
		mPathText.setText(getResources().getString(R.string.file_current_path) + path);		
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.file_back) {
			if (mCurrentPath.equals(ROOT_PATH)) {
				// ���Ѿ��Ǹ�Ŀ¼�����˳������
				onBackPressed();
			} else {
				// ���µ�ǰĿ¼
				int separator = mCurrentPath.lastIndexOf(File.separator);
				mCurrentPath = mCurrentPath.substring(0, separator);
				updatePath(mCurrentPath);
				initList(mCurrentPath);
				// ˢ��ListView
				mAdapter.notifyDataSetChanged();
				// ����mBackText������
				if (mCurrentPath.equals(ROOT_PATH)) {
					mBackText.setText(getResources().getString(R.string.file_back_exit));
				}
			}
		}
	}
	
	@Override
	public void onBackPressed() {
		// ����д�˷��������򷵻�intent����
		Intent intent = getIntent();
		intent.putExtra("filename", "");
		FileBrowserActivity.this.setResult(0, intent);
		FileBrowserActivity.this.finish();
//		super.onBackPressed();
	}
	
	private class ListFile implements Comparable<ListFile> {
	
		String fileName;
		boolean isFolder;
		
		public ListFile(String name, boolean type) {
			fileName = name;
			isFolder = type;
		}

		@Override
		public int compareTo(ListFile list) {
			// �ȽϹ����ļ������ȣ�����ĸ������
			if (this.isFolder != list.isFolder) {
				return this.isFolder ? -1 : 1;
			}
			return this.fileName.compareTo(list.fileName);
		}
	}
	
	private class FileListAdapter extends BaseAdapter implements OnItemClickListener {
		
		private LayoutInflater mInflater = null;
		
		private FileListAdapter(Context context) {
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// ViewHolder����
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = mInflater.inflate(R.layout.file_list, null);
				holder.listText = (TextView) convertView.findViewById(R.id.file_list_text);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			// �ؼ�
			ListFile list = mList.get(position);
			String fileName = list.fileName;
			boolean isFolder = list.isFolder;
			Drawable drawable = null;
			if (isFolder) {
				drawable = getResources().getDrawable(R.drawable.file_folder);
			} else {
				drawable = getResources().getDrawable(R.drawable.file_xls);
			}
			// ����ͼƬ��С������ͼƬ����ʾ
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			holder.listText.setCompoundDrawables(drawable, null, null, null);
			holder.listText.setText(fileName);
			
			return convertView;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			ListFile list = mList.get(position);
			String fileName = list.fileName;
			boolean isFolder = list.isFolder;
			if (isFolder) {
				// ���ļ���
				// ����mBackText����
				if (mCurrentPath.equals(ROOT_PATH)) {
					mBackText.setText(getResources().getString(R.string.file_back_higher_level));
				}
				mCurrentPath += (File.separator + fileName);
				updatePath(mCurrentPath);
				initList(mCurrentPath);
				// ˢ��ListView
				this.notifyDataSetChanged();
			} else {
				// ���ļ�
				mCurrentPath += (File.separator + fileName);
				Intent intent = getIntent();
				intent.putExtra("filename", mCurrentPath);
				FileBrowserActivity.this.setResult(0, intent);
				FileBrowserActivity.this.finish();
			}
		}
	}
	
	private static class ViewHolder {
		TextView listText;
	}
}
