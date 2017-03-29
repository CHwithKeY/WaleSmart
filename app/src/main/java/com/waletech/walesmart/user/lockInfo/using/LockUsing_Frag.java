package com.waletech.walesmart.user.lockInfo.using;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waletech.walesmart.GeneralShoeRycAdapter;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Frag;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/6/28.
 */
public class LockUsing_Frag extends Base_Frag implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout using_refresh;
    private ArrayList<ObjectShoe> shoeList;

    private UsingAction usingAction;

    private boolean isFstInit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lock_using_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit();

        setupSwipeRefresh(view);
    }

    private void varInit() {
        shoeList = new ArrayList<>();

        usingAction = new UsingAction(getContext(), this);
        usingAction.getUsingList();

        isFstInit = true;
    }

    private void setupSwipeRefresh(View view) {
        using_refresh = (SwipeRefreshLayout) view.findViewById(R.id.using_sr);
        using_refresh.setColorSchemeResources(R.color.colorMain, R.color.colorSpecial, R.color.colorSpecialConverse);
        using_refresh.setOnRefreshListener(this);
    }

    private void setupRecyclerView(View view) {
        GeneralShoeRycAdapter adapter = new GeneralShoeRycAdapter(shoeList, getContext());
        adapter.setModuleType(GeneralShoeRycAdapter.TYPE_USING);

        final RecyclerView using_rv = (RecyclerView) view.findViewById(R.id.using_rv);
        using_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        using_rv.setItemAnimator(new DefaultItemAnimator());
        using_rv.setAdapter(adapter);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        onStopRefresh();

        shoeList = usingAction.handleResponse(result);

        if (getView() != null) {
            if (isFstInit) {
                setupRecyclerView(getView());
                isFstInit = false;
                return;
            }

            final RecyclerView using_rv = (RecyclerView) getView().findViewById(R.id.using_rv);
            GeneralShoeRycAdapter adapter = (GeneralShoeRycAdapter) using_rv.getAdapter();
            adapter.setModuleType(GeneralShoeRycAdapter.TYPE_USING);
            adapter.setShoeList(shoeList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNullResponse() throws JSONException {
        if (isFstInit) {
            ((AppCompatActivity) getContext()).finish();
        }

        onStopRefresh();
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
        usingAction.getUsingList();
    }

    private void onStopRefresh() {
        if (getView() != null && using_refresh != null && using_refresh.isRefreshing()) {
            using_refresh.setRefreshing(false);
        }
    }
}
