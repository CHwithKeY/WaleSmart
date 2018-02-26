package com.waletech.walesmart.user;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.waletech.walesmart.user.managerInfo.Manager_Act;
import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicSet.PermissionSet;
import com.waletech.walesmart.update.Update_Act;

import org.json.JSONException;

public class User_Act extends Base_Act implements Toolbar.OnMenuItemClickListener {

    private ClickListener clickListener;

    public static AppCompatActivity user_act;

    private LineToast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_layout);

        varInit();

        setupToolbar();

        setupHeadView();

        setupTextBar();

        setupRecyclerView();

    }

    private void varInit() {
        user_act = this;

        toast = new LineToast(this);
        clickListener = new ClickListener(this);
    }

    @Override
    protected void setupToolbar() {
        setTbTitle(getString(R.string.user_toolbar_title));
        setTbNavigation();

        toolbar.showOverflowMenu();
        toolbar.setOnMenuItemClickListener(this);
    }

    private void setupHeadView() {
        final TextView nick_tv = (TextView) findViewById(R.id.user_head_tv0);
        nick_tv.setText(sharedAction.getNickname());

        final ImageView edit_img = (ImageView) findViewById(R.id.user_head_img1);
        edit_img.setOnClickListener(clickListener);
    }

    private void setupTextBar() {
        TextView record_tv = (TextView) findViewById(R.id.user_tv0);
        TextView cart_tv = (TextView) findViewById(R.id.user_tv1);
        TextView ticket_tv = (TextView) findViewById(R.id.user_tv2);

        TextView[] bar_tv = {record_tv, cart_tv, ticket_tv};
        for (TextView aBar_tv : bar_tv) {
            aBar_tv.setOnClickListener(clickListener);
        }
    }

    private void setupRecyclerView() {
        final RecyclerView record_rv = (RecyclerView) findViewById(R.id.user_rv);

        GridRycAdapter adapter = new GridRycAdapter();
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);

        record_rv.setLayoutManager(manager);
        record_rv.setAdapter(adapter);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
//        switch (tag) {
//            case HttpTag.LOGIN_LOGIN:
//                break;
//
//            default:
//                break;
//        }
    }

    @Override
    public void onNullResponse() throws JSONException {

    }

    @Override
    public void onPermissionAccepted(int permission_code) {
        switch (permission_code) {
            case PermissionSet.CHANGE_CONFIGURATION:
//                switchLanguage();
                break;

            default:
                break;
        }
    }

    @Override
    public void onPermissionRefused(int permission_code) {
        switch (permission_code) {
            case PermissionSet.CHANGE_CONFIGURATION:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.auth_dialog_title))
                        .setMessage(getString(R.string.auth_dialog_language_permission_msg))
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_user_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menu_tb_user_item1:

//                final View dialog_view = View.inflate(this, R.layout.dialog_user_menu_manager_view, null);
//                final Dialog dialog = new AlertDialog.Builder(this)
//                        .setTitle("管理者模式")
//                        .setView(dialog_view)
//                        .create();
//
//                Button confirm_btn = (Button) dialog_view.findViewById(R.id.user_dialog_menu_manager_confrim_btn);
//                confirm_btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        EditText psd_et = (EditText) dialog_view.findViewById(R.id.user_dialog_menu_manager_psd_et);
//                        String psd_str = psd_et.getText().toString();
//
//                        if (psd_str.isEmpty()) {
//                            toast.showToast("输入密码为空");
//                            return;
//                        }
//
//                        if (!psd_str.equals("rfid123456")) {
//                            toast.showToast("密码错误");
//                            return;
//                        }
//
//                        Intent manager_int = new Intent(User_Act.this, Manager_Act.class);
//                        startActivity(manager_int);
//
//                        if (dialog != null) {
//                            dialog.cancel();
//                            dialog.dismiss();
//                        }
//                    }
//                });
//                dialog.show();
//                break;

            case R.id.menu_tb_user_item2:
                Intent update_int = new Intent(this, Update_Act.class);
                startActivity(update_int);
                break;
        }

        return false;
    }
}
