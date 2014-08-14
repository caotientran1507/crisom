package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zdh.crimson.R;
import com.zdh.crimson.lazylist.ImageLoader;
import com.zdh.crimson.model.CartObject;
import com.zdh.crimson.utility.CommonUtil;
import com.zdh.crimson.utility.FileUtil;

public class CheckoutAdapter extends BaseAdapter {

	//	CategoryHolder holder = null;
	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<CartObject> listCart = new ArrayList<CartObject>();	
	public ImageLoader imageLoader; 


	public CheckoutAdapter(Context context,ArrayList<CartObject> listCart) {
		this.listCart = listCart;		
		this.context = context;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(this.context);
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
			
			holder.mWatcher = new MutableWatcher(listCart,holder.tvSubtotal);
	        holder.edtQuantity.addTextChangedListener(holder.mWatcher);

			view.setTag(holder);
		} else {
			holder = (CategoryHolder) view.getTag();			
		}

	    holder.mWatcher.setPosition(position);
	    holder.mWatcher.setActive(true);

		// ------load data--------

		holder.tvPartNumber.setText(listCart.get(position).getOptionName());

		String des = "<strong><font color=\"#2f3a76\">Desciption: </font></strong>"+ listCart.get(position).getDesc();
		holder.tvDes.setText(Html.fromHtml(des));

		holder.tvColor.setText(listCart.get(position).getColor());
		holder.edtQuantity.setText(String.valueOf(listCart.get(position).getQuantity()));		
		holder.tvPrice.setText(CommonUtil.formatMoney(listCart.get(position).getPrice()));
		holder.tvSubtotal.setText(CommonUtil.formatMoney(listCart.get(position).getPrice()*listCart.get(position).getQuantity()));
		imageLoader.DisplayImage(listCart.get(position).getImage(), holder.ivAvatar);
		
		if (!FileUtil.listCartChange.containsKey(listCart.get(position).getIdItem())) {
			FileUtil.listCartChange.put(listCart.get(position).getIdItem(), String.valueOf(listCart.get(position).getQuantity()));
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

    private ArrayList<CartObject> mlistRecent;
    private int mPosition;
    private boolean mActive;
    private TextView tvSubTotal;

    public MutableWatcher(ArrayList<CartObject> listRecent,TextView tvSubTotal ) {
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