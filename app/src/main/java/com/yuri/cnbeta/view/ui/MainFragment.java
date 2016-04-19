package com.yuri.cnbeta.view.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yuri.cnbeta.R;
import com.yuri.cnbeta.contract.MainFragmentContract;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.response.Article;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.model.bean.NewsType;
import com.yuri.cnbeta.presenter.MainFragmentPresenter;
import com.yuri.cnbeta.view.adapter.BaseViewHolder;
import com.yuri.cnbeta.view.ui.core.BaseListFragment;
import com.yuri.cnbeta.view.widgets.PullRecycler;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yuri on 2016/4/7.
 */
public class MainFragment extends BaseListFragment<Article> implements MainFragmentContract.View {

    public static final String NEWS_TYPE = "mainfragment.news_type";
    public static final String NEWS_PARAM = "mainfragment.news_param";
    private MainFragmentPresenter mPresenter;
    private String mLastSid;

    private NewsType mNewsType;

    public static MainFragment getInstance(NewsType newsType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(NEWS_TYPE, newsType);
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    public static MainFragment getInstance(NewsType newsType, String param) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(NEWS_TYPE, newsType);
        bundle.putString(NEWS_PARAM, param);
        MainFragment mainFragment = new MainFragment();
        mainFragment.setArguments(bundle);
        return mainFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNewsType = (NewsType) getArguments().getSerializable(NEWS_TYPE);
        String param = getArguments().getString(NEWS_PARAM);
        Log.d("newstype:" + mNewsType.getValue() + ",param:" + param);

        mPresenter = new MainFragmentPresenter(getActivity(), getArguments(), this);

//        String dialyRank = HttpConfigure.getDialyRank("comments");
//        Log.d(dialyRank);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mDataList != null) {
            mDataList.clear();
        }
        //启动自动刷新
        mRecycler.setRefreshing();
        if (mNewsType == NewsType.MONTHLY || mNewsType == NewsType.DAILY) {
            mRecycler.enableLoadMore(false);
        } else {
            mRecycler.enableLoadMore(true);
        }
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_main_list_item, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onRefresh(int action) {
        Log.d("action:" + action);
        //交给Presenter去实现数据获取操作
        switch (action) {
            case PullRecycler.ACTION_PULL_TO_REFRESH:
            case PullRecycler.ACTION_IDLE:
                mPresenter.getData();
                break;
            case PullRecycler.ACTION_LOAD_MORE_REFRESH:
                mPresenter.getMoreData(mLastSid);
                break;
        }
    }

    @Override
    public void showData(boolean isMore, List<Article> articleList) {
        Log.d("" + articleList.size());
        if (!isMore) {
            //自动加载、下拉刷新
            if (mDataList != null && mDataList.size() > 0) {
                mDataList.clear();
            }
            mDataList = articleList;
        } else {
            mDataList.addAll(articleList);
        }
        Article lastArticle = mDataList.get(mDataList.size() - 1);
//        Log.d("lastArticle.title:" + lastArticle.getTitle());
        mLastSid = lastArticle.getSid();

        mAdapter.notifyDataSetChanged();
        mRecycler.onRefreshCompleted();
    }

    @Override
    public void showError(String message) {
        mRecycler.onRefreshCompleted();
        Log.d(message);
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
            Article article = mDataList.get(position);
            mTitleView.setText(article.getTitle());
            mSummaryView.setText(article.getSummary());
            mDateView.setText(article.getPubtime());
            mCommentView.setText(article.getComments());
            mCounterView.setText(article.getCounter());
            Glide.with(getActivity())
                    .load(article.getTopicLogo())
                    .centerCrop()
                    .placeholder(R.mipmap.ic_launcher)
                    .crossFade()
                    .into(mNewsIconView);
        }

        @Override
        public void onItemClick(View view, int position) {
            Article article = mDataList.get(position);
            Log.d("" + article.getSid());

            Intent intent = NewsDetailActivity.getIntent(getActivity(), article.getSid(), article.getTopicLogo());
            startActivity(intent);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.cancelRequestBySign(MainFragment.class);
    }
}
