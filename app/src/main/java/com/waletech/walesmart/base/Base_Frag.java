package com.waletech.walesmart.base;

import android.support.v4.app.Fragment;

import org.json.JSONException;

import java.io.Serializable;

/**
 * Created by KeY on 2016/6/6.
 */
public abstract class Base_Frag extends Fragment implements Serializable{

    // 根据不同的Handler可以处理不同的反馈结果，最强大
    public abstract void onMultiHandleResponse(String tag, String result) throws JSONException;

    public abstract void onNullResponse() throws JSONException;

    public abstract void setCustomTag(String tag);

    public abstract String getCustomTag();

}
