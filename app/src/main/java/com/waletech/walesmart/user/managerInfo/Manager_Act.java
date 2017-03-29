package com.waletech.walesmart.user.managerInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;

import org.json.JSONException;

public class Manager_Act extends Base_Act implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_layout);

        setupToolbar();

        setupItemText();
    }

    @Override
    protected void setupToolbar() {
        setTbTitle("管理者模式");
        setTbNavigation();
    }

    private void setupItemText() {
        final TextView check_record_tv = (TextView) findViewById(R.id.manager_check_record_tv);
        check_record_tv.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.manager_check_record_tv:
                Intent check_int = new Intent(Manager_Act.this, CheckRecord_Act.class);
                startActivity(check_int);
//                LinearLayout manager_ll = (LinearLayout) findViewById(R.id.manager_ll);
//                manager_ll.removeAllViews();
//
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                CheckRecord_Frag checkRecord_frag = new CheckRecord_Frag();
//                transaction.add(R.id.manager_ll, checkRecord_frag, "checkRecord_frag");
//                transaction.commit();
                break;

            default:
                break;
        }
    }
}
