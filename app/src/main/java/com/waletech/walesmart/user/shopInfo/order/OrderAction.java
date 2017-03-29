package com.waletech.walesmart.user.shopInfo.order;

import android.content.Context;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicObject.ObjectOrder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/7/25.
 */
public class OrderAction extends BaseAction {

    public OrderAction(Context context) {
        super(context);
    }

    public void getOrderList() {
        String[] key = {HttpSet.KEY_USERNAME};
        String[] value = {sharedAction.getUsername()};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_ORDER);
        action.setTag(HttpTag.ORDER_GET_ORDER);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public ArrayList<ObjectOrder> handleResponse(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        ArrayList<ObjectOrder> orderList = new ArrayList<>();

        if (array.length() == 0) {
            toast.showToast(context.getString(R.string.base_toast_no_more_item));
            return orderList;
        }

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ObjectOrder order = new ObjectOrder();

            for (int k = 0; k < obj.length(); k++) {
                order.value_set[k] = obj.getString(ObjectOrder.key_set[k]);
            }
            orderList.add(order);
        }

//        String last_id = orderList.get(orderList.size() - 1).getLastId();
//        sharedAction.setLastId(Integer.parseInt(last_id));

        return orderList;
    }
}
