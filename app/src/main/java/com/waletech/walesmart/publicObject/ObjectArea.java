package com.waletech.walesmart.publicObject;

import com.waletech.walesmart.http.HttpResult;

/**
 * Created by KeY on 2016/8/5.
 */
public class ObjectArea {

    private String name;
    private String row;
    private String column;

    public String[] value_set = {name, row, column};

    public static String[] key_set = {HttpResult.AREA_NAME, HttpResult.AREA_ROW, HttpResult.AREA_COLUMN};

    public String getName() {
        return value_set[0];
    }

    public String getRow() {
        return value_set[1];
    }

    public String getColumn() {
        return value_set[2];
    }
}
