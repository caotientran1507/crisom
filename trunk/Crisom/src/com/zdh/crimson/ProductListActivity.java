package com.zdh.crimson;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zdh.crimson.adapter.CheckboxProductListAdapter;
import com.zdh.crimson.adapter.ProductListAdapter;
import com.zdh.crimson.model.Category;
import com.zdh.crimson.model.Product;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;

public class ProductListActivity extends BaseActivity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCart,lnContact,lnNarrowTitle,lnNarrowContent;
	private ListView lvProduct,lvCheckbox;
	private ImageView ivCategory,ivNarrowShow;	
	private TextView tvTitle;
	private ProgressDialog pDialog;
	ProductListAdapter adapter;
	static int currentCategory = 0;

	CheckboxProductListAdapter checkboxProductListAdapter;

	private Spinner spnTvSize, spnType1, spnType2;
	private CheckBox cbxAll;
	private Button btnSearch, btnClearFilter;

	ArrayList<Category> listCategoryTvSize = new ArrayList<Category>();
	ArrayList<String> listTvSizes = new ArrayList<String>();
	ArrayAdapter<String> tvSizeAdapter;

	ArrayList<Category> listCategoryProductType = new ArrayList<Category>();
	ArrayList<String> listProductTypes = new ArrayList<String>();
	ArrayAdapter<String> productTypeAdapter;

	ArrayList<Category> listCategoryProductTypeChild = new ArrayList<Category>();
	ArrayList<String> listProductTypesChild = new ArrayList<String>();
	ArrayAdapter<String> productTypeAdapterChild;

	ArrayList<Category> listCategoryCheck = new ArrayList<Category>();
	ArrayList<Boolean> listCheckbox = new ArrayList<Boolean>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_list);
		currentCategory = getIntent().getExtras().getInt(Constants.KEY_CATEGORYID);		
		init();
	}
	@Override
	protected void onResume() {
		super.onResume();
		ChangeTextButtonLogin();
		FileUtil.listProduct.clear();
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
		

	}

	private void init(){
		initView();
		initData();
		initDataWebservice();
		handleOtherAction();
	}

	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lnCart);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);	

		lnNarrowTitle = (LinearLayout)findViewById(R.id.productlist_Narrow_lnTitle);
		lnNarrowContent = (LinearLayout)findViewById(R.id.productlist_Narrow_lnContent);
		ivNarrowShow = (ImageView)findViewById(R.id.productlist_Narrow_img);	
		lvProduct = (ListView)findViewById(R.id.productlist_lv);
		lvCheckbox = (ListView)findViewById(R.id.productlist_lvCheckbox);

		ivCategory = (ImageView)findViewById(R.id.include_footer_ivcategory);	
		
