package com.waletech.walesmart.user.shopInfo.order;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.publicObject.ObjectOrder;

import org.json.JSONException;

import java.util.ArrayList;

public class Order_Act extends Base_Act implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout order_refresh;
    private RecyclerView order_rv;

    private OrderAction orderAction;

    private ArrayList<ObjectOrder> orderList;

    private boolean isFstInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_layout);

        varInit();

        setupToolbar();

        setupSwipeRefresh();
    }

    private void varInit() {
        orderAction = new OrderAction(this);
        orderAction.getOrderList();

        isFstInit = true;
    }

    @Override
    protected void setupToolbar() {
        setTbTitle(getString(R.string.order_toolbar_title));
        setTbNavigation();
    }

    private void setupSwipeRefresh() {
        order_refresh = (SwipeRefreshLayout) findViewById(R.id.order_sr);
        order_refresh.setColorSchemeResources(R.color.colorMain, R.color.colorSpecial, R.color.colorSpecialConverse);
        order_refresh.setOnRefreshListener(this);
    }

    private void setupRecyclerView() {
        OrderRycAdapter adapter = new OrderRycAdapter(orderList);

        order_rv = (RecyclerView) findViewById(R.id.order_rv);
        order_rv.setLayoutManager(new LinearLayoutManager(this));
        order_rv.setAdapter(adapter);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

        onStopRefresh();

        if (isFstInit) {
            orderList = orderAction.handleResponse(result);

            setupRecyclerView();

            isFstInit = false;
            return;
        }

        OrderRycAdapter adapter = (OrderRycAdapter) order_rv.getAdapter();

        adapter.setOrderList(orderList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNullResponse() throws JSONException {
        onStopRefresh();

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

    private void onStopRefresh() {
        if (order_refresh != null && order_refresh.isRefreshing()) {
            order_refresh.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        orderAction.getOrderList();
    }
}
