package com.zdh.crimson.utility;

import java.io.File;

public class Constants {
	public static final int BUTTON_HOME = 1;
	public static final int BUTTON_CATEGORY = 2;
	public static final int BUTTON_SEARCH = 3;	
	public static final int BUTTON_ACCOUNT = 4;
	public static final int BUTTON_MORE = 5;
	
	public static final String URL = "http://crimsonav.com";
	public static final String URL_PAYPAL = "https://www.paypal.com/us/webapps/mpp/paypal-popup";
	
	public static final String URL_GETCATEGORIESBYID = "http://crimsonav.com/api/result/getCategoriesById";
	public static final String URL_GETPRODUCTBYCATEGORYID = "http://crimsonav.com/api/result/getProductsByCategoryId";
	public static final String URL_SEARCH = "http://crimsonav.com/api/result/search";
	public static final String URL_GETPRODUCTBYID = "http://crimsonav.com/api/result/getProductById";
	public static final String URL_LOGIN = "http://crimsonav.com/api/result/login";
	public static final String URL_GETMANUFACTURER = "http://crimsonav.com/api/result/getManufacturer";
	public static final String URL_GETMODEL = "http://crimsonav.com/api/result/getModel";
	public static final String URL_GETCARTITEM = "http://crimsonav.com/api/result/getCartItem";
	public static final String URL_GETCOUNTRY = "http://crimsonav.com/api/result/getCountry";
	public static final String URL_GETSTATE = "http://crimsonav.com/api/result/getStateByCountryId";
	public static final String URL_ADDTOCART = "http://crimsonav.com/api/result/addCart";
	public static final String URL_ROOT_PRODUCT_IMAGE = "http://crimsonav.com/media/catalog/product";
	public static final String URL_CLEARCART = "http://crimsonav.com/api/result/clearCart";
	public static final String URL_UPDATECART = "http://crimsonav.com/api/result/updateCart";
	public static final String URL_GETALLADDRESS = "http://crimsonav.com/api/result/getAllAddress";
	public static final String URL_APPLYCOUPON = "http://crimsonav.com/api/result/applyCoupon";
	public static final String URL_CANCELCOUPON = "http://crimsonav.com/api/result/cancelCoupon";
	public static final String URL_GETCARTCOST = "http://crimsonav.com/api/result/getCartCost";
	public static final String URL_GETSHIPPINGMETHOD = "http://crimsonav.com/api/result/getShippingMethod";
	public static final String URL_GETCREDITCARD = "http://crimsonav.com/api/result/getCCSaved";
	public static final String URL_MOUNTFINDER = "http://crimsonav.com/api/result/mountFinder";	
	
	public static final String EXTENSION_FILE_DWG = ".dwg";
	public static final String EXTENSION_FILE_PDF = ".pdf";
		
	public static final String KEY_CATEGORYID = "KEY_CATEGORYID";
	public static final String KEY_MOUNTFINDER_MODEL = "KEY_MOUNTFINDER_MODEL";
	public static final String KEY_MOUNTFINDER_MANUFACTURER = "KEY_MOUNTFINDER_MANUFACTURER";
	public static final String KEY_MOUNTFINDER_DEVICE = "KEY_MOUNTFINDER_DEVICE";
	
	public static final String KEY_PRODUCTID = "KEY_PRODUCTID";
	
	public static final String PathFolderDataRoot = "Crisom";
	public static final String PathFolderDocument = PathFolderDataRoot + File.separator +"Document";
	
	public static final String TEXT_BUTTON_LOGIN = "Log In";
	public static final String TEXT_BUTTON_LOGOUT = "Log Out";
	public static final String TEXT_THEREIS = "There is ";
	public static final String TEXT_THEREARE = "There are ";
	public static final String TEXT_ITEM = " item";
	public static final String TEXT_ITEMS = " items";
	
	public static final String CONFIRM_LOGOUT_MESSAGE = "Are you sure you want log out now?";
	public static final String CONFIRM_LOGOUT_TITLE = "Confirm Log out...";
	public static final String WARNING_LOGIN_MESSAGE = "You need to log in before going to cart!";
	public static final String WARNING_LOGIN_TITLE = "Warning log in...";
	
	public static final String VERIFY_OK = "This mount is compatible.";
	public static final String VERIFY_NOK = "This mount is not compatible, please choose a different Crimson model or use EZ-Mount Finder to narrow your search";
	
	public static final int KEY_DEVIDE_FLATPANEL = 1;
	public static final int KEY_DEVIDE_PROJECTOR = 2;
}
