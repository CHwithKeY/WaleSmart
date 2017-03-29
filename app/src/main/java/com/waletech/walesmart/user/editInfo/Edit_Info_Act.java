package com.waletech.walesmart.user.editInfo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicSet.ToastSet;

import org.json.JSONException;

public class Edit_Info_Act extends Base_Act implements View.OnClickListener {

    private final static int TYPE_NICKNAME = 0;
    private final static int TYPE_PASSWORD = 1;

    private EditInfoAction editInfoAction;

    private LineToast toast;

    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit_info_layout);

        varInit();

        setupToolbar();

        setupEditNickname();

        setupEditPassword();
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    private void varInit() {
        editInfoAction = new EditInfoAction(this);

        toast = new LineToast(this);
    }

    @Override
    protected void setupToolbar() {
        setTbTitle(getString(R.string.edit_info_toolbar_title));
        setTbNavigation();
    }

    private void setupEditNickname() {
        final TextView edit_nicn_tv = (TextView) findViewById(R.id.edit_info_nicn_tv);
        edit_nicn_tv.setOnClickListener(this);
    }

    private void setupEditPassword() {
        final TextView edit_psd_tv = (TextView) findViewById(R.id.edit_info_psd_tv);
        edit_psd_tv.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        editInfoAction.handleResponse(result);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_info_nicn_tv:
                onCreateUpdateDialog(TYPE_NICKNAME);
                break;

            case R.id.edit_info_psd_tv:
                onCreateUpdateDialog(TYPE_PASSWORD);
                break;

            default:
                break;
        }
    }

    private void onCreateUpdateDialog(final int update_type) {
        View view = null;
        switch (update_type) {
            case TYPE_NICKNAME:
                view = View.inflate(this, R.layout.dialog_edit_info_edit_nicn_view, null);
                break;

            case TYPE_PASSWORD:
                view = View.inflate(this, R.layout.dialog_edit_info_edit_psd_view, null);
                break;

            default:
                break;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view);
        builder.setPositiveButton(getString(R.string.base_dialog_btn_sure), null);
        builder.setNegativeButton(getString(R.string.base_dialog_btn_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final View builder_view = view;
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (update_type) {
                    case TYPE_NICKNAME:
                        onUpdateNickname(builder_view);
                        break;

                    case TYPE_PASSWORD:
                        onUpdatePassword(builder_view);
                        break;

                    default:
                        break;
                }
            }
        });

//        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });

    }

    private void onUpdateNickname(View view) {
        EditText nickname_et = (EditText) view.findViewById(R.id.update_info_nicn_et);
        String nickname_str = nickname_et.getText().toString().trim();
        if (nickname_str.equals("")) {
            toast.showToast(getString(R.string.edit_info_toast_no_blank));
            return;
        }
        editInfoAction.updateNickname(nickname_str);
    }

    private void onUpdatePassword(View view) {
        EditText old_psd_et = (EditText) view.findViewById(R.id.edit_info_psd_et0);
        String old_psd_str = old_psd_et.getText().toString();

        EditText new_psd_et = (EditText) view.findViewById(R.id.edit_info_psd_et1);
        String new_psd_str = new_psd_et.getText().toString();

        EditText confirm_psd_et = (EditText) view.findViewById(R.id.edit_info_psd_et2);
        String confirm_psd_str = confirm_psd_et.getText().toString();

        if (TextUtils.isEmpty(old_psd_str) || TextUtils.isEmpty(new_psd_str) || TextUtils.isEmpty(confirm_psd_str)) {
            toast.showToast(getString(R.string.edit_info_toast_no_blank));
            return;
        }

        if (!new_psd_str.equals(confirm_psd_str)) {
            toast.showToast(getString(R.string.edit_info_toast_not_duplication_psd));
            return;
        }

        editInfoAction.updatePassword(old_psd_str, new_psd_str);
    }

}
