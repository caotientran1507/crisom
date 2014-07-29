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
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class ProductListAdapter extends BaseAdapter {

	int currentPosition;
	int positionD;
	CategoryHolder holder = null;
	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<Product> listProduct = new ArrayList<Product>();	
	public ImageLoader imageLoader; 


	public ProductListAdapter(Context context,ArrayList<Product> listProduct) {
		this.listProduct = listProduct;		
		this.context = context;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader=new ImageLoader(this.context);
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

		TextView tvTitle;	
		TextView tvDes1;
		TextView tvDes2;
		TextView tvDivider;
		LinearLayout lnAddtoCart;
		final EditText edtQuantity;
		LinearLayout btnAddtoCart = null;
		final TextView tvQuantity;
		ImageView ivAvatar;
		View view = convertView;
		currentPosition = position;
		if (convertView == null) {
			view = inflater.inflate(R.layout.row_recent_a, null);
			holder = new CategoryHolder();
			
			tvTitle = (TextView) view.findViewById(R.id.row_product_tvTitle);
			tvDes1 = (TextView) view.findViewById(R.id.row_product_tvDes1);
			tvDes2 = (TextView) view.findViewById(R.id.row_product_tvDes2);
			tvQuantity = (TextView) view.findViewById(R.id.row_product_tvQuantityIncart);
			edtQuantity = (EditText) view.findViewById(R.id.row_product_edtQuantityAddtocart);
			btnAddtoCart = (LinearLayout) view.findViewById(R.id.row_product_btnAddtocart);
			ivAvatar = (ImageView) view.findViewById(R.id.row_product_ivAvatar);
			tvDivider = (TextView) view.findViewById(R.id.row_product_tvDivider);
			lnAddtoCart = (LinearLayout) view.findViewById(R.id.row_product_lnAddtocart);
			
			holder.ivAvatar = ivAvatar;
			holder.tvTitle = tvTitle;
			holder.tvDes1 = tvDes1;
			holder.tvDes2 = tvDes2;
			holder.edtQuantity = edtQuantity;
			holder.lnAddtoCart = lnAddtoCart;
			holder.tvQuantity = tvQuantity;			
			holder.tvDivider = tvDivider;
			holder.btnAddtoCart = btnAddtoCart;

			view.setTag(holder);
		} else {
			holder = (CategoryHolder) view.getTag();			
			ivAvatar = holder.ivAvatar;
			tvTitle = holder.tvTitle;
			tvDes1 = holder.tvDes1;
			tvDes2 = holder.tvDes2;
			edtQuantity = holder.edtQuantity;
			btnAddtoCart = holder.btnAddtoCart;
			tvQuantity = holder.tvQuantity;
			tvDivider = holder.tvDivider;
			lnAddtoCart = holder.lnAddtoCart;
			
		}
		
		ivAvatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,ProductDetailActivity.class);
				intent.putExtra(Constants.KEY_PRODUCTID, listProduct.get(currentPosition).getId());
				context.startActivity(intent);
			}
		});
		
		btnAddtoCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!edtQuantity.getText().toString().trim().equals("")) {
					try {
						int number = Integer.parseInt(edtQuantity.getText().toString().trim());
						int quantityCurrent = Integer.parseInt(tvQuantity.getText().toString());
						tvQuantity.setText(String.valueOf(number+quantityCurrent));
					} catch (Exception e) {
						Toast.makeText(context, "Please input number!", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		if (SharedPreferencesUtil.getFlagLogin(context)) {
			tvDivider.setVisibility(View.VISIBLE);
			lnAddtoCart.setVisibility(View.VISIBLE);
		} else {
			tvDivider.setVisibility(View.INVISIBLE);
			lnAddtoCart.setVisibility(View.INVISIBLE);
		}
		
		// ------load data--------

		tvTitle.setText(listProduct.get(currentPosition).getName());
		tvDes1.setText(listProduct.get(currentPosition).getShortDes());
		tvDes2.setText(listProduct.get(currentPosition).getDes());
		imageLoader.DisplayImage(listProduct.get(currentPosition).getImage(), ivAvatar);
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