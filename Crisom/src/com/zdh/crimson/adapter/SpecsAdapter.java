package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zdh.crimson.R;
import com.zdh.crimson.model.SpecsObject;

public class SpecsAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<SpecsObject> listSpecs = new ArrayList<SpecsObject>();
	private static LayoutInflater inflater = null;

	public SpecsAdapter(Activity a, ArrayList<SpecsObject> listSpecs) {
		activity = a;
		this.listSpecs = listSpecs;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
	}

	public int getCount() {
		return listSpecs.size();
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
			vi = inflater.inflate(R.layout.row_specs, null);

		TextView title = (TextView) vi.findViewById(R.id.row_specs_tvTitle); 	
		TextView content = (TextView) vi.findViewById(R.id.row_specs_tvContent); 			
		content.setText(listSpecs.get(position).getContent());
		title.setText(listSpecs.get(position).getTitle());
		
		return vi;
	}
}