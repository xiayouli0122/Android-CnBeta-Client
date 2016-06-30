package com.yuri.cnbeta.newscomment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuri.cnbeta.R;
import com.yuri.cnbeta.http.response.HttpCommentItem;
import com.yuri.cnbeta.utils.SpannableStringUtils;
import com.yuri.cnbeta.base.adapter.BaseViewHolder;
import com.yuri.cnbeta.base.BaseListActivity;
import com.yuri.cnbeta.view.widgets.ExtendPopMenu;
import com.yuri.cnbeta.view.widgets.PullRecycler;
import com.yuri.cnbeta.view.widgets.textdrawable.TextDrawable;
import com.yuri.cnbeta.view.widgets.textdrawable.util.ColorGenerator;
import com.yuri.xlog.Log;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 评论界面
 * Created by Yuri on 2016/4/11.
 */
public class NewsCommentActivity extends BaseListActivity<HttpCommentItem> implements NewsCommentContract.View {

    public static final String EXTRA_SID = "extra_sid";
    public static final String EXTRA_SN = "extra_sn";

    private String mSID;
    private int mPage = 1;
    private int mCounters = 0;
    private String mSN;

    private NewsCommentPresenter mPresenter;

    public static Intent getIntent(Context context, String sid, String sn) {
        Intent intent = new Intent();
        intent.setClass(context, NewsCommentActivity.class);
        intent.putExtra(EXTRA_SID, sid);
        intent.putExtra(EXTRA_SN, sn);
        return intent;
    }

    @Override
    protected void setUpData() {
        super.setUpData();

        mPresenter = new NewsCommentPresenter(this);

        mSID = getIntent().getStringExtra(EXTRA_SID);
        mSN = getIntent().getStringExtra(EXTRA_SN);
        Log.d("sID：" + mSID);

        setUpTitle("评论");
        setUpMenu(R.menu.menu_comment);
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
        mPresenter.getNewsComment(mPage, mSID, mSN);
    }

    @Override
    public void showData(List<HttpCommentItem> commentItemList) {
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
    }

    @Override
    public void onGetCodeImage(Bitmap bitmap) {

    }

    @Override
    public void showError(String message) {
        Log.d(message);
        recycler.onRefreshCompleted();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_comment:
                Intent intent = new Intent();
                intent.setClass(this, AddCommentActivity.class);
                startActivity(intent);
                break;
        }
        return super.onMenuItemClick(item);
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

        SampleViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            mTextBuilder = TextDrawable.builder().round();
            mColorGenerator = ColorGenerator.MATERIAL;
            mPopMenu = new ExtendPopMenu(getApplicationContext(), mMoreView);
        }



        @Override
        public void onBindViewHolder(int position) {
            HttpCommentItem commentItem = mDataList.get(position);

            String userName;
            if (TextUtils.isEmpty(commentItem.name)) {
                userName = "匿名用户";
            } else {
                userName = commentItem.name;
            }

            userName = userName + " (" + (position + 1) + "楼)";
            mNameView.setText(userName);

            mImageView.setImageDrawable(mTextBuilder.build(String.valueOf(userName.charAt(0)),
                    mColorGenerator.getColor(commentItem.tid)));
            String address;
            if (TextUtils.isEmpty(commentItem.host_name)) {
                address = "火星网友";
            } else {
                address = commentItem.host_name;
            }
            mAddressView.setText(address);
            if (TextUtils.isEmpty(commentItem.refContent)) {
                mParentContent.setVisibility(View.GONE);
            } else {
                mParentContent.setVisibility(View.VISIBLE);
                mParentContent.setText(commentItem.refContent);
            }

            boolean showEmoji = true;
            if (showEmoji) {
                mContentView.setText(SpannableStringUtils.span(getApplicationContext(),
                        Html.fromHtml(commentItem.comment).toString()));
            } else {
                mContentView.setText(Html.fromHtml(commentItem.comment).toString());
            }
            mAgainstView.setText(commentItem.reason + "");
            mSupportView.setText(commentItem.score + "");
            mDateView.setText(commentItem.date);

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
