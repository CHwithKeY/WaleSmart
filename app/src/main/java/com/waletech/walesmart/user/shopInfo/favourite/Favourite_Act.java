package com.waletech.walesmart.user.shopInfo.favourite;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.waletech.walesmart.LoadScrollListener;
import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicObject.ObjectShoe;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Favourite_Act extends Base_Act implements SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener, AdapterView.OnItemSelectedListener {

    private SwipeRefreshLayout fav_refresh;
    private RecyclerView fav_rv;

    private FavAction favAction;

    private ArrayList<ObjectShoe> shoeList;

    private LineToast toast;

    private boolean isFstInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shop_info_favourite_layout);

        varInit();

        setupToolbar();

        setupSwipeRefresh();
    }

    private void varInit() {
        shoeList = new ArrayList<>();

        favAction = new FavAction(this);
        favAction.getFavList();

        toast = new LineToast(this);

        isFstInit = true;
    }

    @Override
    protected void setupToolbar() {
        setTbTitle(getString(R.string.favourite_toolbar_title));
        setTbNavigation();

        toolbar.showOverflowMenu();
        toolbar.setOnMenuItemClickListener(this);
    }

    private final static int TAG_BRAND = 0;
    private final static int TAG_SHOP = 1;

    private ArrayAdapter<String> initSpinnerData(int tag) {
        HashSet<String> set = new HashSet<>();

        for (int i = 0; i < shoeList.size(); i++) {
            ObjectShoe shoe = shoeList.get(i);
            switch (tag) {
                case TAG_BRAND:
                    set.add(shoe.getBrand());
                    break;

                case TAG_SHOP:
                    set.add(shoe.getShopName());
                    break;

                default:
                    break;
            }
        }

        Iterator<String> iterator = set.iterator();

        ArrayList<String> list = new ArrayList<>();

        while (iterator.hasNext()) {
            list.add(iterator.next());
        }

        switch (tag) {
            case TAG_BRAND:
                list.add(0, getString(R.string.base_item_all));
                break;

            case TAG_SHOP:
                list.add(0, getString(R.string.base_item_all));
                break;

            default:
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_dark_text_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        return adapter;
    }

//    private void setupFilterBar() {
//        final TextView date_tv = (TextView) findViewById(R.id.filter_tv);
//        date_tv.setVisibility(View.GONE);
//
//        final AppCompatSpinner fst_spn = (AppCompatSpinner) findViewById(R.id.filter_spn2);
//        fst_spn.setVisibility(View.GONE);
//
//        final AppCompatSpinner brand_spn = (AppCompatSpinner) findViewById(R.id.filter_spn0);
//        final AppCompatSpinner shop_spn = (AppCompatSpinner) findViewById(R.id.filter_spn1);
//
//        brand_spn.setAdapter(initSpinnerData(TAG_BRAND));
//        brand_spn.setOnItemSelectedListener(this);
//        brand_spn.setTag(TAG_BRAND);
//        brand_spn.setDropDownVerticalOffset(brand_spn.getLayoutParams().height);
//
//        shop_spn.setAdapter(initSpinnerData(TAG_SHOP));
//        shop_spn.setOnItemSelectedListener(this);
//        shop_spn.setTag(TAG_SHOP);
//        shop_spn.setDropDownVerticalOffset(shop_spn.getLayoutParams().height);
//    }

    private void setupSwipeRefresh() {
        fav_refresh = (SwipeRefreshLayout) findViewById(R.id.fav_sr);
        fav_refresh.setColorSchemeResources(R.color.colorMain, R.color.colorSpecial, R.color.colorSpecialConverse);
        fav_refresh.setOnRefreshListener(this);
    }

    private void setupRecyclerView() {
        FavRycAdapter adapter = new FavRycAdapter(shoeList, this);
        adapter.setBaseAction(favAction);

        fav_rv = (RecyclerView) findViewById(R.id.fav_rv);
        fav_rv.setLayoutManager(new LinearLayoutManager(this));
        fav_rv.setItemAnimator(new DefaultItemAnimator());
        fav_rv.setAdapter(adapter);
        fav_rv.addOnScrollListener(new LoadScrollListener(adapter));
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        onStopRefresh();

        if (isFstInit) {
            shoeList = favAction.handleListResponse(result);

            setupRecyclerView();
            // setupFilterBar();

            isFstInit = false;
            return;
        }

        FavRycAdapter adapter = (FavRycAdapter) fav_rv.getAdapter();

        switch (tag) {
            case HttpTag.FAVOURITE_GET_FAVOURITE_LIST:
                switch (favAction.getAction()) {
                    case BaseAction.ACTION_DEFAULT_REFRESH:
                        shoeList = favAction.handleListResponse(result);
                        break;

                    case BaseAction.ACTION_FILTER:
                        shoeList = favAction.handleListResponse(result);
                        break;

                    case BaseAction.ACTION_LOAD_MORE:
                        ArrayList<ObjectShoe> tempList = favAction.handleListResponse(result);

                        for (int i = 0; i < tempList.size(); i++) {
                            shoeList.add(tempList.get(i));
                        }
                        break;

                    default:
                        break;
                }

                adapter.setShoeList(shoeList);
                adapter.initInnerList();

                adapter.notifyDataSetChanged();
                break;

            case HttpTag.FAVOURITE_DELETE_BATCH_FAVOURITE:
                favAction.handleResponse(result);
                favAction.getFavList();
                break;

//            case HttpTag.FAVOURITE_DELETE_FAVOURITE:
//                ArrayList<String> epcList = adapter.getEpcList();
//                epcList.remove(adapter.getEpcList().size() - 1);
//                if (adapter.getEpcList().size() == 0) {
//                    favAction.handleResponse(result);
//                    favAction.getFavList();
//                } else {
//                    favAction.operate(FavAction.OPERATION_DELETE, epcList.get(adapter.getEpcList().size() - 1));
//                }
//                break;

            default:
                break;
        }
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

    @Override
    public void onRefresh() {
        favAction.getFavList();
        // favAction.getFilterList("", "");
    }

    private void onStopRefresh() {
        if (fav_refresh != null && fav_refresh.isRefreshing()) {
            fav_refresh.setRefreshing(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_favourite_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.base_dialog_delete_title))
                .setMessage(getString(R.string.base_dialog_delete_message))
                .setPositiveButton(getString(R.string.base_dialog_btn_yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FavRycAdapter adapter = (FavRycAdapter) fav_rv.getAdapter();
                        ArrayList<String> epcList = adapter.getEpcList();

                        if (epcList == null || epcList.size() == 0) {
                            toast.showToast(getString(R.string.base_toast_no_item_been_selected));
                        } else {
                            StringBuilder epc_code_builder = new StringBuilder("");

                            for (int i = 0; i < epcList.size(); i++) {
                                if (i == (epcList.size() - 1)) {
                                    epc_code_builder.append(epcList.get(i));
                                } else {
                                    epc_code_builder.append(epcList.get(i)).append("-");
                                }
                            }

                            favAction.operate(FavAction.OPERATION_DELETE_BATCH, epc_code_builder.toString());
                        }
                    }
                })
                .setNegativeButton(getString(R.string.base_dialog_btn_no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();

        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int tag = Methods.cast(parent.getTag());

        TextView item_tv = (TextView) view;
        String filter_str = item_tv.getText().toString();

        if (filter_str.equals(getString(R.string.base_item_all))) {
            filter_str = "All";
        }

        switch (tag) {
            case TAG_BRAND:
                favAction.getFilterList(filter_str, "");
                break;

            case TAG_SHOP:
                favAction.getFilterList("", filter_str);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
