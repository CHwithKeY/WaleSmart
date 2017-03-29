package com.waletech.walesmart.base;

import android.content.Context;
import android.view.View;

import com.waletech.walesmart.sharedinfo.SharedAction;

/**
 * Created by KeY on 2016/6/5.
 */
public abstract class BaseClickListener implements View.OnClickListener {

    protected Context context;
    protected SharedAction sharedAction;

    public BaseClickListener(Context context, BaseAction baseAction) {
        this.context = context;

        sharedAction = new SharedAction(context);
    };

    @Override
    public abstract void onClick(View v);
}
