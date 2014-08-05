package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdh.crimson.R;
import com.zdh.crimson.lazylist.ImageLoader;
import com.zdh.crimson.model.Category;

public class CheckboxProductListAdapter extends BaseAdapter {

	int currentPosition;	

	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<Category> listCheck = new ArrayList<Category>();	
	public ImageLoader imageLoader; 
	
	private ArrayList<Boolean> listCheckbox = new ArrayList<Boolean>();


	public CheckboxProductListAdapter(Context context,ArrayList<Category> listCheck,ArrayList<Boolean> listCheckbox) {
		this.listCheck = listCheck;		
		this.context = context;
		this.listCheckbox = listCheckbox;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(this.context);
	}

	public int getCount() {
		return listCheck.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		View view = convertView;
		currentPosition = position;
		if (convertView == null) {
			view = inflater.inflate(R.layout.row_checkbox, null);
			holder = new Holder();

			holder.tv = (TextView) view.findViewById(R.id.row_checkbox_tv);
			holder.cbx = (CheckBox) view.findViewById(R.id.row_checkbox_cbx);
			holder.ln = (LinearLayout) view.findViewById(R.id.row_checkbox_ln);

			view.setTag(holder);
		} else {
			holder = (Holder) view.getTag();			

		}

		holder.ln.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {		

			}
		});
		holder.tv.setText(listCheck.get(position).getName());

		holder.cbx.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {		
				if (listCheckbox.get(position)) {
					listCheckbox.set(position, false);
				}else{
					listCheckbox.set(position, true);
				}
			}
		});
		
		if (listCheckbox.get(position)) {
			holder.cbx.setChecked(true);
		}else{
			holder.cbx.setChecked(false);
		}
		holder.tv.setText(listCheck.get(position).getName());


		return view;
	}

	private class Holder {
		TextView tv;	
		CheckBox cbx;
		LinearLayout ln;
	}
}