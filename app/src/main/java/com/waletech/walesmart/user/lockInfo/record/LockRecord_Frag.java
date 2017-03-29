package com.waletech.walesmart.user.lockInfo.record;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.Base_Frag;
import com.waletech.walesmart.publicClass.DatePickerFragDialog;
import com.waletech.walesmart.publicObject.ObjectShoe;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

/**
 * Created by KeY on 2016/6/28.
 */
public class LockRecord_Frag extends Base_Frag implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private SwipeRefreshLayout record_refresh;

    private RecordAction recordAction;

    private ArrayList<ObjectShoe> shoeList;

    private ArrayList<String> shopList;
    private ArrayList<String> brandList;

    private boolean isFstInit;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lock_record_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit();

        setupSwipeRefresh(view);
    }

    private void varInit() {
        shoeList = new ArrayList<>();

        recordAction = new RecordAction(getContext(), this);
        recordAction.getDefaultRecord();

        isFstInit = true;
    }

    private void onSpinnerListInit() {
        if (shoeList != null) {
//            shopList = new ArrayList<>();
//            brandList = new ArrayList<>();

            HashSet<String> shopSet = new HashSet<>();
            HashSet<String> brandSet = new HashSet<>();
            for (int i = 0; i < shoeList.size(); i++) {
                ObjectShoe shoe = shoeList.get(i);

                shopSet.add(shoe.getShopName());
                brandSet.add(shoe.getBrand());
            }

            shopList = new ArrayList<>(shopSet);
            brandList = new ArrayList<>(brandSet);

            shopList.add(0, "全部店铺");
            brandList.add(0, "全部品牌");

            if (getView() != null) {
                setupFilterBar(getView());
            }
        }
    }

    private void setupFilterBar(View view) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String date = format.format(new Date());
        final TextView date_tv = (TextView) view.findViewById(R.id.filter_tv);
        date_tv.setText(date);
        date_tv.setOnClickListener(this);

        // 店铺的Spinner
        final AppCompatSpinner shop_spn = (AppCompatSpinner) view.findViewById(R.id.filter_spn0);
        ArrayAdapter<String> shop_adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_dark_text_item, shopList);
        shop_spn.setAdapter(shop_adapter);
        shop_spn.setOnItemSelectedListener(new SelectedListener(recordAction, SelectedListener.SPINNER_SHOP));

        // 品牌的Spinner
        final AppCompatSpinner brand_spn = (AppCompatSpinner) view.findViewById(R.id.filter_spn1);
        ArrayAdapter<String> brand_adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_dark_text_item, brandList);
        brand_spn.setAdapter(brand_adapter);
        brand_spn.setOnItemSelectedListener(new SelectedListener(recordAction, SelectedListener.SPINNER_BRAND));

        final AppCompatSpinner address_spn = (AppCompatSpinner) view.findViewById(R.id.filter_spn2);
        address_spn.setVisibility(View.GONE);
    }

    private void setupSwipeRefresh(View view) {
        record_refresh = (SwipeRefreshLayout) view.findViewById(R.id.record_sr);
        record_refresh.setColorSchemeResources(R.color.colorMain, R.color.colorSpecial, R.color.colorSpecialConverse);
        record_refresh.setOnRefreshListener(this);
    }

    private void setupRecyclerView(View view) {
        RecordShoeRycAdapter adapter = new RecordShoeRycAdapter(shoeList, getContext());
        adapter.setBaseAction(recordAction);

        final RecyclerView record_rv = (RecyclerView) view.findViewById(R.id.record_rv);
        record_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        record_rv.setAdapter(adapter);
        record_rv.setItemAnimator(new DefaultItemAnimator());
        // record_rv.addOnScrollListener(new LoadScrollListener(adapter));
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        onStopRefresh();

        Log.i("Result", "lock record");

        if (getView() != null) {
            if (isFstInit) {
                shoeList = recordAction.handleResponse(result);
                isFstInit = false;

                onSpinnerListInit();

                // setupFilterBar(getView());

                setupRecyclerView(getView());

                return;
            }

            final RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.record_rv);
            RecordShoeRycAdapter adapter = (RecordShoeRycAdapter) recyclerView.getAdapter();

            switch (recordAction.getAction()) {
                case BaseAction.ACTION_DEFAULT_REFRESH:
                    shoeList = recordAction.handleResponse(result);
                    break;

                case BaseAction.ACTION_FILTER:
                    shoeList = recordAction.handleResponse(result);
                    break;

                case BaseAction.ACTION_LOAD_MORE:
                    ArrayList<ObjectShoe> shoes = recordAction.handleResponse(result);
                    for (int i = 0; i < shoes.size(); i++) {
                        shoeList.add(shoes.get(i));
                    }
                    break;

                default:
                    break;
            }

            adapter.setShoeList(shoeList);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNullResponse() throws JSONException {
        onStopRefresh();

        if (isFstInit) {
            ((AppCompatActivity) getContext()).finish();
        }
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
        recordAction.getFilterRecord("", "", "", "");
    }

    private void onStopRefresh() {
        if (getView() != null && record_refresh != null && record_refresh.isRefreshing()) {
            record_refresh.setRefreshing(false);
        }
    }

    @Override
    public void onClick(View v) {
        DatePickerFragDialog datePicker = new DatePickerFragDialog();
        datePicker.setBaseAction(recordAction, DatePickerFragDialog.DATE_RECORD);
        datePicker.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), "datePicker");
    }

}
