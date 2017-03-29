package com.waletech.walesmart.register;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicClass.Methods;

import org.json.JSONException;

public class Register_Act extends Base_Act {

    private ClickListener clickListener;
    private RegisterAction registerAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);

        varInit();

        setupToolbar();

        setupInputField();

        setupIdcBtn();

        setupRegisterBtn();
    }

    private void varInit() {
        registerAction = new RegisterAction(this);
        clickListener = new ClickListener(this, registerAction);
    }

    @Override
    protected void setupToolbar() {
        setTbTitle(getString(R.string.reg_toolbar_title));
        setTbNavigation();
    }

    private void setupInputField() {
        // 监听点击屏幕空白处隐藏软键盘
        final LinearLayout login_ll = (LinearLayout) findViewById(R.id.reg_ll);
        login_ll.setOnClickListener(clickListener);

        final EditText usn_et = (EditText) findViewById(R.id.reg_et0);
        usn_et.requestFocus();
        Methods.expandIME(usn_et);

        final TextView eye_tv = (TextView) findViewById(R.id.reg_tv0);
        final TextView clear_tv = (TextView) findViewById(R.id.reg_tv1);

        eye_tv.setOnClickListener(clickListener);
        clear_tv.setOnClickListener(clickListener);
    }

    private void setupIdcBtn() {
        final Button idc_btn = (Button) findViewById(R.id.reg_btn1);
        idc_btn.setOnClickListener(clickListener);
    }

    private void setupRegisterBtn() {
        final Button next_btn = (Button) findViewById(R.id.reg_btn2);
        next_btn.setOnClickListener(clickListener);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.REGISTER_REGISTER:
                registerAction.handleResponse(result);
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

    }

    @Override
    public void onPermissionRefused(int permission_code) {

    }
}
