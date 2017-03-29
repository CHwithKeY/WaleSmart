package com.waletech.walesmart.shop.area;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.http.HttpSet;
import com.waletech.walesmart.product.Product_Act;
import com.waletech.walesmart.publicClass.BitmapCache;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicSet.IntentSet;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/7/8.
 */
public class AreaRycAdapter extends RecyclerView.Adapter<AreaRycAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private ArrayList<ObjectShoe> shoeList;

//    private String[] native_number = {
//            "11", "12", "13",
//            "21", "22", "23",
//            "31", "32", "33"};

    private String[] native_number;

    private BitmapCache cache;

    public AreaRycAdapter(ArrayList<ObjectShoe> shoeList, int row, int column) {
        this.shoeList = shoeList;

        cache = new BitmapCache();

        native_number = onCreateNativeMatrix(row, column);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_shoe_area_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        boolean isCheck = false;

        for (int i = 0; i < shoeList.size(); i++) {
            Log.i("Result", "smark is : " + shoeList.get(i).getSmarkNum());
        }

        Context context = holder.item_ll.getContext();

        for (int i = 0; i < shoeList.size(); i++) {
            ObjectShoe shoe = shoeList.get(i);
            String smark_num_str = shoe.getSmarkNum();

            String remote_number = smark_num_str.substring(smark_num_str.length() - 2, smark_num_str.length());

            if (native_number[position].equals(remote_number)) {

                Log.i("Result", "pos is : " + position);
                Log.i("Result", "remote is : " + remote_number);

                isCheck = true;

                holder.smark_num_tv.setText(shoe.getSmarkNum());
                holder.status_tv.setText(shoe.getStatus());

                switch (shoe.getStatus()) {
                    case "暂无鞋":
                    case "No shoes":
                        holder.status_tv.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
                        holder.shoe_img.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
                        break;

                    case "在柜":
                    case "In cabinet":
                        holder.status_tv.setTextColor(context.getResources().getColor(R.color.colorSpecial));
                        break;

                    case "试鞋中":
                    case "Enjoying":
                        holder.status_tv.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
                        break;

                    default:
                        break;
                }

                if (shoe.getEpcCode() == null) {
                    break;
                }

                holder.item_ll.setTag(i);
                holder.item_ll.setOnClickListener(this);
                holder.item_ll.setOnLongClickListener(this);

                String params_str = shoe.getBrand() + "\t" + shoe.getSize();
                holder.params_tv.setText(params_str);

                if (shoe.getImagePath() != null && (!shoe.getImagePath().equals(""))) {
                    holder.shoe_img.setTag(HttpSet.BASE_URL + shoe.getImagePath());
                    loadImage(holder, i);
                }

                break;
            }
        }

        if (isCheck) {
            return;
        }

        String smark_num_str = native_number[position];
        holder.smark_num_tv.setText(smark_num_str);

        String status_str = context.getString(R.string.area_shoe_status_no_shoes_tv);
        holder.status_tv.setText(status_str);
    }

    @Override
    public int getItemCount() {
        return native_number.length;
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout item_ll;

        TextView smark_num_tv;
        TextView status_tv;

        ImageView shoe_img;

        TextView params_tv;


        public ViewHolder(View itemView) {
            super(itemView);

            item_ll = (LinearLayout) itemView.findViewById(R.id.rv_ll);

            smark_num_tv = (TextView) itemView.findViewById(R.id.rv_smark_num_tv);
            status_tv = (TextView) itemView.findViewById(R.id.rv_status_tv);

            shoe_img = (ImageView) itemView.findViewById(R.id.rv_img);

            params_tv = (TextView) itemView.findViewById(R.id.rv_params_tv);
        }
    }

    private void loadImage(ViewHolder holder, int real_position) {

        // 先判断缓存中是否缓存了这个item的imageview所在的url的图片
        String img_url = holder.shoe_img.getTag().toString();

        // 如果没有
        if (cache.getBitmap(img_url) == null) {
            // 请求下载图片
            Context context = holder.shoe_img.getContext();

            holder.shoe_img.setBackgroundColor(context.getResources().getColor(android.R.color.white));
            Methods.downloadImage(holder.shoe_img, HttpSet.BASE_URL + shoeList.get(real_position).getImagePath(), cache);
        } else {
            holder.shoe_img.setImageBitmap(cache.getBitmap(img_url));
        }

    }

    private String[] onCreateNativeMatrix(int row, int column) {
        ArrayList<String> nativeList = new ArrayList<>();

        for (int i = 1; i <= row; i++) {
            for (int k = 1; k <= column; k++) {
                String native_number = i + "" + k;
                nativeList.add(native_number);
            }
        }

        String[] native_number = nativeList.toArray(new String[nativeList.size()]);
        return native_number;
    }

    @Override
    public void onClick(View v) {
        Context context = v.getContext();

        if (!HttpAction.checkNet(context)) {
            return;
        }

        int position = Methods.cast(v.getTag());

        ObjectShoe shoe = shoeList.get(position);
        String epc_code_str = shoe.getEpcCode();

        Log.i("Result", "epc is : " + epc_code_str);

        Intent product_int = new Intent(context, Product_Act.class);
        product_int.putExtra(IntentSet.KEY_EPC_CODE, epc_code_str);
        product_int.putExtra(IntentSet.KEY_IS_FROM_SHOP, true);
        context.startActivity(product_int);
    }
}
