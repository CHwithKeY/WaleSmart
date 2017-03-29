package com.waletech.walesmart.searchResult;

import android.content.Context;
import android.util.Log;

import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/6/29.
 */
public class ResultAction extends BaseAction {

    public ResultAction(Context context) {
        super(context);
    }

    private String save_search_word = "All";

    private String save_brand = "All";
    private String save_gender = "All";

    private String save_province = "All";
    private String save_city = "All";
    private String save_county = "All";

    public void searchFussy(String search_word) {
        if (!checkNet()) {
            return;
        }

        action = BaseAction.ACTION_DEFAULT_REFRESH;
        sharedAction.clearLastIdInfo();

        save_search_word = search_word;
        interaction();
    }

    public void searchFilter(String search_word, String brand, String gender, String province, String city, String county) {
        if (!checkNet()) {
            return;
        }

        Log.i("Result", "net is : " + HttpSet.BASE_URL);

        action = BaseAction.ACTION_FILTER;
        sharedAction.clearLastIdInfo();

        if (!search_word.equals("")) {
            save_search_word = search_word;
        }

        if (!brand.equals("")) {
            save_brand = brand;
        }

        if (!gender.equals("")) {
            save_gender = gender;
        }

        if (!province.equals("")) {
            save_province = province;
        }

        if (!city.equals("")) {
            save_city = city;
        }

        if (!county.equals("")) {
            save_county = county;
        }

        interaction();
    }

    public void resultRefresh() {
        if (!checkNet()) {
            return;
        }

        sharedAction.clearLastIdInfo();
        action = BaseAction.ACTION_DEFAULT_REFRESH;

        searchFilter("", "", "", "", "", "");
    }

    public void resultLoad() {
        if (!checkNet()) {
            return;
        }

        action = BaseAction.ACTION_LOAD_MORE;

        interaction();
    }

    private void interaction() {
        String[] key = {HttpSet.KEY_SEARCH_WORD, HttpSet.KEY_BRAND, HttpSet.KEY_GENDER, HttpSet.KEY_PROVINCE, HttpSet.KEY_CITY, HttpSet.KEY_COUNTY, HttpSet.KEY_LAST_ID};
        String[] value = {save_search_word, save_brand, save_gender, save_province, save_city, save_county, "" + sharedAction.getLastId()};

        Log.i("Result", "brand is : " + save_brand);

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_SEARCH_FILTER);
        action.setDialog(context.getString(R.string.search_progress_title), context.getString(R.string.search_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setTag(HttpTag.RESULT_SEARCH_FILTER);
        action.setMap(key, value);
        action.interaction();
    }

    public ArrayList<ObjectShoe> handleResponse(String result) throws JSONException {
        Log.i("Result", "result is : " + result);

        ArrayList<ObjectShoe> shoeList = new ArrayList<>();
        JSONArray array = new JSONArray(result);

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
        Log.i("Result", "last id is : " + last_id);
        sharedAction.setLastId(Integer.parseInt(last_id));

        return shoeList;
    }
}
