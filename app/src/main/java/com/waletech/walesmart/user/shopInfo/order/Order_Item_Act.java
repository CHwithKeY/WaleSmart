package com.waletech.walesmart.user.shopInfo.order;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicSet.IntentSet;

import org.json.JSONException;

import java.util.ArrayList;

public class Order_Item_Act extends Base_Act {

    private OrderItemAction orderItemAction;

    private ArrayList<ObjectShoe> shoeList;

    private boolean isFstInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_layout);

        varInit();

        setupToolbar();

    }

    private void varInit() {
        String order_num = getIntent().getStringExtra(IntentSet.KEY_ORDER_NUM);

        orderItemAction = new OrderItemAction(this);
        orderItemAction.getShoeList(order_num);

        isFstInit = true;
    }

    @Override
    protected void setupToolbar() {
        setTbTitle(getString(R.string.order_item_toolbar_title));
        setTbNavigation();
    }

    private void setupRecyclerView() {
        OrderItemRycAdapter adapter = new OrderItemRycAdapter(shoeList, this);

        final RecyclerView order_item_rv = (RecyclerView) findViewById(R.id.order_item_rv);
        order_item_rv.setLayoutManager(new LinearLayoutManager(this));
        order_item_rv.setAdapter(adapter);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

        if (isFstInit) {
            shoeList = orderItemAction.handleResponse(result);

            setupRecyclerView();

            isFstInit = false;
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

    }


}
