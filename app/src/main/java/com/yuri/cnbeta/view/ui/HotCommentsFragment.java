package com.yuri.cnbeta.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuri.cnbeta.R;
import com.yuri.cnbeta.contract.HotCommentContract;
import com.yuri.cnbeta.http.response.HotComment;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.presenter.HotCommentPresenter;
import com.yuri.cnbeta.view.adapter.BaseViewHolder;
import com.yuri.cnbeta.view.ui.core.BaseListFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yuri on 2016/4/14.
 */
public class HotCommentsFragment extends BaseListFragment<HotComment> implements HotCommentContract.View {

    private HotCommentPresenter mPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //启动自动刷新
        mRecycler.setRefreshing();

        mPresenter = new HotCommentPresenter(getActivity(), this);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_hot_comment_item, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onRefresh(int action) {
        mPresenter.getHotComment();
    }

    @Override
    public void showData(List<HotComment> newsItemList) {
        if (mDataList != null) {
            mDataList.clear();
        }
        mDataList = newsItemList;
        mAdapter.notifyDataSetChanged();
        mRecycler.onRefreshCompleted();
    }

    @Override
    public void showError(String message) {

    }

    class SampleViewHolder extends BaseViewHolder {

        @Bind(R.id.tv_hot_comment_content)
        TextView mCommentView;
        @Bind(R.id.tv_hot_comment_username)
        TextView mUserNameView;
        @Bind(R.id.tv_hot_comment_subject)
        TextView mSubjectView;

        public SampleViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            HotComment hotComment = mDataList.get(position);
            mCommentView.setText(hotComment.comment);
            String username = hotComment.username.equals("") ? "匿名用户" : hotComment.username;
            String string = "来自" + username + "的评论";
            mUserNameView.setText(string);
            mSubjectView.setText(hotComment.subject);
        }

        @Override
        public void onItemClick(View view, int position) {
            HotComment hotComment = mDataList.get(position);
            Log.d("" + hotComment.sid);
            Intent intent = NewsDetailActivity.getIntent(getActivity(), hotComment.sid, "");
            startActivity(intent);
        }

    }
}
