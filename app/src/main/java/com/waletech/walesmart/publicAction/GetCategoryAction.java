package com.waletech.walesmart.publicAction;

import android.content.Context;

import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;

/**
 * Created by KeY on 2016/8/4.
 */
public class GetCategoryAction extends BaseAction {

    public GetCategoryAction(Context context) {
        super(context);
    }

    public void getCategory() {
        String[] key = {""};
        String[] value = {""};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_ADDRESS);
        action.setMap(key, value);
        action.setTag(HttpTag.GET_ADDRESS);
        action.setHandler(new HttpHandler(context));
        action.interaction();
    }

    public void handleResponse(String result) {

    }

}