//		lvProduct.setOnTouchListener(new OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				v.getParent().requestDisallowInterceptTouchEvent(true);
//				return false;
//			}
//		});

		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		btnBack = (Button)findViewById(R.id.include_header_btnBack);

		spnTvSize = (Spinner)findViewById(R.id.productlist_spnTVsize);
		spnType1 = (Spinner)findViewById(R.id.productlist_spnType1);
		spnType2 = (Spinner)findViewById(R.id.productlist_spnType2);
		btnSearch = (Button)findViewById(R.id.productlist_btnSearch);
		btnClearFilter = (Button)findViewById(R.id.productlist_btnClearFilter);
		cbxAll = (CheckBox)findViewById(R.id.productlist_cbxAll);

		ivCategory.setImageResource(R.drawable.ico_category_active);
		tvTitle.setText("CATEGORIES");

		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		lnNarrowTitle.setOnClickListener(this);
		cbxAll.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		btnClearFilter.setOnClickListener(this);

		lvProduct.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {	

				Intent intent = new Intent(ProductListActivity.this,ProductDetailActivity.class);
				intent.putExtra(Constants.KEY_PRODUCTID, FileUtil.listProduct.get(position).getId());
				startActivity(intent);
				FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_PRODUCTDETAIL;
			} 
		});

		pDialog = new ProgressDialog(ProductListActivity.this);

	}

	private void initData() {
		clearSpinnerTVSize();
		clearSpinnerProductType1();
		clearSpinnerProductType2();

		tvSizeAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, listTvSizes);
		productTypeAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, listProductTypes);
		productTypeAdapterChild = new ArrayAdapter<String>(this,R.layout.spinner_item, listProductTypesChild);

		spnTvSize.setAdapter(tvSizeAdapter);
		spnType1.setAdapter(productTypeAdapter);
		spnType2.setAdapter(productTypeAdapterChild);

		adapter = new ProductListAdapter(ProductListActivity.this, FileUtil.listProduct);
		lvProduct.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		checkboxProductListAdapter = new CheckboxProductListAdapter(ProductListActivity.this, listCategoryCheck,listCheckbox);
	}

	private void initDataWebservice(){
		new GetProductsByCategoryIdAsyncTask(currentCategory).execute();

	}

	private void handleOtherAction(){
		spnTvSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				if (i != 0) {
					
				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 
		
		spnType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				if (i != 0) {

				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 

		spnType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				if (i != 0) {

				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {

		case R.id.productlist_Narrow_lnTitle:
			if (lnNarrowContent.getVisibility() == View.VISIBLE) {
				lnNarrowContent.setVisibility(View.GONE);
				ivNarrowShow.setImageResource(R.drawable.ico_down_white);
			}else{
				lnNarrowContent.setVisibility(View.VISIBLE);
				ivNarrowShow.setImageResource(R.drawable.ico_next_white);
				new GetTVSizeAsyncTask().execute();
			}

			break;	
		case R.id.productlist_cbxAll:

			break;	
		case R.id.productlist_btnSearch:

			break;
		case R.id.productlist_btnClearFilter:

			break;


		default:
			break;
		}

	}

	//------------------------------------------------------------

	public class GetProductsByCategoryIdAsyncTask extends AsyncTask<String, String, String> {

		private int cat_id;
		private String json;

		public GetProductsByCategoryIdAsyncTask(int cat_id){
			this.cat_id = cat_id;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}	


		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cat_id", String.valueOf(cat_id)));

				json = JsonParser.makeHttpRequest(
						Constants.URL_GETPRODUCTBYCATEGORYID, "GET", paramsUrl);
				Log.d("json", "json"+json);
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					FileUtil.listProduct.clear();
					for (int j = 0; j < array.length(); j++) {
						Product temp = new Product();
						temp.setId(array.getJSONObject(j).getInt("entity_id"));
						temp.setName(array.getJSONObject(j).getString("name"));
						temp.setDes(array.getJSONObject(j).getString("description"));    
						temp.setShortDes(array.getJSONObject(j).getString("short_description"));    
						temp.setImage(array.getJSONObject(j).getString("image"));
						FileUtil.listProduct.add(temp);					
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			adapter.notifyDataSetChanged();
			pDialog.dismiss();	      
		}
	}

	//------------------------------------------------------------

	public class NarrowSearchAsyncTask extends AsyncTask<String, String, String> {

		private String json;

		public NarrowSearchAsyncTask(){
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}	


		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();

//				paramsUrl.add(new BasicNameValuePair("cateids", String.valueOf(cat_id)));
//				paramsUrl.add(new BasicNameValuePair("size", String.valueOf(cat_id)));

				json = JsonParser.makeHttpRequest(Constants.URL_NARROWSEARCH, "GET", paramsUrl);

				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					FileUtil.listProduct.clear();
					for (int j = 0; j < array.length(); j++) {
						Product temp = new Product();
						temp.setId(array.getJSONObject(j).getInt("entity_id"));
						temp.setName(array.getJSONObject(j).getString("name"));
						temp.setDes(array.getJSONObject(j).getString("description"));    
						temp.setShortDes(array.getJSONObject(j).getString("short_description"));    
						temp.setImage(array.getJSONObject(j).getString("image"));
						FileUtil.listProduct.add(temp);					
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			adapter.notifyDataSetChanged();
			pDialog.dismiss();	      
		}
	}


	//------------------------------------------------------------

	public class GetTVSizeAsyncTask extends AsyncTask<String, String, String> {

		private String json;

		public GetTVSizeAsyncTask(){
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}	


		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cat_id", "16"));

				json = JsonParser.makeHttpRequest(
						Constants.URL_GETCATEGORIESBYID, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					clearSpinnerTVSize();
					listCategoryTvSize.clear();
					listTvSizes.clear();
					for (int j = 0; j < array.length(); j++) {
						Category temp = new Category();
						temp.setId(array.getJSONObject(j).getInt("id"));
						temp.setName(array.getJSONObject(j).getString("name"));
						temp.setSubcat(array.getJSONObject(j).getBoolean("subcat"));    
						temp.setIdParent(array.getJSONObject(j).getInt("id_parent"));
						listCategoryTvSize.add(temp);		
						listTvSizes.add(temp.getName());
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			adapter.notifyDataSetChanged();
			new GetProductTypeAsyncTask().execute();
			pDialog.dismiss();	      
		}
	}

	public class GetProductTypeAsyncTask extends AsyncTask<String, String, String> {

		private String json;

		public GetProductTypeAsyncTask(){
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}	
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cat_id", "13"));

				json = JsonParser.makeHttpRequest(
						Constants.URL_GETCATEGORIESBYID, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					clearSpinnerProductType1();
					listCategoryProductType.clear();
					for (int j = 0; j < array.length(); j++) {
						Category temp = new Category();
						temp.setId(array.getJSONObject(j).getInt("id"));
						temp.setName(array.getJSONObject(j).getString("name"));
						temp.setSubcat(array.getJSONObject(j).getBoolean("subcat"));    
						temp.setIdParent(array.getJSONObject(j).getInt("id_parent"));
						listCategoryProductType.add(temp);		
						listProductTypes.add(temp.getName());
					}	        			
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			productTypeAdapter.notifyDataSetChanged();
			new GetProductTypeChildAsyncTask().execute();
			pDialog.dismiss();	      
		}
	}

	public class GetProductTypeChildAsyncTask extends AsyncTask<String, String, String> {

		private String json;

		public GetProductTypeChildAsyncTask(){
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}	
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cat_id", "13"));

				json = JsonParser.makeHttpRequest(
						Constants.URL_GETCATEGORIESBYID, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					clearSpinnerProductType2();
					listCategoryProductTypeChild.clear();
					for (int j = 0; j < array.length(); j++) {
						Category temp = new Category();
						temp.setId(array.getJSONObject(j).getInt("id"));
						temp.setName(array.getJSONObject(j).getString("name"));
						temp.setSubcat(array.getJSONObject(j).getBoolean("subcat"));    
						temp.setIdParent(array.getJSONObject(j).getInt("id_parent"));
						listCategoryProductTypeChild.add(temp);		
						listProductTypesChild.add(temp.getName());
					}	        			
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			productTypeAdapterChild.notifyDataSetChanged();
			pDialog.dismiss();	      
		}
	}
	
	public class GetCategoryCheckAsyncTask extends AsyncTask<String, String, String> {

		private String json;

		public GetCategoryCheckAsyncTask(){
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}	
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cat_id", "13"));

				json = JsonParser.makeHttpRequest(
						Constants.URL_GETCATEGORIESBYID, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					listCategoryCheck.clear();
					listCheckbox.clear();
					for (int j = 0; j < array.length(); j++) {
						Category temp = new Category();
						temp.setId(array.getJSONObject(j).getInt("id"));
						temp.setName(array.getJSONObject(j).getString("name"));
						temp.setSubcat(array.getJSONObject(j).getBoolean("subcat"));    
						temp.setIdParent(array.getJSONObject(j).getInt("id_parent"));
						listCategoryCheck.add(temp);		
						listCheckbox.add(false);
					}	        			
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			checkboxProductListAdapter.notifyDataSetChanged();
			pDialog.dismiss();	      
		}
	}

	private void clearSpinnerTVSize(){
		String first = "All";
		listTvSizes.clear();	
		listTvSizes.add(first);
	}

	private void clearSpinnerProductType1(){
		String first = "All";
		listProductTypes.clear();	
		listProductTypes.add(first);
	}

	private void clearSpinnerProductType2(){
		String first = "All";
		listProductTypesChild.clear();	
		listProductTypesChild.add(first);
	}


}
