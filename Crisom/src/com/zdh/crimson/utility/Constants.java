package com.zdh.crimson.utility;

import java.io.File;

public class Constants {
	public static final int BUTTON_HOME = 1;
	public static final int BUTTON_CATEGORY = 2;
	public static final int BUTTON_SEARCH = 3;	
	public static final int BUTTON_ACCOUNT = 4;
	public static final int BUTTON_MORE = 5;
	
	public static final String URL = "http://crimsonav.com";
	public static final String URL_STAGING = "http://23.21.185.157";
	public static final String URL_PAYPAL = "https://www.paypal.com/us/webapps/mpp/paypal-popup";
	
	public static final String URL_CUSTOMAPI = URL + "/customapi/result/";
	
	public static final String URL_GETCATEGORIESBYID = URL_CUSTOMAPI + "getCategoriesById";
	public static final String URL_GETPRODUCTBYCATEGORYID = URL_CUSTOMAPI + "getProductsByCategoryId";
	public static final String URL_SEARCH = URL_CUSTOMAPI + "search";
	public static final String URL_GETPRODUCTBYID = URL_CUSTOMAPI + "getProductById";
	public static final String URL_LOGIN = URL_CUSTOMAPI + "login";
	public static final String URL_GETMANUFACTURER = URL_CUSTOMAPI + "getManufacturer";
	public static final String URL_GETMODEL = URL_CUSTOMAPI + "getModel";
	public static final String URL_GETCARTITEM = URL_CUSTOMAPI + "getCartItem";
	public static final String URL_GETCOUNTRY = URL_CUSTOMAPI + "getCountry";
	public static final String URL_GETSTATE = URL_CUSTOMAPI + "getStateByCountryId";
	public static final String URL_ADDTOCART = URL_CUSTOMAPI + "addCart";
	public static final String URL_ROOT_PRODUCT_IMAGE = "http://23.21.185.157/media/catalog/product";
	public static final String URL_CLEARCART = URL_CUSTOMAPI + "clearCart";
	public static final String URL_UPDATECART = URL_CUSTOMAPI + "updateCart";
	public static final String URL_GETALLADDRESS = URL_CUSTOMAPI + "getAllAddress";
	public static final String URL_APPLYCOUPON = URL_CUSTOMAPI + "applyCoupon";
	public static final String URL_CANCELCOUPON = URL_CUSTOMAPI + "cancelCoupon";
	public static final String URL_GETCARTCOST = URL_CUSTOMAPI + "getCartCost";
	public static final String URL_GETSHIPPINGMETHOD = URL_CUSTOMAPI + "getShippingMethod";
	public static final String URL_GETCREDITCARD = URL_CUSTOMAPI + "getCCSaved";
	public static final String URL_MOUNTFINDER = URL_CUSTOMAPI + "mountFinder";	
	public static final String URL_VERIFY = URL_CUSTOMAPI + "verify";	
	public static final String URL_SAVESHIPPINGMETHOD = URL_CUSTOMAPI + "saveShippingMethod";	
	public static final String URL_SAVEPAYMENT = URL_CUSTOMAPI + "savePayment";	
	public static final String URL_NARROWSEARCH = URL_CUSTOMAPI + "narrowSearch";	
	
	
	public static final String EXTENSION_FILE_DWG = ".dwg";
	public static final String EXTENSION_FILE_PDF = ".pdf";
		
	public static final String KEY_CATEGORYID = "KEY_CATEGORYID";
	public static final String KEY_MOUNTFINDER_MODEL = "KEY_MOUNTFINDER_MODEL";
	public static final String KEY_SEARCH_KEYWORD = "KEY_SEARCH_KEYWORD";
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
	
	public static final String METHOD_LINKPOINT = "linkpoint";
	public static final String METHOD_PAYPAL = "paypal";
	
	public static final String CONFIRM_LOGOUT_MESSAGE = "Are you sure you want log out now?";
	public static final String CONFIRM_LOGOUT_TITLE = "Confirm Log out...";
	public static final String WARNING_LOGIN_MESSAGE = "You need to log in before going to cart!";
	public static final String WARNING_LOGIN_TITLE = "Warning log in...";
	
	public static final String VERIFY_OK = "This mount is compatible.";
	public static final String VERIFY_NOK = "This mount is not compatible, please choose a different Crimson model or use EZ-Mount Finder to narrow your search";
	
	public static final int KEY_DEVIDE_FLATPANEL = 1;
	public static final int KEY_DEVIDE_PROJECTOR = 2;
	
	public static final int POSITION_ACTIVITY_LOGIN = 0;
	public static final int POSITION_ACTIVITY_HOME = 1;
	public static final int POSITION_ACTIVITY_CATEGORY = 2;
	public static final int POSITION_ACTIVITY_PRODUCTLIST = 3;
	public static final int POSITION_ACTIVITY_SEARCH = 4;
	public static final int POSITION_ACTIVITY_PRODUCTDETAIL = 5;
	public static final int POSITION_ACTIVITY_CART = 6;
	public static final int POSITION_ACTIVITY_CHECKOUT = 7;
	public static final int POSITION_ACTIVITY_CHECKOUTDETAIL = 8;
	public static final int POSITION_ACTIVITY_CONTACT = 9;
}
