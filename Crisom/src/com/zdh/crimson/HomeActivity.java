package com.zdh.crimson;


import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gcm.GCMRegistrar;
import com.zdh.crimson.adapter.HomeAdapter;
import com.zdh.crimson.model.Category;
import com.zdh.crimson.utility.ALog;
import com.zdh.crimson.utility.CommonUtil;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.ExpandableHeightListView;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.GCMIntentService;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.ServerUtilities;
import com.zdh.crimson.utility.SharedPreferencesUtil;
import com.zdh.crimson.utility.StackActivity;
import com.zdh.crimson.utility.UserEmailFetcher;

public class HomeActivity extends BaseActivity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnSearch,lnCategory,lnCart,lnContact,lnBrowser,lnSearchImage;
	private ImageView ivHome;	
	private TextView tvTitle, tvMainSite;
	private EditText edtSearch;
	private Spinner spnManufacturer,spnModel;
	private RadioButton rbnFlatpanel,rbnProjector;	
	private ExpandableHeightListView lvCategory;
	private Button btnSearchMounter;

	private Uri uriUrl = Uri.parse(Constants.URL);
	private Animation slideLeftIn, slideLeftOut;
	private ViewFlipper mViewFlipper;	
	private ArrayList<ImageView> listImage = new ArrayList<ImageView>();
	private ImageView img1, img2, img3, img4;


	ArrayAdapter<String> manufacturerAdapter;
	ArrayAdapter<String> modelAdapter;

	static String keySearch ="";

	private ProgressDialog pDialog;

	HomeAdapter adapter;

	String radioChecked = Constants.KEY_DEVIDE_FLATPANEL;
	String keyDevice = Constants.KEY_DEVIDE_FLATPANEL;

	AsyncTask<Void, Void, Void> mRegisterTask;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Stack<Activity> stack = StackActivity.getInstance().getStack();
		if(stack.size() == 2){
			stack.get(0).finish();  
		}
		checkNotNull(CommonUtil.SERVER_URL, "SERVER_URL");
		checkNotNull(CommonUtil.SENDER_ID, "SENDER_ID");
		// Make sure the device has the proper dependencies.
		GCMRegistrar.checkDevice(this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(this);
		setContentView(R.layout.activity_home);		
		init();

		final String regId = GCMRegistrar.getRegistrationId(this);
		Log.d("tag", "regID:"+regId);

		if (regId.equals("")) {
			// Automatically registers application on startup.
			GCMRegistrar.register(this, CommonUtil.SENDER_ID);
		} else {
			// Device is already registered on GCM, check server.
			if (GCMRegistrar.isRegisteredOnServer(this)) {
				// Skips registration.
			} else {
				// Try to register again, but not in the UI thread.
				// It's also necessary to cancel the thread onDestroy(),
				// hence the use of AsyncTask instead of a raw thread.
				final Context context = this;
				mRegisterTask = new AsyncTask<Void, Void, Void>() {

					@Override
					protected Void doInBackground(Void... params) {

						String email = UserEmailFetcher.getEmail(context);

						boolean registered = ServerUtilities.register(context, regId, "",email);
						// At this point all attempts to register with the app
						// server failed, so we need to unregister the device
						// from GCM - the app will try to register again when
						// it is restarted. Note that GCM will send an
						// unregistered callback upon completion, but
						// GCMIntentService.onUnregistered() will ignore it.
						if (!registered) {
							GCMRegistrar.unregister(context);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						mRegisterTask = null;
					}

				};
				mRegisterTask.execute(null, null, null);
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(!FileUtil.count.equals("") && !FileUtil.count.equals("null") && !FileUtil.count.equals("0"))
		GCMIntentService.generateNotificationCheckOut(this,"It will notify them that they checked out with thing in their cart");
		super.onBackPressed();
	}
	@Override
	protected void onResume() {
		super.onResume();
		if(!keySearch.equals(""))
			edtSearch.setText(keySearch);
		ChangeTextButtonLogin();
		if (SharedPreferencesUtil.getFlagLogin(HomeActivity.this)) {
			new GetCountItem(HomeActivity.this, SharedPreferencesUtil.getIdCustomerLogin(HomeActivity.this)+"").execute();
		}
		if (btnSearchMounter!=null) {
			btnSearchMounter.setVisibility(View.GONE);
		}
	}
	

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		super.onDestroy();
	}

	private void init(){
		initView();		
		initData();
		initDataWebservice();
		handleOtherAction();
	}

	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(
					getString(R.string.error_config, name));
		}
	}

	private void initData() {

		adapter = new HomeAdapter(HomeActivity.this, FileUtil.listHome);
		lvCategory.setAdapter(adapter);
		lvCategory.setExpanded(true);
		modelAdapter = new ArrayAdapter<String>(HomeActivity.this,R.layout.spinner_item, FileUtil.listModelName);
		spnModel.setAdapter(modelAdapter);
		manufacturerAdapter = new ArrayAdapter<String>(HomeActivity.this,R.layout.spinner_item, FileUtil.listManufacturerName);
		spnManufacturer.setAdapter(manufacturerAdapter);
		adapter.notifyDataSetChanged();

		//--------------------------------------------------------------------

	}

	private void initView(){		
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lnCart);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);	
		lnBrowser =(LinearLayout)findViewById(R.id.home_lnBrowsercategories);	

		edtSearch = (EditText)findViewById(R.id.include_search_edt);
		lnSearchImage = (LinearLayout)findViewById(R.id.include_search_lnImage);
		ivHome = (ImageView)findViewById(R.id.include_footer_ivhome);	

		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);

		lvCategory =(ExpandableHeightListView)findViewById(R.id.home_lv);		
		spnManufacturer = (Spinner)findViewById(R.id.home_spnManufacturer);
		spnModel = (Spinner)findViewById(R.id.home_spnModel);
		rbnFlatpanel = (RadioButton)findViewById(R.id.home_rbnFlatpanel);
		rbnProjector = (RadioButton)findViewById(R.id.home_rbnProjector);		

		btnSearchMounter = (Button)findViewById(R.id.home_btnSearchMounter);

		tvMainSite = (TextView)findViewById(R.id.home_tv_mainsite);

		mViewFlipper = (ViewFlipper)findViewById(R.id.home_vf);		

		ivHome.setImageResource(R.drawable.ico_home_active);
		tvTitle.setText("HOME");

		img1 = (ImageView) findViewById(R.id.home_ivMark1);
		img2 = (ImageView) findViewById(R.id.home_ivMark2);
		img3 = (ImageView) findViewById(R.id.home_ivMark3);
		img4 = (ImageView) findViewById(R.id.home_ivMark4);

		listImage.add(img1);
		listImage.add(img2);
		listImage.add(img3);
		listImage.add(img4);

		tvMainSite.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		img1.setOnClickListener(this);
		img2.setOnClickListener(this);
		img3.setOnClickListener(this);
		img4.setOnClickListener(this);
		tvTitle.setOnClickListener(this);
		lnBrowser.setOnClickListener(this);		
		rbnFlatpanel.setOnClickListener(this);	
		rbnProjector.setOnClickListener(this);	
		lnSearchImage.setOnClickListener(this);	
		btnLogin.setOnClickListener(this);
		btnSearchMounter.setOnClickListener(this);

		pDialog = new ProgressDialog(HomeActivity.this);		

		ChangeTextButtonLogin();
	}

	private void initDataWebservice(){

		clearSpinnerManufacturer();
		clearSpinnerModel();
		if (!CommonUtil.TestNetWork(HomeActivity.this)) {			
			CommonUtil.showWifiNetworkAlert(HomeActivity.this);
		}else{
			new GetCategoriesHomeAsyncTask().execute();		

		}
	}

	private void clearSpinnerManufacturer(){		
		String manufacturer = "Select Manufacturer";		
		FileUtil.listManufacturerName.clear();		
		FileUtil.listManufacturerName.add(manufacturer);
	}

	private void clearSpinnerModel(){
		String model = "Select Model";
		FileUtil.listModelName.clear();	
		FileUtil.listModelName.add(model);
	}

	private void handleOtherAction(){
		edtSearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				boolean handled = false;
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					callSearchFeature();
					handled = true;

				}
				return handled;
			}
		});

		spnManufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 	
				if (FileUtil.listManufacturerName.size() > 1 ) {
					if (i != 0) {
						spnModel.setVisibility(View.VISIBLE);
						FileUtil.positionManufacturerName = i;
						new GetModelAsyncTask(radioChecked, FileUtil.listManufacturerName.get(i)).execute();
						
					}
					else{
						clearSpinnerModel();						
						modelAdapter.notifyDataSetChanged();
						btnSearchMounter.setVisibility(View.GONE);
					}
				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 

		spnModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
				if (FileUtil.listModelName.size() > 1) {
					if (i != 0) {
						FileUtil.positionModelName = i;	
						btnSearchMounter.setVisibility(View.VISIBLE);
					}else{
						
						btnSearchMounter.setVisibility(View.GONE);
					}
				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 

		lvCategory.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {	

				int narrowSearchID = Constants.KEY_CATEGORY_OTHER;				   
				if (FileUtil.listHome.get(position).getName().equalsIgnoreCase("TV SIZE")) {					   
					narrowSearchID = Constants.KEY_CATEGORY_TVSIZE;
				}
				Intent intent = new Intent(HomeActivity.this,CategoryActivity.class);
				intent.putExtra(Constants.KEY_CATEGORYID, FileUtil.listHome.get(position).getId());	
				intent.putExtra(Constants.KEY_CATEGORY_NARROWSEARCH, narrowSearchID);	
				startActivity(intent);
				overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
				FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_CATEGORY;
			} 
		});
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		//----------------Click other--------------------
		case R.id.home_tv_mainsite:
			Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
			startActivity(launchBrowser);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			break;


		case R.id.include_search_lnImage:
			callSearchFeature();
			break;	


		case R.id.home_lnBrowsercategories:
			Intent browsercategories = new Intent(HomeActivity.this, CategoryActivity.class);
			browsercategories.putExtra(Constants.KEY_CATEGORYID, Constants.CATEGORY_ROOT);
			startActivity(browsercategories);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_CATEGORY;
			break;	

			//--------click radio button----------

		case R.id.home_rbnFlatpanel:
			if (keyDevice.equals(Constants.KEY_DEVIDE_PROJECTOR)) {
				keyDevice = Constants.KEY_DEVIDE_FLATPANEL;
				spnModel.setVisibility(View.GONE);
				btnSearchMounter.setVisibility(View.GONE);
				rbnFlatpanel.setChecked(true);
				rbnProjector.setChecked(false);
				radioChecked = Constants.KEY_DEVIDE_FLATPANEL;
				new GetManufacturerAsyncTask(radioChecked).execute();
			}
			

			break;	
		case R.id.home_rbnProjector:
			if (keyDevice.equals(Constants.KEY_DEVIDE_FLATPANEL)) {
				keyDevice = Constants.KEY_DEVIDE_PROJECTOR;
				spnModel.setVisibility(View.GONE);
				btnSearchMounter.setVisibility(View.GONE);
				rbnFlatpanel.setChecked(false);
				rbnProjector.setChecked(true);
				radioChecked = Constants.KEY_DEVIDE_PROJECTOR;
				new GetManufacturerAsyncTask(radioChecked).execute();
			}
									
			break;	


			//-----------------------------------------------------
			//---------click marker slide image------------
		case R.id.home_ivMark1:
			for (int j = 0; j < listImage.size(); j++) {
				if (j != 0) {
					listImage.get(j).setImageResource(R.drawable.viewpager_nomal);
				}
			}
			listImage.get(0).setImageResource(R.drawable.viewpager_active);
			mViewFlipper.setDisplayedChild(0);
			break;	

		case R.id.home_ivMark2:
			for (int j = 1; j < listImage.size(); j++) {
				if (j != 1) {
					listImage.get(j).setImageResource(R.drawable.viewpager_nomal);
				}
			}
			listImage.get(1).setImageResource(R.drawable.viewpager_active);
			mViewFlipper.setDisplayedChild(1);		
			break;

		case R.id.home_ivMark3:
			for (int j = 2; j < listImage.size(); j++) {
				if (j != 2) {
					listImage.get(j).setImageResource(R.drawable.viewpager_nomal);
				}
			}
			listImage.get(2).setImageResource(R.drawable.viewpager_active);
			mViewFlipper.setDisplayedChild(2);
			break;

		case R.id.home_ivMark4:
			for (int j = 3; j < listImage.size(); j++) {
				if (j != 3) {
					listImage.get(j).setImageResource(R.drawable.viewpager_nomal);
				}
			}
			listImage.get(3).setImageResource(R.drawable.viewpager_active);
			mViewFlipper.setDisplayedChild(3);
			break;

			//---------------------------------------------------------	
			
		case R.id.home_btnSearchMounter:			
			Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
			intent.putExtra(Constants.KEY_MOUNTFINDER_MODEL, FileUtil.listModelName.get(FileUtil.positionModelName));
			intent.putExtra(Constants.KEY_MOUNTFINDER_MANUFACTURER, FileUtil.listManufacturerName.get(FileUtil.positionManufacturerName));
			intent.putExtra(Constants.KEY_MOUNTFINDER_DEVICE, String.valueOf(radioChecked));
			startActivity(intent);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_SEARCH;
			break;

		default:
			break;
		}

	}


	private void callSearchFeature(){
		keySearch = edtSearch.getText().toString().trim();
		if (keySearch.equals("")) {
			Toast.makeText(HomeActivity.this, "Please input keyword!", Toast.LENGTH_SHORT).show();
		}else{			
			CommonUtil.hideSoftKeyboard(HomeActivity.this);
			Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
			intent.putExtra(Constants.KEY_SEARCH_KEYWORD, keySearch);
			startActivity(intent);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_SEARCH;

		}
	}


	//--------------------GetCategoriesHomeAsyncTask----------------------------------------

	public class GetCategoriesHomeAsyncTask extends AsyncTask<String, String, String> {

		private String json;

		public GetCategoriesHomeAsyncTask(){

		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			pDialog.setContentView(R.layout.dialog_process);
		}

		protected String doInBackground(String... params) {

			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cat_id", String.valueOf(2)));

				json = JsonParser.makeHttpRequest(
						Constants.URL_GETCATEGORIESBYID, "GET", paramsUrl);

				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					FileUtil.listHome.clear();
					for (int j = 0; j < array.length(); j++) {
						Category temp = new Category();
						temp.setId(array.getJSONObject(j).getInt("id"));
						temp.setName(array.getJSONObject(j).getString("name").trim());
						temp.setSubcat(array.getJSONObject(j).getBoolean("subcat"));        						
						FileUtil.listHome.add(temp);
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {	
			adapter.notifyDataSetChanged();
			new GetManufacturerAsyncTask(radioChecked).execute();

		}
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
						FileUtil.listManufacturerName.add(name);
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {			
			manufacturerAdapter.notifyDataSetChanged();
			spnManufacturer.setSelection(0);
			initViewFlipper();
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
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
						FileUtil.listModelName.add(name);
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

	private void initViewFlipper(){
		//-------------------------set Animation--------------------------------
		slideLeftIn = AnimationUtils.loadAnimation(this, R.anim.left_in);
		slideLeftOut = AnimationUtils.loadAnimation(this, R.anim.left_out);
		mViewFlipper.setInAnimation(slideLeftIn);
		mViewFlipper.setOutAnimation(slideLeftOut);
		mViewFlipper.startFlipping();

		slideLeftOut.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				int currentViewIndex = mViewFlipper.getDisplayedChild();
				for (int j = 0; j < listImage.size(); j++) {
					if (j != currentViewIndex) {
						listImage.get(j).setImageResource(R.drawable.viewpager_nomal);
					}
				}
				listImage.get(currentViewIndex).setImageResource(R.drawable.viewpager_active);

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {

			}

		});
	}

	//--------------------GetModelAsyncTask----------------------------------------
	public class GetCountItem extends AsyncTask<String, String, String> {

		private String json;
		Context mContext;
		String mCid="0";
		String count = "0";
		public GetCountItem(Context context,String cid){
			this.mContext = context;
			this.mCid = cid;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();		
		}

		protected String doInBackground(String... params) {
			try {
				// Building Parameters
				List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
				paramsUrl.add(new BasicNameValuePair("cid", mCid));
				json = JsonParser.makeHttpRequest(Constants.URL_COUNT_ITEM_CART, "GET", paramsUrl);                
				if ((json != null) || (!json.equals(""))) {               
					JSONObject obj = new JSONObject(json);
					count = obj.getString("count");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return count;
		}
		protected void onPostExecute(String count) {	      
			FileUtil.count = count;
			ALog.d("TAG_COUNT","Count:"+count);
		}
	}
}
