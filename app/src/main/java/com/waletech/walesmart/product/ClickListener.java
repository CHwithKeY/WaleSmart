package com.waletech.walesmart.product;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.waletech.walesmart.user.authInfo.PermissionAction;
import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.BaseClickListener;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.pattern.Pattern_Frag;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicSet.MapSet;
import com.waletech.walesmart.publicSet.PermissionSet;
import com.waletech.walesmart.user.shopInfo.cart.Cart_Act;
import com.waletech.walesmart.user.shopInfo.favourite.FavAction;

import java.util.HashMap;

/**
 * Created by KeY on 2016/6/30.
 */
class ClickListener extends BaseClickListener {

    private ProductAction productAction;

    public ClickListener(Context context, BaseAction baseAction) {
        super(context, baseAction);

        productAction = (ProductAction) baseAction;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_tv1:
                onShowOther(v);
                break;

            case R.id.product_imgbtn0:
                if (PermissionAction.checkAutoRequest(context, Manifest.permission.CAMERA, PermissionSet.CAMERA)) {
                    productAction.unlockCheck();
                }
                break;

            case R.id.product_imgbtn1:
                if (!new BaseAction(v.getContext()).checkLoginStatus()) {
                    return;
                }

                HashMap<String, Object> map = Methods.cast(v.getTag());
                boolean isHaveFav = (boolean) map.get(MapSet.KEY_IS_HAVE_FAVOURITE);
                String epc_code = (String) map.get(MapSet.KEY_EPC_CODE);

                if (isHaveFav) {
                    productAction.onFavouriteOperate(FavAction.OPERATION_DELETE, epc_code);
                } else {
                    productAction.onFavouriteOperate(FavAction.OPERATION_ADD, epc_code);
                }
                break;

            case R.id.product_imgbtn2:
                if (!new BaseAction(v.getContext()).checkLoginStatus()) {
                    return;
                }
                Intent cart_int = new Intent(context, Cart_Act.class);
                context.startActivity(cart_int);
                break;

            case R.id.product_btn:
                ObjectShoe shoe = Methods.cast(v.getTag());

                Bundle bundle = new Bundle();
                bundle.putString("epc_code", shoe.getEpcCode());
                bundle.putString("brand", shoe.getBrand());
                bundle.putString("design", shoe.getDesign());

                FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                Pattern_Frag pattern_frag = new Pattern_Frag();
                pattern_frag.setArguments(bundle);

                transaction.add(R.id.product_rl, pattern_frag, "pattern_frag");
                transaction.commit();
                break;
        }
    }

    private void onShowOther(View v) {
        if (!HttpAction.checkNet(context)) {
            return;
        }

        String epc_code = Methods.cast(v.getTag());
        productAction.getOtherShop(epc_code);
    }

}
