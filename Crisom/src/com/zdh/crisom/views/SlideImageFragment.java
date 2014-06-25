package com.zdh.crisom.views;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zdh.crisom.R;

public class SlideImageFragment  extends Fragment{

	ImageView imageView;
	View view;
	int pageView = 0;
	int imagesShow;
	private static final String IMAGE = "image";
	
	public static SlideImageFragment newInstance(int imagesShow) {
		SlideImageFragment sif = new SlideImageFragment();	
		Bundle arg = new Bundle();
		arg.putInt(IMAGE, imagesShow);		
		sif.setArguments(arg);
		return sif;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.view =  inflater.inflate(R.layout.fragment_slideimage, container, false);	
		imageView = (ImageView) view.findViewById(R.id.slideimage_iv);
		imageView.setImageResource(getArguments().getInt(IMAGE));
		return view;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		
	}
	
	
	
	@Override
	public View getView() {
		return super.getView();
	}

}

