package com.zdh.crimson.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zdh.crimson.ProductDetailActivity;
import com.zdh.crimson.R;
import com.zdh.crimson.lazylist.ImageLoader;
import com.zdh.crimson.model.DocumentObject;
import com.zdh.crimson.model.OptionObject;
import com.zdh.crimson.model.Product;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.ExpandableHeightListView;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class SearchAdapter extends BaseAdapter {

	private Activity context;
	private LayoutInflater inflater = null;
	private ArrayList<Product> listProduct = new ArrayList<Product>();	
	public ImageLoader imageLoader;

	Dialog dialogChildrenProduct;
	ExpandableHeightListView lvDialog;
	TextView tvDialog;


	public SearchAdapter(Activity context,ArrayList<Product> listProduct) {
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
			holder.lnValue = (LinearLayout) view.findViewById(R.id.row_product_tvValue);
			holder.productlist_message_cart_no_login = (TextView)view.findViewById(R.id.productlist_message_cart_no_login);
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
				context.overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			}
		});

		holder.lnValue.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,ProductDetailActivity.class);
				intent.putExtra(Constants.KEY_PRODUCTID, listProduct.get(position).getId());
				context.startActivity(intent);
				context.overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			}
		});

		holder.lnAddtoCart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialogChildrenProduct(listProduct.get(position));
			}
		});

		if (SharedPreferencesUtil.getFlagLogin(context)) {
		    holder.lnAddtoCart.setVisibility(View.VISIBLE);
		    holder.productlist_message_cart_no_login.setVisibility(View.GONE);
		} else {
			holder.lnAddtoCart.setVisibility(View.GONE);
		    holder.productlist_message_cart_no_login.setVisibility(View.VISIBLE);
		}

		// -----------load data------------

		holder.tvTitle.setText(listProduct.get(position).getName());
		if (listProduct.get(position).getShortDes().equals("null")) {
			holder.tvDes1.setVisibility(View.GONE);
		}else{
			holder.tvDes1.setText(listProduct.get(position).getShortDes());
		}
		if (listProduct.get(position).getDes().equals("null")) {
			holder.tvDes2.setVisibility(View.GONE);
		}else{
			holder.tvDes2.setText(listProduct.get(position).getDes());
		}
		imageLoader.DisplayImage(listProduct.get(position).getImage(), holder.ivAvatar);
		return view;
	}

	private class CategoryHolder {
		TextView tvTitle;	
		TextView tvDes1;
		TextView tvDes2;
		LinearLayout lnValue;
		LinearLayout lnAddtoCart;
		ImageView ivAvatar;
		TextView productlist_message_cart_no_login;
	}

	private void showDialogChildrenProduct(Product product)
	{		
		if (SharedPreferencesUtil.getFlagLogin(context)) {
			new GetProductByIdAsyncTask(context,SharedPreferencesUtil.getIdCustomerLogin(context), product.getId()).execute();
		} else {
			new GetProductByIdAsyncTask(context,0, product.getId()).execute();
		}
		
		dialogChildrenProduct = new Dialog(context, R.style.FullHeightDialog);
		dialogChildrenProduct.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

		dialogChildrenProduct.setContentView(R.layout.dialog_child);
		lvDialog = (ExpandableHeightListView) dialogChildrenProduct.findViewById(R.id.dialog_child_lv);
		tvDialog = (TextView) dialogChildrenProduct.findViewById(R.id.include_header_tvTitle);

		dialogChildrenProduct.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialogChildrenProduct.dismiss(); 

			}
		});


	}



	public class GetProductByIdAsyncTask extends AsyncTask<String, String, String> {

		//------------------------------------------------------------
		private ProgressDialog pDialog;
		private Product product = new Product();
		private int cid;
		private int pid;
		private String json;
		private Context mContext;

		public GetProductByIdAsyncTask(Context context,int cid, int pid){
			this.cid = cid;
			this.pid = pid;
			this.mContext = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
			pDialog.setContentView(R.layout.dialog_process);

		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("pid", String.valueOf(pid)));
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(cid)));

				json = JsonParser.makeHttpRequest(
						Constants.URL_GETPRODUCTBYID, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) { 
					JSONObject jsonObject = new JSONObject(json);	                	
					product.setId(jsonObject.getInt("entity_id"));
					product.setName(jsonObject.getString("name"));
					product.setDes(jsonObject.getString("description"));    
					product.setShortDes(jsonObject.getString("short_description"));    
					product.setUrl(jsonObject.getString("url"));    
					product.setFaq(jsonObject.getString("faq"));


					JSONObject jsonTemp= (JSONObject) new JSONTokener(json).nextValue();
					//-------get image------
					JSONArray jsonImage = jsonTemp.getJSONArray("image");
					if (jsonImage != null && jsonImage.length() != 0) {
						product.setImage(jsonImage.getString(0)); 
					}

					//------get all document for product-----
					JSONArray jsonArrayDocument = jsonTemp.getJSONArray("document");
					if (jsonArrayDocument != null && jsonArrayDocument.length() != 0) {
						for (int i = 0; i < jsonArrayDocument.length(); i++) {
							DocumentObject documentObject = new DocumentObject();
							documentObject.setDocType(jsonArrayDocument.getJSONObject(i).getString("doc_type"));
							documentObject.setFile(jsonArrayDocument.getJSONObject(i).getString("file"));
							product.getListDocument().add(documentObject);
						}
					} 


					//------get all options for product-----
					JSONArray jsonArrayOptions = jsonTemp.getJSONArray("options");
					if (jsonArrayOptions != null && jsonArrayOptions.length() != 0) {
						for (int i = 0; i < jsonArrayOptions.length(); i++) {
							OptionObject optionObject = new OptionObject();
							optionObject.setSku(jsonArrayOptions.getJSONObject(i).getString("sku"));
							optionObject.setColor(jsonArrayOptions.getJSONObject(i).getString("color"));
							optionObject.setWeight(jsonArrayOptions.getJSONObject(i).getDouble("weight"));
							optionObject.setPrice(jsonArrayOptions.getJSONObject(i).getString("price"));
							optionObject.setMsrp(jsonArrayOptions.getJSONObject(i).getString("msrp"));
							optionObject.setOid(jsonArrayOptions.getJSONObject(i).getInt("oid"));
							optionObject.setValue(jsonArrayOptions.getJSONObject(i).getInt("value"));
							optionObject.setOtherFieldTitle(jsonArrayOptions.getJSONObject(i).getString("other_field_title"));
							optionObject.setOtherFieldValue(jsonArrayOptions.getJSONObject(i).getString("other_field_value"));
							optionObject.setInCart(jsonArrayOptions.getJSONObject(i).getInt("incart"));
							product.getListOption().add(optionObject);
						}
					}

				}	
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			if(product.getListOption()!=null && product.getListOption().size()>0){
				ChildSearchActivityAdapter childAdapter = new ChildSearchActivityAdapter(mContext, product.getListOption(),product.getId());
				childAdapter.setUpdate_layout_SearchActivity(true);
				lvDialog.setAdapter(childAdapter);
				dialogChildrenProduct.show();

			}
			pDialog.dismiss();	      
		}
	}



}