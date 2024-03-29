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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zdh.crimson.adapter.ParcelServiceAdapter;
import com.zdh.crimson.adapter.ReviewCheckoutDetailAdapter;
import com.zdh.crimson.model.Address;
import com.zdh.crimson.model.CarrierObject;
import com.zdh.crimson.model.CountryObject;
import com.zdh.crimson.model.CreditCardObject;
import com.zdh.crimson.model.StateObject;
import com.zdh.crimson.utility.Constants;
import com.zdh.crimson.utility.ExpandableHeightListView;
import com.zdh.crimson.utility.FileUtil;
import com.zdh.crimson.utility.JsonParser;
import com.zdh.crimson.utility.SharedPreferencesUtil;
import com.zdh.crimson.utility.StackActivity;

public class CheckoutDetailActivity extends BaseActivity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnContact,lnPaypal
	,ln1BillingInfomation,ln2ShippingInfomation,ln3ShippingMethod,ln4PaymentInfomation,ln5OrderReview
	,ln1BillingInfomationContent,ln2ShippingInfomationContent,ln3ShippingMethodContent,ln4PaymentInfomationContent,ln5OrderReviewContent
	,lnCreditCardOnFileContent, lnCreditCardContent;
	private ImageView ivCart,ivPaypal;	
	private Button btnBillingContinue,btnShippingContinue,btnShippingMethodContinue,btnPaymentContinue,btnPlaceOrder;
	private TextView tvTitle,tvSubtotal,tvShippingHandling,tvTax,tvGrandTotal,tvDiscount,tvEditYourCard,tvRedirectedPaypal
	,tvWhatIsThis,tvWhatIsPaypal,tvShipThisAddress,tvShipDifferentAddress,tvCreditCardOnFile,tvCreditCard
	,tvUseBillingAddress,tvSaveCreditCard;

	private EditText edtFirstName2,edtLastName2,edtCompany2,edtAddress2,edtCity2,edtPostalCode2,edtTelephone2,edtFax2,edtPo2,edtNotes2,edtState2;	
	private EditText edtFirstName1,edtLastName1,edtCompany1,edtAddress1,edtCity1,edtPostalCode1,edtTelephone1,edtFax1,edtPo1,edtNotes1,edtState1;
	private Spinner spnCountry2,spnState2,spnCountry1,spnState1;
	private LinearLayout lnNewAddress1, lnNewAddress2;
	LinearLayout lnSaveAddress1,lnSaveAddress2, lnUseBillingAddress;

	Spinner spnBillingAddress,spnShippingAddress,spnCreditCardOnFile,spnCreditCardType,spnExpirationMonth,spnExpirationYear;
	RadioButton rbnShipThisAddress,rbnShipDifferentAddress,rbnCreditCardOnFile,rbnPaypal,rbnCreditCard;
	CheckBox cbxUseBillingAddress,cbxSaveCreditCard,cbxSaveAddress,cbxSaveAddressBook1,cbxSaveAddressBook2;
	ExpandableHeightListView lvParcelService;
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

	int positionState1 = 0 ;
	int positionCountry1 = 0 ;
	int positionState2 = 0 ;
	int positionCountry2 = 0 ;

	ArrayAdapter<String> countriesAdapter;
	ArrayAdapter<String> statesAdapter;

	boolean flagSaveShipping = false;

	String savecc_id = "";

	String sSubtotal = "";
	String sShipping = "";
	String sTax = "";
	String sGrandTotal = "";
	String sDiscount = "";

	String orderID = "";


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
		if(!SharedPreferencesUtil.getFlagLogin(getApplicationContext())){
			this.finish();
		}else{
			reviewCheckoutDetailAdapter.notifyDataSetChanged();
		}
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
		tvDiscount = (TextView)findViewById(R.id.checkoutdetail_tvDiscount);
		tvEditYourCard = (TextView)findViewById(R.id.checkoutdetail_tvEditYourCart);

		spnBillingAddress = (Spinner)findViewById(R.id.checkoutdetail_spnBillingAddress);
		spnShippingAddress = (Spinner)findViewById(R.id.checkoutdetail_spnShippingAddress);

		rbnShipThisAddress = (RadioButton)findViewById(R.id.checkoutdetail_rbnShipThisAddress);
		rbnShipDifferentAddress = (RadioButton)findViewById(R.id.checkoutdetail_rbnShipDifferentAddress);		
		rbnCreditCardOnFile = (RadioButton)findViewById(R.id.checkoutdetail_rbnCreditCardOnFile);
		rbnPaypal = (RadioButton)findViewById(R.id.checkoutdetail_rbnPaypal);
		rbnCreditCard = (RadioButton)findViewById(R.id.checkoutdetail_rbnCreditCard);

		tvShipThisAddress = (TextView)findViewById(R.id.checkoutdetail_tvShipThisAddress);
		tvShipDifferentAddress = (TextView)findViewById(R.id.checkoutdetail_tvShipDifferentAddress);
		tvCreditCardOnFile = (TextView)findViewById(R.id.checkoutdetail_tvCreditCardOnFile);
		tvCreditCard = (TextView)findViewById(R.id.checkoutdetail_tvCreditCard);
		ivPaypal = (ImageView)findViewById(R.id.checkoutdetail_ivPaypal);

		lvParcelService = (ExpandableHeightListView)findViewById(R.id.checkoutdetail_lvParcelService);
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

		tvUseBillingAddress  = (TextView)findViewById(R.id.checkoutdetail_tvUseBillingAddress);
		tvSaveCreditCard  = (TextView)findViewById(R.id.checkoutdetail_tvSaveCreditCard);

		edtFirstName2 = (EditText)findViewById(R.id.checkoutdetail_edtFirstName2);
		edtLastName2 = (EditText)findViewById(R.id.checkoutdetail_edtLastName2);
		edtCompany2 = (EditText)findViewById(R.id.checkoutdetail_edtCompany2);
		edtAddress2 = (EditText)findViewById(R.id.checkoutdetail_edtAddress2);
		edtCity2 = (EditText)findViewById(R.id.checkoutdetail_edtCity2);
		edtTelephone2 = (EditText)findViewById(R.id.checkoutdetail_edtTelephone2);
		edtPostalCode2 = (EditText)findViewById(R.id.checkoutdetail_edtPostalCode2);
		edtFax2 = (EditText)findViewById(R.id.checkoutdetail_edtFax2);
		edtPo2 = (EditText)findViewById(R.id.checkoutdetail_edtPO2);
		edtNotes2 = (EditText)findViewById(R.id.checkoutdetail_edtNotes2);
		spnCountry2 = (Spinner)findViewById(R.id.checkoutdetail_spnCountry2);
		spnState2 = (Spinner)findViewById(R.id.checkoutdetail_spnState2);
		edtState2 = (EditText)findViewById(R.id.checkoutdetail_edtState2);
		lnSaveAddress2 = (LinearLayout)findViewById(R.id.checkoutdetail_lnSaveAddressBook2);
		lnNewAddress2 = (LinearLayout)findViewById(R.id.checkoutdetail_lnNewAddress2);		
		lnUseBillingAddress = (LinearLayout)findViewById(R.id.checkoutdetail_lnUseBillingAddress);
		cbxSaveAddressBook2 = (CheckBox)findViewById(R.id.checkoutdetail_cbxSaveAddressBook2);


		edtFirstName1 = (EditText)findViewById(R.id.checkoutdetail_edtFirstName1);
		edtLastName1= (EditText)findViewById(R.id.checkoutdetail_edtLastName1);
		edtCompany1 = (EditText)findViewById(R.id.checkoutdetail_edtCompany1);
		edtAddress1 = (EditText)findViewById(R.id.checkoutdetail_edtAddress1);
		edtCity1 = (EditText)findViewById(R.id.checkoutdetail_edtCity1);
		edtTelephone1 = (EditText)findViewById(R.id.checkoutdetail_edtTelephone1);
		edtPostalCode1 = (EditText)findViewById(R.id.checkoutdetail_edtPostalCode1);
		edtFax1 = (EditText)findViewById(R.id.checkoutdetail_edtFax1);
		edtPo1 = (EditText)findViewById(R.id.checkoutdetail_edtPO1);
		edtNotes1 = (EditText)findViewById(R.id.checkoutdetail_edtNotes1);
		spnCountry1 = (Spinner)findViewById(R.id.checkoutdetail_spnCountry1);
		spnState1 = (Spinner)findViewById(R.id.checkoutdetail_spnState1);
		edtState1 = (EditText)findViewById(R.id.checkoutdetail_edtState1);
		lnSaveAddress1 = (LinearLayout)findViewById(R.id.checkoutdetail_lnSaveAddressBook1);		
		lnNewAddress1 = (LinearLayout)findViewById(R.id.checkoutdetail_lnNewAddress1);
		cbxSaveAddressBook1 = (CheckBox)findViewById(R.id.checkoutdetail_cbxSaveAddressBook1);

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

		tvShipThisAddress.setOnClickListener(this);
		tvShipDifferentAddress.setOnClickListener(this);
		tvCreditCardOnFile.setOnClickListener(this);
		tvCreditCard.setOnClickListener(this);
		ivPaypal.setOnClickListener(this);
		tvUseBillingAddress.setOnClickListener(this);
		tvSaveCreditCard.setOnClickListener(this);

		lnSaveAddress1.setOnClickListener(this);
		lnSaveAddress2.setOnClickListener(this);
		lnUseBillingAddress.setOnClickListener(this);

	}

	private void initData() {
		Collections.sort(addresses);   		

		countriesAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, FileUtil.countries);
		spnCountry1.setAdapter(countriesAdapter);
		spnCountry2.setAdapter(countriesAdapter);

		statesAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, FileUtil.states);
		spnState1.setAdapter(statesAdapter);
		spnState2.setAdapter(statesAdapter);

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

		reviewCheckoutDetailAdapter = new ReviewCheckoutDetailAdapter(CheckoutDetailActivity.this, FileUtil.listCart);
		lvReview.setAdapter(reviewCheckoutDetailAdapter);		
		lvReview.setExpanded(true);


		parcelServiceAdapter = new ParcelServiceAdapter(CheckoutDetailActivity.this, FileUtil.listCarrier);
		lvParcelService.setAdapter(parcelServiceAdapter);
		lvParcelService.setExpanded(true);

		pDialog = new ProgressDialog(CheckoutDetailActivity.this);
		if (FileUtil.listCountry.size() > 0) {
			setPositionSpinnerCountry();
		}		
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
				if (i == addresses.size() - 1) {
					lnNewAddress2.setVisibility(View.VISIBLE);
					if (FileUtil.listCountry.size() <= 0) {
						new GetCountryAsyncTask().execute();
					}
				}else{
					lnNewAddress2.setVisibility(View.GONE);
				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		});
		spnBillingAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				positionBilling = i;
				if (i == addresses.size() - 1) {
					lnNewAddress1.setVisibility(View.VISIBLE);
					if (FileUtil.listCountry.size() <= 0) {
						new GetCountryAsyncTask().execute();
					}					
				}else{
					lnNewAddress1.setVisibility(View.GONE);
				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		});

		spnCreditCardOnFile.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 
				if (listCreditCard.size() > 0) {
					if (i != 0) {
						savecc_id = listCreditCard.get(i-1).getId();
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
					positionYear = i;
				}
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		});

		spnCountry1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 	
				if (checkValid(FileUtil.listCountry.get(i).getCode())) {
					new GetStateAsyncTask(FileUtil.listCountry.get(i).getCode()).execute();
					positionCountry1 = i;
					spnState1.setVisibility(View.VISIBLE);
					edtState1.setVisibility(View.GONE);
				}else{
					spnState1.setVisibility(View.GONE);
					edtState1.setVisibility(View.VISIBLE);
				}				

			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 

		spnState1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				positionState1 = i;
			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 

		spnCountry2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 	
				if (FileUtil.listCountry.size() > 0) {
					if (checkValid(FileUtil.listCountry.get(i).getCode())) {
						new GetStateAsyncTask(FileUtil.listCountry.get(i).getCode()).execute();
						positionCountry2 = i;
						spnState2.setVisibility(View.VISIBLE);
						edtState2.setVisibility(View.GONE);
					}else{
						spnState2.setVisibility(View.GONE);
						edtState2.setVisibility(View.VISIBLE);
					}


				}

			} 

			public void onNothingSelected(AdapterView<?> adapterView) {
				return;
			} 
		}); 

		spnState2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { 		    
				positionState2 = i;
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
			if (positionBilling == addresses.size() - 1) {
				flagSaveShipping = true;
			}
			if (rbnShipThisAddress.isChecked()) {
				if (cbxSaveAddressBook1.isChecked()) {
					if (checkRequireFieldBilling()) {
						callSaveBilling();
						clickContinue1();
						positionShipping = positionBilling;
						spnShippingAddress.setSelection(positionBilling);
					}

				}else{
					clickContinue1();
					positionShipping = positionBilling;
					spnShippingAddress.setSelection(positionBilling);
					callSaveBillingExist();
					//					new GetShippingMethodAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this), getKeyAddress(addresses.get(positionShipping))).execute();
				}

			} else {
				if (cbxSaveAddressBook1.isChecked()) {
					if (checkRequireFieldBilling()) {
						callSaveBilling();
						clickContinue1DifferntAddress();
					}

				}else{
					clickContinue1DifferntAddress();
					callSaveBillingExist();
					//					new GetShippingMethodAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this), getKeyAddress(addresses.get(positionShipping))).execute();
				}

			}	

			break;
		case R.id.checkoutdetail_Shipping_btnContinue:
			if (positionShipping == addresses.size() - 1) {
				flagSaveShipping = true;
			}
			if (cbxSaveAddressBook2.isChecked()) {
				if (checkRequireFieldShipping()) {
					callSaveShipping();
					if (cbxUseBillingAddress.isChecked()) {
						positionBilling = positionShipping;
						spnBillingAddress.setSelection(positionBilling);
					}
					clickContinue2();

				}
			}else{
				if (cbxUseBillingAddress.isChecked()) {
					positionBilling = positionShipping;
					spnBillingAddress.setSelection(positionBilling);
				}
				clickContinue2();
				callSaveShippingExist();
				//				new GetShippingMethodAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this), getKeyAddress(addresses.get(positionShipping))).execute();
			}


			break;
		case R.id.checkoutdetail_ShippingMethod_btnContinue:
			new SaveShippingMethodAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this),FileUtil.listCarrier.get(FileUtil.selectedIndexUPS).getCode()).execute();

			clickContinue3();


			break;
		case R.id.checkoutdetail_Payment_btnContinue:
			new GetCartCodeAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this)).execute();
			if (rbnCreditCardOnFile.isChecked()) {
				if (positionCreditCardOnFile == 0) {
					Toast.makeText(CheckoutDetailActivity.this, "Please select Credit Card Type!", Toast.LENGTH_SHORT).show();
				}else{					
					clickContinue4();

				}
			}
			else{
				if (rbnCreditCard.isChecked()) {
					if (checkInputDataCreditCard()) {						
						clickContinue4();
					}
				}else{					
					clickContinue4();

				}
			}


			break;
		case R.id.checkoutdetail_btnPlaceOrder:
			String method = Constants.METHOD_LINKPOINT;
			if (rbnPaypal.isChecked()) {
				method = Constants.METHOD_PAYPAL;
				new ValidatePaypalAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this)).execute();
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
					String sSaveCreditCard = "0";
					if (cbxSaveCreditCard.isChecked()) {
						sSaveCreditCard = "1";
					}
					new SubmitOrderAsyncTask(idCustomer,method,cc_type,cc_number,cc_exp_month,cc_exp_year,cc_cid,sSaveCreditCard).execute();
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

		case R.id.checkoutdetail_tvShipThisAddress:
			rbnShipThisAddress.setChecked(true);
			rbnShipDifferentAddress.setChecked(false);	
			break;

		case R.id.checkoutdetail_tvShipDifferentAddress:
			rbnShipThisAddress.setChecked(false);
			rbnShipDifferentAddress.setChecked(true);	
			break;

		case R.id.checkoutdetail_tvCreditCardOnFile:
			rbnCreditCardOnFile.setChecked(true);
			rbnPaypal.setChecked(false);	
			rbnCreditCard.setChecked(false);
			tvRedirectedPaypal.setVisibility(View.GONE);
			lnCreditCardContent.setVisibility(View.GONE);
			lnCreditCardOnFileContent.setVisibility(View.VISIBLE);
			break;

		case R.id.checkoutdetail_tvCreditCard:
			rbnCreditCardOnFile.setChecked(false);
			rbnPaypal.setChecked(false);	
			rbnCreditCard.setChecked(true);
			tvRedirectedPaypal.setVisibility(View.GONE);	
			lnCreditCardOnFileContent.setVisibility(View.GONE);
			lnCreditCardContent.setVisibility(View.VISIBLE);
			break;

		case R.id.checkoutdetail_ivPaypal:
			rbnCreditCardOnFile.setChecked(false);
			rbnPaypal.setChecked(true);	
			rbnCreditCard.setChecked(false);
			tvRedirectedPaypal.setVisibility(View.VISIBLE);	
			lnCreditCardOnFileContent.setVisibility(View.GONE);
			lnCreditCardContent.setVisibility(View.GONE);
			break;

		case R.id.checkoutdetail_tvUseBillingAddress:
			if (cbxUseBillingAddress.isChecked()) {
				cbxUseBillingAddress.setChecked(false);
			} else {
				cbxUseBillingAddress.setChecked(true);
			}
			break;

		case R.id.checkoutdetail_tvSaveCreditCard:
			if (cbxSaveCreditCard.isChecked()) {
				cbxSaveCreditCard.setChecked(false);
			} else {
				cbxSaveCreditCard.setChecked(true);
			}
			break;
		case R.id.checkoutdetail_lnSaveAddressBook2:

			break;
		case R.id.checkoutdetail_lnUseBillingAddress:

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
				pDialog.setCancelable(false);
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
					for (int i = 0; i < array.length(); i++) {
						Address temp = new Address();
						temp.setKey(array.getJSONObject(i).getString("key"));   
						temp.setValue(array.getJSONObject(i).getString("value")); 
						if (array.getJSONObject(i).getInt("default") == 1) {
							positionBilling = i;
							positionShipping = i;
						}

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
			spnBillingAddress.setSelection(positionBilling);
			spnShippingAddress.setSelection(positionShipping);
			addNewAddressSpinner();
			addressesAdapter.notifyDataSetChanged();
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}
		}
	}

	//--------------------United Parcel Service----------------------------------------
	public class GetShippingMethodAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;

		public GetShippingMethodAsyncTask(int idCustomer){
			this.idCustomer = idCustomer;
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
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));							
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
//				FileUtil.codeRadioButtonShippingMethod = FileUtil.listCarrier.get(0).getCode();
			}			
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}
			//			new GetCreditCardOnFileAsyncTask(idCustomer).execute();
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
				pDialog.setCancelable(false);
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
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}
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
				pDialog.setCancelable(false);
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
					JSONObject jsonObjectDiscount = jsonObject.getJSONObject("discount");

					sSubtotal = jsonObjectSubtotal.getString("cost");
					sShipping = jsonObjectShipping.getString("cost");
					sTax = jsonObjectTax.getString("cost");
					sGrandTotal = jsonObjectGrandTotal.getString("cost");
					sDiscount = jsonObjectDiscount.getString("cost");

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							tvSubtotal.setText(sSubtotal);
							tvShippingHandling.setText(sShipping);
							tvTax.setText(sTax);
							tvGrandTotal.setText(sGrandTotal);
							tvDiscount.setText(sDiscount);
						}
					});

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
				pDialog.setCancelable(false);
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

			return "false";
		}

		protected void onPostExecute(String result) {	      
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}
			if (result.equals("true")) {
				Toast.makeText(CheckoutDetailActivity.this, "Save shipping method success!", Toast.LENGTH_SHORT).show();				
			}else{
				Toast.makeText(CheckoutDetailActivity.this, "Save shipping method fail!", Toast.LENGTH_SHORT).show();
			}
			new GetCreditCardOnFileAsyncTask(idCustomer).execute();

		}
	}

	//--------------------SaveShippingAddress----------------------------------------
	public class SaveShippingAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		String firstname = "",lastname,company,street1,street2,city,region,postcode,country_id,telephone
				,fax,use_for_shipping,address_id;
		public SaveShippingAsyncTask(int idCustomer, String firstname, String lastname, String company
				, String street1, String street2, String city, String region, String postcode, String country_id
				, String telephone, String fax, String use_for_shipping){
			this.idCustomer = idCustomer;
			this.firstname = firstname;
			this.lastname = lastname;
			this.company = company;
			this.street1 = street1;
			this.street2 = street2;
			this.city = city;
			this.region = region;
			this.postcode = postcode;
			this.country_id = country_id;
			this.telephone = telephone;
			this.fax = fax;
			this.use_for_shipping = use_for_shipping;
		}

		public SaveShippingAsyncTask(int idCustomer, String use_for_shipping, String address_id){
			this.idCustomer = idCustomer;
			this.use_for_shipping = use_for_shipping;
			this.address_id = address_id;
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
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));				
				paramsUrl.add(new BasicNameValuePair("use_for_shipping", use_for_shipping));
				if (!firstname.equals("")) {
					paramsUrl.add(new BasicNameValuePair("firstname", firstname));
					paramsUrl.add(new BasicNameValuePair("lastname", lastname));
					paramsUrl.add(new BasicNameValuePair("company", company));
					paramsUrl.add(new BasicNameValuePair("street1", street1));
					paramsUrl.add(new BasicNameValuePair("street2", street2));
					paramsUrl.add(new BasicNameValuePair("city", city));
					paramsUrl.add(new BasicNameValuePair("region", region));
					paramsUrl.add(new BasicNameValuePair("postcode", postcode));
					paramsUrl.add(new BasicNameValuePair("country_id", country_id));
					paramsUrl.add(new BasicNameValuePair("telephone", telephone));
					paramsUrl.add(new BasicNameValuePair("fax", fax));
					if (cbxSaveAddressBook2.isChecked()) {
						paramsUrl.add(new BasicNameValuePair("save_in_address_book", "1"));
					}	
				}else{
					paramsUrl.add(new BasicNameValuePair("address_id", address_id));
				}

				json = JsonParser.makeHttpRequest(
						Constants.URL_SAVESHIPPING, "GET", paramsUrl);
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

			return "";
		}

		protected void onPostExecute(String result) {	      
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}	
			if (result.equals("true")) {
				Toast.makeText(CheckoutDetailActivity.this, "Save shipping address success!", Toast.LENGTH_SHORT).show();				
			}else{
				Toast.makeText(CheckoutDetailActivity.this, "Save shipping address fail!", Toast.LENGTH_SHORT).show();
			}
			new GetShippingMethodAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this)).execute();
		}
	}

	//--------------------SaveShippingAddress----------------------------------------
	public class SaveBillingAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;
		String firstname = "",lastname,company,street1,street2,city,region,postcode,country_id,telephone
				,fax,use_for_shipping,address_id;
		public SaveBillingAsyncTask(int idCustomer, String firstname, String lastname, String company
				, String street1, String street2, String city, String region, String postcode, String country_id
				, String telephone, String fax, String use_for_shipping){
			this.idCustomer = idCustomer;
			this.firstname = firstname;
			this.lastname = lastname;
			this.company = company;
			this.street1 = street1;
			this.street2 = street2;
			this.city = city;
			this.region = region;
			this.postcode = postcode;
			this.country_id = country_id;
			this.telephone = telephone;
			this.fax = fax;
			this.use_for_shipping = use_for_shipping;
		}

		public SaveBillingAsyncTask(int idCustomer, String use_for_shipping, String address_id){
			this.idCustomer = idCustomer;
			this.use_for_shipping = use_for_shipping;
			this.address_id = address_id;
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
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));				
				paramsUrl.add(new BasicNameValuePair("use_for_shipping", use_for_shipping));
				if (!firstname.equals("")) {
					paramsUrl.add(new BasicNameValuePair("firstname", firstname));
					paramsUrl.add(new BasicNameValuePair("lastname", lastname));
					paramsUrl.add(new BasicNameValuePair("company", company));
					paramsUrl.add(new BasicNameValuePair("street1", street1));
					paramsUrl.add(new BasicNameValuePair("street2", street2));
					paramsUrl.add(new BasicNameValuePair("city", city));
					paramsUrl.add(new BasicNameValuePair("region", region));
					paramsUrl.add(new BasicNameValuePair("postcode", postcode));
					paramsUrl.add(new BasicNameValuePair("country_id", country_id));
					paramsUrl.add(new BasicNameValuePair("telephone", telephone));
					paramsUrl.add(new BasicNameValuePair("fax", fax));
					if (cbxSaveAddressBook1.isChecked()) {
						paramsUrl.add(new BasicNameValuePair("save_in_address_book", "1"));
					}	
				}else{
					paramsUrl.add(new BasicNameValuePair("address_id", address_id));
				}				
				json = JsonParser.makeHttpRequest(
						Constants.URL_SAVEBILLING, "GET", paramsUrl);
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

			return "";
		}

		protected void onPostExecute(String result) {	      
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}	
			if (result.equals("true")) {
				Toast.makeText(CheckoutDetailActivity.this, "Save billing infomation success!", Toast.LENGTH_SHORT).show();

			}else{
				Toast.makeText(CheckoutDetailActivity.this, "Save billing infomation fail!", Toast.LENGTH_SHORT).show();
			}	

			new GetShippingMethodAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this)).execute();

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
				pDialog.setCancelable(false);
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
					orderID = jsonObject.getString("orderId");

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return "";
		}

		protected void onPostExecute(String result) {	      
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}	
			if (result.equals("")) {
				Intent intent = new Intent(CheckoutDetailActivity.this, PaypalSuccessActivity.class); 
				intent.putExtra(Constants.KEY_ORDERID, orderID);
				startActivity(intent);
				overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
				FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_PAYPAL_SUCCESS;
				StackActivity.getInstance().finishAll();	
			}else{
				Log.d("message", ""+result);
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
		String save_card;

		public SubmitOrderAsyncTask(int idCustomer, String method, String cc_type, String cc_number, String cc_exp_month, String cc_exp_year, String cc_cid,String sSaveCreditCard){
			this.idCustomer = idCustomer;
			this.method = method;
			this.cc_type = cc_type;
			this.cc_number = cc_number;
			this.cc_exp_month = cc_exp_month;
			this.cc_exp_year = cc_exp_year;
			this.cc_cid = cc_cid;
			this.save_card = sSaveCreditCard;
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
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));
				paramsUrl.add(new BasicNameValuePair("method", method));
				paramsUrl.add(new BasicNameValuePair("cc_type", cc_type));
				paramsUrl.add(new BasicNameValuePair("cc_number", cc_number));
				paramsUrl.add(new BasicNameValuePair("cc_exp_month", cc_exp_month));
				paramsUrl.add(new BasicNameValuePair("cc_exp_year", cc_exp_year));
				paramsUrl.add(new BasicNameValuePair("cc_cid", cc_cid));
				paramsUrl.add(new BasicNameValuePair("save_card", save_card));

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
					orderID = jsonObject.getString("orderId");

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return "";
		}

		protected void onPostExecute(String result) {	      
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}	
			if (result.equals("")) {
				Intent intent = new Intent(CheckoutDetailActivity.this, PaypalSuccessActivity.class); 
				intent.putExtra(Constants.KEY_ORDERID, orderID);
				startActivity(intent);
				overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
				FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_PAYPAL_SUCCESS;
				StackActivity.getInstance().finishAll();	
			}else{
				Toast.makeText(CheckoutDetailActivity.this, result, Toast.LENGTH_LONG).show();
			}
		}
	}


	//--------------------SubmitOrderAsyncTask----------------------------------------
	public class ValidatePaypalAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		int idCustomer;

		public ValidatePaypalAsyncTask(int idCustomer){
			this.idCustomer = idCustomer;			
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
				paramsUrl.add(new BasicNameValuePair("cid", String.valueOf(idCustomer)));

				json = JsonParser.makeHttpRequest(
						Constants.URL_VALIDATEPAYPAL, "POST", paramsUrl);
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
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}	
			if (result.equals("")) {
				Intent browserIntent = new Intent(CheckoutDetailActivity.this, PaypalActivity.class);
				browserIntent.putExtra(Constants.KEY_URL_PAYPAL
						, Constants.URL_PAYPAL_CART + "" +SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this));
				startActivity(browserIntent);
				overridePendingTransition(R.anim.fly_in_from_right, R.anim.fly_out_to_left);
				FileUtil.POSITION_ACTIVITY = Constants.POSITION_ACTIVITY_PAYPAL;
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
		if (positionMonth == 0) {
			Toast.makeText(CheckoutDetailActivity.this, "Please select Month!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (positionYear == 0) {
			Toast.makeText(CheckoutDetailActivity.this, "Please select Year!", Toast.LENGTH_SHORT).show();
			return false;
		}
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

	private void addNewAddressSpinner(){
		addresses.add("New Address");
	}

	private boolean checkRequireFieldBilling(){
		if (checkEdittextEmpty(edtFirstName1)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input first name!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (checkEdittextEmpty(edtLastName1)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input last name!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (checkEdittextEmpty(edtAddress1)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input address!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (checkEdittextEmpty(edtCity1)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input city!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (edtState1.getVisibility() == View.VISIBLE
				&& checkEdittextEmpty(edtState1)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input State!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (checkEdittextEmpty(edtPostalCode1)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input postal code!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (checkEdittextEmpty(edtTelephone1)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input telephone!", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private boolean checkRequireFieldShipping(){
		if (checkEdittextEmpty(edtFirstName2)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input first name!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (checkEdittextEmpty(edtLastName2)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input last name!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (checkEdittextEmpty(edtAddress2)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input address!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (checkEdittextEmpty(edtCity2)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input city!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (edtState2.getVisibility() == View.VISIBLE
				&& checkEdittextEmpty(edtState2)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input State!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (checkEdittextEmpty(edtPostalCode2)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input postal code!", Toast.LENGTH_SHORT).show();
			return false;
		}
		if (checkEdittextEmpty(edtTelephone2)) {
			Toast.makeText(CheckoutDetailActivity.this, "Please input telephone!", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	private boolean checkEdittextEmpty(EditText editText){
		if (editText.getText().toString().trim().equals("")) {
			return true;
		}else{
			return false;
		}
	}

	private void callSaveBilling(){
		String countryCode = "";
		if (edtState1.getVisibility() == View.VISIBLE) {
			countryCode = edtState1.getText().toString().trim();
		}else{
			countryCode = FileUtil.listCountry.get(positionCountry1).getCode();
		}
		new SaveBillingAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this)
				, edtFirstName1.getText().toString().trim(), edtLastName1.getText().toString().trim()
				, edtCompany1.getText().toString().trim(), edtAddress1.getText().toString().trim(), ""
				, edtCity1.getText().toString().trim(), FileUtil.listState.get(positionState1).getCode()
				, edtPostalCode1.getText().toString().trim(), countryCode
				, edtTelephone1.getText().toString().trim(), edtFax1.getText().toString().trim(), getUseForShipping()).execute();
	}

	private void callSaveBillingExist(){		
		new SaveBillingAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this), getUseForShipping(),getKeyAddress(addresses.get(positionBilling))).execute();
	}


	private void callSaveShipping(){
		String countryCode = "";
		if (edtState2.getVisibility() == View.VISIBLE) {
			countryCode = edtState2.getText().toString().trim();
		}else{
			countryCode = FileUtil.listCountry.get(positionCountry2).getCode();
		}
		new SaveShippingAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this)
				, edtFirstName2.getText().toString().trim(), edtLastName2.getText().toString().trim()
				, edtCompany2.getText().toString().trim(), edtAddress2.getText().toString().trim(), ""
				, edtCity2.getText().toString().trim(), FileUtil.listState.get(positionState2).getCode()
				, edtPostalCode2.getText().toString().trim(),countryCode 
				, edtTelephone2.getText().toString().trim(), edtFax2.getText().toString().trim(), "0").execute();
	}

	private void callSaveShippingExist(){		
		new SaveShippingAsyncTask(SharedPreferencesUtil.getIdCustomerLogin(CheckoutDetailActivity.this), "0",getKeyAddress(addresses.get(positionShipping))).execute();
	}

	private String getUseForShipping(){
		if (rbnShipThisAddress.isChecked()) {
			return "1";
		}
		return "0";
	}


	private void clickContinue1(){
		ln1BillingInfomationContent.setVisibility(View.GONE);
		ln2ShippingInfomationContent.setVisibility(View.GONE);
		ln3ShippingMethodContent.setVisibility(View.VISIBLE);
		ln4PaymentInfomationContent.setVisibility(View.GONE);
		ln5OrderReviewContent.setVisibility(View.GONE);
	}
	
	private void clickContinue1DifferntAddress(){
		ln1BillingInfomationContent.setVisibility(View.GONE);
		ln2ShippingInfomationContent.setVisibility(View.VISIBLE);
		ln3ShippingMethodContent.setVisibility(View.GONE);
		ln4PaymentInfomationContent.setVisibility(View.GONE);
		ln5OrderReviewContent.setVisibility(View.GONE);
	}

	private void clickContinue2(){
		ln1BillingInfomationContent.setVisibility(View.GONE);
		ln2ShippingInfomationContent.setVisibility(View.GONE);
		ln3ShippingMethodContent.setVisibility(View.VISIBLE);
		ln4PaymentInfomationContent.setVisibility(View.GONE);
		ln5OrderReviewContent.setVisibility(View.GONE);
	}

	private void clickContinue3(){
		ln1BillingInfomationContent.setVisibility(View.GONE);
		ln2ShippingInfomationContent.setVisibility(View.GONE);
		ln3ShippingMethodContent.setVisibility(View.GONE);
		ln4PaymentInfomationContent.setVisibility(View.VISIBLE);
		ln5OrderReviewContent.setVisibility(View.GONE);
	}

	private void clickContinue4(){
		ln1BillingInfomationContent.setVisibility(View.GONE);
		ln2ShippingInfomationContent.setVisibility(View.GONE);
		ln3ShippingMethodContent.setVisibility(View.GONE);
		ln4PaymentInfomationContent.setVisibility(View.GONE);
		ln5OrderReviewContent.setVisibility(View.VISIBLE);
	}

	//--------------------GetState----------------------------------------
	public class GetStateAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		String idCountry;
		public GetStateAsyncTask(String idCountry){
			this.idCountry = idCountry;
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
				paramsUrl.add(new BasicNameValuePair("country_id", String.valueOf(idCountry)));
				json = JsonParser.makeHttpRequest(
						Constants.URL_GETSTATE, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					FileUtil.listState.clear();
					FileUtil.states.clear();
					for (int i = 0; i < array.length(); i++) {
						StateObject temp = new StateObject();
						temp.setName(array.getJSONObject(i).getString("name"));  
						temp.setCode(array.getJSONObject(i).getString("code"));   
						FileUtil.listState.add(temp);
						FileUtil.states.add(temp.getName());
					}

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {	 
			statesAdapter.notifyDataSetChanged();
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}	
		}
	}

	//--------------------GetCountry----------------------------------------
	public class GetCountryAsyncTask extends AsyncTask<String, String, String> {

		private String json;
		public GetCountryAsyncTask(){
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
				json = JsonParser.makeHttpRequest(
						Constants.URL_GETCOUNTRY, "GET", paramsUrl);
				if ((json != null) || (!json.equals(""))) {               
					JSONArray array = new JSONArray(json);
					FileUtil.listCountry.clear();
					FileUtil.countries.clear();
					for (int j = 0; j < array.length(); j++) {
						CountryObject temp = new CountryObject();
						temp.setName(array.getJSONObject(j).getString("name"));  
						temp.setCode(array.getJSONObject(j).getString("country_id"));   
						FileUtil.listCountry.add(temp);
						FileUtil.countries.add(temp.getName());
					}					

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(String file_url) {	 			
			countriesAdapter.notifyDataSetChanged();
			if ((pDialog != null) && pDialog.isShowing()) { 
				pDialog.dismiss();
			}	
			setPositionSpinnerCountry();			
		}
	}

	private void setPositionSpinnerCountry(){
		for (int i = 0; i < FileUtil.listCountry.size(); i++) {
			if (FileUtil.listCountry.get(i).getCode().equals("US")) {
				positionCountry1 = i;
				spnCountry1.setSelection(positionCountry1);
				positionCountry2 = i;
				spnCountry2.setSelection(positionCountry2);
				break;
			}
		}
	}

	private boolean checkValid(String sCountryCode){
		for (int i = 0; i < FileUtil.validCountry.length; i++) {
			if (sCountryCode.equalsIgnoreCase(FileUtil.validCountry[i])) {
				return true;
			}

		}
		return false;
	}

}
