package com.zdh.crimson;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.zdh.crimson.utility.Constants;

public class CheckoutDetailActivity extends Activity  implements View.OnClickListener{

	//--------define variables---------
	private LinearLayout lnHome,lnSearch,lnCategory,lnCart,lnContact,lnPaypal
	,ln1BillingInfomation,ln2ShippingInfomation,ln3ShippingMethod,ln4PaymentInfomation,ln5OrderReview
	,ln1BillingInfomationContent,ln2ShippingInfomationContent,ln3ShippingMethodContent,ln4PaymentInfomationContent,ln5OrderReviewContent;
	private ImageView ivCart;	
	private Button btnLogin,btnBack,btnBillingContinue,btnShippingContinue,btnShippingMethodContinue,btnPaymentContinue,btnPlaceOrder;
	private TextView tvTitle,tvPartNumber,tvColor,tvDesciption,tvPrice,tvQuantity,tvSubtotal,tvSubtotal2,
	tvShippingHandling,tvTax,tvGrandTotal,tvEditYourCard;
	
	Spinner spnBillingAddress,spnShippingAddress;
	RadioButton rbnShipThisAddress,rbnShipDifferentAddress,rbnFree,rbnCreditCard,rbnPaypal,rbnCreditTerms;
	CheckBox cbxUseBillingAddress;
	ListView lvParcelService;
	
	ArrayList<Boolean> listRadioButton = new ArrayList<Boolean>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkout_detail);
		init();
	}
	
	private void init(){
		initView();
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

		tvPartNumber = (TextView)findViewById(R.id.checkoutdetail_tvPartNumber);
		tvColor = (TextView)findViewById(R.id.checkoutdetail_tvColor);
		tvDesciption = (TextView)findViewById(R.id.checkoutdetail_tvDescription);
		tvPrice = (TextView)findViewById(R.id.checkoutdetail_tvPrice);
		tvQuantity = (TextView)findViewById(R.id.checkoutdetail_tvQuantity);
		tvSubtotal = (TextView)findViewById(R.id.checkoutdetail_tvSubtotal);
		tvSubtotal2 = (TextView)findViewById(R.id.checkoutdetail_tvSubtotal2);
		tvShippingHandling = (TextView)findViewById(R.id.checkoutdetail_tvShippingHandling);
		tvTax = (TextView)findViewById(R.id.checkoutdetail_tvTax);
		tvGrandTotal = (TextView)findViewById(R.id.checkoutdetail_tvGrandTotal);
		tvEditYourCard = (TextView)findViewById(R.id.checkoutdetail_tvEditYourCart);
		
		rbnShipThisAddress = (RadioButton)findViewById(R.id.checkoutdetail_rbnShipThisAddress);
		rbnShipDifferentAddress = (RadioButton)findViewById(R.id.checkoutdetail_rbnShipDifferentAddress);
		rbnFree = (RadioButton)findViewById(R.id.checkoutdetail_rbnFree);
		rbnCreditCard = (RadioButton)findViewById(R.id.checkoutdetail_rbnCreditCard);
		rbnPaypal = (RadioButton)findViewById(R.id.checkoutdetail_rbnPaypal);
		rbnCreditTerms = (RadioButton)findViewById(R.id.checkoutdetail_rbnCreditTerms);
		
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
		rbnFree.setOnClickListener(this);
		rbnCreditCard.setOnClickListener(this);
		rbnPaypal.setOnClickListener(this);
		rbnCreditTerms.setOnClickListener(this);
				
	}
	
	private void initData() {
		
	}
	
	private void initDataWebservice(){
	
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
			Intent login = new Intent(CheckoutDetailActivity.this, LoginActivity.class);
			startActivity(login);
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
			ln1BillingInfomationContent.setVisibility(View.GONE);
			ln2ShippingInfomationContent.setVisibility(View.VISIBLE);
			ln3ShippingMethodContent.setVisibility(View.GONE);
			ln4PaymentInfomationContent.setVisibility(View.GONE);
			ln5OrderReviewContent.setVisibility(View.GONE);		
			break;
		case R.id.checkoutdetail_ln3ShippingMethod:
			ln1BillingInfomationContent.setVisibility(View.GONE);
			ln2ShippingInfomationContent.setVisibility(View.GONE);
			ln3ShippingMethodContent.setVisibility(View.VISIBLE);
			ln4PaymentInfomationContent.setVisibility(View.GONE);
			ln5OrderReviewContent.setVisibility(View.GONE);
			break;
		case R.id.checkoutdetail_ln4PaymentInfomation:
			ln1BillingInfomationContent.setVisibility(View.GONE);
			ln2ShippingInfomationContent.setVisibility(View.GONE);
			ln3ShippingMethodContent.setVisibility(View.GONE);
			ln4PaymentInfomationContent.setVisibility(View.VISIBLE);
			ln5OrderReviewContent.setVisibility(View.GONE);
			break;
		case R.id.checkoutdetail_ln5OrderReview:
			ln1BillingInfomationContent.setVisibility(View.GONE);
			ln2ShippingInfomationContent.setVisibility(View.GONE);
			ln3ShippingMethodContent.setVisibility(View.GONE);
			ln4PaymentInfomationContent.setVisibility(View.GONE);
			ln5OrderReviewContent.setVisibility(View.VISIBLE);
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
			
		case R.id.checkoutdetail_rbnCreditCard:
			rbnCreditCard.setChecked(true);
			rbnPaypal.setChecked(false);	
			rbnCreditTerms.setChecked(false);	
			break;	
		case R.id.checkoutdetail_rbnPaypal:
			rbnCreditCard.setChecked(false);
			rbnPaypal.setChecked(true);	
			rbnCreditTerms.setChecked(false);	
			break;	
		case R.id.checkoutdetail_rbnCreditTerms:
			rbnCreditCard.setChecked(false);
			rbnPaypal.setChecked(false);	
			rbnCreditTerms.setChecked(true);	
			break;	
			
		//-----------------Click button continue---------------------------------	
			
		case R.id.checkoutdetail_lnPaypal:
			
			break;
		case R.id.checkoutdetail_Billing_btnContinue:
					
			break;
		case R.id.checkoutdetail_Shipping_btnContinue:
			
			break;
		case R.id.checkoutdetail_ShippingMethod_btnContinue:
			
			break;
		case R.id.checkoutdetail_Payment_btnContinue:
			
			break;
		case R.id.checkoutdetail_btnPlaceOrder:
			
			break;
			
		default:
			break;
		}
		
	}
	
	
	
	
	
	
	
}
