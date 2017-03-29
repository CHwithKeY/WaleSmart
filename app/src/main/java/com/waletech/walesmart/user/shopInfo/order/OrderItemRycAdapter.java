package com.waletech.walesmart.user.shopInfo.order;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waletech.walesmart.BaseShoeRycAdapter;
import com.waletech.walesmart.R;
import com.waletech.walesmart.publicObject.ObjectShoe;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/7/26.
 */
public class OrderItemRycAdapter extends BaseShoeRycAdapter {

    public OrderItemRycAdapter(ArrayList<ObjectShoe> shoeList, Context context) {
        super(shoeList, context);
    }

    @Override
    public BaseShoeRycAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_shoe_order_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseShoeRycAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ObjectShoe shoe = shoeList.get(position);
        holder.item_ll.setOnClickListener(null);

        ViewHolder child_holder = (ViewHolder) holder;

        String count_str = context.getString(R.string.order_item_item_count) + "\n" + shoe.getCount();
        child_holder.count_tv.setText(count_str);

        String price_str = context.getString(R.string.order_item_item_price) + "\n" + shoe.getPrice();
        child_holder.price_tv.setText(price_str);

//        if (!shoe.getSmarkId().equals("miss")) {
//            child_holder.missed_tv.setVisibility(View.GONE);
//        } else {
//            child_holder.missed_tv.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class ViewHolder extends BaseShoeRycAdapter.ViewHolder {
        TextView count_tv;
        TextView price_tv;

//        TextView missed_tv;

        public ViewHolder(View itemView) {
            super(itemView);

            count_tv = (TextView) itemView.findViewById(R.id.rv_count_tv);
            price_tv = (TextView) itemView.findViewById(R.id.rv_price_tv);

//            missed_tv = (TextView) itemView.findViewById(R.id.rv_missed_tv);
        }
    }
}
