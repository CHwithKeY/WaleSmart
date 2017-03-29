package com.waletech.walesmart.base;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.waletech.walesmart.R;
import com.waletech.walesmart.login.Login_Act;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.sharedinfo.SharedAction;


/**
 * Created by KeY on 2016/6/3.
 */
public class BaseAction {

    public final static int ACTION_DEFAULT_REFRESH = 0;
    public final static int ACTION_LOAD_MORE = 1;
    public final static int ACTION_FILTER = 2;

    protected Context context;
    protected LineToast toast;
    protected SharedAction sharedAction;

    public BaseAction(Context context) {
        this.context = context;

        varInit();
    }

    protected int action = ACTION_DEFAULT_REFRESH;

    public int getAction() {
        return action;
    }

    protected void varInit() {
        if (toast == null) {
            toast = new LineToast(context);
        }

        if (sharedAction == null) {
            sharedAction = new SharedAction(context);
        }
    }

    protected boolean checkNet() {
        varInit();
        if (!Methods.isNetworkAvailable(context)) {
            toast.showToast(context.getString(R.string.base_toast_net_down));
        }

        return Methods.isNetworkAvailable(context);
    }

    public boolean checkLoginStatus() {
        if (!sharedAction.getLoginStatus()) {
            Intent login_int = new Intent(context, Login_Act.class);
            context.startActivity(login_int);
            toast.showToast(context.getString(R.string.base_toast_login_first));
        }

        return sharedAction.getLoginStatus();
    }

    // 具体的检测网络的方法
//    private boolean isNetworkAvailable(Context context) {
//        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
//        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        if (connectivityManager == null) {
//            return false;
//        } else {
//            // 获取NetworkInfo对象
//            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
//
//            if (networkInfo != null && networkInfo.length > 0) {
//                for (int i = 0; i < networkInfo.length; i++) {
//                    //System.out.println(i + "===状态===" + networkInfo[i].getState());
//                    //System.out.println(i + "===类型===" + networkInfo[i].getTypeName());
//                    // 判断当前网络状态是否为连接状态
//                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

}
