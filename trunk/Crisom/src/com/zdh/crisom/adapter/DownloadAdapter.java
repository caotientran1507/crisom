package com.zdh.crisom.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdh.crisom.R;
import com.zdh.crisom.model.DocumentObject;
import com.zdh.crisom.utility.CommonUtil;
import com.zdh.crisom.utility.Constants;

public class DownloadAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<DocumentObject> listDocument = new ArrayList<DocumentObject>();
	private static LayoutInflater inflater = null;

	public DownloadAdapter(Activity a, ArrayList<DocumentObject> listDocument) {
		activity = a;
		this.listDocument = listDocument;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}

	public int getCount() {
		return listDocument.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.row_download, null);

		TextView name = (TextView) vi.findViewById(R.id.row_download_tv); 	
		ImageView ivAvatar = (ImageView) vi.findViewById(R.id.row_download_iv); 
		name.setText(listDocument.get(position).getDocType());
		if (CommonUtil.getExtensionFile(listDocument.get(position).getFile()).equalsIgnoreCase(Constants.EXTENSION_FILE_DWG)) {
			ivAvatar.setImageResource(R.drawable.dwg);			
		}else{
			ivAvatar.setImageResource(R.drawable.pdf);
		}
		return vi;
	}
}