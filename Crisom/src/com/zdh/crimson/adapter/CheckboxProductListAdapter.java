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
import com.zdh.crimson.model.Product;

public class CheckboxProductListAdapter extends BaseAdapter {

	int currentPosition;	
	CategoryHolder holder = null;
	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<Product> listProduct = new ArrayList<Product>();	
	public ImageLoader imageLoader; 


	public CheckboxProductListAdapter(Context context,ArrayList<Product> listProduct) {
		this.listProduct = listProduct;		
		this.context = context;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(this.context);
	}

	public int getCount() {
		return listProduct.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent) {

		TextView tv;	
		CheckBox cbx;
		LinearLayout ln;
		View view = convertView;
		currentPosition = position;
		if (convertView == null) {
			view = inflater.inflate(R.layout.row_checkbox, null);
			holder = new CategoryHolder();
			
			tv = (TextView) view.findViewById(R.id.row_checkbox_tv);
			cbx = (CheckBox) view.findViewById(R.id.row_checkbox_cbx);
			ln = (LinearLayout) view.findViewById(R.id.row_checkbox_ln);
			
			
			holder.tv = tv;
			holder.cbx = cbx;
			holder.ln = ln;

			view.setTag(holder);
		} else {
			holder = (CategoryHolder) view.getTag();			
			tv = holder.tv;
			cbx = holder.cbx;	
			ln = holder.ln;
		}
		
		ln.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {		
				
			}
		});
		
		
		return view;
	}

	private class CategoryHolder {
		TextView tv;	
		CheckBox cbx;
		LinearLayout ln;
	}
}