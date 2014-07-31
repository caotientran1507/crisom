package com.zdh.crimson.adapter;

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

import com.zdh.crimson.R;
import com.zdh.crimson.lazylist.ImageLoader;
import com.zdh.crimson.model.RecentObject;
import com.zdh.crimson.utility.CommonUtil;

@SuppressLint("InflateParams")
public class RecentAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<RecentObject> listRecent = new ArrayList<RecentObject>();
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader; 

	public RecentAdapter(Activity a, ArrayList<RecentObject> listRecent) {
		activity = a;
		this.listRecent = listRecent;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		imageLoader=new ImageLoader(activity);
	}

	public int getCount() {
		return listRecent.size();
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
			vi = inflater.inflate(R.layout.row_recent, null);

		TextView tvCost = (TextView) vi.findViewById(R.id.row_recent_tvCost); 
		TextView tvQuantity = (TextView) vi.findViewById(R.id.row_recent_tvQuantity); 
		TextView tvTitle = (TextView) vi.findViewById(R.id.row_recent_tvTitle); 	
		ImageView ivAvatar = (ImageView) vi.findViewById(R.id.row_recent_ivAvatar); 
		tvCost.setText(CommonUtil.formatMoney(listRecent.get(position).getPrice()));
		tvQuantity.setText(String.valueOf(listRecent.get(position).getQuantity()));
		tvTitle.setText(listRecent.get(position).getName());
		imageLoader.DisplayImage(listRecent.get(position).getImage(), ivAvatar);
		
		return vi;
	}
}