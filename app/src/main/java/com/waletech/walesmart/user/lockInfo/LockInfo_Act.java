package com.waletech.walesmart.user.lockInfo;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicAction.GetServiceVersionAction;
import com.waletech.walesmart.publicClass.ViewTadAdapter;
import com.waletech.walesmart.user.lockInfo.record.LockRecord_Frag;
import com.waletech.walesmart.user.lockInfo.using.LockUsing_Frag;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class LockInfo_Act extends Base_Act {

    public static boolean isView = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_lock_info_layout);

        if (HttpSet.getBaseService().equals("/ClientBaseService/")) {
            HttpSet.setBaseIP(sharedAction.getNetIP(), this);
            HttpSet.setBaseService(sharedAction.getNetService(), this);
        }

//        if (HttpAction.checkNet(this)) {
//            finish();
//        }

        setupContent();
    }

    private void setupContent() {
        isView = true;

        setupToolbar();

        setupViewPager();
    }

    @Override
    protected void setupToolbar() {
        setTbTitle(getString(R.string.lockinfo_toolbar_title));
        setTbNavigation();
    }

    private void setupViewPager(/*ArrayList<ObjectShoe> using_shoeList, ArrayList<ObjectShoe> record_shoeList*/) {
        final ViewPager main_vp = (ViewPager) findViewById(R.id.lock_info_vp);

        List<Fragment> fragments = new ArrayList<>();

        LockUsing_Frag tab01 = new LockUsing_Frag();
        LockRecord_Frag tab02 = new LockRecord_Frag();

        fragments.add(tab01);
        fragments.add(tab02);

        String[] titles = {getString(R.string.lockinfo_tab_unlock_using), getString(R.string.lockinfo_tab_unlock_record)};

        ViewTadAdapter adapter = new ViewTadAdapter(getSupportFragmentManager());
        adapter.setFragments(fragments);
        adapter.setTitles(titles);

        main_vp.setAdapter(adapter);
        main_vp.setOffscreenPageLimit(fragments.size());

        setupTabLayout(main_vp);
    }

    private void setupTabLayout(ViewPager viewPager) {
        final TabLayout lockinfo_tab = (TabLayout) findViewById(R.id.lock_info_tab);
        lockinfo_tab.setupWithViewPager(viewPager);
        // 设置tab的文字，在被选中后和没被选中的时候，分别显示的颜色
        lockinfo_tab.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorMain));
        lockinfo_tab.setTabTextColors(getResources().getColor(R.color.colorAssist), getResources().getColor(R.color.colorMain));
        lockinfo_tab.setSelectedTabIndicatorHeight(7);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
    }

    @Override
    public void onNullResponse() throws JSONException {
        // finish();
    }

    @Override
    public void onPermissionAccepted(int permission_code) {

    }

    @Override
    public void onPermissionRefused(int permission_code) {

    }

}
