package com.zdh.crisom.views;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zdh.crisom.R;
import com.zdh.crisom.utility.TextViewEx;

public class AboutFragment extends Fragment {
	
	TextViewEx tvAbout,tvMission,tvClient,tvProduct;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	View fragview = inflater.inflate(R.layout.fragment_about, container, false);
    	init(fragview);
    	return fragview;
    }
    
    private void init(View fragview){
    	initView(fragview);
    	initData();
    }
    
	private void initView(View fragview) {
		
		tvAbout = (TextViewEx) fragview.findViewById(R.id.about_tv_about);
		tvMission = (TextViewEx) fragview.findViewById(R.id.about_tv_mission);
		tvClient = (TextViewEx) fragview.findViewById(R.id.about_tv_client);
		tvProduct = (TextViewEx) fragview.findViewById(R.id.about_tv_product);
		
		tvAbout.setText(tvAbout.getText().toString(), true);
		tvMission.setText(tvMission.getText().toString(), true);
		tvClient.setText(tvClient.getText().toString(), true);
		tvProduct.setText(tvProduct.getText().toString(), true);
			
	}

	private void initData(){
    	
    }
    

}