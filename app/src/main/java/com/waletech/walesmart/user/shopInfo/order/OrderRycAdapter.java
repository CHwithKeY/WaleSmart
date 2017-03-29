package com.waletech.walesmart.user.shopInfo.order;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicObject.ObjectOrder;
import com.waletech.walesmart.publicSet.IntentSet;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/7/26.
 */
public class OrderRycAdapter extends RecyclerView.Adapter<OrderRycAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;

    private ArrayList<ObjectOrder> orderList;

    public OrderRycAdapter(ArrayList<ObjectOrder> orderList) {
        this.orderList = orderList;
    }

    public void setOrderList(ArrayList<ObjectOrder> orderList) {
        this.orderList = orderList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_order_item, null);
        context = parent.getContext();

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ObjectOrder order = orderList.get(position);

        holder.date_tv.setText(order.getDateTime());

        String order_num_str = context.getString(R.string.order_item_order_number) + order.getOrderNum();
        holder.order_num_tv.setText(order_num_str);
        holder.order_num_tv.setTag(position);
        holder.order_num_tv.setOnClickListener(this);

        String total_price_str = context.getString(R.string.order_item_whole_price) + order.getTotalPrice();
        holder.total_price_tv.setText(total_price_str);

    }

    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();
        int position = Methods.cast(v.getTag());
        ObjectOrder order = orderList.get(position);

        Intent item_int = new Intent(context, Order_Item_Act.class);
        item_int.putExtra(IntentSet.KEY_ORDER_NUM, order.getOrderNum());
        context.startActivity(item_int);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView date_tv;
        TextView order_num_tv;

        TextView total_price_tv;

        public ViewHolder(View itemView) {
            super(itemView);

            date_tv = (TextView) itemView.findViewById(R.id.rv_date_tv);
            order_num_tv = (TextView) itemView.findViewById(R.id.rv_order_num_tv);

            total_price_tv = (TextView) itemView.findViewById(R.id.rv_total_price_tv);
        }
    }
}
