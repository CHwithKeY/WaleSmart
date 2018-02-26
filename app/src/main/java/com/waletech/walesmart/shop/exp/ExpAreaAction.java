package com.waletech.walesmart.shop.exp;

import android.content.Context;
import android.util.Log;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.Base_Frag;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.shop.exp.http.ExpHttpResult;
import com.waletech.walesmart.shop.exp.http.ExpHttpSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by spilkaka on 2017/11/16.
 */

public class ExpAreaAction extends BaseAction {

    private Base_Frag fragment;

    public ExpAreaAction(Context context) {
        super(context);
    }

    public ExpAreaAction(Context context, Base_Frag fragment) {
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
        action.setUrl(ExpHttpSet.URL_GET_EXP_SHOP_SHOE);
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

    public ArrayList<ObjectSmark> handleExpListResponse(String result) throws JSONException {
        Log.i("Result", "result is : " + result);

        JSONArray array = new JSONArray(result);

        ArrayList<ObjectSmark> smarkList = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            ObjectSmark smark = new ObjectSmark();

            JSONArray shoeArray = obj.getJSONArray(ExpHttpResult.SHOE_LIST);
            ArrayList<ObjectShoe> shoeList = new ArrayList<>();

            for (int j = 0; j < shoeArray.length(); j++) {
                JSONObject shoeObj = shoeArray.getJSONObject(j);
                ObjectShoe shoe = new ObjectShoe();

                for (int k = 0; k < shoeObj.length(); k++) {
                    shoe.value_set[k] = shoeObj.getString(ObjectShoe.key_set[k]);
                }
                shoeList.add(shoe);
            }

            smark.setShoeList(shoeList);
            smark.setAreaName(obj.getString(ExpHttpResult.SMARK_AREA_NAME));
            smark.setEpcSize(obj.getString(ExpHttpResult.EPC_SIZE));
            smark.setShopName(obj.getString(ExpHttpResult.SHOP_NAME));
            smark.setSmarkNum(obj.getString(ExpHttpResult.SMARK_NUM));
            smark.setSmarkType(obj.getString(ExpHttpResult.SMARK_TYPE));
            smark.setLastId(obj.getString(ExpHttpResult.LAST_ID));
            smark.setRemark(obj.getString(ExpHttpResult.REMARK));

            smarkList.add(smark);

        }

        return smarkList;
    }
}
