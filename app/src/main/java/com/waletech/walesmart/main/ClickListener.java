package com.waletech.walesmart.main;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.BaseClickListener;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.login.Login_Act;
import com.waletech.walesmart.publicAction.UnlockAction;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicSet.IntentSet;
import com.waletech.walesmart.publicSet.PermissionSet;
import com.waletech.walesmart.searchResult.Result_Act;
import com.waletech.walesmart.user.User_Act;
import com.waletech.walesmart.user.authInfo.PermissionAction;

/**
 * Created by KeY on 2016/6/27.
 */
class ClickListener extends BaseClickListener {

    private UnlockAction unlockAction;
    private LineToast toast;

    public ClickListener(Context context, BaseAction baseAction) {
        super(context, baseAction);

        unlockAction = (UnlockAction) baseAction;
        toast = new LineToast(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_bar_tv:
                onResult();
                break;

            case R.id.main_img:
                onUser();
                break;

            case R.id.main_imgbtn:
                if (PermissionAction.checkAutoRequest(context, Manifest.permission.CAMERA, PermissionSet.CAMERA)) {
                    unlockAction.check();
                }
//                unlockAction.unlock("226");
                break;

            case R.id.main_net_tv:
                switchNet();
                break;

            default:
                break;
        }
    }

    private void switchNet() {
        final int NORMAL_NET = 0;
        final int DEDICATED_NET = 1;

        final int DEFAULT_NET = HttpSet.getBaseIp().equals(HttpSet.DEDICATED_IP) ? DEDICATED_NET : NORMAL_NET;

        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.user_menu_item_switch_net_dialog_title))
                .setSingleChoiceItems(new String[]{
                                context.getString(R.string.user_menu_item_switch_net_dialog_item_normal),
                                context.getString(R.string.user_menu_item_switch_net_dialog_item_dedicated)}
                        , DEFAULT_NET, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case NORMAL_NET:
                                        if (DEFAULT_NET != NORMAL_NET) {
                                            HttpSet.setBaseIP(HttpSet.NORMAL_IP, context);
                                            toast.showToast(context.getString(R.string.main_toast_choose_normal_net));
                                            onResetNet();
//                                            onRestartApp();
                                        }
                                        break;

                                    case DEDICATED_NET:
                                        if (DEFAULT_NET != DEDICATED_NET) {
                                            HttpSet.setBaseIP(HttpSet.DEDICATED_IP, context);
                                            toast.showToast(context.getString(R.string.main_toast_choose_dedicated_net));
                                            onResetNet();
//                                            onRestartApp();
                                        }
                                        break;

                                    default:
                                        break;
                                }
                            }
                        }
                )
                .show();
    }

    private void onResetNet() {
        ((MainActivity) context).setupNetText();
    }

//    private void onRestartApp() {
//        Intent main_int = new Intent(context, MainActivity.class);
//        main_int.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        main_int.putExtra(IntentSet.KEY_APP_FST_LAUNCH, false);
//        context.startActivity(main_int);
//        ((AppCompatActivity) context).finish();
//    }

    private void onResult() {
        if (!HttpAction.checkNet(context)) {
            return;
        }

        Log.i("Result", "http is : " + HttpSet.URL_SEARCH_FILTER);

        Intent result_int = new Intent(context, Result_Act.class);
        context.startActivity(result_int);
    }

    private void onUser() {
        if (!sharedAction.getLoginStatus()) {
            Intent login_int = new Intent(context, Login_Act.class);
            context.startActivity(login_int);
            return;
        }

        Intent user_int = new Intent(context, User_Act.class);
        context.startActivity(user_int);
    }
}
