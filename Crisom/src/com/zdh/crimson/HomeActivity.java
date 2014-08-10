package com.zdh.crimson;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.ProgressDialog;
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

import com.zdh.crimson.adapter.HomeAdapter;
import com.zdh.crimson.model.Category;
import com.zdh.crimson.utility.CommonUtil;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.ExpandableHeightListView;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;

public class HomeActivity extends BaseActivity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact,lnBrowser,lnSearchImage;
	private ImageView ivHome;	
	private TextView tvTitle, tvMainSite;
	private EditText edtSearch;
	private Spinner spnManufacturer,spnModel;
	private RadioButton rbnFlatpanel,rbnProjector;	
	private ExpandableHeightListView lvCategory;
	
	private Uri uriUrl = Uri.parse(Constants.URL);
	private Animation slideLeftIn, slideLeftOut;
	private ViewFlipper mViewFlipper;	
	private ArrayList<ImageView> listImage = new ArrayList<ImageView>();
	private ImageView img1, img2, img3, img4;
	
	ArrayList<String> listManufacturerName = new ArrayList<String>();
	ArrayList<String> listModelName = new ArrayList<String>();
	ArrayAdapter<String> manufacturerAdapter;
	ArrayAdapter<String> modelAdapter;
	
	private ProgressDialog pDialog;
	
	HomeAdapter adapter;
	
	int radioChecked = 1;
	int positionManufacturerName = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		new GetCategoriesHomeAsyncTask().execute();
		init();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		if(!edtSearch.getText().equals(""))
			edtSearch.setText("");
		
		ChangeTextButtonLogin();
		
	}
	
	private void init(){
		initView();		
		initData();
		initDataWebservice();
		handleOtherAction();
	}
	
	private void initData() {
		
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
		//--------------------------------------------------------------------
		
		if (!CommonUtil.TestNetWork(HomeActivity.this)) {
			CommonUtil.showWifiNetworkAlert(HomeActivity.this);
		}
		
	}

	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
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
				
		pDialog = new ProgressDialog(HomeActivity.this);		
		
		ChangeTextButtonLogin();
	}
	
	private void initDataWebservice(){
		clearSpinnerManufacturer();
		clearSpinnerModel();
		new GetManufacturerAsyncTask(radioChecked).execute();
		
		adapter = new HomeAdapter(HomeActivity.this, FileUtil.listHome);
		lvCategory.setAdapter(adapter);
		lvCategory.setExpanded(true);
		adapter.notifyDataSetChanged();
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
		    	if (listManufacturerName.size() > 1 ) {
		    		if (i != 0) {
			    		spnModel.setVisibility(View.VISIBLE);
			    		new GetModelAsyncTask(radioChecked, listManufacturerName.get(i)).execute();
			    		positionManufacturerName = i;
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
			    		Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
						intent.putExtra(Constants.KEY_MOUNTFINDER_MODEL, listModelName.get(i));
						intent.putExtra(Constants.KEY_MOUNTFINDER_MANUFACTURER, listManufacturerName.get(positionManufacturerName));
						intent.putExtra(Constants.KEY_MOUNTFINDER_DEVICE, String.valueOf(radioChecked));
						startActivity(intent);
						overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
						FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_SEARCH;
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
				   
				   Intent intent = new Intent(HomeActivity.this,CategoryActivity.class);
				   intent.putExtra(Constants.KEY_CATEGORYID, FileUtil.listHome.get(position).getId());
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
			startActivity(browsercategories);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_CATEGORY;
			break;	
			
		//--------click radio button----------
			
		case R.id.home_rbnFlatpanel:
			spnModel.setVisibility(View.GONE);
			rbnFlatpanel.setChecked(true);
			rbnProjector.setChecked(false);
			radioChecked = 1;
			new GetManufacturerAsyncTask(radioChecked).execute();
			
			break;	
		case R.id.home_rbnProjector:
			spnModel.setVisibility(View.GONE);
			rbnFlatpanel.setChecked(false);
			rbnProjector.setChecked(true);
			radioChecked = 2;
			new GetManufacturerAsyncTask(radioChecked).execute();						
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

		default:
			break;
		}
		
	}
	
	
	private void callSearchFeature(){
		if (edtSearch.getText().toString().trim().equals("")) {
			Toast.makeText(HomeActivity.this, "Please input keyword!", Toast.LENGTH_SHORT).show();
		}else{
			Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
			intent.putExtra(Constants.KEY_SEARCH_KEYWORD, edtSearch.getText().toString().trim());
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
	    }

	    protected String doInBackground(String... params) {
	    	
	    	try {
                // Building Parameters
                List<NameValuePair> paramsUrl = new ArrayList<NameValuePair>();
                paramsUrl.add(new BasicNameValuePair("cat_id", String.valueOf(13)));

                json = JsonParser.makeHttpRequest(
                		Constants.URL_GETCATEGORIESBYID, "GET", paramsUrl);
                
                if ((json != null) || (!json.equals(""))) {               
                	JSONArray array = new JSONArray(json);
                	FileUtil.listHome.clear();
        			for (int j = 0; j < array.length(); j++) {
        				Category temp = new Category();
        				temp.setId(array.getJSONObject(j).getInt("id"));
        				temp.setName(array.getJSONObject(j).getString("name"));
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
	    	
	    }
	}
	
	//--------------------GetManufacturerAsyncTask----------------------------------------
	public class GetManufacturerAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idDevide;
		
		public GetManufacturerAsyncTask(int idDevide){
			this.idDevide = idDevide;
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
                paramsUrl.add(new BasicNameValuePair("devide", String.valueOf(idDevide)));

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
	    	manufacturerAdapter = new ArrayAdapter<String>(HomeActivity.this,R.layout.spinner_item, listManufacturerName);
	    	spnManufacturer.setAdapter(manufacturerAdapter);
	    	pDialog.dismiss();
	    }
	}
	
	//--------------------GetModelAsyncTask----------------------------------------
	public class GetModelAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idDevide;
		String manu;
		public GetModelAsyncTask(int idDevide, String manu){
			this.idDevide = idDevide;
			this.manu = manu;
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
                paramsUrl.add(new BasicNameValuePair("devide", String.valueOf(idDevide)));
                paramsUrl.add(new BasicNameValuePair("manu", manu));
                Log.d("idDevide+manu", "idDevide"+idDevide+"manu"+manu);
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
	    	pDialog.dismiss();	
			modelAdapter = new ArrayAdapter<String>(HomeActivity.this,R.layout.spinner_item, listModelName);
			spnModel.setAdapter(modelAdapter);
	    }
	}
}
