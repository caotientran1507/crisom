package com.zdh.crimson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zdh.crimson.adapter.ChildAdapter;
import com.zdh.crimson.adapter.DownloadAdapter;
import com.zdh.crimson.lazylist.ImageLoader;
import com.zdh.crimson.model.DocumentObject;
import com.zdh.crimson.model.OptionObject;
import com.zdh.crimson.model.Product;
import com.zdh.crimson.utility.CommonUtil;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class ProductDetailActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact,lnTitle,
	lnPrice,lnDownload,lnFaq,lnVideo;
	private ListView lvPrice,lvDownload,lvVideo;
	private ImageView ivCategory,ivAvatar;	
	private Button btnLogin,btnVerify,btnBack;
	private TextView tvTitle,tvTitle1,tvShortDes,tvDes,tvMainSite,tvPrice,tvDownload,tvFAQ,tvVideo,tvFaqContent;	
	static int currentProduct = 0;
	private ProgressDialog pDialog;
	private Product product = new Product();
	public ImageLoader imageLoader; 

	public static final int progress_bar_type = 0; 
	DownloadAdapter downloadAdapter;
	ChildAdapter childAdapter;
	String urlInSDCard ="";

	Dialog dialogVerifyNumber;
	Spinner spnManufacturer,spnModel;
	Button btnDialog;
	TextView tvDialog;
	LinearLayout lnFlatpanel, lnProjector;
	RadioButton rdbFlatpanel, rdbProjector;

	ArrayList<String> listManufacturerName = new ArrayList<String>();
	ArrayList<String> listModelName = new ArrayList<String>();
	ArrayAdapter<String> manufacturerAdapter;
	ArrayAdapter<String> modelAdapter;

	int radioChecked = 1;
	int positionManufacturerName = 0;
	int positionModelName = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product);
		currentProduct = getIntent().getExtras().getInt(Constants.KEY_PRODUCTID);

		init();
		initDataWebservice();
	}

	@Override
	protected void onResume() {
		super.onResume();
		ChangeTextButtonLogin();

	}

	private void init(){
		initView();
		handleOtherAction();
		initData();
	}

	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lnCart);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);	

		ivCategory = (ImageView)findViewById(R.id.include_footer_ivcategory);	

		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		btnBack = (Button)findViewById(R.id.include_header_btnBack);

		ivCategory.setImageResource(R.drawable.ico_category_active);

		tvTitle1 = (TextView)findViewById(R.id.product_tvTitle1);
		tvShortDes = (TextView)findViewById(R.id.product_tvTitle2);
		tvDes = (TextView)findViewById(R.id.product_tvDescription);
		tvMainSite = (TextView)findViewById(R.id.product_tvMainSite);
		lnTitle = (LinearLayout)findViewById(R.id.product_lnTitle);
		tvPrice = (TextView)findViewById(R.id.product_tvPrice);
		tvDownload = (TextView)findViewById(R.id.product_tvDownload);
		tvFAQ = (TextView)findViewById(R.id.product_tvFAQ);
		tvVideo = (TextView)findViewById(R.id.product_tvVideo);

		btnVerify = (Button)findViewById(R.id.product_btnVerify);
		ivAvatar = (ImageView)findViewById(R.id.product_ivAvatar);

		lnPrice = (LinearLayout)findViewById(R.id.product_lnPrice);
		lnDownload = (LinearLayout)findViewById(R.id.product_lnDownload);
		lnFaq = (LinearLayout)findViewById(R.id.product_lnFaq);
		lnVideo = (LinearLayout)findViewById(R.id.product_lnVideo);

		lvPrice = (ListView)findViewById(R.id.product_lvPrice);
		lvDownload = (ListView)findViewById(R.id.product_lvDownload);		
		lvVideo = (ListView)findViewById(R.id.product_lvVideo);
		tvFaqContent = (TextView)findViewById(R.id.product_tvFaqContent);

		imageLoader = new ImageLoader(ProductDetailActivity.this);

		tvTitle.setText("PRODUCT");
		btnBack.setVisibility(View.VISIBLE);

		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		tvPrice.setOnClickListener(this);
		tvDownload.setOnClickListener(this);
		tvFAQ.setOnClickListener(this);
		tvVideo.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnVerify.setOnClickListener(this);
		tvMainSite.setOnClickListener(this);
		pDialog = new ProgressDialog(ProductDetailActivity.this);

	}

	private void handleOtherAction(){
		lvDownload.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long arg) {	
				new DownloadFileFromURLAsyncTask().execute(product.getListDocument().get(position).getFile());
			} 
		});
	}

	private void initData() {
		downloadAdapter = new DownloadAdapter(ProductDetailActivity.this, product.getListDocument());
		childAdapter = new ChildAdapter(ProductDetailActivity.this, product.getListOption(),currentProduct);
		lvDownload.setAdapter(downloadAdapter);
		lvPrice.setAdapter(childAdapter);
	}

	private void initDataWebservice(){
		new GetProductByIdAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(ProductDetailActivity.this), currentProduct).execute();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		//----------------Click tab bottom--------------------
		//----------Home is clicked----------
		case R.id.include_footer_lnHome:
			Intent home = new Intent(ProductDetailActivity.this, HomeActivity.class);
			startActivity(home);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;		
			//----------Search is clicked----------
		case R.id.include_footer_lnSearch:
			Intent intent = new Intent(ProductDetailActivity.this, SearchActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;

			//----------Category is clicked----------
		case R.id.include_footer_lnCategory:

			break;

			//----------Cart is clicked----------
		case R.id.include_footer_lnCart:
			Intent cart = new Intent(ProductDetailActivity.this, CartActivity.class);
			startActivity(cart);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			break;

			//----------Contact is clicked----------
		case R.id.include_footer_lnContact:
			Intent contact = new Intent(ProductDetailActivity.this, ContactActivity.class);
			startActivity(contact);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			break;	


		case R.id.include_header_btnLogin:
			if (btnLogin.getText().toString().trim().equals(Constants.TEXT_BUTTON_LOGIN)) {
				Intent login = new Intent(ProductDetailActivity.this, LoginActivity.class);
				startActivity(login);
			}else{
				showDialog(ProductDetailActivity.this);
			}
			break;	
		case R.id.include_header_btnBack:
			finish();
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;	

		case R.id.product_btnVerify:
			showDialogVerifyCardNumber();
			break;

		case R.id.product_tvMainSite:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(product.getUrl()));
			startActivity(browserIntent);
			break;	
		case R.id.product_tvPrice:
			lnTitle.setBackgroundResource(R.drawable.tab_a);
			lnPrice.setVisibility(View.VISIBLE);
			lnDownload.setVisibility(View.GONE);
			lnFaq.setVisibility(View.GONE);
			lnVideo.setVisibility(View.GONE);
			break;	
		case R.id.product_tvDownload:
			lnTitle.setBackgroundResource(R.drawable.tab_b);
			lnPrice.setVisibility(View.GONE);
			lnDownload.setVisibility(View.VISIBLE);
			lnFaq.setVisibility(View.GONE);
			lnVideo.setVisibility(View.GONE);
			break;	
		case R.id.product_tvFAQ:
			lnTitle.setBackgroundResource(R.drawable.tab_c);
			lnPrice.setVisibility(View.GONE);
			lnDownload.setVisibility(View.GONE);
			lnFaq.setVisibility(View.VISIBLE);
			lnVideo.setVisibility(View.GONE);
			break;	
		case R.id.product_tvVideo:
			lnTitle.setBackgroundResource(R.drawable.tab_d);
			lnPrice.setVisibility(View.GONE);
			lnDownload.setVisibility(View.GONE);
			lnFaq.setVisibility(View.GONE);
			lnVideo.setVisibility(View.VISIBLE);
			break;	

		default:
			break;
		}

	}

	//------------------------------------------------------------

	public class GetProductByIdAsyncTask extends AsyncTask<String, String, String> {

		private int cid;
		private int pid;
		private String json;

		public GetProductByIdAsyncTask(int cid, int pid){
			this.cid = cid;
			this.pid = pid;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ProductDetailActivity.this);
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
							optionObject.setPrice(jsonArrayOptions.getJSONObject(i).getDouble("price"));
							optionObject.setMsrp(jsonArrayOptions.getJSONObject(i).getInt("msrp"));
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
			tvTitle1.setText(product.getName());
			tvShortDes.setText(product.getShortDes());
			tvDes.setTag(product.getDes());
			tvFaqContent.setText(product.getFaq());
			imageLoader.DisplayImage(product.getImage(), ivAvatar);
			pDialog.dismiss();	      
		}
	}



	public void showDialog(final Context mContext){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("Confirm Log out...");

		// Setting Dialog Message
		alertDialog.setMessage("Are you sure you want log out now?");

		// Setting Icon to Dialog
		alertDialog.setIcon(R.drawable.delete);

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int which) {
				SharedPreferencesUtil.saveFlagLogin(false, 0,mContext);
				ChangeTextButtonLogin();
				dialog.dismiss();
			}
		});
		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,	int which) {

				dialog.dismiss();

			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	public void ChangeTextButtonLogin(){
		if (SharedPreferencesUtil.getFlagLogin(ProductDetailActivity.this)) {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		} else {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
		}
	}


	/**
	 * Background Async Task to download file
	 * */
	class DownloadFileFromURLAsyncTask extends AsyncTask<String, String, String> {


		public DownloadFileFromURLAsyncTask(){
			CreateFolderData();
		}

		/**
		 * Before starting background thread
		 * Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(ProductDetailActivity.this);
			pDialog.setMessage("Downloading file. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				// getting file length
				int lenghtOfFile = conection.getContentLength();

				// input stream to read file - with 8k buffer
				InputStream input = new BufferedInputStream(url.openStream(), 8192);

				// Output stream to write file
				urlInSDCard = Environment.getExternalStorageDirectory() + File.separator+ Constants.PathFolderDocument + File.separator+ ""+System.currentTimeMillis()+"-"+CommonUtil.getNameFile(f_url[0]);
				OutputStream output = new FileOutputStream(urlInSDCard);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress(""+(int)((total*100)/lenghtOfFile));

					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();

				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				return "false";
			}

			return "true";
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			pDialog.setProgress(Integer.parseInt(progress[0]));
		}

		/**
		 * After completing background task
		 * Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String param) {
			// dismiss the dialog after the file was downloaded
			pDialog.dismiss();
			if (param.equals("false")) {
				Toast.makeText(ProductDetailActivity.this, "Can not download file!", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(ProductDetailActivity.this, "File was saved in "+urlInSDCard, Toast.LENGTH_LONG).show();
			}

		}

	}

	private void CreateFolderData(){
		File folderRoot = new File(Environment.getExternalStorageDirectory(),Constants.PathFolderDataRoot);
		File folderDocument = new File(Environment.getExternalStorageDirectory(),Constants.PathFolderDocument);
		if(!folderRoot.exists()){
			folderRoot.mkdir();
			folderDocument.mkdir();
		}else{
			if (!folderDocument.exists()) {
				folderDocument.mkdir();
			}			
		}
	}

	private void showDialogVerifyCardNumber()
	{		
		dialogVerifyNumber = new Dialog(ProductDetailActivity.this, R.style.FullHeightDialog);
		dialogVerifyNumber.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogVerifyNumber.setContentView(R.layout.dialog_verify);	

		spnManufacturer = (Spinner)dialogVerifyNumber.findViewById(R.id.dialog_verify_spnManufacturer);
		spnModel = (Spinner)dialogVerifyNumber.findViewById(R.id.dialog_verify_spnModel);
		btnDialog = (Button)dialogVerifyNumber.findViewById(R.id.dialog_verify_btnVerify);
		tvDialog = (TextView)dialogVerifyNumber.findViewById(R.id.dialog_verify_tvResult);
		lnFlatpanel = (LinearLayout)dialogVerifyNumber.findViewById(R.id.dialog_verify_lnFlatpanel);
		lnProjector = (LinearLayout)dialogVerifyNumber.findViewById(R.id.dialog_verify_lnProjector);
		rdbFlatpanel = (RadioButton)dialogVerifyNumber.findViewById(R.id.dialog_verify_rbnFlatpanel);
		rdbProjector = (RadioButton)dialogVerifyNumber.findViewById(R.id.dialog_verify_rbnProjector);
		
		clearSpinnerManufacturer();
		clearSpinnerModel();
		new GetManufacturerAsyncTask(radioChecked).execute();		
		manufacturerAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, listManufacturerName);
		modelAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, listModelName);
		spnManufacturer.setAdapter(manufacturerAdapter);
		spnModel.setAdapter(modelAdapter);

		dialogVerifyNumber.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialogVerifyNumber.dismiss();
				
			}
		});
		
		btnDialog.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new VerifyAsyncTask(String.valueOf(radioChecked), listManufacturerName.get(positionManufacturerName), listModelName.get(positionModelName), product.getListOption().get(0).getSku(), tvDialog).execute();
			}
		});				

		rdbFlatpanel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rdbFlatpanel.setChecked(true);
				rdbProjector.setChecked(false);
				radioChecked = 1;
				new GetManufacturerAsyncTask(radioChecked).execute();
			}
		});

		rdbProjector.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				rdbFlatpanel.setChecked(false);
				rdbProjector.setChecked(true);
				radioChecked = 2;
				new GetManufacturerAsyncTask(radioChecked).execute();
			}
		});

		spnManufacturer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				if (i != 0) {
					spnModel.setVisibility(View.VISIBLE);
					new GetModelAsyncTask(radioChecked, listManufacturerName.get(i)).execute();
					positionManufacturerName = i;
				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 

		spnModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
				if (i != 0) {
					positionModelName = i;
				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 



		dialogVerifyNumber.show();

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
			pDialog = new ProgressDialog(dialogVerifyNumber.getContext());      		 	        
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
			pDialog = new ProgressDialog(dialogVerifyNumber.getContext());     
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
				paramsUrl.add(new BasicNameValuePair("devide", String.valueOf(idDevide)));
				paramsUrl.add(new BasicNameValuePair("manu", manu));
				json = JsonParser.makeHttpRequest(Constants.URL_GETMODEL, "GET", paramsUrl);
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
		}
	}
	
	//--------------------VerifyAsyncTask----------------------------------------
		public class VerifyAsyncTask extends AsyncTask<String, String, String> {

			private String json;
			String device;
			String manufacturer;
			String model;
			String sku;
			TextView tvResult;
			public VerifyAsyncTask(String device, String manufacturer,String model,String sku,TextView tvResult){
				this.device = device;
				this.manufacturer = manufacturer;
				this.model = model;
				this.sku = sku;
				this.tvResult = tvResult;
			}


			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(dialogVerifyNumber.getContext());     
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
					paramsUrl.add(new BasicNameValuePair("device", device));
					paramsUrl.add(new BasicNameValuePair("manufacturer", manufacturer));
					paramsUrl.add(new BasicNameValuePair("model", model));
					paramsUrl.add(new BasicNameValuePair("sku", sku));
					json = JsonParser.makeHttpRequest(Constants.URL_VERIFY, "GET", paramsUrl);
					if ((json != null) || (!json.equals(""))) {               
						JSONObject jsonObject = new JSONObject(json);					
						if (!jsonObject.getBoolean("info")) {
							return "";
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				return "true";
			}

			protected void onPostExecute(String result) {	
				tvResult.setVisibility(View.VISIBLE);
				if (result.equals("true")) {
					tvResult.setText(Constants.VERIFY_OK);
				}else{
					tvResult.setText(Constants.VERIFY_NOK);
				}
				pDialog.dismiss();	
			}
		}

	private void clearSpinnerModel(){
		String model = "Select Model";
		listModelName.clear();	
		listModelName.add(model);
	}
	
	private void clearSpinnerManufacturer(){		
		String manufacturer = "Select Manufacturer";		
		listManufacturerName.clear();		
		listManufacturerName.add(manufacturer);
	}

}
