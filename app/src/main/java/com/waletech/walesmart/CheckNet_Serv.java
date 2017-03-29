package com.waletech.walesmart;

import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.waletech.walesmart.base.Base_Serv;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicAction.GetAddressAction;
import com.waletech.walesmart.publicAction.GetBrandAction;
import com.waletech.walesmart.publicAction.GetServiceVersionAction;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.sharedinfo.SharedAction;

import org.json.JSONException;

/**
 * Created by KeY on 2016/7/14.
 */
public class CheckNet_Serv extends Base_Serv {

    private GetServiceVersionAction getServiceVersionAction;
    private GetAddressAction getAddressAction;
    private GetBrandAction getBrandAction;

    private NetThread net_thread;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Result", "service create");

        getServiceVersionAction = new GetServiceVersionAction(this, this);
        getAddressAction = new GetAddressAction(this, this);
        getBrandAction = new GetBrandAction(this, this);

        net_thread = new NetThread();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        net_thread.start();

        return super.onStartCommand(intent, flags, startId);
    }

    private class NetThread extends Thread {
        @Override
        public void run() {
            super.run();

            if (Methods.isNetworkAvailable(CheckNet_Serv.this)) {

//                Log.i("Result", "service get");

                Looper.prepare();
                getServiceVersionAction.getVersion();
//                if (new SharedAction(CheckNet_Serv.this).getAppFstLaunch()) {
//                    getServiceVersionAction.getVersion();
//                } else {
//                    getAddressAction.getAddress();
//                }
                Looper.loop();
            }
        }
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.GET_SERVICE:
                getServiceVersionAction.handleResponse(result);

                getAddressAction.getAddress();
                break;

            case HttpTag.GET_ADDRESS:
                getAddressAction.handleResponse(result);

                getBrandAction.getBrand();
                break;

            case HttpTag.GET_BRAND:
                getBrandAction.handleResponse(result);
                onStopService();
                break;

            default:
                break;
        }
    }

    @Override
    public void onNullResponse(String tag) throws JSONException {
        Log.i("Result", "null tag is : " + tag);
        if (tag.equals(HttpTag.GET_SERVICE)) {
            HttpSet.setBaseService("/ClientMainService_v1/", this);
        }
        onStopService();
    }

    private void onStopService() {
//        new SharedAction(this).setAppLaunch(false);
//        new SharedAction(this).setAppFstLaunch(false);
        this.stopSelf();
        this.onDestroy();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
