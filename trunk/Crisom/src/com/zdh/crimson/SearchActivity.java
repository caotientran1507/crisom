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
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.zdh.crimson.adapter.SearchAdapter;
import com.zdh.crimson.model.Product;
import com.zdh.crimson.utility.CommonUtil;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;

public class SearchActivity extends BaseActivity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnCategory,lnCart,lnContact,lnSearchImage;
	private ImageView ivSearch;	
	private TextView tvTitle;
	private EditText edtSearch;
	private ListView listview;
	SearchAdapter adapter;
	private ProgressDialog pDialog;
	static String keySearch ="";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		if(FileUtil.listSearch.size()>0 )FileUtil.listSearch.clear();
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ChangeTextButtonLogin();
		adapter.notifyDataSetChanged();
		edtSearch.setText(keySearch);
	}


	@Override
	protected void ChangeTextButtonLogin() {
		super.ChangeTextButtonLogin();

		if (FileUtil.listSearch != null && FileUtil.listSearch.size() > 0 && adapter != null)
			adapter.notifyDataSetChanged();
	}

	private void init(){
		initView();
		handleOtherAction();
		initData();
	}	

	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lnCart);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);		

		edtSearch = (EditText)findViewById(R.id.include_search_edt);
		lnSearchImage = (LinearLayout)findViewById(R.id.include_search_lnImage);

		ivSearch = (ImageView)findViewById(R.id.include_footer_ivsearch);	

		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		ivSearch.setImageResource(R.drawable.ico_category_active);

		listview = (ListView)findViewById(R.id.search_lv);

		tvTitle.setText("SEARCH");
		ivSearch.setImageResource(R.drawable.ico_search_active);

		lnHome.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		lnSearchImage.setOnClickListener(this);	

		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {			
				Intent intent = new Intent(SearchActivity.this,ProductDetailActivity.class);
				intent.putExtra(Constants.KEY_PRODUCTID, FileUtil.listSearch.get(position).getId());
				startActivity(intent);
				overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
				FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_PRODUCTDETAIL;
			} 
		});
		pDialog = new ProgressDialog(SearchActivity.this);		
	}

	private void handleOtherAction(){

		edtSearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					keySearch = edtSearch.getText().toString().trim();
					FileUtil.listSearch.clear();
					adapter.notifyDataSetChanged();
					new SearchAsyncTask(keySearch).execute();		        	
				}
				return handled;
			}
		});
	}

	private void initData() {		
		if (getIntent().getExtras() != null) {
			String dataSearch = getIntent().getExtras().getString(Constants.KEY_SEARCH_KEYWORD);
			String model = getIntent().getExtras().getString(Constants.KEY_MOUNTFINDER_MODEL);
			String manufacturer = getIntent().getExtras().getString(Constants.KEY_MOUNTFINDER_MANUFACTURER);
			String device = getIntent().getExtras().getString(Constants.KEY_MOUNTFINDER_DEVICE);

			if(dataSearch != null && model == null){
				keySearch = dataSearch;
				edtSearch.setText(keySearch);
				new SearchAsyncTask(dataSearch).execute();
			}else{
				new MountFinderAsyncTask(device, manufacturer, model).execute();	
			}
		}

		if(adapter == null){
			adapter = new SearchAdapter(SearchActivity.this, FileUtil.listSearch);
			listview.setAdapter(adapter);
		}else{
			adapter.notifyDataSetChanged();
		}
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {

		case R.id.include_search_lnImage:
			keySearch = edtSearch.getText().toString().trim();
			FileUtil.listSearch.clear();
			adapter.notifyDataSetChanged();
			new SearchAsyncTask(keySearch).execute();
			break;	

		default:
			break;
		}

	}

	//----------------------Search--------------------------------------

	public class SearchAsyncTask extends AsyncTask<String, String, String> {

		private String key;
		private String json;

		public SearchAsyncTask(String key){
			this.key = key;
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
				paramsUrl.add(new BasicNameValuePair("q", key));

				json = JsonParser.makeHttpRequest(
						Constants.URL_SEARCH, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					FileUtil.listSearch.clear();
					for (int j = 0; j < array.length(); j++) {
						Product temp = new Product();
						temp.setId(array.getJSONObject(j).getInt("entity_id"));
						temp.setName(array.getJSONObject(j).getString("name"));
						temp.setDes(array.getJSONObject(j).getString("description"));    
						temp.setShortDes(array.getJSONObject(j).getString("short_description"));    
						temp.setImage(array.getJSONObject(j).getString("image"));
						FileUtil.listSearch.add(temp);					
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
			CommonUtil.hideSoftKeyboard(SearchActivity.this);
		}
	}


	//----------------------Search--------------------------------------

	public class MountFinderAsyncTask extends AsyncTask<String, String, String> {


		private String json;
		private String device;
		private String manufacturer;
		private String model;

		public MountFinderAsyncTask(String device,String manufacturer, String model){
			this.device = device;
			this.manufacturer = manufacturer;
			this.model = model;
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
				paramsUrl.add(new BasicNameValuePair("device", device));
				paramsUrl.add(new BasicNameValuePair("manufacturer", manufacturer));
				paramsUrl.add(new BasicNameValuePair("model", model));

				json = JsonParser.makeHttpRequest(
						Constants.URL_MOUNTFINDER, "GET", paramsUrl);
				Log.d("Json", json);
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					FileUtil.listSearch.clear();
					for (int j = 0; j < array.length(); j++) {
						Product temp = new Product();
						temp.setId(array.getJSONObject(j).getInt("entity_id"));
						temp.setName(array.getJSONObject(j).getString("name"));
						temp.setDes(array.getJSONObject(j).getString("description"));    
						temp.setShortDes(array.getJSONObject(j).getString("short_description"));    
						temp.setImage(array.getJSONObject(j).getString("image"));
						FileUtil.listSearch.add(temp);					
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
	
	@Override
	public void logout() {
		// TODO Auto-generated method stub
		super.logout();
		adapter.notifyDataSetChanged();
		
	}
}
