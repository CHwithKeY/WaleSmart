package com.waletech.walesmart.shop.area;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/7/8.
 */
public class AreaAction extends BaseAction {

    private Base_Frag fragment;

    public AreaAction(Context context) {
        super(context);
    }

    public AreaAction(Context context, Base_Frag fragment) {
        super(context);
        this.fragment = fragment;
    }

    public void getAreaShoe(String area_name) {
        if (!checkNet()) {
            return;
        }

//        String[] key = {HttpSet.KEY_SHOP_NAME};
//        String[] value = {shop_name};

        String[] key = {HttpSet.KEY_AREA_NAME};
        String[] value = {area_name};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_SHOP_SHOE);
        action.setTag(HttpTag.SHOP_GET_SHOP_SHOE);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setMap(key, value);
        if (fragment != null) {
            action.setHandler(new HttpHandler(context, fragment));
        } else {
            action.setHandler(new HttpHandler(context));
        }
        action.interaction();
    }

    public ArrayList<ObjectShoe> handleListResponse(String result) throws JSONException {
        Log.i("Result", "result is : " + result);

        JSONArray array = new JSONArray(result);
        ArrayList<ObjectShoe> shoeList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ObjectShoe shoe = new ObjectShoe();

            for (int k = 0; k < obj.length(); k++) {
                shoe.value_set[k] = obj.getString(ObjectShoe.key_set[k]);
            }
            shoeList.add(shoe);
        }

        return shoeList;
    }

}
