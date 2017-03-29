package com.waletech.walesmart.product;

import android.content.Context;
import android.util.Log;

import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.R;
import com.waletech.walesmart.publicAction.UnlockAction;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.user.shopInfo.favourite.FavAction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/6/30.
 */
public class ProductAction extends BaseAction {

    private static UnlockAction unlockAction;
    private static FavAction favAction;

    public ProductAction(Context context) {
        super(context);

        unlockAction = new UnlockAction(context);
        favAction = new FavAction(context);
    }

    public void getShoeDetails(String epc_code) {
        Log.i("Result", "product get epc is : " + epc_code);

        String[] key = {HttpSet.KEY_EPC_CODE};
        String[] value = {epc_code};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_SHOE_DETAILS);
        action.setTag(HttpTag.PRODUCT_GET_SHOE_DETAILS);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));

        action.interaction();
    }

    public void getOtherShop(String epc_code) {
        String[] key = {HttpSet.KEY_EPC_CODE};
        String[] value = {epc_code};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_SHOE_OTHER_SHOP);
        action.setTag(HttpTag.PRODUCT_GET_SHOE_OTHER_SHOP);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));

        action.interaction();
    }

    public void unlockCheck() {
        unlockAction.check();
    }

    public void unlock(String smark_id) {
        unlockAction.unlock(smark_id);
    }

    public void onCheckFavourite(String epc_code) {
        favAction.checkFav(epc_code);
    }

    public void onFavouriteOperate(int operation_code, String epc_code) {
        favAction.operate(operation_code, epc_code);
    }

    public ObjectShoe handleDetailsResponse(String result) throws JSONException {
        Log.i("Result", "result is : " + result);

        JSONObject obj = new JSONObject(result);
        ObjectShoe shoe = new ObjectShoe();

        for (int i = 0; i < obj.length(); i++) {
            shoe.value_set[i] = obj.getString(ObjectShoe.key_set[i]);
        }

        return shoe;
    }

    public ArrayList<String> handleOtherShopResponse(String result) throws JSONException {
        Log.i("Result", "other shop result is : " + result);

        JSONArray array = new JSONArray(result);
        ArrayList<String> shopNameList = new ArrayList<>();

        if (array.length() == 0) {
            toast.showToast("没有查到其他拥有该产品的店铺");
            return new ArrayList<>();
        }

        for (int i = 0; i < array.length(); i++) {
            String shop_name = array.getString(i);
            shopNameList.add(shop_name);
        }

        return shopNameList;
    }

}
