package com.waletech.walesmart;

import android.os.Bundle;

import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.shop.exp.CabinetMatchUtil;

import org.json.JSONException;

import java.util.ArrayList;

public class Test_Act extends Base_Act {
    private CabinetMatchUtil matchUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_layout);
        setContentView(R.layout.activity_exp_shop_layout);

        ArrayList<String> smarkList = new ArrayList<>();

        smarkList.add(0, "1A11");
        smarkList.add(1, "1A12");
        smarkList.add(2, "1A13");
        smarkList.add(3, "1A14");
        smarkList.add(4, "2A11");
        smarkList.add(5, "2A12");

//        matchUtil = new CabinetMatchUtil(smarkList, Test_Act.this);
//        matchUtil.match();
    }

    @Override
    protected void setupToolbar() {

    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        matchUtil.ExpandCabinet(result);
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
