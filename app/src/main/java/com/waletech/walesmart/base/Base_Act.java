package com.waletech.walesmart.base;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.waletech.walesmart.R;
import com.waletech.walesmart.sharedinfo.SharedAction;
import com.waletech.walesmart.sharedinfo.SharedSet;

import org.json.JSONException;

public abstract class Base_Act extends AppCompatActivity {

    protected Toolbar toolbar;

    protected SharedAction sharedAction;
    private SharedPreferences sp;

    private Snackbar snackbar;
    // cos = Count of SnackBar
    private int cos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = this.getSharedPreferences(SharedSet.NAME, MODE_PRIVATE);
        sharedAction = new SharedAction();
        sharedAction.setShared(sp);
    }

    protected abstract void setupToolbar();

    private void tbInit() {

        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar_tb);
            // title_tv = (TextView) findViewById(R.id.toolbar_tv);

            toolbar.setTitle("");

            setSupportActionBar(toolbar);
        } catch (Exception exception) {
            Log.e(getClass().getName(), "没有引入 toolbar 的布局文件");
        }
    }

    /**
     * 重载了setTbTitle方法，和上一个方法不一样的地方在于，这个方法初始化了toolbar
     * 如果要单独设置setTbTitle，那就要先调用tbInit()
     *
     * @param title 要显示的标题文字
     */
    protected void setTbTitle(String title) {
        tbInit();
        toolbar.setTitle(title);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorAssist));
        // title_tv.setText(title);
    }

    protected void setTbNavigation() {
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_nav_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void showSnack(CharSequence text) {
        CoordinatorLayout snack_col = (CoordinatorLayout) findViewById(R.id.snack_col);

        if (snackbar != null) {
            snackbar = null;
            cos++;
            if (cos > 5) {
                System.gc();
                cos = 0;
            }
        }

        snackbar = Snackbar.make(snack_col, text, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    // 根据不同的Handler可以处理不同的反馈结果，最强大
    public abstract void onMultiHandleResponse(String tag, String result) throws JSONException;

    public abstract void onNullResponse() throws JSONException;

    public abstract void onPermissionAccepted(int permission_code);

    public abstract void onPermissionRefused(int permission_code);

}
