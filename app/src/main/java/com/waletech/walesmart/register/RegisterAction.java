package com.waletech.walesmart.register;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpResult;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.login.Login_Act;
import com.waletech.walesmart.main.MainActivity;
import com.waletech.walesmart.publicClass.Methods;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KeY on 2016/6/1.
 */
public class RegisterAction extends BaseAction {

    public RegisterAction(Context context) {
        super(context);
    }

    public void register(String usn_str, String psd_str, String nicn_str) {
        if (!checkNet()) {
            return;
        }

        if (usn_str.equals("") || psd_str.equals("")) {
            toast.showToast(context.getString(R.string.login_toast_usn_psd_isNull));
            return;
        } else if (nicn_str.equals("")) {
            toast.showToast(context.getString(R.string.reg_toast_nickname_isNull));
            return;
        }

        String app_version = Methods.getVersionName(context);

        String[] key = {
                HttpSet.KEY_USERNAME, HttpSet.KEY_PASSWORD,
                HttpSet.KEY_NICKNAME, HttpSet.KEY_VERSION
        };

        String[] value = {
                usn_str, psd_str,
                nicn_str, app_version
        };

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_REGISTER);
        action.setTag(HttpTag.REGISTER_REGISTER);
        action.setDialog(context.getString(R.string.reg_progress_title), context.getString(R.string.reg_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setMap(key, value);
        action.interaction();
    }

    public void handleResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);

        toast.showToast(obj.getString(HttpResult.RESULT));

        String register_result = obj.getString(HttpResult.RESULT);
        if (register_result.equals(HttpResult.REGISTER_SUCCESS)
                ||
                register_result.equals(HttpResult.REGISTER_SUCCESS_EN)) {
            String username = obj.getString(HttpSet.KEY_USERNAME);
            String nickname = obj.getString(HttpSet.KEY_NICKNAME);

            sharedAction.setLoginStatus(username, nickname);

            // ((AppCompatActivity) context).setResult();
//            Intent user_int = new Intent(context, MainActivity.class);
//            context.startActivity(user_int);
            ((AppCompatActivity) context).finish();
            Login_Act.login_act.finish();
        }
    }


}
