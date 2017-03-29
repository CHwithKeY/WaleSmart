package com.waletech.walesmart.user.shopInfo.cart;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.BaseClickListener;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicSet.MapSet;
import com.waletech.walesmart.publicSet.ToastSet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by KeY on 2016/7/2.
 */
class ClickListener extends BaseClickListener {

    private CartAction cartAction;
    private LineToast toast;

    public ClickListener(Context context, BaseAction baseAction) {
        super(context, baseAction);
        cartAction = (CartAction) baseAction;

        toast = new LineToast(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rv_pattern_fl:
                onHidePattern(v);
                break;

            case R.id.rv_make_order_tv:
                onCreateOrderConfirmDialog(v);
                break;

            default:
                break;
        }
    }

    private void onHidePattern(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        }
    }

    private void onCreateOrderConfirmDialog(final View view) {
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.cart_dialog_order_confirm_title))
                .setMessage(context.getString(R.string.cart_dialog_order_confirm_msg))
                .setPositiveButton(context.getString(R.string.base_dialog_btn_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onMakeOrder(view);
                    }
                })
                .setNegativeButton(context.getString(R.string.base_dialog_btn_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }

    private void onMakeOrder(View view) {
        HashMap<String, Object> map = Methods.cast(view.getTag());

        ArrayList<ObjectShoe> shoeList = Methods.cast(map.get(MapSet.KEY_SHOE_LIST));
        ArrayList<String> checkList = Methods.cast(map.get(MapSet.KEY_CHECK_LIST));

        if (checkList == null || checkList.size() == 0) {
            toast.showToast(context.getString(R.string.base_toast_no_item_been_selected));
            return;
        }

        StringBuilder sku_code_builder = new StringBuilder("");

        for (int i = 0; i < checkList.size(); i++) {
            int position = Integer.parseInt(checkList.get(i));
            ObjectShoe shoe = shoeList.get(position);

            if (i == (checkList.size() - 1)) {
                sku_code_builder.append(shoe.getRemark());
            } else {
                sku_code_builder.append(shoe.getRemark()).append("-");
            }
        }

        cartAction.onCreateOrder(sku_code_builder.toString());
    }

}
