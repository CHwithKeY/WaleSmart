package com.waletech.walesmart.user.shopInfo.cart;

import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.publicClass.BitmapCache;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicObject.ObjectShoe;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/6/30.
 */
public class CartRycAdapter extends RecyclerView.Adapter<CartRycAdapter.ViewHolder> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private ArrayList<ObjectShoe> shoeList;
    private ArrayList<String> checkList;

    private Context context;

    private BitmapCache cache;
    private LineToast toast;

    private boolean whole_check = false;
    private boolean isWholeClick = false;

    private boolean isShowLoad;
    private boolean isEditMode = false;

    private CartAction cartAction;

    public CartRycAdapter(ArrayList<ObjectShoe> shoeList, Context context) {
        this.shoeList = shoeList;
        this.context = context;

        cache = new BitmapCache();
        checkList = new ArrayList<>();

        isShowLoad = false;
    }

    public void setShoeList(ArrayList<ObjectShoe> shoeList) {
        this.shoeList = shoeList;
    }

    public void setWholeCheck(boolean whole_check) {
        this.whole_check = whole_check;
    }

    public void setIsWholeClick(boolean isWholeClick) {
        this.isWholeClick = isWholeClick;
    }

    public void setIsEditMode(boolean isEditMode) {
        this.isEditMode = isEditMode;
    }

    public void setCartAction(CartAction cartAction) {
        this.cartAction = cartAction;
    }

    public void setIsShowLoad(boolean isShowLoad) {
        this.isShowLoad = isShowLoad;
    }

    public boolean getIsShowLoad() {
        return isShowLoad;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        toast = new LineToast(context);
        View view = View.inflate(context, R.layout.recycler_shoe_cart_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ObjectShoe shoe = shoeList.get(position);

        holder.item_ll.setTag(position);
        holder.item_ll.setOnClickListener(this);

        View[] views = {holder.pattern_select_img, holder.count_add_img, holder.count_minus_img};
        for (View view : views) {
            view.setTag(position);
            view.setOnClickListener(this);

            if (isEditMode) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }

        holder.item_cb.setTag(position);
        holder.item_cb.setOnCheckedChangeListener(this);
        if (whole_check) {
            Log.i("Result", "setCheck true");
            holder.item_cb.setChecked(true);
        } else {
            Log.i("Result", "setCheck false");
            holder.item_cb.setChecked(false);
        }

        String name_str = shoe.getBrand() + "\t" + shoe.getDesign();
        holder.name_tv.setText(name_str);

        String params_str = shoe.getColor() + "\t" + shoe.getSize();
        holder.params_tv.setText(params_str);

        holder.shoe_img.setTag(HttpSet.BASE_URL + shoe.getImagePath());
        loadImage(holder, position);

        String count_str = shoe.getCount();
        holder.count_tv.setText(count_str);

        String price_str = "¥" + shoe.getPrice();
        holder.price_tv.setText(price_str);

        onLoadMore(holder, position);
    }

    @Override
    public int getItemCount() {
        return shoeList == null ? 0 : shoeList.size();
    }

    @Override
    public void onClick(View v) {
        int position = cast(v.getTag());

        ObjectShoe shoe = shoeList.get(position);

        View parent_view = (View) v.getParent();

        switch (v.getId()) {
            case R.id.rv_ll:
                // 购物车里是没有EPC的概念的，是以SKU为标准
//                String epc_code_str = shoe.getEpcCode();
//
//                Intent product_int = new Intent(context, Product_Act.class);
//                product_int.putExtra(IntentSet.KEY_EPC_CODE, epc_code_str);
//                context.startActivity(product_int);
                break;

            case R.id.rv_pattern_select_img:
                getPattern();
                break;

            case R.id.rv_count_add_img:
                int shoe_stock = Integer.parseInt(shoe.getCount());
                onAddCount(parent_view, shoe.getRemark());
                break;

            case R.id.rv_count_minus_img:
                onMinusCount(parent_view, shoe.getRemark());
                break;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout item_ll;

        CheckBox item_cb;

        TextView name_tv;
        TextView params_tv;

        ImageView shoe_img;

        ImageView pattern_select_img;

        TextView count_tv;
        ImageView count_add_img;
        ImageView count_minus_img;

        TextView price_tv;

        LinearLayout load_ll;

        public ViewHolder(View itemView) {
            super(itemView);

            item_ll = (LinearLayout) itemView.findViewById(R.id.rv_ll);

            item_cb = (CheckBox) itemView.findViewById(R.id.rv_cb);

            name_tv = (TextView) itemView.findViewById(R.id.rv_name_tv);
            params_tv = (TextView) itemView.findViewById(R.id.rv_params_tv);

            shoe_img = (ImageView) itemView.findViewById(R.id.rv_img);

            pattern_select_img = (ImageView) itemView.findViewById(R.id.rv_pattern_select_img);

            count_tv = (TextView) itemView.findViewById(R.id.rv_count_tv);
            count_add_img = (ImageView) itemView.findViewById(R.id.rv_count_add_img);
            count_minus_img = (ImageView) itemView.findViewById(R.id.rv_count_minus_img);

            price_tv = (TextView) itemView.findViewById(R.id.rv_price_tv);

            load_ll = (LinearLayout) itemView.findViewById(R.id.rv_load_ll);
        }
    }

    private void loadImage(ViewHolder holder, int position) {
        // 先判断缓存中是否缓存了这个item的imageview所在的url的图片
        String img_url = holder.shoe_img.getTag().toString();

        // 如果没有
        if (cache.getBitmap(img_url) == null) {
            // 请求下载图片
            Methods.downloadImage(holder.shoe_img, HttpSet.BASE_URL + shoeList.get(position).getImagePath(), cache);
            // downloadImage(holder, position, cache);
        } else {
            holder.shoe_img.setImageBitmap(cache.getBitmap(img_url));
        }
    }

    @SuppressWarnings("unchecked")
    protected static <T> T cast(Object obj) {
        return (T) obj;
    }

    private void getPattern() {
        // cartAction
    }

    private void onAddCount(View view, String sku_code) {
        final TextView count_tv = (TextView) view.findViewById(R.id.rv_count_tv);
        int old_count = Integer.parseInt(count_tv.getText().toString());
        // if (old > )
        int new_count = old_count + 1;

        cartAction.updateCount(sku_code, new_count);
    }

    private void onMinusCount(View view, String sku_code) {
        final TextView count_tv = (TextView) view.findViewById(R.id.rv_count_tv);
        int old_count = Integer.parseInt(count_tv.getText().toString());
        if (old_count == 1) {
            toast.showToast(context.getString(R.string.cart_toast_count_can_not_be_zero));
            return;
        }

        int new_count = old_count - 1;

        cartAction.updateCount(sku_code, new_count);
    }

    private void onLoadMore(final ViewHolder holder, int position) {
        if (position == (shoeList.size() - 1)) {
            if (isShowLoad) {
                holder.load_ll.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!HttpAction.checkNet(context)) {
                            holder.load_ll.setVisibility(View.GONE);
                        } else {
                            cartAction.getLoadList();
                        }
                        isShowLoad = false;
                    }
                }, 1000);
            }
        } else {
            holder.load_ll.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        String position = "" + (int) buttonView.getTag();

        if (isChecked) {
            checkList.add(position);
        } else {
            checkList.remove(position);
        }

        Context context = buttonView.getContext();
        AppCompatActivity compatActivity = (AppCompatActivity) context;
        ((Cart_Act) compatActivity).onModifyPrice(this);

//        if (isWholeClick) {
//            clearCheckList();
//            if (whole_check) {
//                fillCheckList();
//            }
//        }

        Log.i("Result", "checkList size in adapter is : " + checkList.size());
    }

    public ArrayList<String> getCheckList() {
        return checkList;
    }

    public void clearCheckList() {
        while (checkList.size() != 0) {
            checkList.remove(checkList.size() - 1);
        }
    }

    public void fillCheckList() {
        for (int i = 0; i < shoeList.size(); i++) {
            checkList.add("" + i);
        }
    }

}
