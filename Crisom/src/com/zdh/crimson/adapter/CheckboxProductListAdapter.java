package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdh.crimson.ProductListActivity;
import com.zdh.crimson.R;
import com.zdh.crimson.lazylist.ImageLoader;
import com.zdh.crimson.model.Category;
import com.zdh.crimson.utility.CommonUtil;

public class CheckboxProductListAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<Category> listCheck = new ArrayList<Category>();	
	public ImageLoader imageLoader; 
	private CheckBox cbxAll;
	private ArrayList<Boolean> listCheckbox = new ArrayList<Boolean>();

	Holder holder = null;
	public CheckboxProductListAdapter(Context context,ArrayList<Category> listCheck,ArrayList<Boolean> listCheckbox, CheckBox cbxAll) {
		this.listCheck = listCheck;		
		this.context = context;
		this.listCheckbox = listCheckbox;
		this.cbxAll = cbxAll;
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
		
		View view = convertView;
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
				Log.d("holder.cbx", "holder.cbx"+position);
				if (listCheckbox.get(position)) {
					listCheckbox.set(position, false);
					holder.cbx.setChecked(false);
					ProductListActivity.flagCheckAll = false;
					cbxAll.setChecked(false);
					
				}else{
					listCheckbox.set(position, true);
					holder.cbx.setChecked(true);
					if (CommonUtil.checkAllCheckBox(listCheckbox) == listCheckbox.size()) {
						ProductListActivity.flagCheckAll = true;
						cbxAll.setChecked(true);
					}
				}
				notifyDataSetChanged();
			}
		});
		
		holder.tv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d("holder.tv", "holder.tv"+position);
				if (listCheckbox.get(position)) {
					Log.d("holder.tvTRUE", "holder.tv"+position);
					listCheckbox.set(position, false);
					holder.cbx.setChecked(false);
					ProductListActivity.flagCheckAll = false;
					cbxAll.setChecked(false);
				}else{
					Log.d("holder.tvFALSE", "holder.tv"+position);
					listCheckbox.set(position, true);
					holder.cbx.setChecked(true);
					if (CommonUtil.checkAllCheckBox(listCheckbox) == listCheckbox.size()) {
						ProductListActivity.flagCheckAll = true;
						cbxAll.setChecked(true);
					}
				}
				notifyDataSetChanged();
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

	public class Holder {
		TextView tv;	
		CheckBox cbx;
		LinearLayout ln;
	}
	
	
}