package com.waletech.walesmart.user.shopInfo.order;

import android.content.Context;
import android.util.Log;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicObject.ObjectShoe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/7/26.
 */
public class OrderItemAction extends BaseAction {

    public OrderItemAction(Context context) {
        super(context);
    }

    public void getShoeList(String order_num) {
        String[] key = {HttpSet.KEY_ORDER_NUM};
        String[] value = {order_num};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_ORDER_ITEM);
        action.setTag(HttpTag.ORDER_GET_ORDER_ITEM);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public ArrayList<ObjectShoe> handleResponse(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        ArrayList<ObjectShoe> shoeList = new ArrayList<>();

        Log.i("Result", "order item result is : " + result);

        if (array.length() == 0) {
            toast.showToast("没有查找到更多的数据");
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

//        String last_id = shoeList.get(shoeList.size() - 1).getLastId();
//        Log.i("Result", "last_id is : " + last_id);
//        sharedAction.setLastId(Integer.parseInt(last_id));

        return shoeList;
    }
}
