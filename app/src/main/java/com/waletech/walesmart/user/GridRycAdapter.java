package com.waletech.walesmart.user;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.publicClass.ScreenSize;
import com.waletech.walesmart.sharedinfo.SharedAction;
import com.waletech.walesmart.user.authInfo.Authorize_Act;
import com.waletech.walesmart.user.shopInfo.favourite.Favourite_Act;
import com.waletech.walesmart.user.shopInfo.order.Order_Act;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by KeY on 2016/6/6.
 */
class GridRycAdapter extends RecyclerView.Adapter<GridRycAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<HashMap<String, Integer>> data_List = initDataSource();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_grid_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (data_List.get(position).get("itemImage") == 0) {
            holder.icon_img.setImageResource(0);
            holder.icon_tv.setText("");
        } else {
            holder.icon_img.setImageResource(data_List.get(position).get("itemImage"));
            holder.icon_tv.setText(data_List.get(position).get("itemText"));
        }

        holder.icon_ll.setTag(position);
        holder.icon_ll.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return data_List.size();
    }

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();

        if (!HttpAction.checkNet(context)) {
            return;
        }

        int position = (int) v.getTag();

        Intent click_int = new Intent();
        switch (position) {
            case 0:
                click_int.setClass(context, Order_Act.class);
                break;

            case 1:
                click_int.setClass(context, Favourite_Act.class);
                break;

//            case 2:
//                // History
//                break;
//
//            case 3:
//                // Wallet
//                break;
//
//            case 4:
//                // Advice
//                break;

            case 5:
                Intent auth_int = new Intent(context, Authorize_Act.class);
                context.startActivity(auth_int);
                return;

            case 6:
                // LogOut
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.user_grid_logout_dialog_title))
                        .setMessage(context.getString(R.string.user_grid_logout_dialog_msg))
                        .setPositiveButton(context.getString(R.string.base_dialog_btn_yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new SharedAction(context).clearLoginStatus();
                                ((AppCompatActivity) context).finish();
                            }
                        })
                        .setNegativeButton(context.getString(R.string.base_dialog_btn_no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
                return;

            default:
                return;
        }
        context.startActivity(click_int);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout icon_ll;

        ImageView icon_img;
        TextView icon_tv;

        public ViewHolder(View itemView) {
            super(itemView);

            icon_img = (ImageView) itemView.findViewById(R.id.rv_img);
            icon_tv = (TextView) itemView.findViewById(R.id.rv_tv);

            icon_ll = (LinearLayout) itemView.findViewById(R.id.rv_ll);
            setupIconLinear(itemView, icon_ll);
        }
    }

    private void setupIconLinear(View itemView, LinearLayout icon_ll) {
        ScreenSize size = new ScreenSize(itemView.getContext());
        int w = size.getWidth() / 4;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(w, w);
        icon_ll.setLayoutParams(params);
    }

    private ArrayList<HashMap<String, Integer>> initDataSource() {
        int[] image_res = {
                R.drawable.ic_user_grid_order, R.drawable.ic_user_grid_favorite,
                R.drawable.ic_user_grid_history, R.drawable.ic_user_grid_wallet,

                R.drawable.ic_user_grid_advice, R.drawable.ic_user_grid_authorize,
                R.drawable.ic_user_grid_logout, 0
        };

        int[] text_res = {
                R.string.user_grid_order, R.string.user_grid_favourite,
                R.string.user_grid_history, R.string.user_grid_wallet,

                R.string.user_grid_advice, R.string.user_grid_authorize,
                R.string.user_grid_logout, 0
        };

        ArrayList<HashMap<String, Integer>> data_List = new ArrayList<>();
        for (int i = 0; i < image_res.length; i++) {
            HashMap<String, Integer> map = new HashMap<>();
            map.put("itemImage", image_res[i]);
            map.put("itemText", text_res[i]);
            data_List.add(map);
        }

        return data_List;
    }

}
