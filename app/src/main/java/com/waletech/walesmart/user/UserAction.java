package com.waletech.walesmart.user;

import android.content.Context;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;

/**
 * Created by lenovo on 2016/12/22.
 */

public class UserAction extends BaseAction {

    public UserAction(Context context) {
        super(context);
    }

    public void checkPrivilege() {
        HttpAction action = new HttpAction(context);

//        action.setUrl();
//        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
//        action.setMap();
//        action.setTag();
//        action.setHandler(new HttpHandler(context));
//
//        action.interaction();
    }
}
