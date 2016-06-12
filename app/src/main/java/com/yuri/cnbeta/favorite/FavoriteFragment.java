package com.yuri.cnbeta.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuri.cnbeta.R;
import com.yuri.cnbeta.db.model.NewsItem;
import com.yuri.cnbeta.newsdetial.NewsDetailActivity;
import com.yuri.cnbeta.base.adapter.BaseViewHolder;
import com.yuri.cnbeta.base.BaseListFragment;
import com.yuri.xlog.Log;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 收藏界面
 * Created by Yuri on 2016/4/14.
 */
public class FavoriteFragment extends BaseListFragment<NewsItem> implements FavoriteContract.View {

    private FavoritePresenter mPresenter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //启动自动刷新
        mRecycler.setRefreshing();

        mPresenter = new FavoritePresenter(this);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main_list_item, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onRefresh(int action) {
        mPresenter.getFavoriteData();
    }

    @Override
    public void showData(List<NewsItem> newsItemList) {
        if (newsItemList == null) {
            Log.d("empty data");
        } else {
            if (mDataList != null) {
                mDataList.clear();
            }
            mDataList = newsItemList;
            mAdapter.notifyDataSetChanged();
            mRecycler.onRefreshCompleted();
        }
    }

    @Override
    public void showError(String message) {

    }

    class SampleViewHolder extends BaseViewHolder {

        @Bind(R.id.tv_news_title)
        TextView mTitleView;
        @Bind(R.id.tv_news_date)
        TextView mDateView;
        @Bind(R.id.iv_news_icon)
        ImageView mNewsIconView;
        @Bind(R.id.tv_news_summary)
        TextView mSummaryView;
        @Bind(R.id.tv_news_read_count)
        TextView mCounterView;//总共阅读量
        @Bind(R.id.tv_news_comment_count)
        TextView mCommentView;//评论数

        public SampleViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            NewsItem newsItem = mDataList.get(position);
            mTitleView.setText(newsItem.title);
            Log.d("homeText:" + newsItem.hometext);
            String summary = newsItem.hometext.replace("<p>", "");
            summary = summary.replace("</p>", "");
            summary = summary.replace("<strong>", "");
            summary = summary.replace("</strong>", "");
            Log.d("summary：" + summary);
            mSummaryView.setText(summary);
            mDateView.setText(newsItem.time);
            mCommentView.setText(newsItem.comments);
            mCounterView.setText(newsItem.counter);
            Glide.with(getActivity())
                    .load(newsItem.topicLogo)
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(mNewsIconView);
        }

        @Override
        public void onItemClick(View view, int position) {
            NewsItem newsItem = mDataList.get(position);
            Log.d("" + newsItem.sid);
            Intent intent = NewsDetailActivity.getIntent(getActivity(), newsItem.sid, newsItem.topicLogo);
            startActivity(intent);
        }

    }
}
