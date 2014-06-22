package com.zdh.crisom;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.zdh.crisom.utility.TextViewEx;

public class RegisterActivity extends Activity implements View.OnClickListener{

	//--------define variables---------
	LinearLayout  lnPersonalTitle, lnBillingTitle, lnBillingContent, lnShippingTitle, lnShippingContent ;
	EditText edtFirstname, edtLastname, edtCompany, edtTitle, edtEmail, edtFax, edtEmployee, edtWebsite, edtCrisom, 
			edtBillingCompany, edtBillingTelephone, edtBillingStreet1, edtBillingStreet2, edtBillingCity, edtBillingPostalcode,
			edtShippingCompany, edtShippingTelephone, edtShippingStreet1, edtShippingStreet2, edtShippingCity, edtShippingPostalcode ;
	ImageView ivPersonal, ivBilling, ivShipping;
	RadioGroup rdgService, rdgEtailer;
	RadioButton rdbServiceCommercial, rdbServiceResidential, rdbServiceEducation, rdbServiceOther, rdbEtailerYe, rdbEtailerNo ;
	Spinner spnBillingState, spnBillingCountry, spnShippingState, spnShippingCountry;
	CheckBox cbxUseBilling ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_register);
		init();

	}

	private void init() {
		initView();
		
	}

	private void initView() {
		
		cbxUseBilling = (CheckBox)findViewById(R.id.register_billing_cbx_usebilling);
		
		
		lnPersonalTitle = (LinearLayout)findViewById(R.id.register_personal_ln_title);
		lnBillingTitle = (LinearLayout)findViewById(R.id.register_billing_ln_title);
		lnBillingContent = (LinearLayout)findViewById(R.id.register_billing_ln_content);
		lnShippingTitle = (LinearLayout)findViewById(R.id.register_shipping_ln_title);
		lnShippingContent = (LinearLayout)findViewById(R.id.register_shipping_ln_content);
		
		
		ivPersonal = (ImageView)findViewById(R.id.register_personal_img);
		ivBilling = (ImageView)findViewById(R.id.register_billing_img);
		ivShipping = (ImageView)findViewById(R.id.register_shipping_img);
		
		
		rdgService = (RadioGroup)findViewById(R.id.register_rdg_service); 
		rdgEtailer = (RadioGroup)findViewById(R.id.register_rdg_etailer);
		
		
		rdbServiceCommercial = (RadioButton)findViewById(R.id.register_rdb_service_commercial);
		rdbServiceResidential = (RadioButton)findViewById(R.id.register_rdb_service_residential);
		rdbServiceEducation = (RadioButton)findViewById(R.id.register_rdb_service_education);
		rdbServiceOther = (RadioButton)findViewById(R.id.register_rdb_service_other);
		rdbEtailerYe = (RadioButton)findViewById(R.id.register_rdb_etailer_yes);
		rdbEtailerNo  = (RadioButton)findViewById(R.id.register_rdb_etailer_no);
		
		
		spnBillingState = (Spinner)findViewById(R.id.register_billing_spn_state);
		spnBillingCountry = (Spinner)findViewById(R.id.register_billing_spn_country);
		spnShippingState = (Spinner)findViewById(R.id.register_shipping_spn_state);
		spnShippingCountry = (Spinner)findViewById(R.id.register_shipping_spn_country);
		
		
		edtFirstname = (EditText)findViewById(R.id.register_edt_firstname);
		edtLastname = (EditText)findViewById(R.id.register_edt_lastname); 
		edtCompany = (EditText)findViewById(R.id.register_edt_company);
		edtTitle = (EditText)findViewById(R.id.register_edt_title);
		edtEmail = (EditText)findViewById(R.id.register_edt_email);
		edtFax = (EditText)findViewById(R.id.register_edt_fax);
		edtEmployee = (EditText)findViewById(R.id.register_edt_employee);
		edtWebsite = (EditText)findViewById(R.id.register_edt_website);
		edtCrisom = (EditText)findViewById(R.id.register_edt_crisom);
		
		edtBillingCompany = (EditText)findViewById(R.id.register_billing_edt_company);
		edtBillingTelephone = (EditText)findViewById(R.id.register_billing_edt_telephone);
		edtBillingStreet1 = (EditText)findViewById(R.id.register_billing_edt_street1);
		edtBillingStreet2 = (EditText)findViewById(R.id.register_billing_edt_street2);
		edtBillingCity = (EditText)findViewById(R.id.register_billing_edt_city);
		edtBillingPostalcode = (EditText)findViewById(R.id.register_billing_edt_postalcode);
		
		edtShippingCompany = (EditText)findViewById(R.id.register_shipping_edt_company);
		edtShippingTelephone = (EditText)findViewById(R.id.register_shipping_edt_telephone);
		edtShippingStreet1 = (EditText)findViewById(R.id.register_shipping_edt_street1);
		edtShippingStreet2 = (EditText)findViewById(R.id.register_shipping_edt_street2);
		edtShippingCity = (EditText)findViewById(R.id.register_shipping_edt_city);
		edtShippingPostalcode = (EditText)findViewById(R.id.register_shipping_edt_postalcode);
		
		
	}

	@Override
	public void onClick(View v) {
		
		
	}
}
