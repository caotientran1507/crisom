package com.zdh.crisom.utility;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.zdh.crisom.model.Category;
import com.zdh.crisom.model.CountryObject;
import com.zdh.crisom.model.Product;
import com.zdh.crisom.model.RecentObject;
import com.zdh.crisom.model.StateObject;

public class FileUtil {
//	public static boolean flagLogin = false;
	public static int currentButton = Constants.BUTTON_HOME;
	public static ArrayList<String> countries = new ArrayList<String>();
	public static ArrayList<String> listImageSlide = new ArrayList<String>();
	public static String imageSlide1 = "imageslidea";
	public static String imageSlide2 = "imageslideb";
	public static String imageSlide3 = "imageslidec";
	public static String imageSlide4 = "imageslided";
	
	public static ArrayList<Category> listHome = new ArrayList<Category>();
	public static ArrayList<Category> listCategory = new ArrayList<Category>();
	public static ArrayList<Product> listProduct = new ArrayList<Product>();
	public static ArrayList<Product> listSearch = new ArrayList<Product>();
	public static ArrayList<RecentObject> listRecent = new ArrayList<RecentObject>();
	public static ConcurrentMap<Integer, String> listCartChange = new ConcurrentHashMap<Integer, String>();
	
	public static ArrayList<StateObject> listState = new ArrayList<StateObject>();
	public static ArrayList<CountryObject> listCountry = new ArrayList<CountryObject>();
	
//	public static String imageSlide1 = "http://devcrimsonav.com/media/WM.jpg";
//	public static String imageSlide2 = "http://devcrimsonav.com/media/CV100SU-01.jpg";
//	public static String imageSlide3 = "http://devcrimsonav.com/media/CM-01.jpg";
//	public static String imageSlide4 = "http://devcrimsonav.com/media/CV100S-01.jpg";
}
