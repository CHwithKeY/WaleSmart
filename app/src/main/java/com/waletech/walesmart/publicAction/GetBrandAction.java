package com.waletech.walesmart.publicAction;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.Base_Serv;
import com.waletech.walesmart.datainfo.BrandSet;
import com.waletech.walesmart.datainfo.DataBaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by KeY on 2016/6/6.
 */
public class GetBrandAction extends BaseAction {

    private Base_Serv service;

    public GetBrandAction(Context context) {
        super(context);
    }

    public GetBrandAction(Context context, Base_Serv service) {
        super(context);
        this.service = service;
    }

    public void getBrand() {
        String[] key = {""};
        String[] value = {""};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_BRAND);
        action.setMap(key, value);
        action.setTag(HttpTag.GET_BRAND);
        if (service != null) {
            action.setHandler(new HttpHandler(context, service));
        } else {
            action.setHandler(new HttpHandler(context));
        }
        action.interaction();
    }

    public void handleResponse(String result) throws JSONException {
        Log.i("Result", "get brand result is : " + result);

        JSONArray array = new JSONArray(result);

        DataBaseAction action = DataBaseAction.onCreate(context);
        action.delete(BrandSet.TABLE_NAME);
        action.insert(BrandSet.TABLE_NAME, BrandSet.ALL, array);
    }
}
