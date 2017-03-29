package com.waletech.walesmart.main.experience;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Frag;
import com.waletech.walesmart.datainfo.AddressSet;
import com.waletech.walesmart.datainfo.DataBaseAction;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.map.MapHelper;
import com.waletech.walesmart.publicClass.ScreenSize;
import com.waletech.walesmart.publicObject.ObjectShop;
import com.waletech.walesmart.user.authInfo.PermissionAction;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/6/28.
 */
public class Experience_Frag extends Base_Frag implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private SwipeRefreshLayout exp_refresh;

    private ArrayList<ObjectShop> shopList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_experience_store_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit();

        setupNetDownView(view);
    }

    private void varInit() {
        shopList = new ArrayList<>();
    }

    private void setupNetDownView(View view) {
        if (!HttpAction.checkNet(getContext())) {
            View net_down_view = view.findViewById(R.id.exp_net_down_view);
            net_down_view.setVisibility(View.VISIBLE);

            ImageButton net_down_imgbtn = (ImageButton) view.findViewById(R.id.net_down_imgbtn);
            net_down_imgbtn.setOnClickListener(this);

            setupSwipeRefresh(view);
            setupRecyclerView(view, "", "", "");
            return;
        }

        initExpView(view);
    }

    private void initExpView(View view) {
        setupSwipeRefresh(view);

        setupFindShopBtn(view);

        // 初始化，因为百度地图加载有时候速度太慢
        // 容易导致误触
        setupRecyclerView(view, "init", "init", "init");

        setupMapView(view);
    }

    private void setupSwipeRefresh(View view) {
        exp_refresh = (SwipeRefreshLayout) view.findViewById(R.id.exp_sr);
        exp_refresh.setColorSchemeResources(R.color.colorMain, R.color.colorSpecial, R.color.colorSpecialConverse);
        exp_refresh.setOnRefreshListener(this);
    }

    private void setupMapView(final View view) {
        final MapView exp_mv = (MapView) view.findViewById(R.id.exp_mv);
        new MapHelper(getContext(), exp_mv) {
            @Override
            public void onLoadLocationDone(String province, String city, String county) {
                setupRecyclerView(view, province, city, county);
            }
        };
    }

    private void setupRecyclerView(View view, String province, String city, String county) {

        shopList = initShopListData(province, city, county);

        if (shopList.size() == 0) {
            ObjectShop shop = new ObjectShop();
            shop.setName(getString(R.string.exp_no_store_this_area_tv));
            shop.setLocation("");
            shopList.add(shop);
        }

        ExpRycAdapter adapter = new ExpRycAdapter(shopList);

        final RecyclerView exp_rv = (RecyclerView) view.findViewById(R.id.exp_rv);
        exp_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        exp_rv.setAdapter(adapter);
        exp_rv.addOnScrollListener(scrollListener);

        onStopRefresh();
    }

    private ArrayList<ObjectShop> initShopListData(String province, String city, String county) {
        ArrayList<ObjectShop> shopList = new ArrayList<>();

        DataBaseAction baseAction = DataBaseAction.onCreate(getContext());
        ArrayList<String> shopNameList = baseAction.
                query(AddressSet.TABLE_NAME,
                        new String[]{AddressSet.PROVINCE, AddressSet.CITY, AddressSet.COUNTY},
                        new String[]{province, city, county},
                        AddressSet.SHOP, "");
        ArrayList<String> shopLocList = new ArrayList<>();

        for (int i = 0; i < shopNameList.size(); i++) {
            String shop_name = shopNameList.get(i);
            ArrayList<String> singleLoc_List = baseAction.
                    query(AddressSet.TABLE_NAME, new String[]{AddressSet.SHOP}, new String[]{shop_name}, AddressSet.ROAD, "");
            shopLocList.add(singleLoc_List.get(0));
        }

        for (int i = 0; i < shopNameList.size(); i++) {
            ObjectShop shop = new ObjectShop();

            shop.setName(shopNameList.get(i));
            shop.setLocation(shopLocList.get(i));
            shopList.add(shop);
        }

        return shopList;
    }

    private void setupFindShopBtn(View view) {
        final Button find_shop_btn = (Button) view.findViewById(R.id.exp_btn);
        find_shop_btn.setOnClickListener(this);
    }

    private String province_str = "";
    private String city_str = "";
    private String county_str = "";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setupSpinnerGroup(final View v) {
        DataBaseAction baseAction = DataBaseAction.onCreate(getContext());
        ArrayList<String> provinceList = baseAction.query(AddressSet.TABLE_NAME, new String[]{""}, new String[]{""}, AddressSet.PROVINCE, "");
        provinceList.add(0, getString(R.string.base_item_all));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text_item, provinceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner province_spn = (Spinner) v.findViewById(R.id.location_spn0);
        province_spn.setAdapter(adapter);
        province_spn.setDropDownVerticalOffset(province_spn.getLayoutParams().height);
        province_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView item_tv = (TextView) view;
                DataBaseAction baseAction = DataBaseAction.onCreate(getContext());

                province_str = item_tv.getText().toString();

                ArrayList<String> cityList =
                        baseAction.query(AddressSet.TABLE_NAME, new String[]{AddressSet.PROVINCE}, new String[]{province_str}, AddressSet.CITY, "");
                cityList.add(0, getString(R.string.base_item_all));

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text_item, cityList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                final Spinner city_spn = (Spinner) v.findViewById(R.id.location_spn1);
                city_spn.setAdapter(adapter);
                city_spn.setDropDownVerticalOffset(city_spn.getLayoutParams().height);

                city_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView item_tv = (TextView) view;

                        city_str = item_tv.getText().toString();

                        DataBaseAction baseAction = DataBaseAction.onCreate(getContext());
                        ArrayList<String> countyList =
                                baseAction.query(AddressSet.TABLE_NAME, new String[]{AddressSet.CITY}, new String[]{city_str}, AddressSet.COUNTY, "");
                        countyList.add(0, getString(R.string.base_item_all));

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.spinner_text_item, countyList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        final Spinner county_spn = (Spinner) v.findViewById(R.id.location_spn2);
                        county_spn.setAdapter(adapter);
                        county_spn.setDropDownVerticalOffset(county_spn.getLayoutParams().height);

                        county_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                TextView item_tv = (TextView) view;
                                county_str = item_tv.getText().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupConfirmBtn(View view) {
        final Button confirm_btn = (Button) view.findViewById(R.id.location_btn);
        confirm_btn.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        onStopRefresh();
    }

    @Override
    public void onNullResponse() throws JSONException {
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
        if (!HttpAction.checkNet(getContext())) {
            return;
        }

        if (!PermissionAction.check(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)) {
            onStopRefresh();

            new AlertDialog.Builder(getContext())
                    .setTitle(getContext().getString(R.string.auth_dialog_title))
                    .setMessage(getContext().getString(R.string.auth_dialog_map_permission_msg))
                    .setPositiveButton(getContext().getString(R.string.base_dialog_btn_okay), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();

            return;
        }

        setupMapView(getView());
    }

    private void onStopRefresh() {
        if (getView() != null && exp_refresh != null && exp_refresh.isRefreshing()) {
            exp_refresh.setRefreshing(false);
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.exp_btn:
                onCreateLocationDialog();
                break;

            case R.id.location_btn:
                onConfirmLocation();
                break;

            case R.id.net_down_imgbtn:
                onRefreshLayout(v);
                break;

            default:
                break;
        }
    }

    private void onCreateLocationDialog() {
        LayoutInflater inflater = getLayoutInflater(null);
        View view = inflater.inflate(R.layout.public_location_spinner_item, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);
        setupSpinnerGroup(view);

        // 获取屏幕的高度，设定标准的对话框高度为屏幕的一半的大小
        ScreenSize size = new ScreenSize(getContext());
        int standard_height = (int) (size.getHeight() * 0.5);

        Dialog dialog = builder.create();
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.height = standard_height;

        dialogWindow.setAttributes(lp);
        dialog.show();
        dialog.show();

        setupConfirmBtn(view);
    }

    private void onConfirmLocation() {
        if (province_str.equals(getString(R.string.base_item_all))) {
            province_str = "";
        }

        if (city_str.equals(getString(R.string.base_item_all))) {
            city_str = "";
        }

        if (county_str.equals(getString(R.string.base_item_all))) {
            county_str = "";
        }

        DataBaseAction baseAction = DataBaseAction.onCreate(getContext());
        ArrayList<String> shopNameList =
                baseAction.query(AddressSet.TABLE_NAME,
                        new String[]{AddressSet.PROVINCE, AddressSet.CITY, AddressSet.COUNTY},
                        new String[]{province_str, city_str, county_str},
                        AddressSet.SHOP, "");

        onCreateShopDialog(shopNameList);

//        if (dialog != null && dialog.isShowing()) {
//            dialog.dismiss();
//        }
    }

    private void onRefreshLayout(View view) {
        final View child = view;
        final View parent = (View) view.getParent();

        child.setVisibility(View.INVISIBLE);

        final TextView net_down_tv = (TextView) parent.findViewById(R.id.net_down_tv);
        net_down_tv.setVisibility(View.INVISIBLE);

        final ContentLoadingProgressBar net_down_pb = (ContentLoadingProgressBar) parent.findViewById(R.id.net_down_pb);
        net_down_pb.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (HttpAction.checkNet(getContext())) {
                    final View net_down_rl = parent.findViewById(R.id.exp_net_down_view);
                    net_down_rl.setVisibility(View.GONE);

                    initExpView(getView());
                } else {
                    child.setVisibility(View.VISIBLE);
                    net_down_tv.setVisibility(View.VISIBLE);
                    net_down_pb.setVisibility(View.GONE);
                }
            }
        }, 2 * 1000);
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int topRowVerticalPosition =
                    (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
            exp_refresh.setEnabled(topRowVerticalPosition >= 0);
        }
    };

    private void onCreateShopDialog(ArrayList<String> shopNameList) {
        if (shopNameList == null || (shopNameList.size() == 0)) {
            return;
        }

        ExpRycAdapter adapter = new ExpRycAdapter(initShopListData(shopNameList));

        LayoutInflater inflater = getLayoutInflater(null);
        View view = inflater.inflate(R.layout.public_shop_list_page, null);

        final RecyclerView shop_list_rv = (RecyclerView) view.findViewById(R.id.shop_list_rv);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        shop_list_rv.setLayoutManager(manager);
        shop_list_rv.setItemAnimator(new DefaultItemAnimator());
        shop_list_rv.setAdapter(adapter);

        // 获取屏幕的高度，设定标准的对话框高度为屏幕的一半的大小
        ScreenSize size = new ScreenSize(getContext());
        int standard_height = size.getHeight() / 2;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view);

        Dialog dialog = builder.create();

        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.height = standard_height;

        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    private ArrayList<ObjectShop> initShopListData(ArrayList<String> shopNameList) {
        ArrayList<ObjectShop> shopList = new ArrayList<>();

        DataBaseAction baseAction = DataBaseAction.onCreate(getContext());
        if (shopNameList == null) {
            shopNameList = baseAction.query(AddressSet.TABLE_NAME, new String[]{""}, new String[]{""}, AddressSet.SHOP, "");
        }
        ArrayList<String> shopLocList = new ArrayList<>();

        for (int i = 0; i < shopNameList.size(); i++) {
            String shop_name = shopNameList.get(i);
            ArrayList<String> singleLoc_List = baseAction.
                    query(AddressSet.TABLE_NAME, new String[]{AddressSet.SHOP}, new String[]{shop_name}, AddressSet.ROAD, "");
            shopLocList.add(singleLoc_List.get(0));
        }

        for (int i = 0; i < shopNameList.size(); i++) {
            ObjectShop shop = new ObjectShop();

            shop.setName(shopNameList.get(i));
            shop.setLocation(shopLocList.get(i));
            shopList.add(shop);
        }

        return shopList;
    }
}
