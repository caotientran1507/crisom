package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zdh.crimson.ProductDetailActivity;
import com.zdh.crimson.R;
import com.zdh.crimson.lazylist.ImageLoader;
import com.zdh.crimson.model.Product;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class ParcelServiceAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<Product> listProduct = new ArrayList<Product>();	
	public ImageLoader imageLoader; 
	public ParcelServiceAdapter(Context context,ArrayList<Product> listProduct) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view = convertView;
		final CategoryHolder holder;
		if (convertView == null) {
			view = inflater.inflate(R.layout.row_product, null);
			holder = new CategoryHolder();
			holder.ivAvatar =  (ImageView) view.findViewById(R.id.row_product_ivAvatar);
			holder.tvTitle = (TextView) view.findViewById(R.id.row_product_tvTitle);
			holder.tvDes1 = (TextView) view.findViewById(R.id.row_product_tvDes1);
			holder.tvDes2 =  (TextView) view.findViewById(R.id.row_product_tvDes2);
			holder.lnAddtoCart = (LinearLayout) view.findViewById(R.id.row_product_lnAddtocart);		
			holder.tvDivider =  (TextView) view.findViewById(R.id.row_product_tvDivider);
			view.setTag(holder);
		} else {
			holder = (CategoryHolder) view.getTag();			
		}
		
		holder.ivAvatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,ProductDetailActivity.class);
				intent.putExtra(Constants.KEY_PRODUCTID, FileUtil.listSearch.get(position).getId());
				context.startActivity(intent);
			}
		});
		
		holder.btnAddtoCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!holder.edtQuantity.getText().toString().trim().equals("")) {
					try {
						int number = Integer.parseInt(holder.edtQuantity.getText().toString().trim());
						int quantityCurrent = Integer.parseInt(holder.edtQuantity.getText().toString());
						holder.edtQuantity.setText(String.valueOf(number+quantityCurrent));
					} catch (Exception e) {
						Toast.makeText(context, "Please input number!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		if (SharedPreferencesUtil.getFlagLogin(context)) {
		    holder.tvDivider.setVisibility(View.VISIBLE);
		    holder.lnAddtoCart.setVisibility(View.VISIBLE);
		} else {
		    holder.tvDivider.setVisibility(View.INVISIBLE);
		    holder.lnAddtoCart.setVisibility(View.INVISIBLE);
		}
		
		// ------load data--------

		holder.tvTitle.setText(listProduct.get(position).getName());
		holder.tvDes1.setText(listProduct.get(position).getShortDes());
		holder.tvDes2.setText(listProduct.get(position).getDes());
		imageLoader.DisplayImage(listProduct.get(position).getImage(), holder.ivAvatar);
		return view;
	}

	private class CategoryHolder {
		TextView tvTitle;	
		TextView tvDes1;
		TextView tvDes2;
		EditText edtQuantity;
		LinearLayout btnAddtoCart;
		TextView tvQuantity;
		TextView tvDivider;
		LinearLayout lnAddtoCart;
		ImageView ivAvatar;
	}
}