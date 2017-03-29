package com.waletech.walesmart.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Frag;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.sharedinfo.SharedAction;

import org.json.JSONException;

/**
 * Created by KeY on 2016/9/19.
 */
public class Splash_Frag extends Base_Frag {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_splash_page_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        if (new SharedAction(getContext()).getUsername().equals("13929572369")) {
//            HttpSet.setBaseIP(HttpSet.DEDICATED_IP, getContext());
//        }
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

    }

    @Override
    public void onNullResponse() throws JSONException {

    }

    @Override
    public void setCustomTag(String tag) {

    }

    @Override
    public String getCustomTag() {
        return null;
    }
}
