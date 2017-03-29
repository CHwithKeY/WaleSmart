package com.waletech.walesmart.update;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicSet.PermissionSet;
import com.waletech.walesmart.user.authInfo.PermissionAction;

import org.json.JSONException;

public class Update_Act extends Base_Act implements View.OnClickListener {

    private LineToast toast;

    private UpdateAction updateAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_layout);

        varInit();

        setupToolbar();

        setupVersionNameText();

        setupCheckUpdateText();

    }

    private void varInit() {
        toast = new LineToast(this);

        updateAction = new UpdateAction(this);
    }

    @Override
    protected void setupToolbar() {
        setTbTitle(getString(R.string.update_toolbar_title));
        setTbNavigation();
    }

    private void setupVersionNameText() {
        final TextView version_tv = (TextView) findViewById(R.id.update_version_tv);
        String version_str = version_tv.getText() + Methods.getVersionName(this);
        version_tv.setText(version_str);
    }

    private void setupCheckUpdateText() {
        final TextView update_tv = (TextView) findViewById(R.id.update_check_tv);
        update_tv.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        updateAction.handleResponse(result, true);
    }

    @Override
    public void onNullResponse() throws JSONException {

    }

    @Override
    public void onPermissionAccepted(int permission_code) {
        updateAction.checkVersion(Methods.getVersionName(this));
    }

    @Override
    public void onPermissionRefused(int permission_code) {
        toast.showToast(getString(R.string.auth_toast_permission_update_authorized));
    }


    @Override
    public void onClick(View v) {
        if (PermissionAction.checkAutoRequest(Update_Act.this, Manifest.permission.WRITE_EXTERNAL_STORAGE, PermissionSet.WRITE_EXTERNAL_STORAGE)) {
            updateAction.checkVersion(Methods.getVersionName(this));
        }

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissionSet.WRITE_EXTERNAL_STORAGE);
//        } else {
//            updateAction.checkVersion(Methods.getVersionName(this));
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionAction.handle(this, requestCode, grantResults);
//        switch (requestCode) {
//            case PermissionSet.WRITE_EXTERNAL_STORAGE:
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    updateAction.checkVersion(Methods.getVersionName(this));
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//
//                } else {
//                    toast.showToast("请打开权限，否则无法下载新版本");
//                    // permission denied, boo! Disable the
//                    // functionality that depends on this permission.
//                }
//                break;
//
//            default:
//                break;
//        }
    }
}
