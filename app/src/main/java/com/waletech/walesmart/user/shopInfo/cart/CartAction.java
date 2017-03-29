package com.waletech.walesmart.user.shopInfo.cart;

import android.content.Context;
import android.util.Log;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpResult;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicObject.ObjectShoe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/6/30.
 */
public class CartAction extends BaseAction {

    public final static int OPERATION_ADD = 0;
    public final static int OPERATION_DELETE = 1;

    public CartAction(Context context) {
        super(context);
    }

    public void getCartList() {
        if (!checkNet()) {
            return;
        }

        sharedAction.clearLastIdInfo();
        action = BaseAction.ACTION_DEFAULT_REFRESH;

        getInteraction();
    }

    public void getLoadList() {
        if (!checkNet()) {
            return;
        }

        action = BaseAction.ACTION_LOAD_MORE;

        getInteraction();
    }

    // 这里的 sku_code_series 是多个 sku_code 通过"-"连接在一起
    public void onCreateOrder(String sku_code_series) {
        String[] key = {
                 HttpSet.KEY_USERNAME, HttpSet.KEY_SKU_CODE};
        String[] value = {
                sharedAction.getUsername(), sku_code_series};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_CREATE_ORDER);
        action.setTag(HttpTag.CART_CREATE_ORDER);
        action.setDialog(context.getString(R.string.base_add_progress_title), context.getString(R.string.base_add_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    private void getInteraction() {
        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_LAST_ID};
        String[] value = {sharedAction.getUsername(), "" + sharedAction.getLastId()};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_CART);
        action.setTag(HttpTag.CART_GET_CART_LIST);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public void deleteCart(String sku_code_series) {
        if (!checkNet()) {
            return;
        }

        sharedAction.clearLastIdInfo();

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_SKU_CODE};
        String[] value = {sharedAction.getUsername(), sku_code_series};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_DELETE_CART);
        action.setTag(HttpTag.CART_DELETE_CART);
        action.setDialog(context.getString(R.string.base_delete_progress_title), context.getString(R.string.base_delete_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public void updateCount(String sku_code, int new_count) {
        if (!checkNet()) {
            return;
        }

        sharedAction.clearLastIdInfo();

        updateInteraction(sku_code, new_count);
    }

    public void updateProduct() {

    }

    private void updateInteraction(String sku_code, int new_count) {
        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_SKU_CODE, HttpSet.KEY_NEW_COUNT};
        String[] value = {sharedAction.getUsername(), sku_code, "" + new_count};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_UPDATE_CART);
        action.setTag(HttpTag.CART_UPDATE_CART);
        action.setDialog(context.getString(R.string.base_refresh_progress_title), context.getString(R.string.base_refresh_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public ArrayList<ObjectShoe> handleListResponse(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        ArrayList<ObjectShoe> shoeList = new ArrayList<>();

        if (array.length() == 0) {
            return shoeList;
        }

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ObjectShoe shoe = new ObjectShoe();

            for (int k = 0; k < obj.length(); k++) {
                shoe.value_set[k] = obj.getString(ObjectShoe.key_set[k]);
            }
            shoeList.add(shoe);
        }

        String last_id = shoeList.get(shoeList.size() - 1).getLastId();
        sharedAction.setLastId(Integer.parseInt(last_id));

        return shoeList;
    }

    public void handleResponse(String result) throws JSONException {
        Log.i("Result", "cart response is : " + result);

        JSONObject obj = new JSONObject(result);
        toast.showToast(obj.getString(HttpResult.RESULT));
    }

}
