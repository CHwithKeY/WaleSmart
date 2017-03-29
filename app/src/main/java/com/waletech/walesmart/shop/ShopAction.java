package com.waletech.walesmart.shop;

import android.content.Context;
import android.util.Log;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicObject.ObjectArea;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/8/5.
 */
public class ShopAction extends BaseAction {

    public ShopAction(Context context) {
        super(context);
    }

    public void getArea(String shop_name) {
        if (!checkNet()) {
            return;
        }

        String[] key = {HttpSet.KEY_SHOP_NAME};
        String[] value = {shop_name};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_SHOP_AREA);
        action.setTag(HttpTag.SHOP_GET_SHOP_AREA);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setMap(key, value);
        action.setHandler(new HttpHandler(context));
        action.interaction();
    }

    public ArrayList<ObjectArea> handleResponse(String result) throws JSONException {

        Log.i("Result", "get shop area result is : " + result);

        JSONArray array = new JSONArray(result);

        if (array.length() == 0) {
            toast.showToast("该店铺尚未分区，暂未营业");
            return new ArrayList<>();
        }

        ArrayList<ObjectArea> areaList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ObjectArea area = new ObjectArea();

            for (int k = 0; k < obj.length(); k++) {
                area.value_set[k] = obj.getString(ObjectArea.key_set[k]);
            }

            areaList.add(area);
        }

        return areaList;
    }
}
