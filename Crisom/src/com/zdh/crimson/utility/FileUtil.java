package com.zdh.crimson.utility;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.zdh.crimson.model.CarrierObject;
import com.zdh.crimson.model.Category;
import com.zdh.crimson.model.CountryObject;
import com.zdh.crimson.model.Product;
import com.zdh.crimson.model.RecentObject;
import com.zdh.crimson.model.StateObject;

public class FileUtil {
	// public static boolean flagLogin = false;
	public static int currentButton = Constants.BUTTON_HOME;
	public static ArrayList<String> countries = new ArrayList<String>();
	public static ArrayList<String> states = new ArrayList<String>();
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

	public static ArrayList<CarrierObject> listCarrier = new ArrayList<CarrierObject>();

	public static ArrayList<StateObject> listState = new ArrayList<StateObject>();
	public static ArrayList<CountryObject> listCountry = new ArrayList<CountryObject>();

	public static String months[] = { "Month", "01 - January", "02 - February",
			"03 - March", "04 - April", "05 - May", "06 - June", "07 - July",
			"08 - August", "09 - September", "10 - October", "11 - November",
			"12 - December" };
	public static String years[] = { "Year", "2014", "2015",
		"2016", "2017", "2018", "2019", "2020",
		"2021", "2022", "2023", "2024"};
	public static String creditCardType[] = { "--Please Select--", "American Express", "Visa",
		"MasterCard", "Discover"};
}
