package com.zdh.crimson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zdh.crimson.adapter.ParcelServiceAdapter;
import com.zdh.crimson.adapter.ReviewCheckoutDetailAdapter;
import com.zdh.crimson.model.Address;
import com.zdh.crimson.model.CarrierObject;
import com.zdh.crimson.model.CreditCardObject;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class CheckoutDetailActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact,lnPaypal
	,ln1BillingInfomation,ln2ShippingInfomation,ln3ShippingMethod,ln4PaymentInfomation,ln5OrderReview
	,ln1BillingInfomationContent,ln2ShippingInfomationContent,ln3ShippingMethodContent,ln4PaymentInfomationContent,ln5OrderReviewContent
	,lnCreditCardOnFileContent, lnCreditCardContent;
	private ImageView ivCart;	
	private Button btnLogin,btnBack,btnBillingContinue,btnShippingContinue,btnShippingMethodContinue,btnPaymentContinue,btnPlaceOrder;
	private TextView tvTitle,tvSubtotal,tvShippingHandling,tvTax,tvGrandTotal,tvEditYourCard,tvRedirectedPaypal
	,tvWhatIsThis,tvWhatIsPaypal;

	Spinner spnBillingAddress,spnShippingAddress,spnCreditCardOnFile,spnCreditCardType,spnExpirationMonth,spnExpirationYear;
	RadioButton rbnShipThisAddress,rbnShipDifferentAddress,rbnCreditCardOnFile,rbnPaypal,rbnCreditTerms;
	CheckBox cbxUseBillingAddress,cbxSaveCreditCard;
	ListView lvParcelService,lvReview;
	EditText edtCreditCardNumber,edtCardVerification;

	ArrayList<Boolean> listRadioButton = new ArrayList<Boolean>();
	ArrayList<Address> listAddress = new ArrayList<Address>();
	ArrayList<CreditCardObject> listCreditCard = new ArrayList<CreditCardObject>();

	ArrayList<String> addresses = new ArrayList<String>();
	ArrayList<String> creditCards = new ArrayList<String>();

	ArrayAdapter<String> addressesAdapter;
	ArrayAdapter<String> monthAdapter;
	ArrayAdapter<String> yearAdapter;
	ArrayAdapter<String> cardTypeAdapter;
	ArrayAdapter<String> cardTypeOnFileAdapter;
	ParcelServiceAdapter parcelServiceAdapter;
	int positionShipping = 0;
	int positionBilling = 0;

	String sSubtotal = "";
	String sShipping = "";
	String sTax = "";
	String sGrandTotal = "";


	ReviewCheckoutDetailAdapter reviewCheckoutDetailAdapter;

	private ProgressDialog pDialog;

	Dialog dialogVerifyNumber;
	ImageView ivCloseDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout_detail);
		init();
	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		reviewCheckoutDetailAdapter.notifyDataSetChanged();
	}



	private void init(){
		initView();
		handleOtherAction();
		initData();
		initDataWebservice();
	}	

	private void initView(){
		lnHome = (LinearLayout)findViewById(R.id.include_footer_lnHome);
		lnSearch = (LinearLayout)findViewById(R.id.include_footer_lnSearch);
		lnCategory = (LinearLayout)findViewById(R.id.include_footer_lnCategory);
		lnCart = (LinearLayout)findViewById(R.id.include_footer_lnCart);
		lnContact = (LinearLayout)findViewById(R.id.include_footer_lnContact);		

		ivCart = (ImageView)findViewById(R.id.include_footer_ivCart);	

		tvTitle = (TextView)findViewById(R.id.include_header_tvTitle);
		btnLogin = (Button)findViewById(R.id.include_header_btnLogin);
		btnBack = (Button)findViewById(R.id.include_header_btnBack);

		lnPaypal = (LinearLayout)findViewById(R.id.checkoutdetail_lnPaypal);
		ln1BillingInfomation = (LinearLayout)findViewById(R.id.checkoutdetail_ln1BillingInfomation);
		ln2ShippingInfomation = (LinearLayout)findViewById(R.id.checkoutdetail_ln2ShippingInfomation);
		ln3ShippingMethod = (LinearLayout)findViewById(R.id.checkoutdetail_ln3ShippingMethod);
		ln4PaymentInfomation = (LinearLayout)findViewById(R.id.checkoutdetail_ln4PaymentInfomation);
		ln5OrderReview = (LinearLayout)findViewById(R.id.checkoutdetail_ln5OrderReview);

		ln1BillingInfomationContent = (LinearLayout)findViewById(R.id.checkoutdetail_ln1BillingInfomationContent);
		ln2ShippingInfomationContent = (LinearLayout)findViewById(R.id.checkoutdetail_ln2ShippingInfomationContent);
		ln3ShippingMethodContent = (LinearLayout)findViewById(R.id.checkoutdetail_ln3ShippingMethodContent);
		ln4PaymentInfomationContent = (LinearLayout)findViewById(R.id.checkoutdetail_ln4PaymentInfomationContent);
		ln5OrderReviewContent = (LinearLayout)findViewById(R.id.checkoutdetail_ln5OrderReviewContent);

		btnBillingContinue = (Button)findViewById(R.id.checkoutdetail_Billing_btnContinue);
		btnShippingContinue = (Button)findViewById(R.id.checkoutdetail_Shipping_btnContinue);
		btnShippingMethodContinue = (Button)findViewById(R.id.checkoutdetail_ShippingMethod_btnContinue);
		btnPaymentContinue = (Button)findViewById(R.id.checkoutdetail_Payment_btnContinue);
		btnPlaceOrder = (Button)findViewById(R.id.checkoutdetail_btnPlaceOrder);

		tvSubtotal = (TextView)findViewById(R.id.checkoutdetail_tvSubtotal);
		tvShippingHandling = (TextView)findViewById(R.id.checkoutdetail_tvShippingHandling);
		tvTax = (TextView)findViewById(R.id.checkoutdetail_tvTax);
		tvGrandTotal = (TextView)findViewById(R.id.checkoutdetail_tvGrandTotal);
		tvEditYourCard = (TextView)findViewById(R.id.checkoutdetail_tvEditYourCart);

		spnBillingAddress = (Spinner)findViewById(R.id.checkoutdetail_spnBillingAddress);
		spnShippingAddress = (Spinner)findViewById(R.id.checkoutdetail_spnShippingAddress);

		rbnShipThisAddress = (RadioButton)findViewById(R.id.checkoutdetail_rbnShipThisAddress);
		rbnShipDifferentAddress = (RadioButton)findViewById(R.id.checkoutdetail_rbnShipDifferentAddress);		
		rbnCreditCardOnFile = (RadioButton)findViewById(R.id.checkoutdetail_rbnCreditCardOnFile);
		rbnPaypal = (RadioButton)findViewById(R.id.checkoutdetail_rbnPaypal);
		rbnCreditTerms = (RadioButton)findViewById(R.id.checkoutdetail_rbnCreditCard);

		lvParcelService = (ListView)findViewById(R.id.checkoutdetail_lvParcelService);
		lvReview = (ListView)findViewById(R.id.checkoutdetail_lvReview);

		lnCreditCardOnFileContent = (LinearLayout)findViewById(R.id.checkoutdetail_lnCreditCardOnFileContent);
		lnCreditCardContent = (LinearLayout)findViewById(R.id.checkoutdetail_lnCreditCardContent);
		tvRedirectedPaypal = (TextView)findViewById(R.id.checkoutdetail_tvRedirectedPaypal);

		spnCreditCardOnFile = (Spinner)findViewById(R.id.checkoutdetail_spnCreditCardOnFile);
		spnCreditCardType = (Spinner)findViewById(R.id.checkoutdetail_spnCreditCardType);
		spnExpirationMonth = (Spinner)findViewById(R.id.checkoutdetail_spnExpirationMonth);
		spnExpirationYear = (Spinner)findViewById(R.id.checkoutdetail_spnExpirationYear);
		edtCardVerification = (EditText)findViewById(R.id.checkoutdetail_edtCardVerification);
		tvWhatIsThis = (TextView)findViewById(R.id.checkoutdetail_tvWhatIsThis);
		edtCreditCardNumber = (EditText)findViewById(R.id.checkoutdetail_edtCreditCardNumber);		
		cbxSaveCreditCard = (CheckBox)findViewById(R.id.checkoutdetail_cbxSaveCreditCard);
		tvWhatIsPaypal = (TextView)findViewById(R.id.checkoutdetail_tvWhatIsPaypal);

		cbxUseBillingAddress = (CheckBox)findViewById(R.id.checkoutdetail_cbxUseBillingAddress);

		ivCart.setImageResource(R.drawable.ico_cart_active);

		tvTitle.setText("CHECK OUT");
		btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		btnBack.setVisibility(View.VISIBLE);

		lnHome.setOnClickListener(this);
		lnSearch.setOnClickListener(this);
		lnCategory.setOnClickListener(this);
		lnCart.setOnClickListener(this);
		lnContact.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		ln1BillingInfomation.setOnClickListener(this);
		ln2ShippingInfomation.setOnClickListener(this);
		ln3ShippingMethod.setOnClickListener(this);
		ln4PaymentInfomation.setOnClickListener(this);
		ln5OrderReview.setOnClickListener(this);
		lnPaypal.setOnClickListener(this);
		btnBillingContinue.setOnClickListener(this);
		btnShippingContinue.setOnClickListener(this);
		btnShippingMethodContinue.setOnClickListener(this);
		btnPaymentContinue.setOnClickListener(this);
		btnPlaceOrder.setOnClickListener(this);

		rbnShipThisAddress.setOnClickListener(this);
		rbnShipDifferentAddress.setOnClickListener(this);
		rbnCreditCardOnFile.setOnClickListener(this);
		rbnPaypal.setOnClickListener(this);
		rbnCreditTerms.setOnClickListener(this);
		tvEditYourCard.setOnClickListener(this);
		tvWhatIsThis.setOnClickListener(this);
		tvWhatIsPaypal.setOnClickListener(this);
	}

	private void initData() {
		Collections.sort(addresses);         
		addressesAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, addresses);
		monthAdapter = new ArrayAdapter<String>(CheckoutDetailActivity.this,R.layout.spinner_item,FileUtil.months);
		yearAdapter = new ArrayAdapter<String>(CheckoutDetailActivity.this,R.layout.spinner_item,FileUtil.years);
		cardTypeAdapter = new ArrayAdapter<String>(CheckoutDetailActivity.this,R.layout.spinner_item,FileUtil.creditCardType);
		cardTypeOnFileAdapter = new ArrayAdapter<String>(CheckoutDetailActivity.this,R.layout.spinner_item,creditCards);

		spnBillingAddress.setAdapter(addressesAdapter);
		spnShippingAddress.setAdapter(addressesAdapter);
		spnExpirationMonth.setAdapter(monthAdapter);
		spnExpirationYear.setAdapter(yearAdapter);
		spnCreditCardType.setAdapter(cardTypeAdapter);

		spnCreditCardOnFile.setAdapter(cardTypeOnFileAdapter);

		reviewCheckoutDetailAdapter = new ReviewCheckoutDetailAdapter(CheckoutDetailActivity.this, FileUtil.listRecent);
		lvReview.setAdapter(reviewCheckoutDetailAdapter);

		parcelServiceAdapter = new ParcelServiceAdapter(CheckoutDetailActivity.this, FileUtil.listCarrier);
		lvParcelService.setAdapter(parcelServiceAdapter);

		pDialog = new ProgressDialog(CheckoutDetailActivity.this);
	}

	private void initDataWebservice(){
		clearSpinnerCreditCardOnFile();
		new GetAllAddressAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this)).execute();
	}

	private void handleOtherAction(){
		spnShippingAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				positionShipping = i;
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		});
		spnBillingAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				positionBilling = i;
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		});
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		//----------------Click tab bottom--------------------
		//----------Home is clicked----------
		case R.id.include_footer_lnHome:
			Intent home = new Intent(CheckoutDetailActivity.this, HomeActivity.class);
			startActivity(home);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;		
			//----------Search is clicked----------
		case R.id.include_footer_lnSearch:
			Intent search = new Intent(CheckoutDetailActivity.this, SearchActivity.class);
			startActivity(search);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;

			//----------Category is clicked----------
		case R.id.include_footer_lnCategory:
			Intent category = new Intent(CheckoutDetailActivity.this, CategoryActivity.class);
			startActivity(category);
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;

			//----------Cart is clicked----------
		case R.id.include_footer_lnCart:

			break;

			//----------Contact is clicked----------
		case R.id.include_footer_lnContact:
			Intent contact = new Intent(CheckoutDetailActivity.this, ContactActivity.class);
			startActivity(contact);
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			break;	


		case R.id.include_header_btnLogin:
			if (btnLogin.getText().toString().trim().equals(Constants.TEXT_BUTTON_LOGIN)) {
				Intent login = new Intent(CheckoutDetailActivity.this, LoginActivity.class);
				startActivity(login);
				overridePendingTransition(R.anim.fly_in_from_top, R.anim.stay);	
			}else{
				showDialog(CheckoutDetailActivity.this,Constants.CONFIRM_LOGOUT_TITLE, Constants.CONFIRM_LOGOUT_MESSAGE);
			}
			break;	

		case R.id.include_header_btnBack:
			finish();
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;		


		case R.id.checkoutdetail_ln1BillingInfomation:
			ln1BillingInfomationContent.setVisibility(View.VISIBLE);
			ln2ShippingInfomationContent.setVisibility(View.GONE);
			ln3ShippingMethodContent.setVisibility(View.GONE);
			ln4PaymentInfomationContent.setVisibility(View.GONE);
			ln5OrderReviewContent.setVisibility(View.GONE);
			break;	

		case R.id.checkoutdetail_ln2ShippingInfomation:
			if (ln5OrderReviewContent.getVisibility() == View.VISIBLE
			|| ln4PaymentInfomationContent.getVisibility() == View.VISIBLE
			|| ln3ShippingMethodContent.getVisibility() == View.VISIBLE) {
				ln1BillingInfomationContent.setVisibility(View.GONE);
				ln2ShippingInfomationContent.setVisibility(View.VISIBLE);
				ln3ShippingMethodContent.setVisibility(View.GONE);
				ln4PaymentInfomationContent.setVisibility(View.GONE);
				ln5OrderReviewContent.setVisibility(View.GONE);	
			}

			break;
		case R.id.checkoutdetail_ln3ShippingMethod:
			if (ln5OrderReviewContent.getVisibility() == View.VISIBLE
			|| ln4PaymentInfomationContent.getVisibility() == View.VISIBLE) {
				ln1BillingInfomationContent.setVisibility(View.GONE);
				ln2ShippingInfomationContent.setVisibility(View.GONE);
				ln3ShippingMethodContent.setVisibility(View.VISIBLE);
				ln4PaymentInfomationContent.setVisibility(View.GONE);
				ln5OrderReviewContent.setVisibility(View.GONE);
			}
			break;
		case R.id.checkoutdetail_ln4PaymentInfomation:
			if (ln5OrderReviewContent.getVisibility() == View.VISIBLE
			|| ln4PaymentInfomationContent.getVisibility() == View.VISIBLE) {
				ln1BillingInfomationContent.setVisibility(View.GONE);
				ln2ShippingInfomationContent.setVisibility(View.GONE);
				ln3ShippingMethodContent.setVisibility(View.GONE);
				ln4PaymentInfomationContent.setVisibility(View.VISIBLE);
				ln5OrderReviewContent.setVisibility(View.GONE);
			}
			break;
		case R.id.checkoutdetail_ln5OrderReview:		
			break;


			//--------click radio button----------		

		case R.id.checkoutdetail_rbnShipThisAddress:
			rbnShipThisAddress.setChecked(true);
			rbnShipDifferentAddress.setChecked(false);			
			break;	
		case R.id.checkoutdetail_rbnShipDifferentAddress:
			rbnShipThisAddress.setChecked(false);
			rbnShipDifferentAddress.setChecked(true);			
			break;				
			//-----------------Click button continue---------------------------------	

		case R.id.checkoutdetail_lnPaypal:

			break;
		case R.id.checkoutdetail_Billing_btnContinue:
			if (rbnShipThisAddress.isChecked()) {
				ln1BillingInfomationContent.setVisibility(View.GONE);
				ln2ShippingInfomationContent.setVisibility(View.GONE);
				ln3ShippingMethodContent.setVisibility(View.VISIBLE);
				ln4PaymentInfomationContent.setVisibility(View.GONE);
				ln5OrderReviewContent.setVisibility(View.GONE);
				positionShipping = positionBilling;
				spnShippingAddress.setSelection(positionShipping);
				new GetShippingMethodAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this), getKeyAddress(addresses.get(positionShipping))).execute();
			} else {
				ln1BillingInfomationContent.setVisibility(View.GONE);
				ln2ShippingInfomationContent.setVisibility(View.VISIBLE);
				ln3ShippingMethodContent.setVisibility(View.GONE);
				ln4PaymentInfomationContent.setVisibility(View.GONE);
				ln5OrderReviewContent.setVisibility(View.GONE);
			}	
			
			break;
		case R.id.checkoutdetail_Shipping_btnContinue:
			if (cbxUseBillingAddress.isChecked()) {
				positionBilling = positionShipping;
				spnBillingAddress.setSelection(positionBilling);
			}
			ln1BillingInfomationContent.setVisibility(View.GONE);
			ln2ShippingInfomationContent.setVisibility(View.GONE);
			ln3ShippingMethodContent.setVisibility(View.VISIBLE);
			ln4PaymentInfomationContent.setVisibility(View.GONE);
			ln5OrderReviewContent.setVisibility(View.GONE);
			new GetShippingMethodAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this), getKeyAddress(addresses.get(positionShipping))).execute();
			break;
		case R.id.checkoutdetail_ShippingMethod_btnContinue:
			ln1BillingInfomationContent.setVisibility(View.GONE);
			ln2ShippingInfomationContent.setVisibility(View.GONE);
			ln3ShippingMethodContent.setVisibility(View.GONE);
			ln4PaymentInfomationContent.setVisibility(View.VISIBLE);
			ln5OrderReviewContent.setVisibility(View.GONE);
			break;
		case R.id.checkoutdetail_Payment_btnContinue:
			ln1BillingInfomationContent.setVisibility(View.GONE);
			ln2ShippingInfomationContent.setVisibility(View.GONE);
			ln3ShippingMethodContent.setVisibility(View.GONE);
			ln4PaymentInfomationContent.setVisibility(View.GONE);
			ln5OrderReviewContent.setVisibility(View.VISIBLE);
			break;
		case R.id.checkoutdetail_btnPlaceOrder:
			String method = Constants.METHOD_LINKPOINT;
			if (rbnPaypal.isChecked()) {
				method = Constants.METHOD_PAYPAL;
			}
			new SavePaymentAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this), "", method).execute();
			break;


		case R.id.checkoutdetail_tvEditYourCart:
			finish();
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;

		case R.id.checkoutdetail_rbnCreditCardOnFile:
			rbnCreditCardOnFile.setChecked(true);
			rbnPaypal.setChecked(false);	
			rbnCreditTerms.setChecked(false);
			tvRedirectedPaypal.setVisibility(View.GONE);
			lnCreditCardContent.setVisibility(View.GONE);
			lnCreditCardOnFileContent.setVisibility(View.VISIBLE);
			break;	

		case R.id.checkoutdetail_rbnCreditCard:
			rbnCreditCardOnFile.setChecked(false);
			rbnPaypal.setChecked(false);	
			rbnCreditTerms.setChecked(true);
			tvRedirectedPaypal.setVisibility(View.GONE);	
			lnCreditCardOnFileContent.setVisibility(View.GONE);
			lnCreditCardContent.setVisibility(View.VISIBLE);
			break;	

		case R.id.checkoutdetail_rbnPaypal:
			rbnCreditCardOnFile.setChecked(false);
			rbnPaypal.setChecked(true);	
			rbnCreditTerms.setChecked(false);
			tvRedirectedPaypal.setVisibility(View.VISIBLE);	
			lnCreditCardOnFileContent.setVisibility(View.GONE);
			lnCreditCardContent.setVisibility(View.GONE);
			break;

		case R.id.checkoutdetail_tvWhatIsThis:
			showDialogVerifyCardNumber();
			break;

		case R.id.checkoutdetail_tvWhatIsPaypal:
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_PAYPAL));
			startActivity(browserIntent);
			break;



		default:
			break;
		}

	}

	//--------------------GetAll----------------------------------------
	public class GetAllAddressAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		public GetAllAddressAsyncTask(int idCustomer){
			this.idCustomer = idCustomer;
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
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
				json = JsonParser.makeHttpRequest(Constants.URL_GETALLADDRESS, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {  
					listAddress.clear();
					addresses.clear();
					JSONArray array = new JSONArray(json);
					for (int j = 0; j < array.length(); j++) {
						Address temp = new Address();
						temp.setKey(array.getJSONObject(j).getString("key"));   
						temp.setValue(array.getJSONObject(j).getString("value"));  
						addresses.add(temp.getValue());
						listAddress.add(temp);
					}        			
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {	 
			addressesAdapter.notifyDataSetChanged();
			pDialog.dismiss();	
		}
	}

	//--------------------United Parcel Service----------------------------------------
	public class GetShippingMethodAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		String address_id;

		public GetShippingMethodAsyncTask(int idCustomer,String address_id){
			this.idCustomer = idCustomer;
			this.address_id = address_id;
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
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
				paramsUrl.add(new BasicNameValuePair("address_id", String.valueOf(address_id)));
				json = JsonParser.makeHttpRequest(Constants.URL_GETSHIPPINGMETHOD, "GET", paramsUrl);				
				if ((json != null) || (!json.equals(""))) {  
					FileUtil.listCarrier.clear();
					JSONArray array = new JSONArray(json);
					for (int j = 0; j < array.length(); j++) {
						CarrierObject temp = new CarrierObject();
						temp.setCode(array.getJSONObject(j).getString("code"));   
						temp.setPrice(array.getJSONObject(j).getString("price"));  
						temp.setTitle(array.getJSONObject(j).getString("title"));  
						FileUtil.listCarrier.add(temp);
					}        			
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {	 
			parcelServiceAdapter.notifyDataSetChanged();
			pDialog.dismiss();	
			new GetCreditCardOnFileAsyncTask(idCustomer).execute();
		}
	}

	//--------------------United Parcel Service----------------------------------------
	public class GetCreditCardOnFileAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;

		public GetCreditCardOnFileAsyncTask(int idCustomer){
			this.idCustomer = idCustomer;
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
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
				json = JsonParser.makeHttpRequest(Constants.URL_GETCREDITCARD, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {  

					JSONArray array = new JSONArray(json);
					listCreditCard.clear();
					clearSpinnerCreditCardOnFile();
					for (int j = 0; j < array.length(); j++) {
						CreditCardObject temp = new CreditCardObject();
						temp.setId(array.getJSONObject(j).getString("id"));   
						temp.setTitle(array.getJSONObject(j).getString("title"));  
						creditCards.add(temp.getTitle());
						listCreditCard.add(temp);
					}        			
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {	 
			cardTypeOnFileAdapter.notifyDataSetChanged();
			new GetCartCodeAsyncTask(idCustomer).execute();
			pDialog.dismiss();	
		}
	}

	private void showDialogVerifyCardNumber()
	{		
		dialogVerifyNumber = new Dialog(CheckoutDetailActivity.this, R.style.FullHeightDialog);
		dialogVerifyNumber.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogVerifyNumber.setContentView(R.layout.dialog_verifycardnumber);
		ivCloseDialog = (ImageView) dialogVerifyNumber.findViewById(R.id.dialog_verifycardnumber_imgClose);


		dialogVerifyNumber.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialogVerifyNumber.dismiss();

			}
		});
		ivCloseDialog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialogVerifyNumber.dismiss();

			}
		});

		dialogVerifyNumber.show();

	}

	private void clearSpinnerCreditCardOnFile(){		
		String creditCard = "--Please Select--";		
		creditCards.clear();		
		creditCards.add(creditCard);
	}
	private void showDialog(final Context mContext, String title, String msg){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(msg);

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

	private void ChangeTextButtonLogin(){
		if (SharedPreferencesUtil.getFlagLogin(CheckoutDetailActivity.this)) {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGOUT);
		} else {
			btnLogin.setText(Constants.TEXT_BUTTON_LOGIN);
		}
	}

	//--------------------GetCartCode----------------------------------------
	public class GetCartCodeAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		public GetCartCodeAsyncTask(int idCustomer){
			this.idCustomer = idCustomer;
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
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
				json = JsonParser.makeHttpRequest(
						Constants.URL_GETCARTCOST, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONObject jsonObject = new JSONObject(json);
					JSONObject jsonObjectSubtotal = jsonObject.getJSONObject("subtotal");
					JSONObject jsonObjectShipping = jsonObject.getJSONObject("shipping");
					JSONObject jsonObjectTax = jsonObject.getJSONObject("tax");
					JSONObject jsonObjectGrandTotal = jsonObject.getJSONObject("grandtotal");

					sSubtotal = jsonObjectSubtotal.getString("cost");
					sShipping = jsonObjectShipping.getString("cost");
					sTax = jsonObjectTax.getString("cost");
					sGrandTotal = jsonObjectGrandTotal.getString("cost");

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tvSubtotal.setText(sSubtotal);
							tvShippingHandling.setText(sShipping);
							tvTax.setText(sTax);
							tvGrandTotal.setText(sGrandTotal);
						}
					});

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

	//--------------------SaveShippingMethod----------------------------------------
	public class SaveShippingMethodAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		String shipping_method;
		public SaveShippingMethodAsyncTask(int idCustomer, String shipping_method){
			this.idCustomer = idCustomer;
			this.shipping_method = shipping_method;
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
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
				paramsUrl.add(new BasicNameValuePair("shipping_method", shipping_method));
				json = JsonParser.makeHttpRequest(
						Constants.URL_SAVESHIPPINGMETHOD, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {   
					JSONObject jsonObject = new JSONObject(json);
					boolean s = jsonObject.getBoolean("info");
					if (s) {
						return "true";
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String result) {	      
			pDialog.dismiss();	
			if (result.equals("true")) {
				Toast.makeText(CheckoutDetailActivity.this, "Save shipping method success!", Toast.LENGTH_SHORT).show();
			}else{
				Toast.makeText(CheckoutDetailActivity.this, "Save shipping method fail!", Toast.LENGTH_SHORT).show();
			}

		}
	}

	//--------------------SaveShippingMethod----------------------------------------
	public class SavePaymentAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		String savecc_id;
		String method;
		public SavePaymentAsyncTask(int idCustomer, String savecc_id, String method){
			this.idCustomer = idCustomer;
			this.savecc_id = savecc_id;
			this.method = method;
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
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
				paramsUrl.add(new BasicNameValuePair("savecc_id", savecc_id));
				paramsUrl.add(new BasicNameValuePair("method", method));
				json = JsonParser.makeHttpRequest(
						Constants.URL_SAVEPAYMENT, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {   
					JSONObject jsonObject = new JSONObject(json);
					boolean s = jsonObject.getBoolean("info");
				
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String result) {	      
			pDialog.dismiss();	
//			if (result.equals("true")) {
//				Toast.makeText(CheckoutDetailActivity.this, "Save shipping method success!", Toast.LENGTH_SHORT).show();
//			}else{
//				Toast.makeText(CheckoutDetailActivity.this, "Save shipping method fail!", Toast.LENGTH_SHORT).show();
//			}

		}
	}

	private String getKeyAddress(String address){
		for (int i = 0; i < listAddress.size(); i++) {
			if (listAddress.get(i).getValue().equals(address)) {
				return listAddress.get(i).getKey();
			}
		}
		return "";
	}
}
