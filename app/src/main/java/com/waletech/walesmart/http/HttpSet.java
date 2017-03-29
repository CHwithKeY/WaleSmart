package com.waletech.walesmart.http;

import android.content.Context;

import com.waletech.walesmart.sharedinfo.SharedAction;

/**
 * Created by KeY on 2016/6/3.
 */

public final class HttpSet {
    // 183.62.156.108:427
    // 192.168.137.1:8080
    public static final String NORMAL_IP = "119.29.154.234:8080";
    //    public static final String DEDICATED_IP = "192.168.137.1:8080";
    public static final String DEDICATED_IP = "192.168.2.211:8080";

    private static String BASE_SERVICE = "/ClientBaseService/";

    //    public static String BASE_URL = "http://" + NORMAL_IP + "/NfcProjectService/";
    public static String BASE_URL = "http://" + NORMAL_IP + BASE_SERVICE;

    private static String BASE_IP = NORMAL_IP;

    public static void setBaseIP(String base_ip, Context context) {
        BASE_URL = "http://" + base_ip + BASE_SERVICE;
//        BASE_URL = "http://" + base_ip + "/NfcProjectService/";
        BASE_IP = base_ip;
        new SharedAction(context).setNetIP(base_ip);
    }

    public static void setBaseService(String base_service, Context context) {
        BASE_URL = "http://" + BASE_IP + base_service;
        BASE_SERVICE = base_service;
        new SharedAction(context).setNetService(base_service);
    }

    public static String getBaseIp() {
        return BASE_IP;
    }

    public static String getBaseService() {
        return BASE_SERVICE;
    }

    //
    public static final String URL_GET_SERVICE = "ClientGetServiceAction.action";
    // Update_Act
    public static final String URL_VERSION = "WaleSmart.apk";
    //
    public final static String URL_PING = "ClientPingAction.action";
    // CheckNet_Serv
    public final static String URL_GET_BRAND = "ClientGetBrandAction.action";
    public final static String URL_GET_ADDRESS = "ClientGetAddressAction.action";
    // Register_Act
    public final static String URL_REGISTER = "ClientRegisterAction.action";
    // Login_Act
    public final static String URL_LOGIN = "ClientLoginAction.action";
    //
    public final static String URL_UNLOCK = "ClientUnlockAction.action";

    // Favourite_Act
    public final static String URL_GET_FAVOURITE = "ClientGetFavouriteAction.action";
    public final static String URL_ADD_FAVOURITE = "ClientAddFavouriteAction.action";
    public final static String URL_DELETE_FAVOURITE = "ClientDeleteFavouriteAction.action";
    public final static String URL_DELETE_BATCH_FAVOURITE = "ClientDeleteBatchFavouriteAction.action";
    public final static String URL_CHECK_FAVOURITE = "ClientCheckFavouriteAction.action";

    // Cart_Act
    public final static String URL_GET_CART = "ClientGetCartAction.action";
    public final static String URL_DELETE_CART = "ClientDeleteCartAction.action";
    public final static String URL_UPDATE_CART = "ClientUpdateCartAction.action";

    public final static String URL_CREATE_ORDER = "ClientCreateOrderAction.action";

    // Order
    public final static String URL_GET_ORDER = "ClientGetOrderAction.action";
    public final static String URL_GET_ORDER_ITEM = "ClientGetOrderItemAction.action";

    // LockInfo
    public final static String URL_GET_SHOE_USING = "ClientGetLockUsingAction.action";
    public final static String URL_GET_SHOE_RECORD = "ClientGetLockRecordAction.action";
    public final static String URL_GET_MANAGER_SHOE_RECORD = "ClientGetManagerRecordAction.action";

    // Product_Act
    public final static String URL_GET_SHOE_DETAILS = "ClientGetShoeDetailsAction.action";
    public final static String URL_GET_SHOE_OTHER_SHOP = "ClientGetShoeOtherShopAction.action";

    // Pattern
    public final static String URL_GET_DEFAULT_STOCK = "ClientGetDefaultStockAction.action";
    public final static String URL_GET_PATTERN = "ClientGetShoeStockAction.action";
    public final static String URL_GET_COLOR = "ClientGetColorAction.action";
    public final static String URL_GET_SIZE = "ClientGetSizeAction.action";
    public final static String URL_GET_IMAGE = "ClientGetShoeImageAction.action";
    public final static String URL_GET_SHOE_DETAILS_BY_SKU = "ClientGetShoeDetailsBySkuAction.action";
    public final static String URL_ADD_CART = "ClientAddCartAction.action";

    // Search & Result_Act
    public final static String URL_SEARCH_FUZZY = "ClientFuzzySearchAction.action";
    public final static String URL_SEARCH_FILTER = "ClientFilterSearchAction.action";

    // Shop_Act
    public final static String URL_GET_SHOP_AREA = "ClientGetShopAreaAction.action";
    public final static String URL_GET_SHOP_SHOE = "ClientGetShopShoeAction.action";

    // Comment_Act
    public final static String URL_COMMENT_COMMIT = "ClientCommitCommentAction.action";

    // Update_Act
    public final static String URL_CHECK_VERSION = "ClientCheckVersionAction.action";

    //
    public final static String URL_EDIT_INFO_UPDATE_NICKNAME = "ClientUpdateNickNameAction.action";
    public final static String URL_EDIT_INFO_UPDATE_PASSWORD = "ClientUpdatePassWordAction.action";

    // HttpHandler
    public final static int httpResponse = 1000;
    public final static int httpNull = 1001;

    // Public Set
    public final static String KEY_LAST_ID = "last_id";

    //
    public final static String KEY_LANGUAGE = "app_language";

    // User Set
    public final static String KEY_USERNAME = "username";
    public final static String KEY_PASSWORD = "password";
    public final static String KEY_NEW_PASSWORD = "new_password";
    public final static String KEY_NICKNAME = "nickname";

    public final static String KEY_DATE_TIME = "date_time";
    public final static String KEY_SHOP_NAME = "shop_name";
    public final static String KEY_AREA_NAME = "area_name";

    public final static String KEY_SEARCH_WORD = "search_word";

    // Update_Info Set

    //
    public final static String KEY_PRODUCT_NAME = "product_name";

    public final static String KEY_GENDER = "gender";
    public final static String KEY_COLOR = "color";
    public final static String KEY_SIZE = "size";


    public final static String KEY_PROVINCE = "province";
    public final static String KEY_CITY = "city";
    public final static String KEY_COUNTY = "county";

    // LockInfo
    public final static String KEY_BRAND = "brand";
    public final static String KEY_ADDRESS = "address";

    // Product Set
    public final static String KEY_EPC_CODE = "epc_code";
    public final static String KEY_COUNT = "count";

    // Cart Set
    public final static String KEY_SKU_CODE = "sku_code";
    public final static String KEY_NEW_COUNT = "new_count";

    public final static String KEY_ORDER_NUM = "order_num";
    public final static String KEY_PRICE = "price";
    public final static String KEY_TOTAL_PRICE = "total_price";

    // Cabinet Set
    public final static String KEY_SMARK_ID = "smark_id";

    // Push Set
    public final static String KEY_CLIENT_ID = "client_id";

    // Comment Set
    public final static String KEY_EXP_WIDTH = "exp_width";
    public final static String KEY_EXP_MATERIAL = "exp_material";
    public final static String KEY_EXP_SPORT = "exp_sport";
    public final static String KEY_EXP_ADVICE = "exp_advice";

    //
    public final static String KEY_VERSION = "version";

}
