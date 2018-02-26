package com.waletech.walesmart.main;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.igexin.sdk.PushManager;
import com.waletech.walesmart.DataInit_Serv;
import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.datainfo.DataBaseAction;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.main.experience.Experience_Frag;
import com.waletech.walesmart.main.home.Home_Frag;
import com.waletech.walesmart.publicAction.UnlockAction;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicClass.ViewTadAdapter;
import com.waletech.walesmart.publicSet.PermissionSet;
import com.waletech.walesmart.receiver.NetworkStateChangedReceiver;
import com.waletech.walesmart.update.UpdateAction;
import com.waletech.walesmart.user.authInfo.PermissionAction;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Base_Act {

    private UpdateAction updateAction;

    private UnlockAction unlockAction;
    private ClickListener clickListener;

    private LineToast toast;

    Activity main_act;

    private Splash_Frag splash_frag;

    private NetworkStateChangedReceiver net_receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PushManager.getInstance().initialize(this.getApplicationContext());

        if (sharedAction.getNoticeEnter()) {
            HttpSet.setBaseIP(sharedAction.getNetIP(), this);
            HttpSet.setBaseService(sharedAction.getNetService(), this);

            launchMain();
        } else {
            onRegisterNetBroadcast();

            setupSplashPage();
//            Handler handler = new Handler();
//            if (getIntent().getBooleanExtra(IntentSet.KEY_APP_FST_LAUNCH, true)) {
//                sharedAction.setNoticeEnter(false);
//                sharedAction.setAppFstLaunch(true);
//                sharedAction.setAppLanguage(SharedSet.LANGUAGE_CHINESE);
//
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        onChooseNet();
//                    }
//                }, 1000);
//            } else {
//                launchPrepare();
//            }
        }
    }

    @Override
    protected void setupToolbar() {

    }

    private void setupSplashPage() {
        splash_frag = new Splash_Frag();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.add(R.id.main_fl, splash_frag, "splash_frag");
        transaction.commit();

        removeSplash();

//        sharedAction.setAppLaunch(true);
    }

    private void removeSplash() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(splash_frag);
                transaction.commit();

                launchMain();
            }
        }, 3000);
    }

    private void launchMain() {
        sharedInfoInit();

        varInit();

        setupNetText();

        setupSearchBar();

        setupUserImage();

        setupViewPager();

        setupUnlockImgBtn();

        setupInitAction();
    }

    private void sharedInfoInit() {
        sharedAction.setNoticeEnter(false);
    }

    private void varInit() {
        main_act = this;

        updateAction = new UpdateAction(this);
        unlockAction = new UnlockAction(this);

        clickListener = new ClickListener(this, unlockAction);

        toast = new LineToast(this);

        String client_id = PushManager.getInstance().getClientid(this);
        Log.i("Result", "client id is : " + client_id);

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionSet.WRITE_EXTERNAL_STORAGE);
//        } else {
//            updateAction.checkVersion(Methods.getVersionName(this));
//        }
    }

    private void setupInitAction() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PermissionAction.checkAutoRequest(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionSet.WRITE_EXTERNAL_STORAGE)) {
                    updateAction.checkVersion(Methods.getVersionName(MainActivity.this));
                }
            }
        }, 2000);

        PermissionAction.checkAutoRequest(this, Manifest.permission.ACCESS_COARSE_LOCATION, PermissionSet.ACCESS_COARSE_LOCATION);
        if (PermissionAction.checkAutoRequest(this, Manifest.permission.READ_PHONE_STATE, PermissionSet.READ_PHONE_STATE)) {
            Log.i("Result", "read phone state check success");
        }
    }

    private void onRegisterNetBroadcast() {
        sharedAction.setAppLaunch(true);

        net_receiver = new NetworkStateChangedReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(net_receiver, filter);
    }

    private void setupSearchBar() {
        final TextView search_tv = (TextView) findViewById(R.id.search_bar_tv);
        search_tv.setOnClickListener(clickListener);
    }

    public void setupNetText() {
        final TextView net_tv = (TextView) findViewById(R.id.main_net_tv);
        net_tv.setOnClickListener(clickListener);

        if (HttpSet.getBaseIp().equals(HttpSet.DEDICATED_IP)) {
            net_tv.setText("B");
        } else {
            net_tv.setText("N");
        }
    }

    private void setupUserImage() {
        final ImageView user_img = (ImageView) findViewById(R.id.main_img);
        user_img.setOnClickListener(clickListener);
    }

    private void setupViewPager() {
        final ViewPager main_vp = (ViewPager) findViewById(R.id.main_vp_compat);

        List<Fragment> fragments = new ArrayList<>();

        Home_Frag tab01 = new Home_Frag();
        Experience_Frag tab02 = new Experience_Frag();

        fragments.add(tab01);
        fragments.add(tab02);

        ViewTadAdapter adapter = new ViewTadAdapter(getSupportFragmentManager());
        adapter.setFragments(fragments);

        main_vp.setAdapter(adapter);
        main_vp.setOffscreenPageLimit(fragments.size());
    }

    private void setupUnlockImgBtn() {
        final ImageButton unlock_imgbtn = (ImageButton) findViewById(R.id.main_imgbtn);
        unlock_imgbtn.setOnClickListener(clickListener);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.UPDATE_CHECK_VERSION:
                updateAction.handleResponse(result, false);
                break;

            case HttpTag.UNLOCK_UNLOCK:
                unlockAction.handleResponse(result);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNullResponse() throws JSONException {

    }

    @Override
    public void onPermissionAccepted(int permission_code) {
        switch (permission_code) {
            case PermissionSet.WRITE_EXTERNAL_STORAGE:
                updateAction.checkVersion(Methods.getVersionName(this));
                break;

            case PermissionSet.CAMERA:
                unlockAction.check();
                break;

            case PermissionSet.READ_PHONE_STATE:
                Log.i("Result", "read phone state accepted");
                break;

            default:
                break;
        }
    }

    @Override
    public void onPermissionRefused(int permission_code) {
        switch (permission_code) {
            case PermissionSet.WRITE_EXTERNAL_STORAGE:
                break;

            case PermissionSet.CAMERA:
                toast.showToast(getString(R.string.auth_toast_permission_camera_authorized));
                break;

            case PermissionSet.ACCESS_COARSE_LOCATION:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.auth_dialog_title))
                        .setMessage(getString(R.string.auth_dialog_map_permission_msg))
                        .setPositiveButton(getString(R.string.base_dialog_btn_okay), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UnlockAction.REQUEST_MAIN) {
                String smark_num = data.getStringExtra("scan_result");
                unlockAction.unlock(smark_num);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionAction.handle(this, requestCode, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private long exitTime = 0;

    // 两次点击返回键退出
    public void exitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, getString(R.string.main_toast_exit_confirm), Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(net_receiver);

        Intent stop_serv_int = new Intent(this, DataInit_Serv.class);
        stopService(stop_serv_int);

        DataBaseAction.close();
    }

}
