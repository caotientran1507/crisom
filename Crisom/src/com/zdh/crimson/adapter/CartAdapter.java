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
import com.zdh.crimson.model.CartObject;
import com.zdh.crimson.utility.CommonUtil;

@SuppressLint("InflateParams")
public class CartAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<CartObject> listCart = new ArrayList<CartObject>();
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader; 

	public CartAdapter(Activity a, ArrayList<CartObject> listCart) {
		activity = a;
		this.listCart = listCart;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
		imageLoader=new ImageLoader(activity);
	}

	public int getCount() {
		return listCart.size();
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

		//-------init row view
		TextView tvCost = (TextView) vi.findViewById(R.id.row_recent_tvCost); 
		TextView tvQuantity = (TextView) vi.findViewById(R.id.row_recent_tvQuantity); 
		TextView tvTitle = (TextView) vi.findViewById(R.id.row_recent_tvTitle); 	
		TextView tvDes = (TextView) vi.findViewById(R.id.row_recent_tvDes); 	
		ImageView ivAvatar = (ImageView) vi.findViewById(R.id.row_recent_ivAvatar); 
		
		//--------set data----------
		tvCost.setText(CommonUtil.formatMoney(listCart.get(position).getPrice()));
		tvQuantity.setText(String.valueOf(listCart.get(position).getQuantity()));
		tvDes.setText(listCart.get(position).getDesc());
		if (listCart.get(position).getColor()!= null || !listCart.get(position).getColor().equals("")) {
			tvTitle.setText(listCart.get(position).getOptionName()+" - "+listCart.get(position).getColor());
		}else{
			tvTitle.setText(listCart.get(position).getOptionName());
		}
		
		imageLoader.DisplayImage(listCart.get(position).getImage(), ivAvatar);
		
		return vi;
	}
}