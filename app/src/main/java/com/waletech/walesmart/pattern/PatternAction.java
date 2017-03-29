package com.waletech.walesmart.pattern;

import android.content.Context;
import android.util.Log;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.Base_Frag;
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
 * Created by KeY on 2016/7/13.
 */
public class PatternAction extends BaseAction {
    private Base_Frag fragment;

    private String save_brand = "";
    private String save_design = "";
    private String save_color = "";
    private String save_size = "";

    public PatternAction(Context context, Base_Frag fragment) {
        super(context);
        this.fragment = fragment;
    }

    public void setSaveSize(String save_size) {
        this.save_size = save_size;
    }

    public void getDefaultStock(String epc_code) {
        if (!checkNet()) {
            return;
        }

        String[] key = {HttpSet.KEY_EPC_CODE};
        String[] value = {epc_code};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_DEFAULT_STOCK);
        action.setTag(HttpTag.PATTERN_GET_DEFAULT_STOCK);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context, fragment));

        action.interaction();
    }

    public void getPattern(String brand, String design) {
        if (!checkNet()) {
            return;
        }

        String[] key = {HttpSet.KEY_BRAND, HttpSet.KEY_PRODUCT_NAME};
        String[] value = {brand, design};

        save_brand = brand;
        save_design = design;

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_PATTERN);
        action.setTag(HttpTag.PATTERN_GET_PATTERN);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context, fragment));

        action.interaction();
    }

    public void getSizeMatch(String color) {
        if (!checkNet()) {
            return;
        }
        String[] key = {HttpSet.KEY_BRAND, HttpSet.KEY_PRODUCT_NAME, HttpSet.KEY_COLOR};
        String[] value = {save_brand, save_design, color};

        save_color = color;

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_SIZE);
        action.setTag(HttpTag.PATTERN_GET_SIZE);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context, fragment));

        action.interaction();
    }

    public void getImageMatch(String color) {
        if (!checkNet()) {
            return;
        }
        String[] key = {HttpSet.KEY_BRAND, HttpSet.KEY_PRODUCT_NAME, HttpSet.KEY_COLOR};
        String[] value = {save_brand, save_design, color};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_IMAGE);
        action.setTag(HttpTag.PATTERN_GET_IMAGE);
        action.setMap(key, value);
        action.setHandler(new HttpHandler(context, fragment));

        action.interaction();
    }

    public void getShoeDetails(String size) {
        if (!checkNet()) {
            return;
        }

        save_size  = size;

        String[] key = {HttpSet.KEY_BRAND, HttpSet.KEY_PRODUCT_NAME, HttpSet.KEY_COLOR, HttpSet.KEY_SIZE};
        String[] value = {save_brand, save_design, save_color, size};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_SHOE_DETAILS_BY_SKU);
        action.setTag(HttpTag.PATTERN_GET_SHOE_DETAILS);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context, fragment));

        action.interaction();
    }

    public void getColorMatch(String size) {
        String[] key = {HttpSet.KEY_BRAND, HttpSet.KEY_PRODUCT_NAME, HttpSet.KEY_SIZE};
        String[] value = {save_brand, save_design, size};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_GET_COLOR);
        action.setTag(HttpTag.PATTERN_GET_COLOR);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context, fragment));

        action.interaction();
    }

    public void OnAddInCart(String sku_code) {
        if (!checkNet()) {
            return;
        }

        if (!checkLoginStatus()) {
            return;
        }

        if (save_color.equals("") || save_size.equals("")) {
            toast.showToast("请选择颜色或尺码");
            return;
        }

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_SKU_CODE};
        String[] value = {sharedAction.getUsername(), sku_code};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_ADD_CART);
        action.setTag(HttpTag.PATTERN_ADD_CART);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_add_progress_title), context.getString(R.string.base_add_progress_msg));
        action.setHandler(new HttpHandler(context, fragment));

        action.interaction();
    }

    public ObjectShoe handleDefaultStockResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        ObjectShoe shoe = new ObjectShoe();

        for (int i = 0; i < obj.length(); i++) {
            shoe.value_set[i] = obj.getString(ObjectShoe.key_set[i]);
        }

        return shoe;
    }

    public ArrayList<ObjectShoe> handlePatternResponse(String result) throws JSONException {
        Log.i("Result", "pattern result is : " + result);
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

    public ArrayList<String> handleSizeResponse(String result) throws JSONException {
        Log.i("Result", "size result is : " + result);
        JSONArray array = new JSONArray(result);
        ArrayList<String> sizeList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            sizeList.add(array.getString(i));
        }
        return sizeList;
    }

    public String handleImageResponse(String result) throws JSONException {
        Log.i("Result", "get shoe image in pattern is : " + result);

        JSONObject obj = new JSONObject(result);
        return obj.getString(HttpResult.RESULT);
    }

    public ObjectShoe handleShoeDetailsResponse(String result) throws JSONException {
        Log.i("Result", "get shoe detail in pattern is : " + result);

        JSONObject obj = new JSONObject(result);
        ObjectShoe shoe = new ObjectShoe();

        for (int k = 0; k < obj.length(); k++) {
            shoe.value_set[k] = obj.getString(ObjectShoe.key_set[k]);
        }

        return shoe;
    }

    public void handleColorResponse(String result) throws JSONException {
        Log.i("Result", "color result is : " + result);
        JSONArray array = new JSONArray(result);
    }

    public void handleAddCartResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        toast.showToast(obj.getString(HttpResult.RESULT));
    }
}
