package com.waletech.walesmart;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.waletech.walesmart.base.BaseAction;

/**
 * Created by KeY on 2016/7/10.
 */
public class LoadScrollListener extends RecyclerView.OnScrollListener {

    private BaseShoeRycAdapter adapter;

    public LoadScrollListener(BaseShoeRycAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

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
