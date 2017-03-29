package com.waletech.walesmart.user.editInfo;

import android.content.Context;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpResult;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.user.User_Act;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KeY on 2016/8/29.
 */
public class EditInfoAction extends BaseAction {

    public EditInfoAction(Context context) {
        super(context);
    }

    public void updateNickname(String nickname) {
        if (!checkNet()) {
            return;
        }

        String[] key = {
                HttpSet.KEY_USERNAME, HttpSet.KEY_NICKNAME};
        String[] value = {
                sharedAction.getUsername(), nickname};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_EDIT_INFO_UPDATE_NICKNAME);
        action.setTag(HttpTag.EDIT_INFO_UPDATE_NICKNAME);
        action.setDialog(context.getString(R.string.base_refresh_progress_title), context.getString(R.string.base_refresh_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public void updatePassword(String old_psd, String new_psd) {
        if (!checkNet()) {
            return;
        }

        String[] key = {
                HttpSet.KEY_USERNAME, HttpSet.KEY_PASSWORD, HttpSet.KEY_NEW_PASSWORD};
        String[] value = {
                sharedAction.getUsername(), old_psd, new_psd};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_EDIT_INFO_UPDATE_PASSWORD);
        action.setTag(HttpTag.EDIT_INFO_UPDATE_PASSWORD);
        action.setDialog(context.getString(R.string.base_refresh_progress_title), context.getString(R.string.base_refresh_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public void handleResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);

        String response = obj.getString(HttpResult.RESULT);

        switch (response) {
            case HttpResult.UPDATE_NICKNAME_SUCCESS:
            case HttpResult.UPDATE_NICKNAME_SUCCESS_EN:
            case HttpResult.UPDATE_PASSWORD_SUCCESS:
            case HttpResult.UPDATE_PASSWORD_SUCCESS_EN:
                ((Edit_Info_Act) context).getDialog().dismiss();
                break;
        }

//        if (response.equals(HttpResult.UPDATE_PASSWORD_SUCCESS) || response.equals(HttpResult.UPDATE_NICKNAME_SUCCESS)) {
//            ((Edit_Info_Act) context).getDialog().dismiss();
//        }

        toast.showToast(obj.getString(HttpResult.RESULT));

        if (!obj.getString(HttpResult.NICKNAME).equals("")) {
            String nickname = obj.getString(HttpResult.NICKNAME);
            sharedAction.setLoginStatus(sharedAction.getUsername(), nickname);

            if (!User_Act.user_act.isFinishing()) {
                User_Act.user_act.finish();
            }
        }
    }
}
