package com.waletech.walesmart.pattern;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.BaseClickListener;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicObject.ObjectShoe;

/**
 * Created by KeY on 2016/7/13.
 */
class ClickListener extends BaseClickListener {

    private PatternAction patternAction;

    public ClickListener(Context context, BaseAction baseAction) {
        super(context, baseAction);
        patternAction = (PatternAction) baseAction;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pattern_imgbtn:
            case R.id.pattern_tv0:
                Pattern_Frag pattern_frag = Methods.cast(v.getTag());

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(pattern_frag);
                transaction.commit();
                break;

            case R.id.pattern_btn0:
                String sku_code_str = Methods.cast(v.getTag());
                patternAction.OnAddInCart(sku_code_str);
                break;

            default:
                break;
        }
    }
}
