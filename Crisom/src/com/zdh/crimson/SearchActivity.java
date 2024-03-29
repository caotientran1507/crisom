package com.zdh.crimson;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.zdh.crimson.adapter.CheckboxSearchAdapter;
import com.zdh.crimson.adapter.SearchAdapter;
import com.zdh.crimson.model.Category;
import com.zdh.crimson.model.Product;
import com.zdh.crimson.utility.CommonUtil;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.ExpandableHeightListView;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;

public class SearchActivity extends BaseActivity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnCategory,lnCart,lnContact,lnSearchImage,lnEzmounter,lnEzmounterContent
	,lnNarrowTitle,lnNarrowContent, lncheckboxAll;
	private ImageView ivSearch, ivNarrowShow,ivEzmounter;	
	private TextView tvTitle,tvAll,tvNrMake,tvNrModel,tvNrTVSize;
	private EditText edtSearch;
	private LinearLayout lnInfo,lnTVSize,lnSize;
	private ExpandableHeightListView lvSearch, lvCheckbox;
	private Spinner spnManufacturer, spnModel;
	private RadioButton rbnFlatpanel,rbnProjector;	
	SearchAdapter adapter;
	private ProgressDialog pDialog;
	static String keySearch ="";	
	int positionManufacturerName = 0;
	int positionModelName = 0;

	//-----------ezmounter-----------------

	ArrayAdapter<String> manufacturerAdapter;
	ArrayAdapter<String> modelAdapter;

	//-------------narrow search------------
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
	private Spinner spnTvSize, spnType1, spnType2;
	private CheckBox cbxAll;
	private Button btnSearch, btnClearFilter;
	private Button btnSearchMounter;
	CheckboxSearchAdapter checkboxSearchAdapter;
	
	private ArrayList<String> listManufacturerName = new ArrayList<String>();
	private ArrayList<String> listModelName = new ArrayList<String>();

	String dataSearch = "";
	String model = "";
	String manufacturer = "";
	String device = "1";
	String tvSizeTitle = "";
	String keyDevice = Constants.KEY_DEVIDE_FLATPANEL;
	

	boolean flagEzMounter = false;
	boolean flagRunWithoutSearch = false;	
	boolean flagClear = false;
	int positionTV = 0;
	int positionType1 = 0;
	int positionType2 = 0;

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

		lvSearch = (ExpandableHeightListView)findViewById(R.id.search_lv);

		//--------ezmounter----------
		spnManufacturer = (Spinner)findViewById(R.id.search_spnManufacturer);
		spnModel = (Spinner)findViewById(R.id.search_spnModel);
		rbnFlatpanel = (RadioButton)findViewById(R.id.search_rbnFlatpanel);
		rbnProjector = (RadioButton)findViewById(R.id.search_rbnProjector);
		lnEzmounter = (LinearLayout)findViewById(R.id.search_lnEzmounter);
		lnEzmounterContent = (LinearLayout)findViewById(R.id.search_lnEzmounterContent);
		ivEzmounter = (ImageView)findViewById(R.id.search_ivEzmounter);
		lnTVSize = (LinearLayout)findViewById(R.id.search_lnTVsize);

		//------narrow search----------
		spnTvSize = (Spinner) findViewById(R.id.search_spnTVsize);
		spnType1 = (Spinner) findViewById(R.id.search_spnType1);
		spnType2 = (Spinner) findViewById(R.id.search_spnType2);
		btnSearch = (Button) findViewById(R.id.search_btnSearch);
		btnClearFilter = (Button) findViewById(R.id.search_btnClearFilter);
		cbxAll = (CheckBox) findViewById(R.id.search_cbxAll);
		tvAll = (TextView) findViewById(R.id.search_tvAll);
		tvNrMake = (TextView) findViewById(R.id.search_make);
		tvNrModel = (TextView) findViewById(R.id.search_mode);
		tvNrTVSize = (TextView) findViewById(R.id.search_tvSize);
		lnNarrowTitle = (LinearLayout) findViewById(R.id.search_Narrow_lnTitle);
		lnNarrowContent = (LinearLayout) findViewById(R.id.search_Narrow_lnContent);
		ivNarrowShow = (ImageView) findViewById(R.id.search_ivNarrow);
		lvCheckbox = (ExpandableHeightListView) findViewById(R.id.search_lvCheckbox);
		lnInfo = (LinearLayout) findViewById(R.id.search_info);
		lnSize = (LinearLayout) findViewById(R.id.search_lnSize);
		

		lncheckboxAll = (LinearLayout) findViewById(R.id.search_lncheckboxAll);
		
		btnSearchMounter = (Button)findViewById(R.id.search_btnSearchMounter);	

		tvTitle.setText("SEARCH");
		ivSearch.setImageResource(R.drawable.ico_search_active);

		lnHome.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		lnSearchImage.setOnClickListener(this);	
		lnEzmounter.setOnClickListener(this);	
		rbnFlatpanel.setOnClickListener(this);	
		rbnProjector.setOnClickListener(this);
		lnNarrowTitle.setOnClickListener(this);
		tvAll.setOnClickListener(this);
		btnSearch.setOnClickListener(this);
		btnClearFilter.setOnClickListener(this);
		lncheckboxAll.setOnClickListener(this);
		cbxAll.setOnClickListener(this);
		btnSearchMounter.setOnClickListener(this);

		pDialog = new ProgressDialog(SearchActivity.this);		
	}

	private void handleOtherAction(){
		lvSearch.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {			
				Intent intent = new Intent(SearchActivity.this,ProductDetailActivity.class);
				intent.putExtra(Constants.KEY_PRODUCTID, FileUtil.listSearch.get(position).getId());
				startActivity(intent);
				overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
				FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_PRODUCTDETAIL;
			} 
		});

		edtSearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					CommonUtil.hideSoftKeyboard(SearchActivity.this);
					keySearch = edtSearch.getText().toString().trim();
					FileUtil.listSearch.clear();
					adapter.notifyDataSetChanged();
					new SearchAsyncTask(keySearch).execute();		        	
				}
				return handled;
			}
		});

		spnManufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 	
				if (listManufacturerName.size() > 1 ) {
					if (i != 0) {
						spnModel.setVisibility(View.VISIBLE);
						if (!flagEzMounter) {
							new GetModelAsyncTask(device, listManufacturerName.get(i)).execute();
						}
						
						positionManufacturerName = i;						
					}else{
						clearSpinnerModel();						
						modelAdapter.notifyDataSetChanged();
					}
				}

			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 

		spnModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
				if (listModelName.size() > 1) {
					if (i != 0) {
						btnSearchMounter.setVisibility(View.VISIBLE);
						positionModelName = i;											
					}
					else{
						
						btnSearchMounter.setVisibility(View.GONE);
					}
				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 

		spnTvSize.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView,
					View view, int i, long l) {
				positionTV = i;
			}

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			}
		});

		spnType1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
				positionType1 = i;
				if (i != 0) {
					spnType2.setVisibility(View.VISIBLE);

					new GetProductTypeChildAsyncTask(String.valueOf(listCategoryProductType.get(i-1).getId())).execute();					
				}else{
					clearSpinnerProductType2();
					if (productTypeAdapterChild != null) {
						productTypeAdapterChild.notifyDataSetChanged();
					}					
				}				
				lncheckboxAll.setVisibility(View.GONE);
				lvCheckbox.setVisibility(View.GONE);

			}

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			}
		});

		spnType2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view,
					int i, long l) {
				positionType2 = i;
				if (i != 0) {
					lncheckboxAll.setVisibility(View.VISIBLE);
					lvCheckbox.setVisibility(View.VISIBLE);
					new GetCategoryCheckAsyncTask(String.valueOf(listCategoryProductTypeChild.get(i-1).getId())).execute();
				}else{
					lncheckboxAll.setVisibility(View.GONE);
					lvCheckbox.setVisibility(View.GONE);
					listCategoryCheck.clear();
					listCheckbox.clear();
					if (checkboxSearchAdapter != null) {
						checkboxSearchAdapter.notifyDataSetChanged();
					}
				}
			}

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			}
		});
	}

	private void initData() {	
		clearSpinnerManufacturer();
		clearSpinnerModel();
		manufacturerAdapter = new ArrayAdapter<String>(SearchActivity.this,R.layout.spinner_item, listManufacturerName);
		spnManufacturer.setAdapter(manufacturerAdapter);		
		modelAdapter = new ArrayAdapter<String>(SearchActivity.this,R.layout.spinner_item, listModelName);
		spnModel.setAdapter(modelAdapter);		
		adapter = new SearchAdapter(SearchActivity.this, FileUtil.listSearch);
		lvSearch.setAdapter(adapter);
		lvSearch.setExpanded(true);

		//-----------------narrow search---------------------------
		clearSpinnerTVSize();
		clearSpinnerProductType1();
		clearSpinnerProductType2();

		tvSizeAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, listTvSizes);
		productTypeAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, listProductTypes);
		productTypeAdapterChild = new ArrayAdapter<String>(this,R.layout.spinner_item, listProductTypesChild);

		spnTvSize.setAdapter(tvSizeAdapter);
		spnType1.setAdapter(productTypeAdapter);
		spnType2.setAdapter(productTypeAdapterChild);

		if (getIntent().getExtras() != null) {
			dataSearch = getIntent().getExtras().getString(Constants.KEY_SEARCH_KEYWORD);
			model = getIntent().getExtras().getString(Constants.KEY_MOUNTFINDER_MODEL);
			manufacturer = getIntent().getExtras().getString(Constants.KEY_MOUNTFINDER_MANUFACTURER);
			device = getIntent().getExtras().getString(Constants.KEY_MOUNTFINDER_DEVICE);

			ivEzmounter.setImageResource(R.drawable.ico_down_white);
			lnTVSize.setVisibility(View.GONE);

			if(dataSearch != null && model == null){
				keySearch = dataSearch;
				edtSearch.setText(keySearch);
				new SearchAsyncTask(dataSearch).execute();
			}else{
				keySearch = "";
				edtSearch.setText(keySearch);

				if (device.equals(Constants.KEY_DEVIDE_FLATPANEL)) {
					rbnFlatpanel.setChecked(true);
					rbnProjector.setChecked(false);
				}else{
					rbnFlatpanel.setChecked(false);
					rbnProjector.setChecked(true);
				}
				keyDevice = device;
				lnEzmounterContent.setVisibility(View.VISIBLE);

				flagEzMounter = true;					
				lnInfo.setVisibility(View.VISIBLE);
				
				listManufacturerName.clear();
				listModelName.clear();
				listManufacturerName.addAll(FileUtil.listManufacturerName);
				listModelName.addAll(FileUtil.listModelName);
				manufacturerAdapter.notifyDataSetChanged();				
				modelAdapter.notifyDataSetChanged();
				
				spnManufacturer.setSelection(FileUtil.positionManufacturerName);
				spnModel.setSelection(FileUtil.positionModelName);
				new GetProductTypeAsyncTask().execute();
			}
		}else{
			flagRunWithoutSearch = true;
			new GetManufacturerAsyncTask(device).execute();
		}
	}


	private void callChangeViewWhenMounter() {
		if (listManufacturerName.size() > 1 && listModelName.size() > 1) {
			lnInfo.setVisibility(View.VISIBLE);
			tvNrMake.setText(listManufacturerName.get(positionManufacturerName));
			tvNrModel.setText(listModelName.get(positionModelName));
		}			
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {

		case R.id.include_search_lnImage:
			CommonUtil.hideSoftKeyboard(SearchActivity.this);
			keySearch = edtSearch.getText().toString().trim();
			FileUtil.listSearch.clear();
			adapter.notifyDataSetChanged();
			new SearchAsyncTask(keySearch).execute();
			break;	

			//--------click radio button----------

		case R.id.search_rbnFlatpanel:
			if (keyDevice.equals(Constants.KEY_DEVIDE_PROJECTOR)) {
				keyDevice = Constants.KEY_DEVIDE_FLATPANEL;
				btnSearchMounter.setVisibility(View.GONE);
				spnModel.setVisibility(View.GONE);
				rbnFlatpanel.setChecked(true);
				rbnProjector.setChecked(false);
				device = Constants.KEY_DEVIDE_FLATPANEL;
				new GetManufacturerAsyncTask(device).execute();
			}
			
			
			

			break;	
		case R.id.search_rbnProjector:
			if (keyDevice.equals(Constants.KEY_DEVIDE_FLATPANEL)) {
				keyDevice = Constants.KEY_DEVIDE_PROJECTOR;
				btnSearchMounter.setVisibility(View.GONE);
				spnModel.setVisibility(View.GONE);
				rbnFlatpanel.setChecked(false);
				rbnProjector.setChecked(true);
				device = Constants.KEY_DEVIDE_PROJECTOR;
				new GetManufacturerAsyncTask(device).execute();	
			}
								
			break;	



		case R.id.search_lnEzmounter:
			if (lnEzmounterContent.getVisibility() == View.VISIBLE) {
				lnEzmounterContent.setVisibility(View.GONE);
				ivEzmounter.setImageResource(R.drawable.ico_next_white);
			} else {
				lnEzmounterContent.setVisibility(View.VISIBLE);
				ivEzmounter.setImageResource(R.drawable.ico_down_white);				
			}

			break;

			//------------------narrow search-------------------
		case R.id.search_Narrow_lnTitle:
			if (lnNarrowContent.getVisibility() == View.VISIBLE) {
				lnNarrowContent.setVisibility(View.GONE);
				ivNarrowShow.setImageResource(R.drawable.ico_next_white);
			}
			else {
				lnNarrowContent.setVisibility(View.VISIBLE);
				ivNarrowShow.setImageResource(R.drawable.ico_down_white);							
			}

			break;

		case R.id.search_cbxAll:
			if (!FileUtil.flagCheckAll) {
				checkAll();
				FileUtil.flagCheckAll = true;
			} else {
				uncheckAll();
				FileUtil.flagCheckAll = false;
			}	
			checkboxSearchAdapter.notifyDataSetChanged();
			break;
		case R.id.search_tvAll:
			if (!FileUtil.flagCheckAll) {
				checkAll();
				FileUtil.flagCheckAll = true;
			} else {
				uncheckAll();
				FileUtil.flagCheckAll = false;
			}

			checkboxSearchAdapter.notifyDataSetChanged();
			break;
		case R.id.search_btnSearch:
			if (positionTV != 0 || positionType1 != 0) {
				new NarrowSearchAsyncTask().execute();
			}else{
				Toast.makeText(SearchActivity.this, "Please select TV Size or Type.", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.search_btnClearFilter:
			clearAll();
			break;
			
		case R.id.search_btnSearchMounter:
			if (positionManufacturerName > 0 && positionModelName > 0) {
				if (listProductTypes.size() > 1) {
					flagRunWithoutSearch = false;
					flagEzMounter = true;
					flagClear = false;
					lnTVSize.setVisibility(View.GONE);
					if (rbnFlatpanel.isChecked()) {
						spnType1.setSelection(1);
					}else{
						spnType1.setSelection(2);
					}
					
					new MountFinderAsyncTask(device, listManufacturerName.get(positionManufacturerName), listModelName.get(positionModelName)).execute();
				}
			}else{
				Toast.makeText(SearchActivity.this, "Please select Manufacturer and Model!", Toast.LENGTH_SHORT).show();
			}
			
			break;
			
			

		default:
			break;
		}

	}

	private void clearAll() {
		lnInfo.setVisibility(View.GONE);
		lnTVSize.setVisibility(View.VISIBLE);
		lncheckboxAll.setVisibility(View.GONE);
		lvCheckbox.setVisibility(View.GONE);
		spnType2.setVisibility(View.GONE);
		clearSpinnerProductType2();
		productTypeAdapterChild.notifyDataSetChanged();
		spnType1.setSelection(0);
		spnType2.setSelection(0);
		spnTvSize.setSelection(0);
		flagClear = true;
		flagRunWithoutSearch = true;
		flagEzMounter = false;
		btnSearchMounter.setVisibility(View.GONE);
		new GetTVSizeAsyncTask().execute();			
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
				pDialog.setCancelable(false);
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
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}
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
				pDialog.setCancelable(false);
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
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}
			callChangeViewWhenMounter();
			adapter.notifyDataSetChanged();
			new GetSizeTitleAsyncTask().execute();			 
		}
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		super.logout();
		adapter.notifyDataSetChanged();

	}

	//--------------------GetManufacturerAsyncTask----------------------------------------
	public class GetManufacturerAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		String idDevide;

		public GetManufacturerAsyncTask(String idDevide){
			this.idDevide = idDevide;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {	        		 	        
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("devide", idDevide));

				json = JsonParser.makeHttpRequest(
						Constants.URL_GETMANUFACTURER, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					clearSpinnerManufacturer();
					for (int j = 0; j < array.length(); j++) {
						String name = array.getString(j);      						
						listManufacturerName.add(name);
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {		
			spnManufacturer.setSelection(0);
			if (flagRunWithoutSearch) {
				
				new GetTVSizeAsyncTask().execute();					
			}else{
				if ((pDialog != null) && pDialog.isShowing()) { 
					pDialog.dismiss();
				}
			}
			

		}
	}

	//--------------------GetModelAsyncTask----------------------------------------
	public class GetModelAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		String idDevide;
		String manu;
		public GetModelAsyncTask(String idDevide, String manu){
			this.idDevide = idDevide;
			this.manu = manu;
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null ) {
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}

		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("devide", idDevide));
				paramsUrl.add(new BasicNameValuePair("manu", manu));				
				json = JsonParser.makeHttpRequest(
						Constants.URL_GETMODEL, "GET", paramsUrl);                
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					clearSpinnerModel();
					for (int j = 0; j < array.length(); j++) {
						String name = array.getString(j);      						
						listModelName.add(name);
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {	  
			modelAdapter.notifyDataSetChanged();
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}	

		}
	}

	private void clearSpinnerManufacturer(){		
		String manufacturer = "Select Manufacturer";		
		listManufacturerName.clear();		
		listManufacturerName.add(manufacturer);
	}

	private void clearSpinnerModel(){
		String model = "Select Model";
		listModelName.clear();	
		listModelName.add(model);
	}

	private void clearSpinnerTVSize() {
		String first = "All";
		listTvSizes.clear();
		listTvSizes.add(first);
	}

	private void clearSpinnerProductType1() {
		String first = "All";
		listProductTypes.clear();
		listProductTypes.add(first);
	}

	private void clearSpinnerProductType2() {
		String first = "All";
		listProductTypesChild.clear();
		listProductTypesChild.add(first);
	}

	private void checkAll() {
		for (int i = 0; i < listCheckbox.size(); i++) {
			listCheckbox.set(i, true);
		}
	}

	private void uncheckAll() {
		for (int i = 0; i < listCheckbox.size(); i++) {
			listCheckbox.set(i, false);
		}
	}

	// ------------------------------------------------------------

	public class NarrowSearchAsyncTask extends
	AsyncTask<String, String, String> {

		private String json;

		public NarrowSearchAsyncTask() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null) {
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}

		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				
				if (lnInfo.getVisibility() == View.VISIBLE) {
					paramsUrl.add(new BasicNameValuePair("device", device));
					paramsUrl.add(new BasicNameValuePair("manufacturer", listManufacturerName.get(positionManufacturerName)));
					paramsUrl.add(new BasicNameValuePair("model", listModelName.get(positionModelName)));
				}

				//-------get checkbox---------
				if (positionType1 != 0) {
					String cateids = "";
					ArrayList<String> listCategoryTemp = new ArrayList<String>();
					if (positionType2 != 0) {
						if (CommonUtil.checkAllCheckBox(listCheckbox) == 0 || CommonUtil.checkAllCheckBox(listCheckbox) == listCheckbox.size()) {
							for (int i = 0; i < listCheckbox.size(); i++) {							
								listCategoryTemp.add(String.valueOf(listCategoryCheck.get(i).getId()));												
							}
						}else{
							for (int i = 0; i < listCheckbox.size(); i++) {
								if (listCheckbox.get(i)) {
									listCategoryTemp.add(String.valueOf(listCategoryCheck.get(i).getId()));
								}					
							}
						}


					} else {
						for (int i = 0; i < listCategoryProductTypeChild.size(); i++) {
							listCategoryTemp.add(String.valueOf(listCategoryProductTypeChild.get(i).getId()));										
						}
					}
					String[] arrIds = new String[listCategoryTemp.size()];
					int i = 0;
					for (String key : listCategoryTemp) {
						arrIds[i] = key;
						i++;
					}
					cateids = CommonUtil.combineString(arrIds, ",");
					paramsUrl.add(new BasicNameValuePair("cateids", cateids));
				}
				if (positionTV != 0) {
					paramsUrl.add(new BasicNameValuePair("size", String.valueOf(listCategoryTvSize.get(positionTV).getId())));
				}

				//-------------get Json--------------
				if (lnInfo.getVisibility() == View.VISIBLE) {					
					json = JsonParser.makeHttpRequest(Constants.URL_MOUNTFINDER,"GET", paramsUrl);
				}else{
					json = JsonParser.makeHttpRequest(Constants.URL_NARROWSEARCH,"GET", paramsUrl);
				}
				

				if ((json != null) || (!json.equals(""))) {
					Log.d("json", json);
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
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}
		}
	}

	// ------------------------------------------------------------

	public class GetTVSizeAsyncTask extends AsyncTask<String, String, String> {

		private String json;

		public GetTVSizeAsyncTask() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null) {
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
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
		}
	}

	public class GetProductTypeAsyncTask extends
	AsyncTask<String, String, String> {

		private String json;

		public GetProductTypeAsyncTask() {
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null) {
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
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

			return "";
		}

		protected void onPostExecute(String file_url) {
			productTypeAdapter.notifyDataSetChanged();
			try {
				if (flagClear) {
					flagClear = false;
					spnType1.setSelection(0);
				}else{
					if (flagRunWithoutSearch) {
						spnType1.setSelection(0);
					}else{
						if(device != null && !device.equals(""))
							spnType1.setSelection(Integer.parseInt(device));
					}
					
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}
			if (flagEzMounter) {
				flagEzMounter = false;
				new MountFinderAsyncTask(device, manufacturer, model).execute();
			}			
		}
	}

	public class GetSizeTitleAsyncTask extends AsyncTask<String, String, String>{


		private String json;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null) {
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}
		}

		@Override
		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("device", device));
				paramsUrl.add(new BasicNameValuePair("manufacturer", listManufacturerName.get(positionManufacturerName)));
				paramsUrl.add(new BasicNameValuePair("model", listModelName.get(positionModelName)));

				json = JsonParser.makeHttpRequest(Constants.URL_GETSIZETITLE, "GET", paramsUrl);
				Log.d("json", json);
				if ((json != null) || (!json.equals(""))) {
					JSONObject object = new JSONObject(json);
					tvSizeTitle =  object.getString("name");
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (tvSizeTitle == null || tvSizeTitle.equals("0") || tvSizeTitle.equals("null")|| tvSizeTitle.equals("")) {
								lnSize.setVisibility(View.GONE);	
							}else{
								lnSize.setVisibility(View.VISIBLE);	
								tvNrTVSize.setText(tvSizeTitle);
							}
								
						}
					});

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "";
		}

		protected void onPostExecute(String result) {
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}	
		}
	}
	public class GetProductTypeChildAsyncTask extends
	AsyncTask<String, String, String> {

		private String json;
		String cat_id;
		public GetProductTypeChildAsyncTask(String cat_id) {
			this.cat_id = cat_id;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null) {
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cat_id", cat_id));

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
			spnType2.setSelection(0);
			productTypeAdapterChild.notifyDataSetChanged();			
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}	
		}
	}

	public class GetCategoryCheckAsyncTask extends
	AsyncTask<String, String, String> {

		private String json;
		String cat_id;
		public GetCategoryCheckAsyncTask(String cat_id) {
			this.cat_id = cat_id;
		}


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			if (pDialog != null) {
				pDialog.setMessage("Loading...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
				pDialog.setContentView(R.layout.dialog_process);
			}
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cat_id", cat_id));

				json = JsonParser.makeHttpRequest(Constants.URL_GETCATEGORIESBYID, "GET", paramsUrl);
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
						listCheckbox.add(true);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {
			if (checkboxSearchAdapter == null) {
				checkboxSearchAdapter = new CheckboxSearchAdapter(SearchActivity.this, 
						listCategoryCheck,listCheckbox,cbxAll);
				lvCheckbox.setAdapter(checkboxSearchAdapter);
				lvCheckbox.setExpanded(true);				
			} else
				checkboxSearchAdapter.notifyDataSetChanged();
			cbxAll.setChecked(true);
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}
		}
	}

}
