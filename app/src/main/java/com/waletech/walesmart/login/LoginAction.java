package com.waletech.walesmart.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpResult;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicSet.IntentSet;
import com.waletech.walesmart.sharedinfo.SharedAction;
import com.waletech.walesmart.user.User_Act;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KeY on 2016/6/3.
 */
public class LoginAction extends BaseAction {

    public LoginAction(Context context) {
        super(context);
    }

    public void login(String usn_str, String psd_str) {
        if (!checkNet()) {
            return;
        }

        if (usn_str.equals("") || psd_str.equals("")) {
            toast.showToast(context.getString(R.string.login_toast_usn_psd_isNull));
            return;
        }

        String app_version = Methods.getVersionName(context);

        String[] key = {HttpSet.KEY_USERNAME, HttpSet.KEY_PASSWORD, HttpSet.KEY_VERSION};
        String[] value = {usn_str, psd_str, app_version};

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_LOGIN);
        action.setDialog(context.getString(R.string.login_progress_title), context.getString(R.string.login_progress_msg));
        action.setHandler(new HttpHandler(context));
        action.setTag(HttpTag.LOGIN_LOGIN);
        action.setMap(key, value);
        action.interaction();
    }

    public void handleResponse(String result) throws JSONException {
        // 这个是，成功以后，获取返回结果
        Log.i("Result", "result is : " + result);
        JSONObject obj = new JSONObject(result);
        String login_result = obj.getString(HttpResult.RESULT);

        SharedAction sharedAction = new SharedAction(context);
        toast.showToast(login_result);

        if (login_result.equals(HttpResult.LOGIN_SUCCESS) || login_result.equals(HttpResult.LOGIN_SUCCESS_EN)) {

            // 保存登陆信息
            String username = obj.getString(HttpResult.USERNAME);
            String nickname = obj.getString(HttpResult.NICKNAME);
            sharedAction.setLoginStatus(username, nickname);

            // 将获取的返回值中的 nickName 的值取出来放在intent中传递到 user_act 中去作为显示的用户名
            Intent user_int = new Intent(context, User_Act.class);
            context.startActivity(user_int);
            ((AppCompatActivity) context).finish();
        } else {
            Log.i("Result", "Login Mismatch");
        }
    }

}
