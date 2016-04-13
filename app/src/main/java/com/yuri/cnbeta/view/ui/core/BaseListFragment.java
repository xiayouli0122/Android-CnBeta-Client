package com.yuri.cnbeta.view.ui.core;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuri.cnbeta.R;
import com.yuri.cnbeta.view.adapter.BaseListAdapter;
import com.yuri.cnbeta.view.adapter.BaseViewHolder;
import com.yuri.cnbeta.view.widgets.DividerItemDecoration;
import com.yuri.cnbeta.view.widgets.PullRecycler;
import com.yuri.cnbeta.view.widgets.layoutmanager.ILayoutManager;
import com.yuri.cnbeta.view.widgets.layoutmanager.MyLinearLayoutManager;

import java.util.List;

/**
 * Created by Stay on 8/3/16.
 * Powered by www.stay4it.com
 */
public abstract class BaseListFragment<T> extends BaseFragment implements PullRecycler.OnRecyclerRefreshListener {
    protected BaseListAdapter mAdapter;
    protected List<T> mDataList;
    protected PullRecycler mRecycler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycler = (PullRecycler) view.findViewById(R.id.pullRecycler);
        setUpAdapter();
        mRecycler.setOnRefreshListener(this);
        mRecycler.setLayoutManager(getLayoutManager());
//        mRecycler.addItemDecoration(getItemDecoration());
        mRecycler.setAdapter(mAdapter);
    }

    protected void setUpAdapter() {
        mAdapter = new ListAdapter();
    }

    protected ILayoutManager getLayoutManager() {
        return new MyLinearLayoutManager(getActivity().getApplicationContext());
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getActivity().getApplicationContext(), R.drawable.list_divider);
    }

    public class ListAdapter extends BaseListAdapter {

        @Override
        protected BaseViewHolder onCreateNormalViewHolder(ViewGroup parent, int viewType) {
            return getViewHolder(parent, viewType);
        }

        @Override
        protected int getDataCount() {
            return mDataList != null ? mDataList.size() : 0;
        }

        @Override
        protected int getDataViewType(int position) {
            return getItemType(position);
        }

        @Override
        public boolean isSectionHeader(int position) {
            return BaseListFragment.this.isSectionHeader(position);
        }
    }

    protected boolean isSectionHeader(int position) {
        return false;
    }

    protected int getItemType(int position) {
        return 0;
    }

    protected abstract BaseViewHolder getViewHolder(ViewGroup parent, int viewType);

}
