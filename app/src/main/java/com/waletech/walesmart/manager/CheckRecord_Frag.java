package com.waletech.walesmart.manager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.waletech.walesmart.user.managerInfo.ManagerRecordShoeRycAdapter;
import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Frag;
import com.waletech.walesmart.http.HttpTag;
import com.waletech.walesmart.publicObject.ObjectShoe;
import com.waletech.walesmart.user.lockInfo.record.RecordAction;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by lenovo on 2016/11/9.
 */

public class CheckRecord_Frag extends Base_Frag {

    private RecordAction recordAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manager_check_record_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        varInit();

    }

    private void varInit() {
        recordAction = new RecordAction(getContext(), this);
        recordAction.getManagerRecord();
    }

    private void setupRecyclerView(ArrayList<ObjectShoe> shoeList) {
        if (getView() != null) {
            View view = getView();

            ManagerRecordShoeRycAdapter adapter = new ManagerRecordShoeRycAdapter(shoeList, getContext());
            adapter.setBaseAction(recordAction);

            final RecyclerView record_rv = (RecyclerView) view.findViewById(R.id.check_record_rv);
            record_rv.setLayoutManager(new LinearLayoutManager(getContext()));
            record_rv.setAdapter(adapter);
            record_rv.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {
        switch (tag) {
            case HttpTag.MANAGER_GET_RECORD:
                ArrayList<ObjectShoe> shoeList = recordAction.handleResponse(result);

                setupRecyclerView(shoeList);
                break;

            default:
                break;
        }
    }

    @Override
    public void onNullResponse() throws JSONException {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.remove(this);
        transaction.commit();

        LinearLayout manager_ll = (LinearLayout) getActivity().findViewById(R.id.manager_ll);
        LinearLayout manager_item_ll = (LinearLayout) getActivity().findViewById(R.id.manager_item_ll);
        manager_ll.addView(manager_item_ll);
    }

    @Override
    public void setCustomTag(String tag) {

    }

    @Override
    public String getCustomTag() {
        return null;
    }
}
