package com.waletech.walesmart.user.managerInfo;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.user.lockInfo.record.RecordAction;

import org.json.JSONException;

import java.util.ArrayList;

public class CheckRecord_Act extends Base_Act {
    private RecordAction recordAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_record_layout);

        varInit();

        setupToolbar();
    }

    @Override
    protected void setupToolbar() {
        setTbTitle("开锁记录");
        setTbNavigation();
    }

    private void varInit() {
        recordAction = new RecordAction(this);
        recordAction.getManagerRecord();
    }

    private void setupRecyclerView(ArrayList<ObjectShoe> shoeList) {
        ManagerRecordShoeRycAdapter adapter = new ManagerRecordShoeRycAdapter(shoeList, this);
        adapter.setBaseAction(recordAction);

        final RecyclerView record_rv = (RecyclerView) findViewById(R.id.check_record_rv);
        record_rv.setLayoutManager(new LinearLayoutManager(this));
        record_rv.setAdapter(adapter);
        record_rv.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.MANAGER_GET_RECORD:
                ArrayList<ObjectShoe> shoeList = recordAction.handleResponse(result);

                setupRecyclerView(shoeList);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNullResponse() throws JSONException {
        finish();
    }

    @Override
    public void onPermissionAccepted(int permission_code) {

    }

    @Override
    public void onPermissionRefused(int permission_code) {

    }
}
