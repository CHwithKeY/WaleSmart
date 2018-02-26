package com.waletech.walesmart.shop.exp;

import com.waletech.walesmart.http.HttpResult;
import com.waletech.walesmart.publicObject.ObjectShoe;

import java.util.ArrayList;

/**
 * Created by spilkaka on 2017/10/19.
 */

public class ObjectSmark {
    private ArrayList<ObjectShoe> shoeList;
    private String smark_num;
    private String epc_size;
    private String smark_type;
    private String shop_name;
    private String area_name;

    private String last_id;
    private String remark;

    public void setShoeList(ArrayList<ObjectShoe> shoeList) {
        this.shoeList = shoeList;
    }

    public void setSmarkNum(String smark_num) {
        this.smark_num = smark_num;
    }

    public void setEpcSize(String epc_size) {
        this.epc_size = epc_size;
    }

    public void setSmarkType(String smark_type) {
        this.smark_type = smark_type;
    }

    public void setShopName(String shop_name) {
        this.shop_name = shop_name;
    }

    public void setAreaName(String area_name) {
        this.area_name = area_name;
    }

    public void setLastId(String last_id) {
        this.last_id = last_id;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ArrayList<ObjectShoe> getShoeList() {
        return shoeList;
    }

    public String getSmarkNum() {
        return smark_num;
    }

    public String getEpcSize() {
        return epc_size;
    }

    public String getSmarkType() {
        return smark_type;
    }

    public String getShopName() {
        return shop_name;
    }

    public String getAreaName() {
        return area_name;
    }

    public String getLastId() {
        return last_id;
    }

    public String getRemark() {
        return remark;
    }

}
