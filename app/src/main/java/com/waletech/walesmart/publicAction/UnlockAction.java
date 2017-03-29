package com.waletech.walesmart.publicAction;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpHandler;
import com.waletech.walesmart.http.HttpResult;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.zbar.lib.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by KeY on 2016/6/27.
 */
public class UnlockAction extends BaseAction {

    public final static int REQUEST_MAIN = 0;

    public UnlockAction(Context context) {
        super(context);
    }

    public void check() {

        if (!checkNet()) {
            return;
        }

        if (!checkLoginStatus()) {
            return;
        }

        String client_id = PushManager.getInstance().getClientid(context);
        Log.i("Result", "client id is ; " + client_id);

        Intent scan_int = new Intent(context, CaptureActivity.class);
        AppCompatActivity activity = (AppCompatActivity) context;
        activity.startActivityForResult(scan_int, REQUEST_MAIN);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void unlock(String smark_id) {
        if (!checkNet()) {
            return;
        }

        String client_id = PushManager.getInstance().getClientid(context);

        String[] key = {HttpSet.KEY_SMARK_ID, HttpSet.KEY_USERNAME, HttpSet.KEY_NICKNAME, HttpSet.KEY_CLIENT_ID};
        String[] value = {smark_id, sharedAction.getUsername(), sharedAction.getNickname(), client_id};

//        for (int i = 0; i < key.length; i++) {
//            Log.i("Result","key is : " + i + "-set is : " + key[i]);
//            Log.i("Result","value is : " + i + "-set is : " + value[i]);
//        }

        HttpAction action = new HttpAction(context);
        action.setUrl(HttpSet.URL_UNLOCK);
        action.setTag(HttpTag.UNLOCK_UNLOCK);
        action.setHandler(new HttpHandler(context));
        action.setDialog(context.getString(R.string.unlock_progress_title), context.getString(R.string.unlock_progress_msg));
        action.setMap(key, value);
        action.interaction();
    }

    public void handleResponse(String result) throws JSONException {
        JSONObject obj = new JSONObject(result);
        toast.showToast(obj.getString(HttpResult.RESULT));
    }
}
