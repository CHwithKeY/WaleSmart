package com.waletech.walesmart.user.lockInfo.record;

import android.content.Context;
import android.util.Log;

import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.Base_Frag;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicSet.ToastSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by KeY on 2016/7/3.
 */
public class RecordAction extends BaseAction {

    public RecordAction(Context context) {
        super(context);
    }

    public RecordAction(Context context, Base_Frag fragment) {
        super(context);
        this.fragment = fragment;
    }

    private String save_date_time = "";
    private String save_shop_name = "";
    private String save_brand = "";
    private String save_address = "";

    private Base_Frag fragment;

    public void getDefaultRecord() {
        if (!checkNet()) {
            return;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        // 当 shop_name 为 "All"，意思就是获取全部店铺的消息
        String date_time = format.format(new Date());
        String shop_name = "All";
        String brand = "All";
        String address = "All";

        sharedAction.clearLastIdInfo();
        action = BaseAction.ACTION_DEFAULT_REFRESH;

        save_date_time = date_time;
        save_shop_name = shop_name;
        save_brand = brand;
        save_address = address;

        interaction(save_date_time, save_shop_name, save_brand, save_address);
    }

    public void getFilterRecord(String date_time, String shop_name, String brand, String address) {
        Log.i("Result", "filter");
        if (!checkNet()) {
            return;
        }

        sharedAction.clearLastIdInfo();
        action = BaseAction.ACTION_FILTER;

        if (!date_time.equals("")) {
            save_date_time = date_time;
        }
        if (!shop_name.equals("")) {
            save_shop_name = shop_name;
        }
        if (!brand.equals("")) {
            save_brand = brand;
        }
        if (!address.equals("")) {
            save_address = address;
        }

        Log.i("Result", "date " + save_date_time + "---- address " + save_address + "----- shop from " + save_shop_name + "----- brand to " + save_brand);

        interaction(save_date_time, save_shop_name, save_brand, save_address);
    }

    public void getLoadRecord() {
        Log.i("Result", "load");
        if (!checkNet()) {
            return;
        }

        action = BaseAction.ACTION_LOAD_MORE;
        interaction(save_date_time, save_shop_name, save_brand, save_address);
    }

    private void interaction(String date_time, String shop_name, String brand, String address) {
        // HttpAction 的配置
        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_DATE_TIME, HttpSet.KEY_SHOP_NAME, HttpSet.KEY_BRAND, HttpSet.KEY_ADDRESS, HttpSet.KEY_LAST_ID};
        String[] value = {sharedAction.getUsername(), date_time, shop_name, brand, address, "" + sharedAction.getLastId()};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_SHOE_RECORD);
        action.setTag(HttpTag.LOCKINFO_GET_RECORD_LIST);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setMap(key, value);
        if (fragment != null) {
            action.setHandler(new HttpHandler(context, fragment));
        } else {
            action.setHandler(new HttpHandler(context));
        }
        action.interaction();
    }

    public void getManagerRecord() {
        sharedAction.clearLastIdInfo();

        String[] key = {HttpSet.KEY_LAST_ID};
        String[] value = {"" + sharedAction.getLastId()};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_MANAGER_SHOE_RECORD);
        action.setTag(HttpTag.MANAGER_GET_RECORD);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setMap(key, value);
        if (fragment != null) {
            action.setHandler(new HttpHandler(context, fragment));
        } else {
            action.setHandler(new HttpHandler(context));
        }
        action.interaction();
    }

    public ArrayList<ObjectShoe> handleResponse(String result) throws JSONException {
        Log.i("Result", "lock record result is : " + result);

        ArrayList<ObjectShoe> shoeList = new ArrayList<>();
        JSONArray array = new JSONArray(result);

        if (result.equals("") || array.length() == 0) {
            toast.showToast(context.getString(R.string.base_toast_no_more_item));
            return new ArrayList<>();
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
}
