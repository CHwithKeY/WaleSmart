package com.waletech.walesmart.main.experience;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicObject.ObjectShop;
import com.waletech.walesmart.publicSet.IntentSet;
import com.waletech.walesmart.shop.Shop_Act;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/6/28.
 */
public class ExpRycAdapter extends RecyclerView.Adapter<ExpRycAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;

    private ArrayList<ObjectShop> shopList;
    private LineToast toast;

    public ExpRycAdapter(ArrayList<ObjectShop> shopList) {
        this.shopList = shopList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        toast = new LineToast(context);

        View view = View.inflate(context, R.layout.recycler_shop_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ObjectShop shop = shopList.get(position);

        if (shop.getLocation().equals("")) {
            holder.shop_name_tv.setText(context.getString(R.string.exp_no_store_this_area_tv));
            return;
        }

        holder.item_ll.setTag(position);
        holder.item_ll.setOnClickListener(this);

        holder.shop_name_tv.setText(shop.getName());
        holder.shop_loc_tv.setText(shop.getLocation());
    }

    @Override
    public int getItemCount() {
        return shopList == null ? 0 : shopList.size();
    }

    @Override
    public void onClick(View v) {
        if (!Methods.isNetworkAvailable(context)) {
            toast.showToast(context.getString(R.string.base_toast_net_down));
            return;
        }

        int position = Methods.cast(v.getTag());
        ObjectShop shop = shopList.get(position);

        Intent shop_int = new Intent(context, Shop_Act.class);
        shop_int.putExtra(IntentSet.KEY_SHOP_NAME, shop.getName());
        shop_int.putExtra(IntentSet.KEY_SHOP_LOCATION, shop.getLocation());
        context.startActivity(shop_int);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout item_ll;

        TextView shop_name_tv;
        TextView shop_loc_tv;

        LinearLayout load_ll;

        public ViewHolder(View itemView) {
            super(itemView);

            item_ll = (LinearLayout) itemView.findViewById(R.id.rv_ll);

            shop_name_tv = (TextView) itemView.findViewById(R.id.rv_shop_name_tv);
            shop_loc_tv = (TextView) itemView.findViewById(R.id.rv_shop_loc_tv);

            load_ll = (LinearLayout) itemView.findViewById(R.id.rv_load_ll);
        }
    }
}
