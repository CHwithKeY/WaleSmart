package com.waletech.walesmart.user.shopInfo.favourite;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waletech.walesmart.BaseShoeRycAdapter;
import com.waletech.walesmart.R;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicSet.MapSet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by KeY on 2016/7/3.
 */
public class FavRycAdapter extends BaseShoeRycAdapter implements CompoundButton.OnCheckedChangeListener {

    private ArrayList<String> epcList;
    private ArrayList<String> checkList;

    public FavRycAdapter(ArrayList<ObjectShoe> shoeList, Context context) {
        super(shoeList, context);

        epcList = new ArrayList<>();
        checkList = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_shoe_favourite_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseShoeRycAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ObjectShoe shoe = shoeList.get(position);
        ViewHolder child_holder = (ViewHolder) holder;

        child_holder.item_cb.setChecked(false);

        HashMap<String, String> map = new HashMap<>();
        map.put(MapSet.KEY_EPC_CODE, shoe.getEpcCode());
        map.put(MapSet.KEY_ITEM_POSITION, "" + position);

        child_holder.item_cb.setTag(map);
        child_holder.item_cb.setOnCheckedChangeListener(this);

        for (int i = 0; i < checkList.size(); i++) {
            String position_str = "" + position;
            if (position_str.equals(checkList.get(i))) {
                child_holder.item_cb.setChecked(true);
            } else {
                child_holder.item_cb.setChecked(false);
            }
        }

        // onShopBar(child_holder, position);

        String location_str = shoe.getProvince() + "-" + shoe.getCity() + "-" + shoe.getCounty() + "-" + shoe.getShopName()
                + shoe.getSmarkRow() + "行" + shoe.getSmarkColumn() + "列";
        child_holder.bottom_bar_tv.setText(location_str);

        onLoadMore(child_holder, position);
    }

    class ViewHolder extends BaseShoeRycAdapter.ViewHolder {

        CheckBox item_cb;

//        CardView shop_cv;
//        TextView shop_tv;

        TextView bottom_bar_tv;

        LinearLayout load_ll;

        public ViewHolder(View itemView) {
            super(itemView);

            item_cb = (CheckBox) itemView.findViewById(R.id.rv_cb);
//            shop_cv = (CardView) itemView.findViewById(R.id.rv_shop_cv);
//            shop_tv = (TextView) itemView.findViewById(R.id.rv_shop_tv);
            bottom_bar_tv = (TextView) itemView.findViewById(R.id.rv_bottom_tv);

            load_ll = (LinearLayout) itemView.findViewById(R.id.rv_load_ll);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        HashMap<String, String> map = Methods.cast(buttonView.getTag());

        String epc_code_str = map.get(MapSet.KEY_EPC_CODE);
        String position_str = map.get(MapSet.KEY_ITEM_POSITION);

        if (isChecked) {
            epcList.add(epc_code_str);
            checkList.add(position_str);
        } else {
            epcList.remove(epc_code_str);
            checkList.add(position_str);
        }
    }

    public void initInnerList() {
        epcList = new ArrayList<>();
        checkList = new ArrayList<>();
    }

    public ArrayList<String> getEpcList() {
        return epcList;
    }

//    private void onShopBar(ViewHolder holder, int position) {
//        ObjectShoe shoe = shoeList.get(position);
//
//        if (position == 0) {
//            holder.shop_cv.setVisibility(View.VISIBLE);
//            holder.shop_tv.setText(shoe.getShopName());
//        } else {
//            ObjectShoe before_shoe = shoeList.get(position - 1);
//            if (!(before_shoe.getShopName().equals(shoe.getShopName()))) {
//                holder.shop_cv.setVisibility(View.VISIBLE);
//                holder.shop_tv.setText(shoe.getShopName());
//            } else {
//                holder.shop_cv.setVisibility(View.GONE);
//            }
//        }
//    }

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
                            ((FavAction) baseAction).getLoadList();
                        }
                        isShowLoad = false;
                    }
                }, 1000);
            }
        } else {
            holder.load_ll.setVisibility(View.GONE);
        }
    }
}
