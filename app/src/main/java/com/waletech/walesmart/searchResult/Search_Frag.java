package com.waletech.walesmart.searchResult;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.waletech.walesmart.R;
import com.waletech.walesmart.base.Base_Frag;
import com.waletech.walesmart.http.HttpAction;
import com.waletech.walesmart.publicClass.LineToast;
import com.waletech.walesmart.publicClass.Methods;

import org.json.JSONException;

/**
 * Created by KeY on 2016/7/19.
 */
public class Search_Frag extends Base_Frag implements SearchView.OnQueryTextListener, View.OnClickListener {

    private LineToast toast;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toast = new LineToast(getContext());

        setupSearchView(view);

        setupShadow(view);

        setupSearchBtn(view);

    }

    private void setupSearchView(View view) {
        final SearchView search_sv = (SearchView) view.findViewById(R.id.search_sv);

        // 实现进入搜索界面后，自动展开搜索栏，弹出软键盘
        // 如果没有以下代码，用户需要自行点击搜索控件才能输入内容，不友好
        search_sv.onActionViewExpanded();
        search_sv.setQueryHint(getString(R.string.search_sv_hint));
        search_sv.setOnQueryTextListener(this);

        Methods.expandIME(search_sv);
    }

    private void setupShadow(View view) {
        final TextView shadow_tv = (TextView) view.findViewById(R.id.search_shadow_tv);
        shadow_tv.setOnClickListener(this);
    }

    private void setupSearchBtn(View view) {
        final ImageButton search_imgbtn = (ImageButton) view.findViewById(R.id.search_imgbtn);

        search_imgbtn.setOnClickListener(this);
    }

    @Override
    public void onMultiHandleResponse(String tag, String result) throws JSONException {

    }

    @Override
    public void onNullResponse() throws JSONException {

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
        switch (v.getId()) {
            case R.id.search_imgbtn:
                if (!HttpAction.checkNet(getContext())) {
                    return;
                }

                View parent = (View) v.getParent();
                final SearchView search_sv = (SearchView) parent.findViewById(R.id.search_sv);
                String search_word = search_sv.getQuery().toString();

                if (search_word.equals("")) {
                    search_word = "All";
//                    toast.showToast("搜索内容不能为空");
//                    return;
                }

                ((Result_Act) getActivity()).setSearchBarText(search_word);
                ResultAction resultAction = ((Result_Act) getActivity()).getResultAction();
                resultAction.searchFilter(search_word, "", "", "", "", "");

            case R.id.search_shadow_tv:
                FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.remove(this);
                transaction.commit();

                Methods.collapseIME(getContext());
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
