package com.waletech.walesmart.netDown;

import android.content.Context;
import android.util.Log;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.Base_Frag;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpResult;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KeY on 2016/7/14.
 */
public class NetDownAction extends BaseAction {

    private Base_Frag fragment;

    public NetDownAction(Context context, Base_Frag fragment) {
        super(context);
        this.fragment = fragment;
    }

    public void ping() {
        String[] key = {""};
        String[] value = {""};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_PING);
        action.setHandler(new HttpHandler(context, fragment));
        action.setTag(HttpTag.NET_DOWN_PING);
        action.setMap(key, value);
        action.interaction();
    }

    public boolean handleResponse(String result) throws JSONException {
        Log.i("Result", "net down result is : " + result);

        JSONObject obj = new JSONObject(result);
        return obj.getString(HttpResult.RESULT).equals("Success");
    }
}
