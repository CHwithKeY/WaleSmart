package com.waletech.walesmart.shop.area;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Frag;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;
import com.waletech.walesmart.publicClass.ScreenSize;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.publicSet.BundleSet;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by KeY on 2016/7/8.
 */
public class Area_Frag extends Base_Frag {

    private ArrayList<ObjectShoe> shoeList;

    private AreaAction areaAction;

    //    private String shop_name = "";
    private String area_name = "";
    private String area_row = "";
    private String area_column = "";

    private boolean isFstInit;

    private LineToast toast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_area_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit();
    }

    @Override
    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);

//        shop_name = bundle.getString(BundleSet.KEY_SHOP_NAME);
        area_name = bundle.getString(BundleSet.KEY_AREA_NAME);
        area_row = bundle.getString(BundleSet.KEY_AREA_ROW);
        area_column = bundle.getString(BundleSet.KEY_AREA_COLUMN);
    }

    private void varInit() {
        shoeList = new ArrayList<>();

        isFstInit = true;

        if (!area_name.equals("")) {
            areaAction = new AreaAction(getContext(), this);
            areaAction.getAreaShoe(area_name);
        } else {
            setupRecyclerView(getView());
        }

        toast = new LineToast(getContext());
    }

    private void setupRecyclerView(View view) {
        ScreenSize size = new ScreenSize(getContext());

        final RecyclerView record_rv = (RecyclerView) view.findViewById(R.id.area_rv);

        int row = Integer.parseInt(area_row);
        int column = Integer.parseInt(area_column);

        AreaRycAdapter adapter = new AreaRycAdapter(shoeList, row, column);

        final LinearLayout item_ll = (LinearLayout) view.findViewById(R.id.area_standard_item_ll);
        int extra_column = column - 3;
        int extra_width = extra_column * item_ll.getLayoutParams().width;

        record_rv.setLayoutManager(new GridLayoutManager(getContext(), column));
        record_rv.setLayoutParams(new FrameLayout.LayoutParams(size.getWidth() + extra_width, ViewGroup.LayoutParams.MATCH_PARENT));

        if (column == 3 && row == 3) {
            final FrameLayout area_fl = (FrameLayout) view.findViewById(R.id.area_fl);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) area_fl.getLayoutParams();
            params.gravity = Gravity.CENTER;
        }

//        record_rv.setLayoutManager(new GridLayoutManager(getContext(), 3));
//        record_rv.setLayoutParams(new FrameLayout.LayoutParams(size.getWidth() - 20, ViewGroup.LayoutParams.MATCH_PARENT));

        record_rv.setClipToPadding(true);
        record_rv.setAdapter(adapter);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < record_rv.getAdapter().getItemCount(); i++) {
                    AreaRycAdapter.ViewHolder holder = (AreaRycAdapter.ViewHolder) record_rv.getChildViewHolder(record_rv.getChildAt(i));
                    if (Methods.isInteger(holder.smark_num_tv.getText().toString())) {
                        getActivity().finish();
                        toast.showToast(getString(R.string.base_toast_net_down));
//                ((AppCompatActivity) getContext()).finish();
                        return;
                    }
                }
            }
        });
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        Log.i("Result", "get Result");

        if (getView() != null) {
            if (isFstInit) {
                shoeList = areaAction.handleListResponse(result);
                isFstInit = false;
                setupRecyclerView(getView());
                return;
            }

            final RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.area_rv);
            AreaRycAdapter adapter = (AreaRycAdapter) recyclerView.getAdapter();

            shoeList = areaAction.handleListResponse(result);

            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNullResponse() throws JSONException {
        Log.i("Result", "onNullResponse");

        if (isFstInit) {
            shoeList = new ArrayList<>();
            isFstInit = false;
            setupRecyclerView(getView());

//            AppCompatActivity compatActivity = (AppCompatActivity) getContext();
//            compatActivity.finish();
        }
    }

    @Override
    public void setCustomTag(String tag) {

    }

    @Override
    public String getCustomTag() {
        return null;
    }

}
