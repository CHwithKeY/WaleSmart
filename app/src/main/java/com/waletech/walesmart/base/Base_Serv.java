package com.waletech.walesmart.base;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;

import com.waletech.walesmart.sharedinfo.SharedAction;
import com.waletech.walesmart.sharedinfo.SharedSet;

import org.json.JSONException;

/**
 * Created by KeY on 2016/8/17.
 */
public abstract class Base_Serv extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public Base_Serv() {
    }

    // 根据不同的Handler可以处理不同的反馈结果，最强大
    public abstract void onMultiHandleResponse(String tag, String result) throws JSONException;

    public abstract void onNullResponse(String tag) throws JSONException;
}
