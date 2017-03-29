package com.waletech.walesmart.http;

import android.app.Service;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.base.Base_Frag;
import com.waletech.walesmart.base.Base_Serv;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.sharedinfo.SharedAction;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by KeY on 2016/3/29.
 */
public final class HttpHandler extends Handler {

    private Context context;

//    private LineToast toast;
//    private SharedAction sharedAction;

    private Base_Frag fragment;
    private Base_Serv service;

    // Activity的请求时候的构造函数
    public HttpHandler(Context context) {
        this.context = context;

//        toast = new LineToast(context);
//        sharedAction = new SharedAction(context);
    }

    // Fragment的请求时候的构造函数
    public HttpHandler(Context context, Base_Frag fragment) {
        this.context = context;
        this.fragment = fragment;

//        toast = new LineToast(context);
//        sharedAction = new SharedAction(context);
    }

    // Service的请求时候的构造函数
    public HttpHandler(Context context, Base_Serv service) {
        this.context = context;
        this.service = service;

//        toast = new LineToast(context);
//        sharedAction = new SharedAction(context);
    }

    public void handleResponse(String tag, String result) throws JSONException {
        // 根据判断决定是哪一方的请求
        if (fragment != null) {
            fragment.onMultiHandleResponse(tag, result);
        } else if (service != null) {
            service.onMultiHandleResponse(tag, result);
        } else {
            ((Base_Act) context).onMultiHandleResponse(tag, result);
        }
    }

    private void handleNullMsg(String tag) throws JSONException {
        if (fragment != null) {
            fragment.onNullResponse();
        } else if (service != null) {
            service.onNullResponse(tag);
        } else {
            ((Base_Act) context).onNullResponse();
        }
    }

    @Override
    public void handleMessage(Message msg) {

        switch (msg.what) {
            case HttpSet.httpResponse:
                HashMap<String, String> map = cast(msg.obj);

                String tag = "";
                Set set = map.keySet();
                for (Object aSet : set) {
                    tag = (String) aSet;
                }

                String result = map.get(tag);

                try {
                    handleResponse(tag, result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            case HttpSet.httpNull:
                String null_tag = cast(msg.obj);

                try {
                    handleNullMsg(null_tag);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T cast(Object obj) {
        return (T) obj;
    }
}
