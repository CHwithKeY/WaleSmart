package com.waletech.walesmart.publicAction;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.Base_Serv;
import com.waletech.walesmart.datainfo.AddressSet;
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
public class GetAddressAction extends BaseAction {

    private Base_Serv service;

    public GetAddressAction(Context context) {
        super(context);
    }

    public GetAddressAction(Context context, Base_Serv service) {
        super(context);
        this.service = service;
    }

    public void getAddress() {
        String[] key = {""};
        String[] value = {""};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_ADDRESS);
        action.setMap(key, value);
        action.setTag(HttpTag.GET_ADDRESS);
        if (service != null) {
            Log.i("Result", "action in service");
            action.setHandler(new HttpHandler(context, service));
        } else {
            action.setHandler(new HttpHandler(context));
        }

        action.interaction();
    }

    public void handleResponse(String result) throws JSONException {
        Log.i("Result", "get address result is : " + result);

        JSONArray array = new JSONArray(result);

        DataBaseAction action = DataBaseAction.onCreate(context);
        action.delete(AddressSet.TABLE_NAME);
        action.insert(AddressSet.TABLE_NAME, AddressSet.ALL, array);
    }
}
