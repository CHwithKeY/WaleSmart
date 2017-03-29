package com.waletech.walesmart.comment;

import android.content.Context;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpResult;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.product.ProductAction;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicSet.MapSet;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by KeY on 2016/7/29.
 */
public class CommentAction extends BaseAction {

    private ProductAction productAction;

    public CommentAction(Context context) {
        super(context);

        productAction = new ProductAction(context);
    }

    public void getShoeDetails(String epc_code) {
        productAction.getShoeDetails(epc_code);
    }

    public void commitComment(String epc_code, HashMap<String, String> comment_map) {
        String exp_width = comment_map.get(MapSet.KEY_EXP_WIDTH);
        String exp_material = comment_map.get(MapSet.KEY_EXP_MATERIAL);
        String exp_sport = comment_map.get(MapSet.KEY_EXP_SPORT);
        String exp_advice = comment_map.get(MapSet.KEY_EXP_ADVICE);

        String[] key = {
                HttpSet.KEY_USERNAME, HttpSet.KEY_EPC_CODE,
                HttpSet.KEY_EXP_WIDTH, HttpSet.KEY_EXP_MATERIAL,
                HttpSet.KEY_EXP_SPORT, HttpSet.KEY_EXP_ADVICE
        };
        String[] value = {
                sharedAction.getUsername(), epc_code,
                exp_width, exp_material,
                exp_sport, exp_advice
        };

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_COMMENT_COMMIT);
        action.setTag(HttpTag.COMMENT_COMMIT);
        action.setMap(key, value);
        action.setDialog(context.getString(R.string.base_search_progress_title), context.getString(R.string.base_search_progress_msg));
        action.setHandler(new HttpHandler(context));

        action.interaction();
    }

    public ObjectShoe handleShoeDetails(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        ObjectShoe shoe = new ObjectShoe();

        for (int i = 0; i < obj.length(); i++) {
            shoe.value_set[i] = obj.getString(ObjectShoe.key_set[i]);
        }

        return shoe;
    }

    public void handleCommentResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        toast.showToast(obj.getString(HttpResult.RESULT));
    }
}
