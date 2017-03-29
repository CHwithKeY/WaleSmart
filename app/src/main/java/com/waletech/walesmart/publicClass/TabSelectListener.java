package com.waletech.walesmart.publicClass;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.waletech.walesmart.R;

/**
 * Created by KeY on 2016/6/2.
 */
public class TabSelectListener implements TabLayout.OnTabSelectedListener {
    private final ViewPager mViewPager;

    private int[] img_resId;
    private int[] img_focus_resId;

    public TabSelectListener(ViewPager viewPager, int[] img_resId, int[] img_focus_resId) {
        mViewPager = viewPager;
        this.img_resId = img_resId;
        this.img_focus_resId = img_focus_resId;

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        mViewPager.setCurrentItem(tab.getPosition());
        View view = View.inflate(mViewPager.getContext(), R.layout.public_tablayout_image_item, null);
        ImageView tab_img = (ImageView) view.findViewById(R.id.tab_img);

        if (tab.getCustomView() != null) {
            tab.setCustomView(null);
        }
        tab_img.setImageResource(img_focus_resId[tab.getPosition()]);
        tab.setCustomView(tab_img);

        // tab.setIcon(img_focus_resId[tab.getPosition()]);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

        View view = View.inflate(mViewPager.getContext(), R.layout.public_tablayout_image_item, null);
        ImageView tab_img = (ImageView) view.findViewById(R.id.tab_img);

        if (tab.getCustomView() != null) {
            tab.setCustomView(null);
        }
        tab_img.setImageResource(img_resId[tab.getPosition()]);
        tab.setCustomView(tab_img);

        // tab.setIcon(img_resId[tab.getPosition()]);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        // No-op
    }

}
