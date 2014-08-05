package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdh.crimson.R;
import com.zdh.crimson.R.color;
import com.zdh.crimson.lazylist.ImageLoader;
import com.zdh.crimson.model.RecentObject;
import com.zdh.crimson.utility.CommonUtil;
import com.zdh.crimson.utility.FileUtil;

public class CheckoutAdapter extends BaseAdapter {

	//	CategoryHolder holder = null;
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
		final CategoryHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.row_checkout, null);
			holder = new CategoryHolder();

			holder.tvPartNumber = (TextView) view.findViewById(R.id.row_checkout_tvPartNumber);
			holder.tvColor = (TextView) view.findViewById(R.id.row_checkout_tvColor);			
			holder.tvDes = (TextView) view.findViewById(R.id.row_checkout_tvDesc);
			holder.edtQuantity = (EditText) view.findViewById(R.id.row_checkout_edtQuantity);
			holder.tvPrice = (TextView) view.findViewById(R.id.row_checkout_tvPrice);
			holder.ivAvatar = (ImageView) view.findViewById(R.id.row_checkout_ivAvatar);
			holder.tvSubtotal = (TextView) view.findViewById(R.id.row_checkout_tvSubtotal);		
			
			holder.mWatcher = new MutableWatcher(listRecent,holder.tvSubtotal);
	        holder.edtQuantity.addTextChangedListener(holder.mWatcher);

			view.setTag(holder);
		} else {
			holder = (CategoryHolder) view.getTag();			
		}

	    holder.mWatcher.setPosition(position);
	    holder.mWatcher.setActive(true);

		// ------load data--------

		holder.tvPartNumber.setText(listRecent.get(position).getName());

		String des = "<strong><font color=\"#2f3a76\">Desciption: </font></strong>"+ listRecent.get(position).getDesc();
		holder.tvDes.setText(Html.fromHtml(des));

		holder.tvColor.setText(listRecent.get(position).getColor());
		holder.edtQuantity.setText(String.valueOf(listRecent.get(position).getQuantity()));		
		holder.tvPrice.setText(CommonUtil.formatMoney(listRecent.get(position).getPrice()));
		holder.tvSubtotal.setText(CommonUtil.formatMoney(listRecent.get(position).getPrice()*listRecent.get(position).getQuantity()));
		imageLoader.DisplayImage(listRecent.get(position).getImage(), holder.ivAvatar);
		
		if (!FileUtil.listCartChange.containsKey(listRecent.get(position).getIdItem())) {
			FileUtil.listCartChange.put(listRecent.get(position).getIdItem(), String.valueOf(listRecent.get(position).getQuantity()));
		}		
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
		 public MutableWatcher mWatcher;
	}
	
	
}

class MutableWatcher implements TextWatcher {

    private ArrayList<RecentObject> mlistRecent;
    private int mPosition;
    private boolean mActive;
    private TextView tvSubTotal;

    public MutableWatcher(ArrayList<RecentObject> listRecent,TextView tvSubTotal ) {
		// TODO Auto-generated constructor stub
    	mlistRecent = listRecent;
    	this.tvSubTotal = tvSubTotal;
	}
    void setPosition(int position) {
        mPosition = position;
    }

    void setActive(boolean active) {
        mActive = active;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
    	
        if (mActive && !s.toString().trim().equals("")) {
        	if (FileUtil.listCartChange.containsKey(mlistRecent.get(mPosition).getIdItem())) {
        		FileUtil.listCartChange.replace(mlistRecent.get(mPosition).getIdItem(), s.toString().trim());
			} else {
				FileUtil.listCartChange.put(mlistRecent.get(mPosition).getIdItem(), s.toString().trim());
			}
        	
        	tvSubTotal.setText(CommonUtil.formatMoney(mlistRecent.get(mPosition).getPrice()*Integer.parseInt(s.toString())));
        	mlistRecent.get(mPosition).setQuantity(Integer.parseInt(s.toString()));
        }
    	
        
    }
}