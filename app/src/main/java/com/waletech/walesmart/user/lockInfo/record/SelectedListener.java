package com.waletech.walesmart.user.lockInfo.record;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

/**
 * Created by KeY on 2016/7/5.
 */
class SelectedListener implements AdapterView.OnItemSelectedListener {

    public final static int SPINNER_SHOP = 0;
    public final static int SPINNER_BRAND = 1;

    private RecordAction recordAction;

    private int itemSpinner;

    public SelectedListener(RecordAction recordAction, int itemSpinner) {
        this.recordAction = recordAction;
        this.itemSpinner = itemSpinner;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        TextView selected_tv = (TextView) view;

        String selected_str = selected_tv.getText().toString();

        if (selected_str.equals("全部品牌") || selected_str.equals("全部店铺")) {
            selected_str = "All";
        }

        switch (itemSpinner) {

            case SPINNER_SHOP:
                recordAction.getFilterRecord("", selected_str, "", "");
                break;

            case SPINNER_BRAND:
                recordAction.getFilterRecord("", "", selected_str, "");
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
