package com.waletech.walesmart.user.shopInfo.favourite;

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
import com.waletech.walesmart.publicSet.ToastSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/6/30.
 */
public class FavAction extends BaseAction {

    public final static int OPERATION_ADD = 0;
    public final static int OPERATION_DELETE = 1;
    public final static int OPERATION_DELETE_BATCH = 2;

    private String save_brand = "";
    private String save_shop_name = "";

    public FavAction(Context context) {
        super(context);
    }

    // default
    public void getFavList() {
        if (!checkNet()) {
            return;
        }

        save_brand = "All";
        save_shop_name = "All";

        sharedAction.clearLastIdInfo();
        action = BaseAction.ACTION_DEFAULT_REFRESH;

        interaction();
    }

    public void getFilterList(String brand, String shop_name) {
        Log.i("Result", "get filter");

        if (!brand.equals("")) {
            save_brand = brand;
        }

        if (!shop_name.equals("")) {
            save_shop_name = shop_name;
        }

        sharedAction.clearLastIdInfo();
        action = BaseAction.ACTION_FILTER;

        interaction();
    }

    public void getLoadList() {
        if (!checkNet()) {
            return;
        }

        action = BaseAction.ACTION_LOAD_MORE;

        interaction();
    }

    private void interaction() {
        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_BRAND, HttpSet.KEY_SHOP_NAME, HttpSet.KEY_LAST_ID};
        String[] value = {sharedAction.getUsername(), save_brand, save_shop_name, "" + sharedAction.getLastId()};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_FAVOURITE);
        action.setTag(HttpTag.FAVOURITE_GET_FAVOURITE_LIST);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public void checkFav(String epc_code) {
        if (!checkNet()) {
            return;
        }

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_EPC_CODE};
        String[] value = {sharedAction.getUsername(), epc_code};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_CHECK_FAVOURITE);
        action.setTag(HttpTag.FAVOURITE_CHECK_FAVOURITE);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public void operate(int operation_code, String epc_code) {
        if (!checkNet()) {
            return;
        }

        sharedAction.clearLastIdInfo();

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_EPC_CODE};
        String[] value = {sharedAction.getUsername(), epc_code};

        HttpAction action = new HttpAction(context);

        switch (operation_code) {
            case OPERATION_ADD:
                action.setUrl(HttpSet.URL_ADD_FAVOURITE);
                action.setTag(HttpTag.FAVOURITE_ADD_FAVOURITE);
                action.setDialog(context.getString(R.string.base_add_progress_title), context.getString(R.string.base_add_progress_msg));
                break;

            case OPERATION_DELETE:
                action.setUrl(HttpSet.URL_DELETE_FAVOURITE);
                action.setTag(HttpTag.FAVOURITE_DELETE_FAVOURITE);
                action.setDialog(context.getString(R.string.base_cancel_progress_title), context.getString(R.string.base_cancel_progress_msg));
                break;

            case OPERATION_DELETE_BATCH:
                action.setUrl(HttpSet.URL_DELETE_BATCH_FAVOURITE);
                action.setTag(HttpTag.FAVOURITE_DELETE_BATCH_FAVOURITE);
                action.setDialog(context.getString(R.string.base_delete_progress_title), context.getString(R.string.base_delete_progress_msg));
                break;

            default:
                break;
        }

        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public ArrayList<ObjectShoe> handleListResponse(String result) throws JSONException {
        Log.i("Result", "Fav result is : " + result);

        JSONArray array = new JSONArray(result);
        ArrayList<ObjectShoe> shoeList = new ArrayList<>();

        if (array.length() == 0) {
            toast.showToast(context.getString(R.string.base_toast_no_more_item));
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
        Log.i("Result", "last_id is : " + last_id);
        sharedAction.setLastId(Integer.parseInt(last_id));

        return shoeList;
    }

    public void handleResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        toast.showToast(obj.getString(HttpResult.RESULT));
    }
}
