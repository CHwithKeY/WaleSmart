package com.waletech.walesmart.register;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.BaseClickListener;
import com.waletech.walesmart.publicClass.Methods;

/**
 * Created by KeY on 2016/6/1.
 */
class ClickListener extends BaseClickListener {

    private Context context;
    private RegisterAction registerAction;

    // 判断点击“小眼睛”以后，是显示或隐藏密码
    private boolean psd_triggle = false;

    public ClickListener(Context context, BaseAction baseAction) {
        super(context, baseAction);

        this.context = context;
        registerAction = (RegisterAction) baseAction;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_ll:
                // 收回软键盘
                Methods.collapseIME(v.getContext());
                break;

            case R.id.reg_tv0:
                // 显示密码
                displayPsd();
                break;

            case R.id.reg_tv1:
                // 清除密码
                clearPsd();
                break;

            case R.id.reg_btn1:
                // 获取验证码
                getIdc();
                break;

            case R.id.reg_btn2:
                onRegister();
                // onNextStep();
                break;

            default:
                break;
        }
    }

    private void displayPsd() {
        AppCompatActivity compatActivity = (AppCompatActivity) context;
        final EditText psd_et = (EditText) compatActivity.findViewById(R.id.reg_et1);

        if (!psd_triggle) {
            psd_et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            psd_triggle = true;
        } else {
            psd_et.setTransformationMethod(PasswordTransformationMethod.getInstance());
            psd_triggle = false;
        }

        psd_et.setSelection(psd_et.length());
    }

    private void clearPsd() {
        AppCompatActivity compatActivity = (AppCompatActivity) context;
        final EditText psd_et = (EditText) compatActivity.findViewById(R.id.reg_et1);

        psd_et.setText("");
    }

    private void getIdc() {
        AppCompatActivity compatActivity = (AppCompatActivity) context;

        final Button idc_press_btn = (Button) compatActivity.findViewById(R.id.reg_btn0);
        final Button idc_btn = (Button) compatActivity.findViewById(R.id.reg_btn1);

        // 倒计时10s后才允许再次发送验证码
        ResendCountTimer timer = new ResendCountTimer(10 * 1000, 1000);
        timer.setBtn(context, idc_btn, idc_press_btn);
        timer.start();

        idc_btn.setVisibility(View.INVISIBLE);
    }

    private void onRegister() {
        AppCompatActivity compatActivity = (AppCompatActivity) context;

        final EditText usn_et = (EditText) compatActivity.findViewById(R.id.reg_et0);
        final EditText psd_et = (EditText) compatActivity.findViewById(R.id.reg_et1);
        final EditText nicn_et = (EditText) compatActivity.findViewById(R.id.reg_et2);

        String usn_str = usn_et.getText().toString();
        String psd_str = psd_et.getText().toString();
        String nicn_str = nicn_et.getText().toString();

        registerAction.register(usn_str, psd_str, nicn_str);
    }
}
