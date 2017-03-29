package com.waletech.walesmart.publicAction;

import android.content.Context;
import android.util.Log;

import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.Base_Serv;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpResult;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicClass.Methods;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KeY on 2016/10/9.
 */
public class GetServiceVersionAction extends BaseAction {

    private Base_Serv service;

    public GetServiceVersionAction(Context context) {
        super(context);
    }

    public GetServiceVersionAction(Context context, Base_Serv service) {
        super(context);
        this.service = service;
    }

    public void getVersion() {
        String username = "";
        if (sharedAction.getLoginStatus()) {
            username = sharedAction.getUsername();
        }

        String app_version = Methods.getVersionName(context);

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_VERSION};
        String[] value = {username, app_version};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_SERVICE);
        action.setTag(HttpTag.GET_SERVICE);
        if (service != null) {
            Log.i("Result", "action in service");
            action.setHandler(new HttpHandler(context, service));
        } else {
            action.setHandler(new HttpHandler(context));
        }
        action.setMap(key, value);
        action.interaction();
    }

    public void handleResponse(String result) throws JSONException {
        Log.i("Result", "service version result : " + result);

        JSONObject obj = new JSONObject(result);
        String service_version = obj.getString(HttpResult.RESULT);

        if (service_version.startsWith("error")) {
            service_version = "/ClientMainService_v1/";
        } else {
            service_version = "/" + service_version + "/";
        }

        Log.i("Result", "get service version : " + service_version);

        HttpSet.setBaseService(service_version, context);
    }
}
