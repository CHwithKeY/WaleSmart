package com.waletech.walesmart.publicClass;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.waletech.walesmart.R;


/**
 * Created by KeY on 2015/12/7.
 */
public class LineToast {
    private Toast toast = null;
    private Context context = null;

    public LineToast(Context context) {
        this.context = context;
    }

    public void showToast(CharSequence text) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.public_line_toast_page, null);

        if (toast == null) {
            toast = new Toast(context);
        }

        setToast(view, text);
        toast.show();
    }

    private void setToast(View view, CharSequence text) {
        int t_h = 0;
        try {
            AppCompatActivity activity = (AppCompatActivity) context;

            ScreenSize size = new ScreenSize(context);
            int s_h = size.getHeight();
            t_h = s_h / 6;
        } catch (ClassCastException exception) {
            t_h = 120;
        }

        TextView tv = (TextView) view.findViewById(R.id.toast_tv);
        tv.setText(text);
        toast.setGravity(Gravity.BOTTOM, 0, t_h);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
    }
}
