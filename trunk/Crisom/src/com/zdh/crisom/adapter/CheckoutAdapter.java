package com.zdh.crisom.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdh.crisom.R;
import com.zdh.crisom.R.color;
import com.zdh.crisom.lazylist.ImageLoader;
import com.zdh.crisom.model.RecentObject;
import com.zdh.crisom.utility.CommonUtil;
import com.zdh.crisom.utility.FileUtil;

public class CheckoutAdapter extends BaseAdapter {

	int currentPosition;	
	CategoryHolder holder = null;
	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<RecentObject> listRecent = new ArrayList<RecentObject>();	
	public ImageLoader imageLoader; 


	public CheckoutAdapter(Context context,ArrayList<RecentObject> listRecent) {
		this.listRecent = listRecent;		
		this.context = context;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(this.context);
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

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {

		TextView tvPartNumber;	
		TextView tvColor;
		TextView tvDes;
		final EditText edtQuantity;
		TextView tvPrice;
		TextView tvSubtotal;
		ImageView ivAvatar;
		View view = convertView;
		currentPosition = position;
		if (convertView == null) {
			view = inflater.inflate(R.layout.row_checkout, null);
			holder = new CategoryHolder();
			
			tvPartNumber = (TextView) view.findViewById(R.id.row_checkout_tvPartNumber);
			tvColor = (TextView) view.findViewById(R.id.row_checkout_tvColor);			
			tvDes = (TextView) view.findViewById(R.id.row_checkout_tvDesc);
			edtQuantity = (EditText) view.findViewById(R.id.row_checkout_edtQuantity);
			tvPrice = (TextView) view.findViewById(R.id.row_checkout_tvPrice);
			ivAvatar = (ImageView) view.findViewById(R.id.row_checkout_ivAvatar);
			tvSubtotal = (TextView) view.findViewById(R.id.row_checkout_tvSubtotal);		
			
			holder.ivAvatar = ivAvatar;
			holder.tvPartNumber = tvPartNumber;
			holder.tvColor = tvColor;
			holder.tvDes = tvDes;
			holder.edtQuantity = edtQuantity;
			holder.tvSubtotal = tvSubtotal;
			holder.tvPrice = tvPrice;

			view.setTag(holder);
		} else {
			holder = (CategoryHolder) view.getTag();			
			ivAvatar = holder.ivAvatar;
			tvPartNumber = holder.tvPartNumber;
			tvColor = holder.tvColor;
			tvDes = holder.tvDes;
			edtQuantity = holder.edtQuantity;
			tvPrice = holder.tvPrice;
			tvSubtotal = holder.tvSubtotal;
			
		}
		
		edtQuantity.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
	        	if (FileUtil.listCartChange.containsKey(listRecent.get(currentPosition).getIdItem())) {
	        		FileUtil.listCartChange.replace(listRecent.get(currentPosition).getIdItem(), edtQuantity.getText().toString());
				} else {
					FileUtil.listCartChange.put(listRecent.get(currentPosition).getIdItem(), edtQuantity.getText().toString());
				}
	            
	        }
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){}
	    });
		
		
		
		// ------load data--------

		tvPartNumber.setText(listRecent.get(currentPosition).getName());
		
		String des = "Desciption: "+ listRecent.get(currentPosition).getDesc();
		Spannable desSpannable=new SpannableStringBuilder(des);
		desSpannable.setSpan(new ForegroundColorSpan(color.crisom_blue), 0, 10, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
		tvDes.setText(desSpannable);
		
		tvColor.setText(listRecent.get(currentPosition).getColor());
		edtQuantity.setText(String.valueOf(listRecent.get(currentPosition).getQuantity()));		
		tvPrice.setText(CommonUtil.formatMoney(listRecent.get(currentPosition).getPrice()));
		tvSubtotal.setText(CommonUtil.formatMoney(listRecent.get(currentPosition).getPrice()*listRecent.get(currentPosition).getQuantity()));
		imageLoader.DisplayImage(listRecent.get(currentPosition).getImage(), ivAvatar);
		return view;
	}

	private class CategoryHolder {
		TextView tvPartNumber;	
		TextView tvColor;
		TextView tvDes;
		EditText edtQuantity;
		TextView tvPrice;
		TextView tvSubtotal;
		ImageView ivAvatar;
	}
}