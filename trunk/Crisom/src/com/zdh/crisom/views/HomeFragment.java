package com.zdh.crisom.views;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zdh.crisom.R;
import com.zdh.crisom.adapter.SlideImageViewPagerAdapter;

public class HomeFragment extends FragmentActivity {
	
public static int page = 0;
//	View view;
	ViewPager viewPager;
	SlideImageViewPagerAdapter slideImageViewPagerAdapter;
	ImageView imageView1,imageView2,imageView3,imageView4;
	ArrayList<ImageView> imageViews = new ArrayList<ImageView>();
	
//	ViewFlipper viewFlipper;
//	ImageLoader imageLoader;
//	ImageView image;
	
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//        Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//    	view = inflater.inflate(R.layout.fragment_home, container, false);
//    	init();
//    	return view;
//    }
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_home_a);
		init();
	}

	private void init(){
    	initView();
    	initData();
    }
    
	private void initView() {
//		viewFlipper = (ViewFlipper)fragview.findViewById(R.id.fra_home_viewflipper);
		viewPager = (ViewPager) findViewById(R.id.home_vp);
		imageView1 = (ImageView) findViewById(R.id.home_image_page1);
		imageView2 = (ImageView) findViewById(R.id.home_image_page2);
		imageView3 = (ImageView) findViewById(R.id.home_image_page3);
		imageView4 = (ImageView) findViewById(R.id.home_image_page4);

		
		imageViews.clear();
		imageViews.add(imageView1);
		imageViews.add(imageView2);
		imageViews.add(imageView3);
		imageViews.add(imageView4);	
		
		FragmentManager fragmentManager = null;
		fragmentManager = getSupportFragmentManager();
		slideImageViewPagerAdapter = new SlideImageViewPagerAdapter(fragmentManager);
		this.viewPager.setAdapter(slideImageViewPagerAdapter);
		this.viewPager
		.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int p) {
				
			}

			@Override
			public void onPageScrolled(int position,
					float positionOffset, int positionOffsetPixels) {
				imageViews.get(page).setImageResource(R.drawable.viewpager_nomal);
				imageViews.get(position).setImageResource(R.drawable.viewpager_active);
				page = position;
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		
	}

	private void initData(){
//    	imageLoader=new ImageLoader(getActivity());		
//		for(int i=0; i<FileUtil.listImageSlide.size(); i++)
//        {
//        //  This will create dynamic image view and add them to ViewFlipper
//			int resID = getResources().getIdentifier(FileUtil.listImageSlide.get(i) , "drawable", getActivity().getPackageName());
//            setFlipperImage(resID);
//        }
//		viewFlipper.startFlipping();
    }
    
//    private void setFlipperImage(int res) {	   
//	    image = new ImageView(getActivity());
//	    LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//	    image.setLayoutParams(vp);
//	    image.setScaleType(ImageView.ScaleType.FIT_XY);
//
//	    image.setBackgroundResource(res);
//	    viewFlipper.addView(image);
//	        
//    }
}