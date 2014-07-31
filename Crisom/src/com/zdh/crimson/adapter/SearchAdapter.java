package com.zdh.crimson.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.zdh.crimson.ProductDetailActivity;
import com.zdh.crimson.R;
import com.zdh.crimson.SearchActivity;
import com.zdh.crimson.lazylist.ImageLoader;
import com.zdh.crimson.model.Product;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class SearchAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater inflater = null;
	private ArrayList<Product> listProduct = new ArrayList<Product>();	
	public ImageLoader imageLoader;
	
	Dialog dialogChildrenProduct;
	ListView lvDialog;
	TextView tvDialog;


	public SearchAdapter(Context context,ArrayList<Product> listProduct) {
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
			holder.lnValue = (LinearLayout) view.findViewById(R.id.row_product_tvValue);
			view.setTag(holder);
		} else {
			holder = (CategoryHolder) view.getTag();			
		}
		
		holder.ivAvatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,ProductDetailActivity.class);
				intent.putExtra(Constants.KEY_PRODUCTID, listProduct.get(position).getId());
				context.startActivity(intent);
			}
		});
		
		holder.lnValue.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,ProductDetailActivity.class);
				intent.putExtra(Constants.KEY_PRODUCTID, listProduct.get(position).getId());
				context.startActivity(intent);
			}
		});
		
		holder.lnAddtoCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialogChildrenProduct(listProduct.get(position));
			}
		});
		
		if (SharedPreferencesUtil.getFlagLogin(context)) {
		    holder.tvDivider.setVisibility(View.VISIBLE);
		    holder.lnAddtoCart.setVisibility(View.VISIBLE);
		} else {
		    holder.tvDivider.setVisibility(View.INVISIBLE);
		    holder.lnAddtoCart.setVisibility(View.INVISIBLE);
		}
		
		// -----------load data------------

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
		TextView tvDivider;
		LinearLayout lnValue;
		LinearLayout lnAddtoCart;
		ImageView ivAvatar;
	}
	
	private void showDialogChildrenProduct(Product product)
	{		
		dialogChildrenProduct = new Dialog(context, R.style.FullHeightDialog);
		dialogChildrenProduct.setContentView(R.layout.dialog_child);
		lvDialog = (ListView) dialogChildrenProduct.findViewById(R.id.dialog_child_lv);
		tvDialog = (TextView) dialogChildrenProduct.findViewById(R.id.include_header_tvTitle);
		
		ChildAdapter childAdapter = new ChildAdapter(context, product.getListOption(),product.getId());
		lvDialog.setAdapter(childAdapter);
		
		dialogChildrenProduct.setOnCancelListener(new OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				dialogChildrenProduct.dismiss();
				
			}
		});
		
		dialogChildrenProduct.show();
	
	}
}