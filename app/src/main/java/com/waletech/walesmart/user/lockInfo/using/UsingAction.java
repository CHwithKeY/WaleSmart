package com.waletech.walesmart.user.lockInfo.using;

import android.content.Context;

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

import java.util.ArrayList;

/**
 * Created by KeY on 2016/7/3.
 */
public class UsingAction extends BaseAction {

    private Base_Frag fragment;

    public UsingAction(Context context) {
        super(context);
    }

    public UsingAction(Context context, Base_Frag fragment) {
        super(context);
        this.fragment = fragment;
    }

    public void getUsingList() {
        if (!checkNet()) {
            return;
        }

        String[] key = {HttpSet.KEY_USERNAME};
        String[] value = {sharedAction.getUsername()};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_SHOE_USING);
        action.setTag(HttpTag.LOCKINFO_GET_USING_LIST);
        if (fragment != null) {
            action.setHandler(new HttpHandler(context, fragment));
        } else {
            action.setHandler(new HttpHandler(context));
        }
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setMap(key, value);
        action.interaction();
    }

    public ArrayList<ObjectShoe> handleResponse(String result) throws JSONException {
        JSONArray array = new JSONArray(result);
        ArrayList<ObjectShoe> shoeList = new ArrayList<>();

        if (array.length() == 0) {
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

        return shoeList;
    }
}
