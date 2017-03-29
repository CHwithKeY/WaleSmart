package com.waletech.walesmart.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.publicClass.Methods;

import org.json.JSONException;

public class Login_Act extends Base_Act {

    private ClickListener clickListener;
    private LoginAction loginAction;

    public static AppCompatActivity login_act;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);

        varInit();

        setupToolbar();

        setupInputField();

        setupRegister();

        setupLogin();
    }

    private void varInit() {
        login_act = this;

        loginAction = new LoginAction(this);
        clickListener = new ClickListener(this, loginAction);
    }

    @Override
    protected void setupToolbar() {
        setTbTitle(getString(R.string.login_toolbar_title));
        setTbNavigation();
    }

    private void setupInputField() {
        // 监听点击屏幕空白处隐藏软键盘
        final LinearLayout login_ll = (LinearLayout) findViewById(R.id.login_ll);
        login_ll.setOnClickListener(clickListener);

        final EditText usn_et = (EditText) findViewById(R.id.login_et0);
        usn_et.requestFocus();
        Methods.expandIME(usn_et);

        final TextView eye_tv = (TextView) findViewById(R.id.login_tv0);
        final TextView clear_tv = (TextView) findViewById(R.id.login_tv1);

        eye_tv.setOnClickListener(clickListener);
        clear_tv.setOnClickListener(clickListener);
    }

    private void setupRegister() {
        final Button reg_btn = (Button) findViewById(R.id.login_btn0);
        reg_btn.setOnClickListener(clickListener);
    }

    private void setupLogin() {
        final Button login_btn = (Button) findViewById(R.id.login_btn1);
        login_btn.setOnClickListener(clickListener);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        loginAction.handleResponse(result);
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
