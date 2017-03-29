package com.waletech.walesmart.publicObject;

import com.waletech.walesmart.http.HttpResult;

/**
 * Created by KeY on 2016/7/8.
 */
public class ObjectShop {

    private String name;
    private String location;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public final static String[] key_set = {HttpResult.SHOP_NAME, HttpResult.SHOP_LOCATION};

}
