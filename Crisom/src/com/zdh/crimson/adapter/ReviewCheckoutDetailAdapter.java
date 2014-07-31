package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zdh.crimson.R;
import com.zdh.crimson.R.color;
import com.zdh.crimson.model.RecentObject;
import com.zdh.crimson.utility.CommonUtil;

@SuppressLint("InflateParams")
public class ReviewCheckoutDetailAdapter extends BaseAdapter {

	private Activity activity;
	private ArrayList<RecentObject> listRecent = new ArrayList<RecentObject>();
	private static LayoutInflater inflater = null;

	public ReviewCheckoutDetailAdapter(Activity a, ArrayList<RecentObject> listRecent) {
		activity = a;
		this.listRecent = listRecent;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
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
			vi = inflater.inflate(R.layout.row_checkoutdetail, null);

		TextView tvPartNumber = (TextView) vi.findViewById(R.id.row_checkoutdetail_tvPartNumber); 	
		TextView tvColor = (TextView) vi.findViewById(R.id.row_checkoutdetail_tvColor); 	
		TextView tvDes = (TextView) vi.findViewById(R.id.row_checkoutdetail_tvDescription); 	
		TextView tvPrice = (TextView) vi.findViewById(R.id.row_checkoutdetail_tvPrice); 	
		TextView tvQty = (TextView) vi.findViewById(R.id.row_checkoutdetail_tvQuantity); 
		TextView tvSubtotal = (TextView) vi.findViewById(R.id.row_checkoutdetail_tvSubtotal); 
		
		tvPartNumber.setText(listRecent.get(position).getName());
		
		String des = "Desciption: "+ listRecent.get(position).getDesc();
		Spannable desSpannable=new SpannableStringBuilder(des);
		desSpannable.setSpan(new ForegroundColorSpan(color.crisom_blue), 0, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tvDes.setText(desSpannable);		
		tvColor.setText(listRecent.get(position).getColor());
		tvQty.setText(String.valueOf(listRecent.get(position).getQuantity()));		
		tvPrice.setText(CommonUtil.formatMoney(listRecent.get(position).getPrice()));
		tvSubtotal.setText(CommonUtil.formatMoney(listRecent.get(position).getPrice()*listRecent.get(position).getQuantity()));
		
		return vi;
	}
}