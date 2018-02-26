package com.waletech.walesmart.shop;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicAction.UnlockAction;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.ViewTadAdapter;
import com.waletech.walesmart.publicObject.ObjectArea;
import com.waletech.walesmart.publicSet.BundleSet;
import com.waletech.walesmart.publicSet.IntentSet;
import com.waletech.walesmart.publicSet.PermissionSet;
import com.waletech.walesmart.shop.area.Area_Frag;
import com.waletech.walesmart.shop.exp.ExpShop_Act;
import com.waletech.walesmart.user.authInfo.PermissionAction;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Shop_Act extends Base_Act implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener {

    private ViewPager shop_vp;
//    private SwipeRefreshLayout shop_refresh;

    private UnlockAction unlockAction;
    private ShopAction shopAction;

    private ArrayList<ObjectArea> areaList;

    private boolean isFstInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_layout);

        varInit();

        setupToolbar();

        setupSwipeRefresh();

//        setupOperateImgbtn();
    }

    private void varInit() {
        unlockAction = new UnlockAction(this);
        shopAction = new ShopAction(this);

        areaList = new ArrayList<>();

        isFstInit = true;
    }

    @Override
    protected void setupToolbar() {
        String shop_name = getIntent().getStringExtra(IntentSet.KEY_SHOP_NAME);
        String shop_location = getIntent().getStringExtra(IntentSet.KEY_SHOP_LOCATION);

        if (shop_name.contains("3店") || shop_name.contains("XieBao_3")) {
            Intent exp_shop_int = new Intent(this, ExpShop_Act.class);
            exp_shop_int.putExtra(IntentSet.KEY_SHOP_NAME, shop_name);
            exp_shop_int.putExtra(IntentSet.KEY_SHOP_LOCATION, shop_location);
            startActivity(exp_shop_int);
            finish();
        }

        setTbTitle(shop_name);
        setTbNavigation();

        toolbar.showOverflowMenu();
        toolbar.setOnMenuItemClickListener(this);

        final TextView location_tv = (TextView) findViewById(R.id.shop_tv);
        location_tv.setText(shop_location);

        shopAction.getArea(shop_name);
    }

    private void setupSwipeRefresh() {
//        shop_refresh = (SwipeRefreshLayout) findViewById(R.id.shop_sr);
//        shop_refresh.setColorSchemeResources(R.color.colorMain, R.color.colorSpecial, R.color.colorSpecialConverse);
//        shop_refresh.setOnRefreshListener(this);
    }

    private void onCreateAreaMatrix() {

    }

    private void setupViewPager() {
//        Area_Frag frag01 = new Area_Frag();
//        Area_Frag frag02 = new Area_Frag();
//        Area_Frag frag03 = new Area_Frag();

//        Bundle bundle = new Bundle();
//        bundle.putString(BundleSet.KEY_SHOP_NAME, toolbar.getTitle().toString());
//        frag01.setArguments(bundle);
//
//        List<Fragment> fragments = new ArrayList<>();
//        fragments.add(frag01);
//        fragments.add(frag02);
//        fragments.add(frag03);
//
//        String[] titles = {"A区", "B区", "C区"};

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();

        for (int i = 0; i < areaList.size(); i++) {
            ObjectArea area = areaList.get(i);

            Area_Frag frag = new Area_Frag();

            for (int k = 0; k < areaList.size(); k++) {
                String area_line = "";
                switch (k) {
                    case 0:
                        area_line = "A";
                        break;

                    case 1:
                        area_line = "B";
                        break;

                    case 2:
                        area_line = "C";
                        break;

                    case 3:
                        area_line = "D";
                        break;

                    case 4:
                        area_line = "E";
                        break;

                    default:
                        break;
                }

                ObjectArea temp_area = areaList.get(k);
                if (temp_area.getName().contains(area_line)) {
                    break;
                }
            }

            Log.i("Result", "area name is : " + area.getName());

            Bundle bundle = new Bundle();
            bundle.putString(BundleSet.KEY_AREA_NAME, area.getName());
            bundle.putString(BundleSet.KEY_AREA_ROW, area.getRow());
            bundle.putString(BundleSet.KEY_AREA_COLUMN, area.getColumn());

            frag.setArguments(bundle);

            fragments.add(frag);
            titles.add(area.getName());
        }

        String[] title_arr = titles.toArray(new String[titles.size()]);

        ViewTadAdapter adapter = new ViewTadAdapter(getSupportFragmentManager());
        adapter.setFragments(fragments);
        adapter.setTitles(title_arr);

        shop_vp = (ViewPager) findViewById(R.id.shop_vp);
        shop_vp.setAdapter(adapter);
        shop_vp.setOffscreenPageLimit(0);

        setupTabLayout(shop_vp);
    }

    private void setupTabLayout(ViewPager viewPager) {
        final TabLayout shop_tab = (TabLayout) findViewById(R.id.shop_tab);
        shop_tab.setupWithViewPager(viewPager);
    }

//    private void setupOperateImgbtn() {
//        final ImageButton unlock_imgbtn = (ImageButton) findViewById(R.id.shop_imgbtn0);
//
//        final ImageButton left_imgbtn = (ImageButton) findViewById(R.id.shop_imgbtn1);
//        final ImageButton right_imgbtn = (ImageButton) findViewById(R.id.shop_imgbtn2);
//
//        ImageButton[] imageButtons = {unlock_imgbtn, left_imgbtn, right_imgbtn};
//
//        for (ImageButton imageButton : imageButtons) {
//            imageButton.setOnClickListener(this);
//        }
//    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        if (isFstInit) {
            isFstInit = false;

            areaList = shopAction.handleResponse(result);

            setupViewPager();
        }

        switch (tag) {
            case HttpTag.UNLOCK_UNLOCK:
                unlockAction.handleResponse(result);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNullResponse() throws JSONException {
        if (isFstInit) {
            finish();
        }
    }

    @Override
    public void onPermissionAccepted(int permission_code) {

    }

    @Override
    public void onPermissionRefused(int permission_code) {
        new LineToast(this).showToast(getString(R.string.auth_toast_permission_camera_authorized));
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
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.shop_imgbtn0:
//                unlockAction.check();
//                break;
//
//            case R.id.shop_imgbtn1:
//                if (shop_vp.getCurrentItem() == 0) {
//                    shop_vp.setCurrentItem(shop_vp.getChildCount());
//                    return;
//                }
//                shop_vp.setCurrentItem(shop_vp.getCurrentItem() - 1);
//                break;
//
//            case R.id.shop_imgbtn2:
//                if ((shop_vp.getCurrentItem() + 1) == shop_vp.getChildCount()) {
//                    shop_vp.setCurrentItem(0);
//                    return;
//                }
//                shop_vp.setCurrentItem(shop_vp.getCurrentItem() + 1);
//                break;
//
//            default:
//                break;
//        }
    }

    @Override
    public void onRefresh() {
//        if (!HttpAction.checkNet(this)) {
//            return;
//        }
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                shop_vp.removeAllViews();
//                setupViewPager();
//
//                shop_refresh.setRefreshing(false);
//            }
//        }, 1500);
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
                if (PermissionAction.checkAutoRequest(Shop_Act.this, Manifest.permission.CAMERA, PermissionSet.CAMERA)) {
                    unlockAction.check();
                }
                break;

            default:
                break;
        }

        return false;
    }

    private void onShopRefresh() {
        shop_vp.removeAllViews();
        setupViewPager();
    }
}
