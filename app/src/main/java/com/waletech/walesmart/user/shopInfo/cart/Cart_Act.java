package com.waletech.walesmart.user.shopInfo.cart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.BaseAction;
import com.waletech.walesmart.base.Base_Act;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicSet.MapSet;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class Cart_Act extends Base_Act implements SwipeRefreshLayout.OnRefreshListener, CompoundButton.OnCheckedChangeListener, Toolbar.OnMenuItemClickListener {

    private SwipeRefreshLayout cart_refresh;
    private RecyclerView cart_rv;

    private CartAction cartAction;
    private ClickListener clickListener;

    private ArrayList<ObjectShoe> shoeList;

    private LineToast toast;

    private boolean isFstInit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shop_info_cart_layout);

        if (!HttpAction.checkNet(this)) {
            finish();
        }

        varInit();

        setupToolbar();

        setupSwipeRefresh();
    }

    private void varInit() {
        cartAction = new CartAction(this);
        cartAction.getCartList();
        clickListener = new ClickListener(this, cartAction);

        shoeList = new ArrayList<>();

        toast = new LineToast(this);

        isFstInit = true;
    }

    @Override
    protected void setupToolbar() {
        setTbTitle(getString(R.string.cart_toolbar_title));
        setTbNavigation();

        toolbar.showOverflowMenu();
        toolbar.setOnMenuItemClickListener(this);
    }

    private void setupSwipeRefresh() {
        cart_refresh = (SwipeRefreshLayout) findViewById(R.id.cart_sr);
        cart_refresh.setColorSchemeResources(R.color.colorMain, R.color.colorSpecial, R.color.colorSpecialConverse);
        cart_refresh.setOnRefreshListener(this);
    }

    private void setupRecyclerView() {
        CartRycAdapter adapter = new CartRycAdapter(shoeList, this);
        adapter.setCartAction(cartAction);

        cart_rv = (RecyclerView) findViewById(R.id.cart_rv);
        cart_rv.setLayoutManager(new LinearLayoutManager(this));
        cart_rv.setAdapter(adapter);
        cart_rv.addOnScrollListener(new LoadScrollListener());
    }

    private void setupBuyBar() {
//        final CheckBox whole_cb = (CheckBox) findViewById(R.id.rv_whole_cb);
//        whole_cb.setChecked(false);
//        whole_cb.setOnCheckedChangeListener(this);

        CartRycAdapter adapter = (CartRycAdapter) cart_rv.getAdapter();
        ArrayList<String> checkList = adapter.getCheckList();

        HashMap<String, Object> map = new HashMap<>();
        map.put(MapSet.KEY_SHOE_LIST, shoeList);
        map.put(MapSet.KEY_CHECK_LIST, checkList);

        final TextView make_order_tv = (TextView) findViewById(R.id.rv_make_order_tv);
        make_order_tv.setTag(map);
        make_order_tv.setOnClickListener(clickListener);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        onStopRefresh();

        if (isFstInit) {
            shoeList = cartAction.handleListResponse(result);

            setupRecyclerView();

            setupBuyBar();

            isFstInit = false;
            return;
        }

        CartRycAdapter adapter = (CartRycAdapter) cart_rv.getAdapter();

        switch (tag) {
            //
            case HttpTag.CART_GET_CART_LIST:
                switch (cartAction.getAction()) {
                    case BaseAction.ACTION_DEFAULT_REFRESH:
                        shoeList = cartAction.handleListResponse(result);
                        break;

                    case BaseAction.ACTION_LOAD_MORE:
                        ArrayList<ObjectShoe> tempList = cartAction.handleListResponse(result);
                        for (int i = 0; i < tempList.size(); i++) {
                            shoeList.add(tempList.get(i));
                        }
                        break;

                    default:
                        break;
                }
                break;

            //
            case HttpTag.CART_UPDATE_CART:
                cartAction.handleResponse(result);

                cartAction.getCartList();
                break;

            case HttpTag.CART_DELETE_CART:
            case HttpTag.CART_CREATE_ORDER:

                adapter.clearCheckList();
//                final CheckBox whole_cb = (CheckBox) findViewById(R.id.rv_whole_cb);
//                whole_cb.setChecked(false);

                cartAction.handleResponse(result);
                cartAction.getCartList();
                return;

            default:
                break;
        }

        adapter.setShoeList(shoeList);
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

    private void onDeleteItem(CartRycAdapter adapter) {
        ArrayList<String> checkList = adapter.getCheckList();

        if (checkList == null || checkList.size() == 0) {
            toast.showToast(getString(R.string.base_toast_no_item_been_selected));
        } else {
            StringBuilder sku_code_builder = new StringBuilder("");

            for (int i = 0; i < checkList.size(); i++) {
                ObjectShoe shoe = shoeList.get(i);

                if (i == (checkList.size() - 1)) {
                    sku_code_builder.append(shoe.getRemark());
                } else {
                    sku_code_builder.append(shoe.getRemark()).append("-");
                }
            }
            cartAction.deleteCart(sku_code_builder.toString());
        }
    }

    public void onModifyPrice(CartRycAdapter adapter) {
        int whole_price = 0;

        ArrayList<String> checkList = adapter.getCheckList();
        for (int i = 0; i < checkList.size(); i++) {
            int position = Integer.parseInt(checkList.get(i));
            ObjectShoe shoe = shoeList.get(position);

            int price = Integer.parseInt(shoe.getPrice()) * Integer.parseInt(shoe.getCount());
            whole_price = whole_price + price;
        }

        final TextView whole_price_tv = (TextView) findViewById(R.id.rv_whole_price_tv);
        String whole_price_str = getString(R.string.cart_whole_price_tv) + whole_price + " " + getString(R.string.cart_rmb_tv);
        whole_price_tv.setText(whole_price_str);
    }

    @Override
    public void onRefresh() {
        cartAction.getCartList();
    }

    private void onStopRefresh() {
        if (cart_refresh != null && cart_refresh.isRefreshing()) {
            cart_refresh.setRefreshing(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        final CartRycAdapter adapter = (CartRycAdapter) cart_rv.getAdapter();

        switch (item.getItemId()) {
            case R.id.menu_tb_cart_item0:
                new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.base_dialog_delete_title))
                        .setMessage(getString(R.string.base_dialog_delete_message))
                        .setPositiveButton(getString(R.string.base_dialog_btn_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onDeleteItem(adapter);
                            }
                        })
                        .setNegativeButton(getString(R.string.base_dialog_btn_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                break;

            case R.id.menu_tb_cart_item1:
                if (item.getTitle().equals(getString(R.string.cart_menu_item_finish))) {
                    item.setTitle(getString(R.string.cart_menu_item_edit));
                    adapter.setIsEditMode(false);

                    cartAction.getCartList();
                } else {
                    item.setTitle(getString(R.string.cart_menu_item_finish));
                    adapter.setIsEditMode(true);
                }

                for (int i = 0; i < adapter.getItemCount(); i++) {
                    adapter.notifyItemChanged(i);
                }
                break;

            default:
                break;
        }
        return false;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        toast.showToast(ToastSet.NO_OPEN_FUNCTION);

//        CartRycAdapter adapter = (CartRycAdapter) cart_rv.getAdapter();
//
//        final TextView whole_price_tv = (TextView) findViewById(R.id.rv_whole_price_tv);
//
//        adapter.clearCheckList();
//        adapter.setIsWholeClick(true);
//
//        if (isChecked) {
//            adapter.setWholeCheck(true);
//
//            int whole_price = 0;
//            for (int i = 0; i < shoeList.size(); i++) {
//                ObjectShoe shoe = shoeList.get(i);
//                int price = Integer.parseInt(shoe.getPrice()) * Integer.parseInt(shoe.getCount());
//                whole_price = whole_price + price;
//            }
//
//            String price_str = "合计金额：" + whole_price + "元";
//            whole_price_tv.setText(price_str);
//        } else {
//            adapter.setWholeCheck(false);
//            String price_str = "合计金额：0.00";
//            whole_price_tv.setText(price_str);
//        }
//
//        for (int i = 0; i < adapter.getItemCount(); i++) {
////            adapter.setIsWholeClick(true);
//            adapter.notifyItemChanged(i);
//        }
//
////        adapter.clearCheckList();
//        if (isChecked) {
//            adapter.fillCheckList();
//        }
//
//        Log.i("Result", "checkList clear size is : " + adapter.getCheckList().size());
    }

    private class LoadScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            CartRycAdapter adapter = (CartRycAdapter) recyclerView.getAdapter();
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();

            if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                int total_item = manager.getItemCount();

                if (total_item == 0) {
                    return;
                }

                int last_item = manager.findLastCompletelyVisibleItemPosition();
                // 如果总共有8个item，即total = 8，但是因为last是从0开始的，最后一个也就是到7，所以last需要多加1
                last_item++;

                // 判断child item的个数有没有超出屏幕，如果有，才允许上拉加载
                // 这里只要判断child item的总高度有没有超过manager(即recyclerview)的高度就可以了

                // 当然在 recyclerview里面判断是否高于屏幕也可以！
                int parent_height = manager.getHeight();

                int child_height = manager.getChildAt(0).getHeight();
                int total_height = total_item * child_height;

                if (total_height < parent_height) {
                    return;
                }

                // 否则就进行上拉加载更多
                if (last_item == total_item) {
                    if (adapter.getIsShowLoad()) {
                        return;
                    }

                    adapter.setIsShowLoad(true);
                    adapter.notifyItemChanged(manager.findLastCompletelyVisibleItemPosition());
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    }

}
