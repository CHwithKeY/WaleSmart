package com.waletech.walesmart.searchResult;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.waletech.walesmart.GeneralShoeRycAdapter;
import com.waletech.walesmart.LoadScrollListener;
import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.datainfo.AddressSet;
import com.waletech.walesmart.datainfo.BrandSet;
import com.waletech.walesmart.datainfo.DataBaseAction;
import com.waletech.walesmart.publicObject.ObjectShoe;

import org.json.JSONException;

import java.util.ArrayList;

public class Result_Act extends Base_Act implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private SwipeRefreshLayout result_refresh;
    private RecyclerView result_rv;

    private ArrayList<ObjectShoe> shoeList;

    private ResultAction resultAction;

    private boolean isFstInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_layout);

        varInit();

        setupSearchBar();

        setupSwipeRefresh();

        setupFilterText();

        setupFilterNav();

        setupGenderRadio();

        setupConfirmText();

        setupTopImgBtn();
    }

    private void varInit() {
        resultAction = new ResultAction(this);

//        String search_word_str = getIntent().getStringExtra(IntentSet.KEY_SEARCH_WORD);
        resultAction.searchFussy("All");

        shoeList = new ArrayList<>();

        isFstInit = true;
    }

    public ResultAction getResultAction() {
        return resultAction;
    }

    @Override
    protected void setupToolbar() {

    }

    private void setupSearchBar() {
        final TextView search_bar_tv = (TextView) findViewById(R.id.search_bar_tv);
        search_bar_tv.setOnClickListener(this);
    }

    public void setSearchBarText(String text) {
        final TextView search_bar_tv = (TextView) findViewById(R.id.search_bar_tv);
        search_bar_tv.setText(text);
    }

    private void setupSwipeRefresh() {
        result_refresh = (SwipeRefreshLayout) findViewById(R.id.result_sr);
        result_refresh.setColorSchemeResources(R.color.colorMain, R.color.colorSpecial, R.color.colorSpecialConverse);
        result_refresh.setOnRefreshListener(this);
    }

    private void setupRecyclerView() {
        GeneralShoeRycAdapter adapter = new GeneralShoeRycAdapter(shoeList, this);
        // 为了 "加载更多" 而用，但是对于 "正在开锁" 的这个部分是不需要的，因为允许开锁的数量肯定不可能超过5个
        // 所以不仅数量上达不到8个，就连高度也就根本不可能高于屏幕
        adapter.setBaseAction(resultAction);
        adapter.setModuleType(GeneralShoeRycAdapter.TYPE_RESULT);

        result_rv = (RecyclerView) findViewById(R.id.result_rv);
        result_rv.setLayoutManager(new LinearLayoutManager(this));
        result_rv.setItemAnimator(new DefaultItemAnimator());
        result_rv.setAdapter(adapter);

        result_rv.addOnScrollListener(new LoadScrollListener(adapter));
    }

    private void setupFilterText() {
        final TextView filter_tv = (TextView) findViewById(R.id.result_tv0);
        filter_tv.setOnClickListener(this);
    }

    private String brand = "";
    private ArrayList<Button> buttonList = new ArrayList<>();

    private void setupFilterNav() {
        DataBaseAction baseAction = DataBaseAction.onCreate(this);
        ArrayList<String> brandList = baseAction.query(BrandSet.TABLE_NAME, new String[]{""}, new String[]{""}, BrandSet.BRAND, "");
        brandList.add(brandList.size(), getString(R.string.base_item_all));

        final NavigationView filter_nav = (NavigationView) findViewById(R.id.result_nav);

        final LinearLayout brand_ll = (LinearLayout) filter_nav.findViewById(R.id.result_brand_ll);
        final Button brand_standard_btn = (Button) filter_nav.findViewById(R.id.result_brand_standard_btn);

        int nav_width = filter_nav.getLayoutParams().width;

        int btn_width = brand_standard_btn.getLayoutParams().width;
        int btn_height = brand_standard_btn.getLayoutParams().height;

        int single_line_count = nav_width / btn_width;

        if (single_line_count > 3) {
            single_line_count = 3;
        }

        int line_count = brandList.size() / single_line_count + 1;

        brand_standard_btn.setVisibility(View.GONE);

        int index = 0;
        for (int k = 0; k < line_count; k++) {
            Log.i("Result", "add linear");
            LinearLayout linear = new LinearLayout(this);
            linear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linear.setOrientation(LinearLayout.HORIZONTAL);
            linear.setGravity(Gravity.CENTER);
            linear.setPadding(0, 5, 0, 5);

            if (k == line_count - 1) {
                single_line_count = brandList.size() - k * single_line_count;
            }

            for (int i = 0; i < single_line_count; i++) {
                Button button = new Button(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(btn_width, btn_height);
                params.setMargins(5, 5, 5, 5);
                button.setLayoutParams(params);
                button.setText(brandList.get(index));
                button.setTextSize(12);
                button.setPadding(5, 5, 5, 5);
                button.setBackgroundResource(R.drawable.main_uncheck_btn);

                button.setTag(index);
                button.setOnClickListener(this);

                buttonList.add(button);

                linear.addView(button);
                index++;

                if ((k == line_count -1 ) && (i == single_line_count - 1)) {
                    button.setBackgroundResource(R.drawable.main_check_btn);
                }
            }

            brand_ll.addView(linear);
        }

        setupSpinnerGroup();
    }

    private String province_str = "";
    private String city_str = "";
    private String county_str = "";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setupSpinnerGroup() {
        DataBaseAction baseAction = DataBaseAction.onCreate(this);
        ArrayList<String> provinceList = baseAction.query(AddressSet.TABLE_NAME, new String[]{""}, new String[]{""}, AddressSet.PROVINCE, "");
        provinceList.add(0, getString(R.string.base_item_all));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_text_item, provinceList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner province_spn = (Spinner) findViewById(R.id.location_spn0);
        province_spn.setAdapter(adapter);
        province_spn.setDropDownVerticalOffset(province_spn.getLayoutParams().height);
        province_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView item_tv = (TextView) view;
                DataBaseAction baseAction = DataBaseAction.onCreate(Result_Act.this);

                province_str = item_tv.getText().toString();
                city_str = "All";
                county_str = "All";

                ArrayList<String> cityList =
                        baseAction.query(AddressSet.TABLE_NAME, new String[]{AddressSet.PROVINCE}, new String[]{province_str}, AddressSet.CITY, "");
                cityList.add(0, getString(R.string.base_item_all));

                ArrayAdapter<String> adapter = new ArrayAdapter<>(Result_Act.this, R.layout.spinner_text_item, cityList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                final Spinner city_spn = (Spinner) findViewById(R.id.location_spn1);
                city_spn.setAdapter(adapter);
                city_spn.setDropDownVerticalOffset(city_spn.getLayoutParams().height);

                city_spn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        TextView item_tv = (TextView) view;

                        city_str = item_tv.getText().toString();
                        county_str = "All";

                        DataBaseAction baseAction = DataBaseAction.onCreate(Result_Act.this);
                        ArrayList<String> countyList =
                                baseAction.query(AddressSet.TABLE_NAME, new String[]{AddressSet.CITY}, new String[]{city_str}, AddressSet.COUNTY, "");
                        countyList.add(0, getString(R.string.base_item_all));

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Result_Act.this, R.layout.spinner_text_item, countyList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        final Spinner county_spn = (Spinner) findViewById(R.id.location_spn2);
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

        final Button confirm_btn = (Button) findViewById(R.id.location_btn);
        confirm_btn.setVisibility(View.GONE);
    }

    private void setupGenderRadio() {
        final RadioButton male_rb = (RadioButton) findViewById(R.id.result_male_rb);
        final RadioButton female_rb = (RadioButton) findViewById(R.id.result_female_rb);
        final RadioButton general_rb = (RadioButton) findViewById(R.id.result_general_rb);
        final RadioButton all_rb = (RadioButton) findViewById(R.id.result_all_rb);

        all_rb.setChecked(true);

        RadioButton[] gender_rb = {male_rb, female_rb, general_rb, all_rb};
        for (RadioButton aGender_rb : gender_rb) {
            aGender_rb.setOnCheckedChangeListener(this);
        }
    }

    private void setupConfirmText() {
        final TextView confirm_tv = (TextView) findViewById(R.id.result_tv1);
        confirm_tv.setOnClickListener(this);
    }

    private void setupTopImgBtn() {
        final ImageButton top_imgbtn = (ImageButton) findViewById(R.id.result_imgbtn);
        top_imgbtn.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        Log.i("Result", "result act is : " + result);

        onStopRefresh();

        if (isFstInit) {
            shoeList = resultAction.handleResponse(result);
            isFstInit = false;

            setupRecyclerView();

            return;
        }

        GeneralShoeRycAdapter adapter = (GeneralShoeRycAdapter) result_rv.getAdapter();

        switch (resultAction.getAction()) {
            case BaseAction.ACTION_DEFAULT_REFRESH:
                shoeList = resultAction.handleResponse(result);
                break;

            case BaseAction.ACTION_FILTER:
                collapseFilterNav();
                shoeList = resultAction.handleResponse(result);
                break;

            case BaseAction.ACTION_LOAD_MORE:
                ArrayList<ObjectShoe> shoes = resultAction.handleResponse(result);
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

    @Override
    public void onNullResponse() throws JSONException {
        onStopRefresh();
        Log.i("Result", "on null response");

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
        resultAction.resultRefresh();
    }

    private void onStopRefresh() {
        if (result_refresh != null && result_refresh.isRefreshing()) {
            result_refresh.setRefreshing(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_bar_tv:
                onSearch();
                break;

            case R.id.result_imgbtn:
                result_rv.smoothScrollToPosition(0);
                break;

            case R.id.result_tv0:
                final DrawerLayout result_dl = (DrawerLayout) findViewById(R.id.result_dl);
                if (!result_dl.isDrawerOpen(GravityCompat.END)) {
                    result_dl.openDrawer(GravityCompat.END);
                }
                break;

            case R.id.result_tv1:
                if (province_str.equals(getString(R.string.base_item_all))) {
                    province_str = "All";
                }
                if (city_str.equals(getString(R.string.base_item_all))) {
                    city_str = "All";
                }
                if (county_str.equals(getString(R.string.base_item_all))) {
                    county_str = "All";
                }

                resultAction.searchFilter("", brand, gender, province_str, city_str, county_str);
                break;

            default:
                Button btn = (Button) v;
                btn.setBackgroundResource(R.drawable.main_check_btn);
                brand = btn.getText().toString();

                if (brand.equals(getString(R.string.base_item_all))) {
                    brand = "All";
                }

                int tag = (int) v.getTag();
                for (int i = 0; i < buttonList.size(); i++) {
                    int btn_tag = (int) buttonList.get(i).getTag();

                    if (btn_tag != tag) {
                        buttonList.get(i).setBackgroundResource(R.drawable.main_uncheck_btn);
                    }
                }
                break;
        }
    }

    private void onSearch() {
        final DrawerLayout result_dl = (DrawerLayout) findViewById(R.id.result_dl);
        if (result_dl.isDrawerOpen(GravityCompat.END)) {
            result_dl.closeDrawer(GravityCompat.END);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        Search_Frag search_frag = new Search_Frag();
        transaction.add(R.id.result_dl, search_frag, "search_frag");
        transaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            final DrawerLayout result_dl = (DrawerLayout) findViewById(R.id.result_dl);
            if (result_dl.isDrawerOpen(GravityCompat.END)) {
                result_dl.closeDrawer(GravityCompat.END);
            } else {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                if (fragmentManager.findFragmentByTag("search_frag") != null) {
                    transaction.remove(fragmentManager.findFragmentByTag("search_frag"));
                    transaction.commit();
                } else {
                    finish();
                }
            }

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void collapseFilterNav() {
        final DrawerLayout result_dl = (DrawerLayout) findViewById(R.id.result_dl);
        if (result_dl.isDrawerOpen(GravityCompat.END)) {
            result_dl.closeDrawer(GravityCompat.END);
        }
    }

    private String gender = "";

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            gender = buttonView.getText().toString();

//            switch (gender) {
//                case "Male":
//                    gender = "Men's shoes";
//                    break;
//
//                case "Female":
//                    gender = "Women's shoes";
//                    break;
//
//                default:
//                    break;
//            }

            if (gender.equals(getString(R.string.base_item_all))) {
                gender = "All";
            }
        }
    }

//    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
//        @Override
//        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
//            GeneralShoeRycAdapter adapter = (GeneralShoeRycAdapter) recyclerView.getAdapter();
//
//            if (newState == RecyclerView.SCROLL_STATE_IDLE) {
//
//                int total_item = manager.getItemCount();
//
//                if (total_item == 0) {
//                    return;
//                }
//
//                int last_item = manager.findLastCompletelyVisibleItemPosition();
//                // 如果总共有8个item，即total = 8，但是因为last是从0开始的，最后一个也就是到7，所以last需要多加1
//                last_item++;
//
//                // 判断child item的个数有没有超出屏幕，如果有，才允许上拉加载
//                // 这里只要判断child item的总高度有没有超过manager(即recyclerview)的高度就可以了
//
//                // 当然在 recyclerview里面判断是否高于屏幕也可以！
//                int parent_height = manager.getHeight();
//
//                int child_height = manager.getChildAt(0).getHeight();
//                int total_height = total_item * child_height;
//
//                if (total_height < parent_height) {
//                    return;
//                }
//
//                // 否则就进行上拉加载更多
//                if (last_item == total_item) {
//                    if (adapter.getIsShowLoad()) {
//                        return;
//                    }
//
//                    adapter.setIsShowLoad(true);
//                    adapter.notifyItemChanged(manager.findLastCompletelyVisibleItemPosition());
//                }
//            }
//        }
//    };
}
