package com.waletech.walesmart.netDown;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Frag;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.main.experience.Experience_Frag;
import com.waletech.walesmart.publicSet.BundleSet;

import org.json.JSONException;

/**
 * Created by KeY on 2016/7/14.
 */
public class NetDown_Frag extends Base_Frag implements View.OnClickListener {
    private ImageButton net_down_imgbtn;
    private TextView net_down_tv;
    private ContentLoadingProgressBar net_down_pb;

    private NetDownAction netDownAction;

    private int contentLayoutResId;
    private Base_Frag fragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_public_net_down_layout, container, false);
    }

    @Override
    public void setArguments(Bundle bundle) {
        super.setArguments(bundle);

        contentLayoutResId = bundle.getInt(BundleSet.KEY_CONTENT_LAYOUT_RES_ID);
        // fragment = (Base_Frag) bundle.getSerializable(BundleSet.KEY_BASE_FRAGMENT);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit(view);

        setupNetDownImgbtn(view);

    }

    private void varInit(View view) {
        netDownAction = new NetDownAction(getContext(), this);

        net_down_tv = (TextView) view.findViewById(R.id.net_down_tv);
        net_down_pb = (ContentLoadingProgressBar) view.findViewById(R.id.net_down_pb);
    }

    private void setupNetDownImgbtn(View view) {
        net_down_imgbtn = (ImageButton) view.findViewById(R.id.net_down_imgbtn);
        net_down_imgbtn.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        boolean isPing = netDownAction.handleResponse(result);

        if (isPing) {
            FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.remove(NetDown_Frag.this);

            // transaction.add(R.id.main_vp, fragment);
            transaction.add(contentLayoutResId, new Experience_Frag());

            transaction.commit();
        } else {
            backToInit();
        }
    }

    @Override
    public void onNullResponse() throws JSONException {
        backToInit();
    }

    @Override
    public void setCustomTag(String tag) {

    }

    @Override
    public String getCustomTag() {
        return null;
    }

    @Override
    public void onClick(View v) {
        final Context context = v.getContext();

        v.setVisibility(View.INVISIBLE);
        net_down_tv.setVisibility(View.INVISIBLE);
        net_down_pb.setVisibility(View.VISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (HttpAction.checkNet(context)) {
                    netDownAction.ping();
                } else {
                    backToInit();
                }
            }
        }, 2 * 1000);
    }

    private void backToInit() {
        net_down_imgbtn.setVisibility(View.VISIBLE);
        net_down_tv.setVisibility(View.VISIBLE);
        net_down_pb.setVisibility(View.GONE);
    }
}
