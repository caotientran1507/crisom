package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zdh.crimson.R;
import com.zdh.crimson.model.Category;

public class HomeAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<Category> listCategory = new ArrayList<Category>();
	private static LayoutInflater inflater = null;

	public HomeAdapter(Activity a, ArrayList<Category> listCategory) {
		activity = a;
		this.listCategory = listCategory;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}

	public int getCount() {
		return listCategory.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.row_category, null);

		TextView nameCategory = (TextView) vi.findViewById(R.id.row_category_tv); 	
		nameCategory.setText(listCategory.get(position).getName());
		
		return vi;
	}
}