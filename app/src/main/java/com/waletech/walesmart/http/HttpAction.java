package com.waletech.walesmart.http;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.waletech.walesmart.R;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.sharedinfo.SharedAction;
import com.waletech.walesmart.sharedinfo.SharedSet;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by KeY on 2016/3/29.
 */
public final class HttpAction {

    private Context context;
    private HttpHandler handler;

    private String url;

    private HashMap<String, String> map;

    private ProgressDialog dialog;

    private String tag;

    private LineToast toast;

    public HttpAction(Context context) {
        this.context = context;
        toast = new LineToast(context);
    }

    public void setUrl(String url) {
//        this.url = url;
        this.url = HttpSet.BASE_URL + url;
        Log.i("Result", "url is : " + this.url);
    }

    public void setMap(String[] key, String[] value) {
        map = new HashMap<>();

        switch (Methods.getLanguage(context)) {
            case "ch":
                map.put(HttpSet.KEY_LANGUAGE, SharedSet.LANGUAGE_CHINESE);
                break;

            case "en":
                map.put(HttpSet.KEY_LANGUAGE, SharedSet.LANGUAGE_ENGLISH);
                break;

            default:
                map.put(HttpSet.KEY_LANGUAGE, SharedSet.LANGUAGE_CHINESE);
                break;
        }

//        if (new SharedAction(context).getAppLanguage().equals(SharedSet.LANGUAGE_CHINESE)) {
//            map.put(HttpSet.KEY_LANGUAGE, SharedSet.LANGUAGE_CHINESE);
//        } else {
//            map.put(HttpSet.KEY_LANGUAGE, SharedSet.LANGUAGE_ENGLISH);
//        }

        for (int i = 0; i < key.length; i++) {
            map.put(key[i], value[i]);
        }
        Log.i("Result", "map value is : " + map.get(HttpSet.KEY_LANGUAGE));

    }

    public void setDialog(String title, String msg) {
        dialog = new ProgressDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(msg);

        // 不允许用户点击 dialog 外部从而导致 dialog 消失
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);

        // 不允许用户点击“返回键”从而导致 dialog 消失
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0;
            }
        });
    }

    public void setHandler(HttpHandler handler) {
        this.handler = handler;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    // 与网络交互
    public void interaction() {
        if (dialog != null) {
            dialog.show();
        }

        RequestQueue queue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, resListener, errListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return map;
            }
        };

        // 这条语句意思是让这个傻逼的 volley 能够重连，保持时间为10s，不然TMD服务器还没反应过来呢
        request.setRetryPolicy(new DefaultRetryPolicy(10 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);

    }

    private Response.Listener<String> resListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String s) {
            if (dialog != null) {
                dialog.dismiss();
            }

            HashMap<String, String> res_map = new HashMap<>();
            res_map.put(tag, s);

            // 发送id
            Message msg = new Message();
            msg.what = HttpSet.httpResponse;
            msg.obj = res_map;
            handler.sendMessage(msg);
        }
    };

    private Response.ErrorListener errListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            // 这个是Volley自带的，例如网络404等等错误都可以在这里处理
            if (dialog != null) {
                dialog.dismiss();
            }

            Message msg = new Message();
            msg.what = HttpSet.httpNull;
            msg.obj = tag;
            handler.sendMessage(msg);

//            handler.sendEmptyMessage(HttpSet.httpNull);

            toast.showToast(context.getString(R.string.base_toast_net_worse));
        }
    };

    //
    public static boolean checkNet(Context context) {
        LineToast toast = new LineToast(context);

        if (!Methods.isNetworkAvailable(context)) {
            toast.showToast(context.getString(R.string.base_toast_net_down));
        }

        return Methods.isNetworkAvailable(context);
    }

//    public static boolean checkNetWithoutToast(Context context) {
//        return Methods.isNetworkAvailable(context);
//    }

    // 具体的检测网络的方法
//    private static boolean isNetworkAvailable(Context context) {
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
