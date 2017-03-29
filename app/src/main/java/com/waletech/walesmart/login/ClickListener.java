package com.waletech.walesmart.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.BaseClickListener;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.register.Register_Act;


/**
 * Created by KeY on 2016/6/1.
 */
class ClickListener extends BaseClickListener {

    private Context context;
    private LoginAction loginAction;

    // 判断点击“小眼睛”以后，是显示或隐藏密码
    private boolean psd_triggle = false;

    public ClickListener(Context context, BaseAction baseAction) {
        super(context, baseAction);
        this.context = context;
        loginAction = (LoginAction) baseAction;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_ll:
                // 收回软键盘
                Methods.collapseIME(context);
                break;

            case R.id.login_tv0:
                // 显示密码
                displayPsd();
                break;

            case R.id.login_tv1:
                // 清除密码
                clearPsd();
                break;

            case R.id.login_tv2:
                // 找回密码
                findPsd();
                break;

            case R.id.login_btn0:
                // 跳转注册
                onRegister();
                break;

            case R.id.login_btn1:
                // 点击登录
                onLogin();
                break;

            default:
                break;
        }
    }

    private void displayPsd() {
        // 通过一个布尔值(psd_triggle)判断此时应该是显示密码还是隐藏密码
        AppCompatActivity compatActivity = (AppCompatActivity) context;
        final EditText psd_et = (EditText) compatActivity.findViewById(R.id.login_et1);

        if (!psd_triggle) {
            psd_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            psd_triggle = true;
        } else {
            psd_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
            psd_triggle = false;
        }

        // 把光标定位在最后一位（因为如果没有这行代码，在显示/隐藏密码后，光标会蹦到首位）
        psd_et.setSelection(psd_et.length());
    }

    private void clearPsd() {
        AppCompatActivity compatActivity = (AppCompatActivity) context;
        final EditText psd_et = (EditText) compatActivity.findViewById(R.id.login_et1);

        psd_et.setText("");
    }

    private void findPsd() {
        // Intent findpsd_int = new Intent(context, FindPsd_Act.class);
        // startActivity(findpsd_int);
    }

    private void onRegister() {
        Intent reg_int = new Intent(context, Register_Act.class);
        context.startActivity(reg_int);
    }

    private void onLogin() {
        AppCompatActivity compatActivity = (AppCompatActivity) context;

        final EditText usn_et = (EditText) compatActivity.findViewById(R.id.login_et0);
        final EditText psd_et = (EditText) compatActivity.findViewById(R.id.login_et1);

        String usn_str = usn_et.getText().toString().trim();
        String psd_str = psd_et.getText().toString().trim();

        loginAction.login(usn_str, psd_str);
    }
}
