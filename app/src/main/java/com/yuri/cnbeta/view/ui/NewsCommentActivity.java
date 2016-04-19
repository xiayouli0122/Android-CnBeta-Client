package com.yuri.cnbeta.view.ui;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuri.cnbeta.R;
import com.yuri.cnbeta.contract.NewsCommentContract;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.model.bean.CommentItem;
import com.yuri.cnbeta.presenter.NewsCommentPresenter;
import com.yuri.cnbeta.utils.SpannableStringUtils;
import com.yuri.cnbeta.view.adapter.BaseViewHolder;
import com.yuri.cnbeta.view.ui.core.BaseListActivity;
import com.yuri.cnbeta.view.widgets.ExtendPopMenu;
import com.yuri.cnbeta.view.widgets.PullRecycler;
import com.yuri.cnbeta.view.widgets.textdrawable.TextDrawable;
import com.yuri.cnbeta.view.widgets.textdrawable.util.ColorGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Yuri on 2016/4/11.
 */
public class NewsCommentActivity extends BaseListActivity<CommentItem> implements NewsCommentContract.View {

    public static final String EXTRA_SID = "extra_sid";
    public static final String EXTRA_COUNTERS = "extra_counters";

    private String mSID;
    private int mPage = 1;
    private int mCounters = 0;

    private NewsCommentPresenter mPresenter;

    public static Intent getIntent(Context context, String sid, int counters) {
        Intent intent = new Intent();
        intent.setClass(context, NewsCommentActivity.class);
        intent.putExtra(EXTRA_SID, sid);
        intent.putExtra(EXTRA_COUNTERS, counters);
        return intent;
    }

    @Override
    protected void setUpData() {
        super.setUpData();

        mPresenter = new NewsCommentPresenter(getApplicationContext(), this);

        mSID = getIntent().getStringExtra(EXTRA_SID);
        mCounters = getIntent().getIntExtra(EXTRA_COUNTERS, 0);
        Log.d("sID：" + mSID);

        setUpTitle("评论");
        recycler.setRefreshing();
        recycler.enableLoadMore(true);
    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_comment_item, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onRefresh(int action) {
        Log.d("action:" + action);
        switch (action) {
            case PullRecycler.ACTION_IDLE:
            case PullRecycler.ACTION_PULL_TO_REFRESH:
                mPage = 1;//下拉刷新从第一页开始加载
                if (mDataList != null) {
                    mDataList.clear();
                }
                break;
            case PullRecycler.ACTION_LOAD_MORE_REFRESH:
                mPage ++;
                break;
        }
        mPresenter.getNewsComment(mPage, mSID);
    }

    @Override
    public void showData(List<CommentItem> commentItemList) {
        if ((mDataList == null) && commentItemList.size() == 0) {
            mEmptyViewTV.setText("暂无评论");
            mEmptyViewTV.setVisibility(View.VISIBLE);
            recycler.onRefreshCompleted();
            return;
        } else if (mDataList != null && mDataList.size() > 0 && commentItemList.size() == 0) {
            Log.d("no more comments");
            recycler.enableLoadMore(false);
            recycler.onRefreshCompleted();
            return;
        } else {
            mEmptyViewTV.setVisibility(View.GONE);
            if (mDataList == null) {
                mDataList = commentItemList;
            } else {
                mDataList.addAll(commentItemList);
            }
            adapter.notifyDataSetChanged();
        }
        recycler.onRefreshCompleted();

        //不需要判断当前评论数量是否比总数小，只需要判断下一次服务器传过来的额数据是否空数据就行
//        if (mDataList.size() < mCounters) {
//            recycler.enableLoadMore(true);
//        } else {
//            recycler.enableLoadMore(false);
//        }
    }

    @Override
    public void showError(String message) {
        Log.d(message);
        recycler.onRefreshCompleted();
    }

    class SampleViewHolder extends BaseViewHolder {

        @Bind(R.id.comment_image)
        ImageView mImageView;
        @Bind(R.id.comment_name)
        TextView mNameView;
        @Bind(R.id.comment_from)
        TextView mAddressView;
        @Bind(R.id.comment_ref)
        TextView mParentContent;
        @Bind(R.id.comment_more)
        ImageView mMoreView;
        @Bind(R.id.comment_content)
        TextView mContentView;
        @Bind(R.id.comment_time)
        TextView mDateView;
        @Bind(R.id.comment_against)
        TextView mAgainstView;//评论数
        @Bind(R.id.comment_support)
        TextView mSupportView;

        private TextDrawable.IBuilder mTextBuilder;
        private ColorGenerator mColorGenerator;

        private ExtendPopMenu mPopMenu;

        public SampleViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mTextBuilder = TextDrawable.builder().round();
            mColorGenerator = ColorGenerator.MATERIAL;

//            Log.d("m:" + mMoreView);
            mPopMenu = new ExtendPopMenu(getApplicationContext(), mMoreView);
        }



        @Override
        public void onBindViewHolder(int position) {
            CommentItem commentItem = mDataList.get(position);

            String userName;
            if (TextUtils.isEmpty(commentItem.username)) {
                userName = "匿名用户";
            } else {
                userName = commentItem.username;
            }

            userName = userName + " (" + (position + 1) + "楼)";
            mNameView.setText(userName);

            mImageView.setImageDrawable(mTextBuilder.build(String.valueOf(userName.charAt(0)),
                    mColorGenerator.getColor(commentItem.tid)));
            mAddressView.setText("火星网友");
            if (TextUtils.isEmpty(commentItem.parentComment)) {
                mParentContent.setVisibility(View.GONE);
            } else {
                mParentContent.setVisibility(View.VISIBLE);
                mParentContent.setText(commentItem.parentComment);
            }

            boolean showEmoji = true;
            if (showEmoji) {
                mContentView.setText(SpannableStringUtils.span(getApplicationContext(), Html.fromHtml(commentItem.content).toString()));
            } else {
                mContentView.setText(Html.fromHtml(commentItem.content).toString());
            }
            mAgainstView.setText(commentItem.against);
            mSupportView.setText(commentItem.support);
            mDateView.setText(commentItem.createdTime);

            mPopMenu.setCommentItem(commentItem);
            mPopMenu.setAdapter(adapter);
        }

        @Override
        public void onItemClick(View view, int position) {
        }

        @OnClick(R.id.comment_more)
        public void onMoreClick() {
            Log.d("more");
            mPopMenu.show();
        }

    }
}
