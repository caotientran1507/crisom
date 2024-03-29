package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.zdh.crimson.R;
import com.zdh.crimson.lazylist.ImageLoader;
import com.zdh.crimson.model.CarrierObject;
import com.zdh.crimson.utility.FileUtil;

public class ParcelServiceAdapter extends BaseAdapter {
	SparseBooleanArray mCheckStates; 
	

	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<CarrierObject> listCarrier = new ArrayList<CarrierObject>();	
	public ImageLoader imageLoader; 
	public ParcelServiceAdapter(Context context,ArrayList<CarrierObject> listCarrier) {
		this.listCarrier = listCarrier;		
		this.context = context;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(this.context);
		mCheckStates = new SparseBooleanArray(listCarrier.size());

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
			holder.ln =  (LinearLayout) view.findViewById(R.id.row_parcel_service_ln);
			holder.tv = (TextView) view.findViewById(R.id.row_parcel_service_tv);

			view.setTag(holder);
		} else {
			holder = (CategoryHolder) view.getTag();			
		}

		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {				
				FileUtil.selectedIndexUPS = position;
				ParcelServiceAdapter.this.notifyDataSetChanged();	
//				FileUtil.codeRadioButtonShippingMethod = listCarrier.get(position).getCode();
			}
		});

		holder.rdb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FileUtil.selectedIndexUPS = position;
				ParcelServiceAdapter.this.notifyDataSetChanged();
//				FileUtil.codeRadioButtonShippingMethod = listCarrier.get(position).getCode();
			}
		});
		
		if (listCarrier.get(position).getTitle() == null || listCarrier.get(position).getTitle().equals("null")) {
			holder.ln.setVisibility(View.GONE);
		}else{
			holder.tv.setText(listCarrier.get(position).getTitle()+" "+listCarrier.get(position).getPrice());
		}


		if (position == FileUtil.selectedIndexUPS) {
			holder.rdb.setChecked( true );
		}
		else {
			holder.rdb.setChecked ( false );
		}

		return view;
	}

	private class CategoryHolder {
		TextView tv;	
		RadioButton rdb;
		LinearLayout ln;
	}

	public int getcurrentPositionSelect() {
		// TODO Auto-generated method stub
		return FileUtil.selectedIndexUPS;
	}
}