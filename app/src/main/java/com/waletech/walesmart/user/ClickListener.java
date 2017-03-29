package com.waletech.walesmart.user;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.waletech.walesmart.R;
import com.waletech.walesmart.user.editInfo.Edit_Info_Act;
import com.waletech.walesmart.user.lockInfo.LockInfo_Act;
import com.waletech.walesmart.user.shopInfo.cart.Cart_Act;

/**
 * Created by KeY on 2016/6/24.
 */
class ClickListener implements View.OnClickListener {

    private Context context;

    public ClickListener(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_head_img1:
                onEditUserHead();
                break;

            case R.id.user_tv0:
                onLockInfo();
                break;

            case R.id.user_tv1:
                onShopCart();
                break;

            case R.id.user_tv2:
                onModifyPsd();
                break;

            default:
                break;
        }
    }


    private void onEditUserHead() {
        Intent edit_int = new Intent(context, Edit_Info_Act.class);
        context.startActivity(edit_int);
    }

    private void onLockInfo() {
        Intent lock_int = new Intent(context, LockInfo_Act.class);
        context.startActivity(lock_int);
    }

    private void onShopCart() {
        Intent lock_int = new Intent(context, Cart_Act.class);
        context.startActivity(lock_int);
    }

    private void onModifyPsd() {

    }
}
