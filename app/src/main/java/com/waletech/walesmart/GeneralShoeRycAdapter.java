package com.waletech.walesmart;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicSet.ToastSet;
import com.waletech.walesmart.searchResult.ResultAction;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/6/28.
 */
public class GeneralShoeRycAdapter extends BaseShoeRycAdapter {

    public final static int TYPE_NULL = 0;
    public final static int TYPE_RESULT = 1;
    public final static int TYPE_USING = 2;

    private int type = 0;

    private LineToast toast;

    public GeneralShoeRycAdapter(ArrayList<ObjectShoe> shoeList, Context context) {
        super(shoeList, context);
    }

    public void setModuleType(int type) {
        this.type = type;
    }

    @Override
    public BaseShoeRycAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        toast = new LineToast(parent.getContext());
        isShowLoad = false;

        View view = View.inflate(parent.getContext(), R.layout.recycler_shoe_general_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BaseShoeRycAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ViewHolder child_holder = (ViewHolder) holder;

        switch (type) {
            case TYPE_NULL:
                toast.showToast(ToastSet.GENERAL_TYPE_IS_NULL);
                break;

            case TYPE_RESULT:
                onResultStatus(child_holder, position);
                break;

            case TYPE_USING:
                onUsingStatus(child_holder, position);
                break;

            default:
                break;
        }
    }

    protected class ViewHolder extends BaseShoeRycAdapter.ViewHolder {

        TextView status_up_tv;
        TextView status_down_tv;

        TextView bottom_bar_tv;

        LinearLayout load_ll;

        public ViewHolder(View itemView) {
            super(itemView);

            status_up_tv = (TextView) itemView.findViewById(R.id.rv_status_tv0);
            status_down_tv = (TextView) itemView.findViewById(R.id.rv_status_tv1);

            bottom_bar_tv = (TextView) itemView.findViewById(R.id.rv_bottom_tv);

            load_ll = (LinearLayout) itemView.findViewById(R.id.rv_load_ll);
        }
    }

    private void onResultStatus(ViewHolder holder, int position) {
        ObjectShoe shoe = shoeList.get(position);

        int count = Integer.parseInt(shoe.getCount());

        if (count > 0) {
            holder.status_up_tv.setVisibility(View.GONE);
            holder.status_down_tv.setVisibility(View.VISIBLE);

//            holder.status_up_tv.setBackgroundResource(R.drawable.shoe_status_multi_color_tv);
//            holder.status_up_tv.setText("多色");

            holder.status_down_tv.setBackgroundResource(R.drawable.shoe_status_multi_size_tv);
            holder.status_down_tv.setText(context.getString(R.string.shoe_in_stock_tv));
        } else {
            holder.status_up_tv.setVisibility(View.GONE);
            holder.status_down_tv.setVisibility(View.GONE);
        }

        String location_str = shoe.getProvince() + "-" + shoe.getCity() + "-" + shoe.getCounty() + "-" + shoe.getShopName()
                + shoe.getSmarkRow() + "行" + shoe.getSmarkColumn() + "列";
        holder.bottom_bar_tv.setText(location_str);

        onLoadMore(holder, position);
    }

    private void onUsingStatus(ViewHolder holder, int position) {
        ObjectShoe shoe = shoeList.get(position);

        // 这里的 getRemark() 是指的是当前正在试鞋的这只或者这双鞋还剩下多少可以体验的时间
        // 这个只有分和秒，没有小时，更没有年月日，到时间了就为 00：00
        String time_str = shoe.getRemark();
        holder.status_up_tv.setVisibility(View.VISIBLE);
        holder.status_up_tv.setText(time_str);

        String status_str = shoe.getStatus();
        holder.status_down_tv.setVisibility(View.VISIBLE);
        if (status_str.equals(context.getString(R.string.area_shoe_status_enjoying_tv))) {
            holder.status_down_tv.setBackgroundResource(R.drawable.shoe_status_ets_tv);
        } else {
            holder.status_down_tv.setBackgroundResource(R.drawable.shoe_status_prs_tv);
        }

        holder.status_down_tv.setText(status_str);

        String location_str = shoe.getProvince() + shoe.getCity() + shoe.getCounty() + shoe.getShopName()
                + shoe.getSmarkRow() + "行" + shoe.getSmarkColumn() + "列";
        holder.bottom_bar_tv.setText(location_str);

        onLoadMore(holder, position);
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
                            ((ResultAction) baseAction).resultLoad();
                        }
                        isShowLoad = false;
                    }
                }, 1000);
            }
        } else {
            holder.load_ll.setVisibility(View.GONE);
        }
    }


    //    // Desperate Methods

    /**
     * @param holder 目的是为了获取子单位 Item 的单位高度
     *               然后拿 item 的单位高度乘以总数量，判断总高度是否超出了整个屏幕
     */
//    private void onMeasureHeight(final ViewHolder holder) {
//        final Context context = holder.load_ll.getContext();
//
//        ScreenSize size = new ScreenSize(context);
//        int s_h = size.getHeight();
//
//        int item_whole_h = shoeList.size() * holder.item_ll.getLayoutParams().height;
//
//        isHigher = s_h < item_whole_h;
//    }

//    // Desperate Methods
//    private void setupLoadItem(final ViewHolder holder,final int position) {
//        final Context context = holder.load_ll.getContext();
//
//        if ((position == (shoeList.size() - 1)) && !isShowLoad) {
//            ScreenSize size = new ScreenSize(context);
//            int s_h = size.getHeight();
//
//            int item_whole_h = shoeList.size() * holder.item_ll.getLayoutParams().height;
//
//            if (s_h < item_whole_h) {
//                holder.load_ll.setVisibility(View.VISIBLE);
//                isShowLoad = true;
//
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        // 判断是否已经发出过 "加载更多" 的网络请求，如果发出过，就不再发送
//                        if (!isSendLoadRequest) {
//                            // 判断是否有网络
//                            if (!HttpAction.checkNet(context)) {
//                                holder.load_ll.setVisibility(View.GONE);
//                            } else {
//                                ((ResultAction) baseAction).resultLoad();
//                                isSendLoadRequest = true;
//                            }
//                        }
//                    }
//                }, 1500);
//            }
//        } else {
//            holder.load_ll.setVisibility(View.GONE);
//        }
//    }
}
