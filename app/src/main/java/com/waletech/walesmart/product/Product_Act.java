package com.waletech.walesmart.product;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.datainfo.AddressSet;
import com.waletech.walesmart.datainfo.DataBaseAction;
import com.waletech.walesmart.http.HttpResult;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.main.experience.ExpRycAdapter;
import com.waletech.walesmart.publicAction.UnlockAction;
import com.waletech.walesmart.publicClass.BitmapCache;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicClass.ScreenSize;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicObject.ObjectShop;
import com.waletech.walesmart.publicSet.IntentSet;
import com.waletech.walesmart.publicSet.MapSet;
import com.waletech.walesmart.shop.Shop_Act;
import com.waletech.walesmart.user.authInfo.PermissionAction;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Product_Act extends Base_Act implements SwipeRefreshLayout.OnRefreshListener, Toolbar.OnMenuItemClickListener {
    private SwipeRefreshLayout product_refresh;
    private ImageButton fav_imgbtn;

    private ObjectShoe shoe;

    private ProductAction productAction;
    private ClickListener clickListener;

    private boolean isFromShop;

    private boolean isFstInit;
    private boolean isHaveFav;

    private BitmapCache cache;

    private LineToast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_layout);

        varInit();

        setupToolbar();

        setupSwipeRefresh();
    }

    private void varInit() {
        String epc_code_str = getIntent().getStringExtra(IntentSet.KEY_EPC_CODE);
        isFromShop = getIntent().getBooleanExtra(IntentSet.KEY_IS_FROM_SHOP, false);

        productAction = new ProductAction(this);
        clickListener = new ClickListener(this, productAction);

        isFstInit = true;
        isHaveFav = false;

        cache = new BitmapCache();

        toast = new LineToast(this);

        productAction.getShoeDetails(epc_code_str);
    }

    @Override
    protected void setupToolbar() {
        setTbTitle(getString(R.string.product_toolbar_title));
        setTbNavigation();

        toolbar.showOverflowMenu();
        toolbar.setOnMenuItemClickListener(this);
    }

    private void setupSwipeRefresh() {
        product_refresh = (SwipeRefreshLayout) findViewById(R.id.product_sr);
        product_refresh.setColorSchemeResources(R.color.colorMain, R.color.colorSpecial, R.color.colorSpecialConverse);
        product_refresh.setOnRefreshListener(this);
    }

    private void setupShoeImage() {
        final ImageView shoe_img = (ImageView) findViewById(R.id.product_img);
        String img_url = HttpSet.BASE_URL + shoe.getImagePath();

        if (cache.getBitmap(img_url) == null) {
            // 请求下载图片
            Methods.downloadImage(shoe_img, img_url, cache);
        } else {
            shoe_img.setImageBitmap(cache.getBitmap(img_url));
        }
    }

    private void setupDetailsList() {
        final ListView standard_lv = (ListView) findViewById(R.id.product_lv0);
        final ListView params_lv = (ListView) findViewById(R.id.product_lv1);

        String location = shoe.getProvince() + shoe.getCity() + shoe.getCounty() + shoe.getShopName();
        String[] detail_info = {shoe.getBrand(), shoe.getDesign(), shoe.getColor(), shoe.getSize(), location};
        List<String> list = Arrays.asList(detail_info);

        ArrayAdapter standard_adapter = ArrayAdapter.createFromResource(this, R.array.product_standard, R.layout.listview_text_item);
        standard_lv.setAdapter(standard_adapter);

        ArrayAdapter params_adapter = new ArrayAdapter<>(this, R.layout.listview_text_item, list);
        params_lv.setAdapter(params_adapter);
    }

    private void setupBottomBar() {
        final Button add_cart_btn = (Button) findViewById(R.id.product_btn);
        add_cart_btn.setTag(shoe);
        add_cart_btn.setOnClickListener(clickListener);

        int count = Integer.parseInt(shoe.getCount());
        if (count > 0) {
            // 有库存
            add_cart_btn.setVisibility(View.VISIBLE);
            add_cart_btn.setOnClickListener(clickListener);
        } else {
            // 没有库存
            add_cart_btn.setVisibility(View.GONE);

            final TextView no_stock_tv = (TextView) findViewById(R.id.product_tv0);
            no_stock_tv.setVisibility(View.VISIBLE);
        }

        // Unlock
        final ImageButton unlock_imgbtn = (ImageButton) findViewById(R.id.product_imgbtn0);
        unlock_imgbtn.setOnClickListener(clickListener);

        // Favourite
        fav_imgbtn = (ImageButton) findViewById(R.id.product_imgbtn1);
        if (isHaveFav) {
            fav_imgbtn.setImageResource(R.drawable.ic_public_favorite_light);
        }
        // 传递到 ClickListener 中，便于 FavAction() 的判断
        onWrapFavTag();
        fav_imgbtn.setOnClickListener(clickListener);

        // Enter Cart
        final ImageButton enter_cart_imgbtn = (ImageButton) findViewById(R.id.product_imgbtn2);
        enter_cart_imgbtn.setOnClickListener(clickListener);
    }

    private void onWrapFavTag() {
        HashMap<String, Object> map = new HashMap<>();
        map.put(MapSet.KEY_IS_HAVE_FAVOURITE, isHaveFav);
        map.put(MapSet.KEY_EPC_CODE, shoe.getEpcCode());
        fav_imgbtn.setTag(map);
    }

    private void setupSearchOtherShop() {
        final TextView other_shop_tv = (TextView) findViewById(R.id.product_tv1);
        other_shop_tv.setTag(shoe.getEpcCode());
        other_shop_tv.setOnClickListener(clickListener);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        onStopRefresh();

        switch (tag) {
            case HttpTag.PRODUCT_GET_SHOE_DETAILS:
                shoe = productAction.handleDetailsResponse(result);
                productAction.onCheckFavourite(shoe.getEpcCode());

                setupSearchOtherShop();

                break;

//            case HttpTag.PRODUCT_GET_SHOE_STOCK:
//                ArrayList<ObjectShoe> shoeList = productAction.handleStockResponse(result);
//                setupShoeStockView(shoeList);
//                break;

            case HttpTag.PRODUCT_GET_SHOE_OTHER_SHOP:
                ArrayList<String> shopNameList = productAction.handleOtherShopResponse(result);
                onCreateShopDialog(shopNameList);
                break;

            case HttpTag.FAVOURITE_CHECK_FAVOURITE:
                // 返回值有两个，"已有该收藏"和"没有该收藏"
                String result_str = new JSONObject(result).getString(HttpResult.RESULT);
                if (result_str.equals(getString(R.string.product_item_already_exists))) {
                    isHaveFav = true;
                }

                setupShoeImage();
                setupDetailsList();
                setupBottomBar();
                break;

            case HttpTag.UNLOCK_UNLOCK:
                toast.showToast(new JSONObject(result).getString(HttpResult.RESULT));
                break;

            case HttpTag.FAVOURITE_ADD_FAVOURITE:
                fav_imgbtn.setImageResource(R.drawable.ic_public_favorite_light);
                isHaveFav = true;
                onWrapFavTag();
                break;

            case HttpTag.FAVOURITE_DELETE_FAVOURITE:
                fav_imgbtn.setImageResource(R.drawable.ic_public_favorite_dark);
                isHaveFav = false;
                onWrapFavTag();
                break;

            default:
                break;
        }
    }

    @Override
    public void onNullResponse() throws JSONException {
        if (isFstInit) {
            finish();
        }
        onStopRefresh();
    }

    @Override
    public void onPermissionAccepted(int permission_code) {

    }

    @Override
    public void onPermissionRefused(int permission_code) {
        toast.showToast(getString(R.string.auth_toast_permission_camera_authorized));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == UnlockAction.REQUEST_MAIN) {
                String smark_num = data.getStringExtra("scan_result");
                productAction.unlock(smark_num);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionAction.handle(this, requestCode, grantResults);
    }

    @Override
    public void onRefresh() {
        productAction.getShoeDetails(shoe.getEpcCode());
    }

    private void onStopRefresh() {
        if (product_refresh != null && product_refresh.isRefreshing()) {
            product_refresh.setRefreshing(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_product_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (!isFromShop) {
            String location = shoe.getProvince() + shoe.getCity() + shoe.getCounty() + shoe.getRoad();

            Intent shop_int = new Intent(this, Shop_Act.class);
            shop_int.putExtra(IntentSet.KEY_SHOP_NAME, shoe.getShopName());
            shop_int.putExtra(IntentSet.KEY_SHOP_LOCATION, location);
            startActivity(shop_int);
        } else {
            finish();
        }
        return false;
    }

    private ArrayList<String> onFilterList(ArrayList<String> shopNameList) {
        for (int i = 0; i < shopNameList.size(); i++) {
            if (shopNameList.get(i).equals(shoe.getShopName())) {
                shopNameList.remove(i);
            }
        }

        return shopNameList;
    }

    private void onCreateShopDialog(ArrayList<String> shopNameList) {
        if (shopNameList == null || (shopNameList.size() == 0)) {
            return;
        }

        shopNameList = onFilterList(shopNameList);

        if (shopNameList == null || (shopNameList.size() == 0)) {
            toast.showToast(getString(R.string.product_toast_find_no_other_shop));
            return;
        }

        ExpRycAdapter adapter = new ExpRycAdapter(initShopListData(shopNameList));

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.public_shop_list_page, null);

        final RecyclerView shop_list_rv = (RecyclerView) view.findViewById(R.id.shop_list_rv);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        shop_list_rv.setLayoutManager(manager);
        shop_list_rv.setItemAnimator(new DefaultItemAnimator());
        shop_list_rv.setAdapter(adapter);

        // 获取屏幕的高度，设定标准的对话框高度为屏幕的一半的大小
        ScreenSize size = new ScreenSize(this);
        int standard_height = (int) (size.getHeight() * 0.5);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

        DataBaseAction baseAction = DataBaseAction.onCreate(this);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            collapsePattern();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 两次点击返回键退出
    public void collapsePattern() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        if (fragmentManager.findFragmentByTag("pattern_frag") != null) {
            transaction.remove(fragmentManager.findFragmentByTag("pattern_frag"));
            transaction.commit();
        } else {
            finish();
        }
    }
}
