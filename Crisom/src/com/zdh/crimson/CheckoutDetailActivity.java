package com.zdh.crimson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
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
import com.zdh.crimson.utility.ExpandableHeightListView;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;

public class CheckoutDetailActivity extends BaseActivity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnContact,lnPaypal
	,ln1BillingInfomation,ln2ShippingInfomation,ln3ShippingMethod,ln4PaymentInfomation,ln5OrderReview
	,ln1BillingInfomationContent,ln2ShippingInfomationContent,ln3ShippingMethodContent,ln4PaymentInfomationContent,ln5OrderReviewContent
	,lnCreditCardOnFileContent, lnCreditCardContent;
	private ImageView ivCart;	
	private Button btnBillingContinue,btnShippingContinue,btnShippingMethodContinue,btnPaymentContinue,btnPlaceOrder;
	private TextView tvTitle,tvSubtotal,tvShippingHandling,tvTax,tvGrandTotal,tvEditYourCard,tvRedirectedPaypal
	,tvWhatIsThis,tvWhatIsPaypal;

	Spinner spnBillingAddress,spnShippingAddress,spnCreditCardOnFile,spnCreditCardType,spnExpirationMonth,spnExpirationYear;
	RadioButton rbnShipThisAddress,rbnShipDifferentAddress,rbnCreditCardOnFile,rbnPaypal,rbnCreditCard;
	CheckBox cbxUseBillingAddress,cbxSaveCreditCard;
	ListView lvParcelService;
	private ExpandableHeightListView lvReview;
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
	int positionCreditCard = 0;
	int positionCreditCardOnFile = 0;
	int positionMonth = 0;
	int positionYear = 0;

	String savecc_id = "";
	String current_year= "";
	String current_month= "";

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
		rbnCreditCard = (RadioButton)findViewById(R.id.checkoutdetail_rbnCreditCard);

		lvParcelService = (ListView)findViewById(R.id.checkoutdetail_lvParcelService);
		lvReview = (ExpandableHeightListView)findViewById(R.id.checkoutdetail_lvReview);
		

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
		rbnCreditCard.setOnClickListener(this);
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
		lvReview.setExpanded(true);
		

		parcelServiceAdapter = new ParcelServiceAdapter(CheckoutDetailActivity.this, FileUtil.listCarrier);
		lvParcelService.setAdapter(parcelServiceAdapter);

		pDialog = new ProgressDialog(CheckoutDetailActivity.this);
	}

	private void initDataWebservice(){
		clearSpinnerCreditCardOnFile();
		new GetAllAddressAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this)).execute();
	}

	private void handleOtherAction(){

		//		spnCreditCardOnFile,spnCreditCardType,spnExpirationMonth,spnExpirationYear
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

		spnCreditCardOnFile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
				if (listCreditCard.size() > 0) {
					if (i != 0) {
						savecc_id = listCreditCard.get(i).getId();
						positionCreditCardOnFile = i;
					}
				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		});
		spnCreditCardType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				positionCreditCard = i;
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		});
		spnExpirationMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				if (i != 0) {
					current_month = FileUtil.months[i];
					positionMonth = i;
				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		});
		spnExpirationYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				if (i != 0) {
					current_year = FileUtil.years[i];
					positionYear = i;
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
			new SaveShippingMethodAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this),FileUtil.codeRadioButtonShippingMethod).execute();

			ln1BillingInfomationContent.setVisibility(View.GONE);
			ln2ShippingInfomationContent.setVisibility(View.GONE);
			ln3ShippingMethodContent.setVisibility(View.GONE);
			ln4PaymentInfomationContent.setVisibility(View.VISIBLE);
			ln5OrderReviewContent.setVisibility(View.GONE);

			break;
		case R.id.checkoutdetail_Payment_btnContinue:
			if (rbnCreditCardOnFile.isChecked()) {
				if (positionShipping == 0) {
					Toast.makeText(CheckoutDetailActivity.this, "Please select select Credit Card Type!", Toast.LENGTH_SHORT).show();
				}else{
					new GetCartCodeAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this)).execute();
					ln1BillingInfomationContent.setVisibility(View.GONE);
					ln2ShippingInfomationContent.setVisibility(View.GONE);
					ln3ShippingMethodContent.setVisibility(View.GONE);
					ln4PaymentInfomationContent.setVisibility(View.GONE);
					ln5OrderReviewContent.setVisibility(View.VISIBLE);
				}
			}
			else{
				if (rbnCreditCard.isChecked()) {
					if (checkInputDataCreditCard()) {
						new GetCartCodeAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this)).execute();
						ln1BillingInfomationContent.setVisibility(View.GONE);
						ln2ShippingInfomationContent.setVisibility(View.GONE);
						ln3ShippingMethodContent.setVisibility(View.GONE);
						ln4PaymentInfomationContent.setVisibility(View.GONE);
						ln5OrderReviewContent.setVisibility(View.VISIBLE);
					}
				}else{
					new GetCartCodeAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this)).execute();
					ln1BillingInfomationContent.setVisibility(View.GONE);
					ln2ShippingInfomationContent.setVisibility(View.GONE);
					ln3ShippingMethodContent.setVisibility(View.GONE);
					ln4PaymentInfomationContent.setVisibility(View.GONE);
					ln5OrderReviewContent.setVisibility(View.VISIBLE);
				}
			}


			break;
		case R.id.checkoutdetail_btnPlaceOrder:
			String method = Constants.METHOD_LINKPOINT;
			if (rbnPaypal.isChecked()) {
				method = Constants.METHOD_PAYPAL;
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.URL_PAYPAL_CART+SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this)));
				startActivity(browserIntent);
				overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
			}else{
				if (rbnCreditCardOnFile.isChecked()) {
					new SubmitOrderOnFileAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this), savecc_id, method).execute();
				}else{

					int idCustomer = SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this);
					String cc_type = FileUtil.creditCardTypeID[positionCreditCard];
					String cc_number = edtCreditCardNumber.getText().toString().trim();
					String cc_exp_month = String.valueOf(positionMonth);
					String cc_exp_year = String.valueOf(positionYear + 2013);
					String cc_cid = edtCardVerification.getText().toString().trim();		

					new SubmitOrderAsyncTask(idCustomer,method,cc_type,cc_number,cc_exp_month,cc_exp_year,cc_cid).execute();
				}
			}
			break;


		case R.id.checkoutdetail_tvEditYourCart:
			finish();
			overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);
			break;

		case R.id.checkoutdetail_rbnCreditCardOnFile:
			rbnCreditCardOnFile.setChecked(true);
			rbnPaypal.setChecked(false);	
			rbnCreditCard.setChecked(false);
			tvRedirectedPaypal.setVisibility(View.GONE);
			lnCreditCardContent.setVisibility(View.GONE);
			lnCreditCardOnFileContent.setVisibility(View.VISIBLE);
			break;	

		case R.id.checkoutdetail_rbnCreditCard:
			rbnCreditCardOnFile.setChecked(false);
			rbnPaypal.setChecked(false);	
			rbnCreditCard.setChecked(true);
			tvRedirectedPaypal.setVisibility(View.GONE);	
			lnCreditCardOnFileContent.setVisibility(View.GONE);
			lnCreditCardContent.setVisibility(View.VISIBLE);
			break;	

		case R.id.checkoutdetail_rbnPaypal:
			rbnCreditCardOnFile.setChecked(false);
			rbnPaypal.setChecked(true);	
			rbnCreditCard.setChecked(false);
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
			overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
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
			if (FileUtil.listCarrier.size() > 0) {
				FileUtil.codeRadioButtonShippingMethod = FileUtil.listCarrier.get(0).getCode();
			}			
			pDialog.dismiss();	
			new GetCreditCardOnFileAsyncTask(idCustomer).execute();
		}
	}

	//--------------------GetCreditCardOnFileAsyncTask----------------------------------------
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
					Log.d("json", json);
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

	//--------------------SubmitOrderOnFileAsyncTask----------------------------------------
	public class SubmitOrderOnFileAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		String savecc_id;
		String method;
		public SubmitOrderOnFileAsyncTask(int idCustomer, String savecc_id, String method){
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
				if ((json != null) || (!json.equals(""))) 
				{   
					Log.d("json", json);
					JSONObject jsonObject = new JSONObject(json);
					boolean b = jsonObject.getBoolean("info");
					if (!b) {
						return jsonObject.getString("message");
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return "";
		}

		protected void onPostExecute(String result) {	      
			pDialog.dismiss();	
			if (result.equals("")) {
				Toast.makeText(CheckoutDetailActivity.this, "Your order has been received.\nThank you for your purchase!", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(CheckoutDetailActivity.this, HomeActivity.class);
				startActivity(intent);
				FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_HOME;
				overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);	
			}else{
				Toast.makeText(CheckoutDetailActivity.this, result, Toast.LENGTH_LONG).show();
			}
		}
	}

	//--------------------SubmitOrderAsyncTask----------------------------------------
	public class SubmitOrderAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		String method;
		String cc_type;
		String cc_number;
		String cc_exp_month;
		String cc_exp_year;
		String cc_cid;

		public SubmitOrderAsyncTask(int idCustomer, String method, String cc_type, String cc_number, String cc_exp_month, String cc_exp_year, String cc_cid){
			this.idCustomer = idCustomer;
			this.method = method;
			this.cc_type = cc_type;
			this.cc_number = cc_number;
			this.cc_exp_month = cc_exp_month;
			this.cc_exp_year = cc_exp_year;
			this.cc_cid = cc_cid;
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
				paramsUrl.add(new BasicNameValuePair("method", method));
				paramsUrl.add(new BasicNameValuePair("cc_type", cc_type));
				paramsUrl.add(new BasicNameValuePair("cc_number", cc_number));
				paramsUrl.add(new BasicNameValuePair("cc_exp_month", cc_exp_month));
				paramsUrl.add(new BasicNameValuePair("cc_exp_year", cc_exp_year));
				paramsUrl.add(new BasicNameValuePair("cc_cid", cc_cid));

				json = JsonParser.makeHttpRequest(
						Constants.URL_SAVEPAYMENT, "POST", paramsUrl);
				if ((json != null) || (!json.equals(""))) 
				{   
					Log.d("json", json);
					JSONObject jsonObject = new JSONObject(json);
					boolean b = jsonObject.getBoolean("info");
					if (!b) {
						return jsonObject.getString("message");
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return "";
		}

		protected void onPostExecute(String result) {	      
			pDialog.dismiss();	
			if (result.equals("")) {
				Toast.makeText(CheckoutDetailActivity.this, "Your order has been received.\nThank you for your purchase!", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(CheckoutDetailActivity.this, HomeActivity.class);
				startActivity(intent);
				FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_HOME;
				overridePendingTransition(R.anim.fly_in_from_left, R.anim.fly_out_to_right);	
			}else{
				Toast.makeText(CheckoutDetailActivity.this, result, Toast.LENGTH_LONG).show();
			}
		}
	}

	private String getKeyAddress(String address){
		if (listAddress.size() > 0 ) {
			for (int i = 0; i < listAddress.size(); i++) {

				if (listAddress.get(i).getValue().equals(address)) {
					return listAddress.get(i).getKey();
				}

			}
		}
		return "";
	}

	private boolean checkInputDataCreditCard(){
		if (positionCreditCard == 0) {
			Toast.makeText(CheckoutDetailActivity.this, "Please select Credit Card Type!", Toast.LENGTH_SHORT).show();
			return false;
		}
		//		if (positionMonth == 0) {
		//			Toast.makeText(CheckoutDetailActivity.this, "Please select Month!", Toast.LENGTH_SHORT).show();
		//			return false;
		//		}
		//		if (positionYear == 0) {
		//			Toast.makeText(CheckoutDetailActivity.this, "Please select Year!", Toast.LENGTH_SHORT).show();
		//			return false;
		//		}
		if (edtCreditCardNumber.getText().toString().trim().equals("")) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input Credit Card Number!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (edtCardVerification.getText().toString().trim().equals("")) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input Card Verification Number!", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
}
