package com.waletech.walesmart.user.managerInfo;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waletech.walesmart.BaseShoeRycAdapter;
import com.waletech.walesmart.R;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.user.lockInfo.record.RecordAction;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/6/29.
 */
public class ManagerRecordShoeRycAdapter extends BaseShoeRycAdapter {

    public ManagerRecordShoeRycAdapter(ArrayList<ObjectShoe> shoeList, Context context) {
        super(shoeList, context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        isShowLoad = false;

        View view = View.inflate(parent.getContext(), R.layout.recycler_shoe_manager_record_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseShoeRycAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        holder.item_ll.setOnClickListener(null);

        ViewHolder child_holder = (ViewHolder) holder;

        ObjectShoe shoe = shoeList.get(position);

        if (!shoe.getSmarkId().equals("miss")) {

            onShopDateBar(child_holder, position);

            String location_str = shoe.getProvince() + shoe.getCity() + shoe.getCounty() + shoe.getShopName()
                    + shoe.getSmarkRow() + "行" + shoe.getSmarkColumn() + "列";

            child_holder.bottom_bar_tv.setText(location_str);
            child_holder.missed_tv.setVisibility(View.GONE);

        } else {
            child_holder.date_tv.setText(shoe.getRemark());
            child_holder.missed_tv.setVisibility(View.VISIBLE);
        }

        // 让 UP 消失而不是 DOWN，是因为 DOWN 已经设定了 TextView的字体颜色为白色，省略了一行代码而已。。。
        child_holder.status_up_tv.setVisibility(View.GONE);

        String status_str = shoe.getStatus();
        child_holder.status_down_tv.setBackgroundResource(R.drawable.shoe_status_hbr_tv);
        child_holder.status_down_tv.setText(status_str);

        child_holder.load_ll.setVisibility(View.GONE);
    }

    class ViewHolder extends BaseShoeRycAdapter.ViewHolder {

        CardView date_cv;
        CardView shop_cv;

        TextView date_tv;
        TextView shop_tv;

        TextView usn_tv;

        TextView status_up_tv;
        TextView status_down_tv;

        TextView bottom_bar_tv;

        TextView missed_tv;

        LinearLayout load_ll;

        public ViewHolder(View itemView) {
            super(itemView);

            date_cv = (CardView) itemView.findViewById(R.id.rv_date_cv);
            shop_cv = (CardView) itemView.findViewById(R.id.rv_shop_cv);

            date_tv = (TextView) itemView.findViewById(R.id.rv_date_tv);
            shop_tv = (TextView) itemView.findViewById(R.id.rv_shop_tv);

//            usn_tv = (TextView) itemView.findViewById(R.id.rv_manager_record_usn_tv);

            status_up_tv = (TextView) itemView.findViewById(R.id.rv_status_tv0);
            status_down_tv = (TextView) itemView.findViewById(R.id.rv_status_tv1);

            bottom_bar_tv = (TextView) itemView.findViewById(R.id.rv_bottom_tv);

            missed_tv = (TextView) itemView.findViewById(R.id.rv_missed_tv);

            load_ll = (LinearLayout) itemView.findViewById(R.id.rv_load_ll);
        }
    }

    private void onShopDateBar(ViewHolder holder, int position) {
        ObjectShoe shoe = shoeList.get(position);

        String[] date_usn_arr = shoe.getRemark().split("!");
        holder.usn_tv.setText(date_usn_arr[0]);
        holder.date_tv.setText(date_usn_arr[1]);

        if (position == 0) {
            holder.shop_cv.setVisibility(View.VISIBLE);
            holder.shop_tv.setText(shoe.getShopName());
        } else {
            ObjectShoe shoe_before = shoeList.get(position - 1);

            String before_shop = shoe_before.getShopName();
            String shop_str = shoe.getShopName();

            if (!before_shop.equals(shop_str)) {
                holder.shop_cv.setVisibility(View.VISIBLE);
                holder.shop_tv.setText(shoe.getShopName());
            } else {
                holder.shop_cv.setVisibility(View.GONE);
            }
        }
    }
}
