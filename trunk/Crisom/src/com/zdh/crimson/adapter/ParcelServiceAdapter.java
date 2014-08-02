package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zdh.crimson.R;
import com.zdh.crimson.lazylist.ImageLoader;
import com.zdh.crimson.model.CarrierObject;

public class ParcelServiceAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<CarrierObject> listCarrier = new ArrayList<CarrierObject>();	
	public ImageLoader imageLoader; 
	public ParcelServiceAdapter(Context context,ArrayList<CarrierObject> listCarrier) {
		this.listCarrier = listCarrier;		
		this.context = context;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(this.context);
	}

	public int getCount() {
		return listCarrier.size();
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
		CategoryHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.row_parcel_service, null);
			holder = new CategoryHolder();
			holder.rdb =  (RadioButton) view.findViewById(R.id.row_parcel_service_rbn);
			holder.tv = (TextView) view.findViewById(R.id.row_parcel_service_tv);
			
			view.setTag(holder);
		} else {
			holder = (CategoryHolder) view.getTag();			
		}
		
//		holder.rdb.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//			}
//		});

		holder.tv.setText(listCarrier.get(position).getTitle()+listCarrier.get(position).getPrice());
		return view;
	}

	private class CategoryHolder {
		TextView tv;	
		RadioButton rdb;
	}
}