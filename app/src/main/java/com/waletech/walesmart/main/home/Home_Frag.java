package com.waletech.walesmart.main.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Frag;

import org.json.JSONException;

/**
 * Created by KeY on 2016/6/28.
 */
public class Home_Frag extends Base_Frag implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout home_refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_home_page_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupSwipeRefresh(view);

        setupRecyclerView(view);
    }

    private void setupSwipeRefresh(View view) {
        home_refresh = (SwipeRefreshLayout) view.findViewById(R.id.home_sr);
        home_refresh.setColorSchemeResources(R.color.colorMain, R.color.colorSpecial, R.color.colorSpecialConverse);
        home_refresh.setOnRefreshListener(this);
    }

    private void setupRecyclerView(View view) {
        HomeRycAdapter adapter = new HomeRycAdapter();

        final RecyclerView home_rv = (RecyclerView) view.findViewById(R.id.home_rv);
        home_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        home_rv.setAdapter(adapter);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

    }

    @Override
    public void onNullResponse() throws JSONException {

    }

    @Override
    public void setCustomTag(String tag) {

    }

    @Override
    public String getCustomTag() {
        return null;
    }

    @Override
    public void onRefresh() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                home_refresh.setRefreshing(false);
            }
        }, 2 * 1000);
    }

    private void onStopRefresh() {
        if (getView() != null && home_refresh != null && home_refresh.isRefreshing()) {
            home_refresh.setRefreshing(false);
        }
    }
}
