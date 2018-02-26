package com.waletech.walesmart.shop.exp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.publicAction.UnlockAction;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicSet.IntentSet;
import com.waletech.walesmart.publicSet.PermissionSet;
import com.waletech.walesmart.shop.exp.http.ExpHttpTag;
import com.waletech.walesmart.user.authInfo.PermissionAction;

import org.json.JSONException;

import java.util.ArrayList;

public class ExpShop_Act extends Base_Act implements Toolbar.OnMenuItemClickListener {

    private UnlockAction unlockAction;
    private CabinetMatchUtil matchUtil;
    private ExpAreaAction expAreaAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp_shop_layout);

        varInit();

        setupToolbar();

        setupAreaText();
    }

    private void varInit() {
        unlockAction = new UnlockAction(this);
        expAreaAction = new ExpAreaAction(this);
    }

    @Override
    protected void setupToolbar() {
        String shop_name = getIntent().getStringExtra(IntentSet.KEY_SHOP_NAME);
        String shop_location = getIntent().getStringExtra(IntentSet.KEY_SHOP_LOCATION);

        setTbTitle(shop_name);
        setTbNavigation();

        toolbar.showOverflowMenu();
        toolbar.setOnMenuItemClickListener(this);

        final TextView location_tv = (TextView) findViewById(R.id.exp_shop_tv);
        location_tv.setText(shop_location);
    }

    private void setupAreaText() {
        final TextView exp_area_tv = (TextView) findViewById(R.id.exp_area_tv);
        exp_area_tv.setText("3A");
        expAreaAction.getAreaShoe("3A");
    }

//    private void setupExpCabinet(ArrayList<ObjectShoe> shoeList) {
//        matchUtil = new CabinetMatchUtil(shoeList, ExpShop_Act.this);
//        matchUtil.match();
//    }

    private void setupExpCabinet(ArrayList<ObjectSmark> smarkList) {
//        matchUtil = new CabinetMatchUtil(shoeList, ExpShop_Act.this);
        matchUtil = new CabinetMatchUtil(smarkList, ExpShop_Act.this);
        matchUtil.match();
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case ExpHttpTag.SHOP_GET_SHOP_SHOE:
                setupExpCabinet(expAreaAction.handleExpListResponse(result));
                break;

            case ExpHttpTag.UNLOCK_UNLOCK:
                unlockAction.handleResponse(result);
                break;

            case ExpHttpTag.EXP_SHOP_GET_CABINET_STUFF:
                matchUtil.ExpandCabinet(result);
                break;
        }
    }

    @Override
    public void onNullResponse() throws JSONException {
    }

    @Override
    public void onPermissionAccepted(int permission_code) {
        new LineToast(this).showToast(getString(R.string.auth_toast_permission_camera_authorized));
    }

    @Override
    public void onPermissionRefused(int permission_code) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == UnlockAction.REQUEST_MAIN) {
                String smark_num = data.getStringExtra("scan_result");
                unlockAction.unlock(smark_num);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionAction.handle(this, requestCode, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_shop_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (!HttpAction.checkNet(this)) {
            return false;
        }

        switch (item.getItemId()) {
            case R.id.menu_tb_shop_item0:
                onShopRefresh();
                break;

            case R.id.menu_tb_shop_item1:
                if (PermissionAction.checkAutoRequest(ExpShop_Act.this, Manifest.permission.CAMERA, PermissionSet.CAMERA)) {
                    unlockAction.check();
                }
                break;

            default:
                break;
        }

        return false;
    }

    private void onShopRefresh() {
        RelativeLayout ark_rl = (RelativeLayout) findViewById(R.id.exp_shop_ark_rl);
        setupAreaText();
    }
}
