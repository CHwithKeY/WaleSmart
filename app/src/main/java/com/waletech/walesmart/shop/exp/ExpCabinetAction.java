package com.waletech.walesmart.shop.exp;

import android.content.Context;
import android.util.Log;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.shop.exp.http.ExpHttpSet;
import com.waletech.walesmart.shop.exp.http.ExpHttpTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by spilkaka on 2017/8/7.
 */

public class ExpCabinetAction extends BaseAction {

    public ExpCabinetAction(Context context) {
        super(context);
    }

    public void getCabinetStuff(String smark_num) {
        if (!checkNet()) {
            return;
        }

        String[] key = {ExpHttpSet.KEY_SMARK_NUM};
        String[] value = {smark_num};

        Log.i("Result", "smark num send is :" + smark_num);

        HttpAction action = new HttpAction(context);
        action.setUrl(ExpHttpSet.URL_GET_EXP_SHOP_CABINET_STUFF);
        action.setTag(ExpHttpTag.EXP_SHOP_GET_CABINET_STUFF);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setMap(key, value);
        action.setHandler(new HttpHandler(context));
        action.interaction();
    }

    public ArrayList<ObjectShoe> handleCabinetStuffResponse(String result) throws JSONException {
        Log.i("Result", "get Result is : " + result);

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
