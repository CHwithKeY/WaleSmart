package com.waletech.walesmart.map;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by KeY on 2016/4/15.
 */
public class LocApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
    }
}
