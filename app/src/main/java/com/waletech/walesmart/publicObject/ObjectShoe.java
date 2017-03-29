package com.waletech.walesmart.publicObject;

import com.waletech.walesmart.http.HttpResult;

/**
 * Created by KeY on 2016/6/10.
 */
public class ObjectShoe {

    private String brand;
    private String product_name;

    private String gender;
    private String color;
    private String size;
    private String count;
    private String price;

    private String epc_code;

    private String province;
    private String city;
    private String county;
    private String road;

    private String shop_name;
    private String smark_id;
    private String smark_column;
    private String smark_row;
    private String smark_num;

    private String status;

    private String image_path;

    private String last_id;

    private String remark;

    public ObjectShoe() {
    }

    public final String[] value_set = {
            // Params
            brand, product_name, gender, color, size, count, price,
            //
            epc_code,
            // Location
            province, city, county, road,
            // Shop & Smark
            shop_name, smark_id, smark_column, smark_row, smark_num,
            // Backup Status
            status,
            // Image Path
            image_path,
            //
            last_id,
            //
            remark
    };

    public final static String[] key_set = {
            HttpResult.SHOE_BRAND, HttpResult.SHOE_PRODUCT_NAME,
            HttpResult.SHOE_GENDER, HttpResult.SHOE_COLOR, HttpResult.SHOE_SIZE,
            HttpResult.SHOE_COUNT, HttpResult.SHOE_PRICE,

            HttpResult.EPC_CODE,

            HttpResult.PROVINCE, HttpResult.CITY, HttpResult.COUNTY, HttpResult.ROAD,

            HttpResult.SHOP_NAME, HttpResult.SMARK_ID, HttpResult.SMARK_COLUMN, HttpResult.SMARK_ROW, HttpResult.SMARK_NUM,

            HttpResult.STATUS,

            HttpResult.IMAGE_PATH,

            HttpResult.LAST_ID,

            HttpResult.REMARK
    };

    public final String[] params_set = {
            brand, product_name, gender, color, size, count, price,
            epc_code
    };

    public final String[] location_set = {
            province, city, county, road, shop_name
    };

    public final String[] smark_set = {
            shop_name, smark_id, smark_column, smark_row, smark_num
    };

    public String getBrand() {
        return value_set[0] != null && !value_set[0].equals("") ? value_set[0] : params_set[0];
    }

    public String getDesign() {
        return value_set[1] != null && !value_set[1].equals("") ? value_set[1] : params_set[1];
    }

    public String getGender() {
        return value_set[2] != null && !value_set[2].equals("") ? value_set[2] : params_set[2];
    }

    public String getColor() {
        return value_set[3] != null && !value_set[3].equals("") ? value_set[3] : params_set[3];
    }

    public String getSize() {
        return value_set[4] != null && !value_set[4].equals("") ? value_set[4] : params_set[4];
    }

    public String getCount() {
        return value_set[5] != null && !value_set[5].equals("") ? value_set[5] : params_set[5];
    }

    public String getPrice() {
        return value_set[6] != null && !value_set[6].equals("") ? value_set[6] : params_set[6];
    }

    public String getEpcCode() {
        return value_set[7] != null && !value_set[7].equals("") ? value_set[7] : params_set[7];
    }

    public String getProvince() {
        return value_set[8] != null && !value_set[8].equals("") ? value_set[8] : location_set[0];
    }

    public String getCity() {
        return value_set[9] != null && !value_set[9].equals("") ? value_set[9] : location_set[1];
    }

    public String getCounty() {
        return value_set[10] != null && !value_set[10].equals("") ? value_set[10] : location_set[2];
    }

    public String getRoad() {
        return value_set[11] != null && !value_set[11].equals("") ? value_set[11] : location_set[3];
    }

    public String getShopName() {
        if (value_set[12] != null && !value_set[12].equals("")) {
            return value_set[12];
        } else if (location_set[4] != null && !location_set[4].equals("")) {
            return location_set[4];
        } else {
            return smark_set[0];
        }
    }

    public String getSmarkId() {
        return value_set[13] != null && !value_set[13].equals("") ? value_set[13] : smark_set[1];
    }

    public String getSmarkColumn() {
        return value_set[14] != null && !value_set[14].equals("") ? value_set[14] : smark_set[2];
    }

    public String getSmarkRow() {
        return value_set[15] != null && !value_set[15].equals("") ? value_set[15] : smark_set[3];
    }

    public String getSmarkNum() {
        return value_set[16] != null && !value_set[16].equals("") ? value_set[16] : smark_set[4];
    }

    public String getStatus() {
        return value_set[17];
    }

    public String getImagePath() {
        return value_set[18];
    }

    public String getLastId() {
        return value_set[19];
    }

    public String getRemark() {
        return value_set[20];
    }

//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//    }
//
//    protected ObjectShoe(Parcel in) {
//        in.readStringArray(value_set);
//    }
//
//    public static final Creator<ObjectShoe> CREATOR = new Creator<ObjectShoe>() {
//        @Override
//        public ObjectShoe createFromParcel(Parcel in) {
//            return new ObjectShoe(in);
//        }
//
//        @Override
//        public ObjectShoe[] newArray(int size) {
//            return new ObjectShoe[size];
//        }
//    };
}
