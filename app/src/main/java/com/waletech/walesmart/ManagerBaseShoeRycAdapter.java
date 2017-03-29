package com.waletech.walesmart;

import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/11/29.
 */

public abstract class ManagerBaseShoeRycAdapter extends BaseRycAdapter {

    public ManagerBaseShoeRycAdapter(ArrayList dataList) {
        super(dataList);
    }

    @Override
    public DataViewHolder onCreateDataViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindDataViewHolder(DataViewHolder parent, int position) {

    }
}
