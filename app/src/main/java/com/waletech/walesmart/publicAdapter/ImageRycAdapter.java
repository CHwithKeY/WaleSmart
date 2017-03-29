package com.waletech.walesmart.publicAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.waletech.walesmart.R;
import com.waletech.walesmart.publicClass.ScreenSize;

/**
 * Created by KeY on 2016/7/15.
 */
public class ImageRycAdapter extends RecyclerView.Adapter<ImageRycAdapter.ViewHolder> {

    private int[] imageResIdArray;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_imgbtn_square_item, null);
        return new ViewHolder(view);
    }

    public void setImageResIdArray(int[] imageResIdArray) {
        this.imageResIdArray = imageResIdArray;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item_imgbtn.setImageResource(imageResIdArray[position]);

        // Context context = holder.item_imgbtn.getContext();
        // holder.item_imgbtn.setImageBitmap(decodeSampledBitmapFromResource(context.getResources(), imageResIdArray[position], View.MeasureSpec.EXACTLY, View.MeasureSpec.EXACTLY));
    }

    @Override
    public int getItemCount() {
        return imageResIdArray == null ? 0 : imageResIdArray.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_ll;
        ImageButton item_imgbtn;

        public ViewHolder(View itemView) {
            super(itemView);

            item_ll = (LinearLayout) itemView.findViewById(R.id.rv_ll);
            item_imgbtn = (ImageButton) itemView.findViewById(R.id.rv_imgbtn);

            ScreenSize size = new ScreenSize(itemView.getContext());
            int s_h = size.getHeight();
            int l_h = s_h / 10;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(l_h, l_h);
            item_ll.setLayoutParams(params);
            item_ll.setGravity(Gravity.CENTER);
        }
    }

}
