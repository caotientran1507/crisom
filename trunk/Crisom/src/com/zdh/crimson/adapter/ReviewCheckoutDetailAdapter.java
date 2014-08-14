package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zdh.crimson.R;
import com.zdh.crimson.model.CartObject;
import com.zdh.crimson.utility.CommonUtil;

@SuppressLint("InflateParams")
public class ReviewCheckoutDetailAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<CartObject> listCart = new ArrayList<CartObject>();
	private static LayoutInflater inflater = null;

	public ReviewCheckoutDetailAdapter(Activity a, ArrayList<CartObject> listCart) {
		activity = a;
		this.listCart = listCart;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
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
			vi = inflater.inflate(R.layout.row_checkoutdetail, null);

		TextView tvPartNumber = (TextView) vi.findViewById(R.id.row_checkoutdetail_tvPartNumber); 	
		TextView tvColor = (TextView) vi.findViewById(R.id.row_checkoutdetail_tvColor); 	
		TextView tvDes = (TextView) vi.findViewById(R.id.row_checkoutdetail_tvDescription); 	
		TextView tvPrice = (TextView) vi.findViewById(R.id.row_checkoutdetail_tvPrice); 	
		TextView tvQty = (TextView) vi.findViewById(R.id.row_checkoutdetail_tvQuantity); 
		TextView tvSubtotal = (TextView) vi.findViewById(R.id.row_checkoutdetail_tvSubtotal); 
		
		tvPartNumber.setText(listCart.get(position).getOptionName());

		String des = "<strong><font color=\"#2f3a76\">Desciption: </font></strong>"+ listCart.get(position).getDesc();
		tvDes.setText(Html.fromHtml(des));		
		tvColor.setText(listCart.get(position).getColor());
		tvQty.setText(String.valueOf(listCart.get(position).getQuantity()));		
		tvPrice.setText(CommonUtil.formatMoney(listCart.get(position).getPrice()));
		tvSubtotal.setText(CommonUtil.formatMoney(listCart.get(position).getPrice()*listCart.get(position).getQuantity()));
		
		return vi;
	}
}