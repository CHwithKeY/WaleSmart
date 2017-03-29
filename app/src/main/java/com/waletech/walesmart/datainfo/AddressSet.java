package com.waletech.walesmart.datainfo;

/**
 * Created by KeY on 2015/12/9.
 */
public final class AddressSet {

    public final static String TABLE_NAME = "address_table";

    public final static String _ID = "_id";
    // 省
    public final static String PROVINCE = "province";
    // 市
    public final static String CITY = "city";
    // 区
    public final static String COUNTY = "county";
    // 路
    public final static String ROAD = "road";
    // 店名
    public final static String SHOP = "shopName";

    // All
    public final static String[] ALL = {PROVINCE, CITY, COUNTY, ROAD, SHOP};
    // Query All
    public final static String[] QUERY_ALL = {PROVINCE, CITY, COUNTY};

}
