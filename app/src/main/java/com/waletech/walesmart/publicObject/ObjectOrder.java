package com.waletech.walesmart.publicObject;

import com.waletech.walesmart.http.HttpResult;

/**
 * Created by KeY on 2016/6/30.
 */
public class ObjectOrder {

    private String order_num;
    private String date_time;
    private String total_price;

    private String last_id;

    public Object[] value_set = {order_num, date_time, total_price, last_id};

    public final static String[] key_set = {
            HttpResult.ORDER_NUM, HttpResult.DATE_TIME, HttpResult.TOTAL_PRICE, HttpResult.LAST_ID};


    public String getOrderNum() {
        return value_set[0].toString();
    }

    public String getDateTime() {
        return value_set[1].toString();
    }

    public String getTotalPrice() {
        return value_set[2].toString();
    }

    public void setLastId(String last_id) {
        this.last_id = last_id;
    }

    public String getLastId() {
        return value_set[3].toString();
    }
}
