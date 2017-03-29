package com.waletech.walesmart.publicAdapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.waletech.walesmart.R;

/**
 * Created by KeY on 2016/7/15.
 */
public class TextRycAdapter extends RecyclerView.Adapter<TextRycAdapter.ViewHolder> {

    private String[] textArray;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recycler_text_rectangle_item, null);
        return new ViewHolder(view);
    }

    public void setTextArray(String[] textArray) {
        this.textArray = textArray;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.item_tv.setText(textArray[position]);
    }

    @Override
    public int getItemCount() {
        return textArray == null ? 0 : textArray.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView item_tv;

        public ViewHolder(View itemView) {
            super(itemView);

            item_tv = (TextView) itemView.findViewById(R.id.rv_tv);
        }
    }
}
