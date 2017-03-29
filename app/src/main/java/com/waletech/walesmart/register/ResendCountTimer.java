package com.waletech.walesmart.register;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import com.waletech.walesmart.R;

/**
 * Created by KeY on 2016/3/29.
 */
class ResendCountTimer extends CountDownTimer {
    private Context context;

    private Button btn;
    private Button press_btn;

    public ResendCountTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    public void setBtn(Context context, Button btn, Button press_btn) {
        this.context = context;
        this.btn = btn;
        this.press_btn = press_btn;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        String text = context.getString(R.string.reg_idc_press_btn) + "(" + millisUntilFinished / 1000 + ")";
        press_btn.setText(text);
    }

    @Override
    public void onFinish() {
        btn.setVisibility(View.VISIBLE);
    }
}
