package com.yuri.cnbeta.view.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;
import com.yuri.cnbeta.R;
import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.request.JsonRequest;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.cnbeta.http.response.Article;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.http.response.Comment;
import com.yuri.cnbeta.model.CommentItem;
import com.yuri.cnbeta.view.adapter.BaseViewHolder;
import com.yuri.cnbeta.view.ui.core.BaseListActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Yuri on 2016/4/11.
 */
public class NewsCommentActivity extends BaseListActivity<CommentItem> {

    public static final String EXTRA_SID = "extra_sid";

    private String mSID;
    private int mPage = 0;

    public static Intent getIntent(Context context, String sid) {
        Intent intent = new Intent();
        intent.setClass(context, NewsCommentActivity.class);
        intent.putExtra(EXTRA_SID, sid);
        return intent;
    }

    @Override
    protected void setUpData() {
        super.setUpData();
        mSID = getIntent().getStringExtra(EXTRA_SID);
        Log.d("sID：" + mSID);

        setUpTitle("评论");
        recycler.setRefreshing();

    }

    @Override
    protected BaseViewHolder getViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_comment_item, parent, false);
        return new SampleViewHolder(view);
    }

    @Override
    public void onRefresh(int action) {
        getData();
    }

    private void getData() {
        final String commentUrl = HttpConfigure.newsComment(mPage + "", mSID);
        Log.d("commentUrl:" + commentUrl);
        Type type = new TypeToken<ApiResponse<List<Comment>>>(){}.getType();
        Request<ApiResponse> jsonRequest = new JsonRequest(commentUrl, type);
        CallServer.getInstance().add(getApplicationContext(), 0,
                jsonRequest, new HttpListener<ApiResponse>() {
                    @Override
                    public void onSuccess(int what, Response<ApiResponse> response) {
                        ApiResponse<List<Comment>> apiResponse = response.get();
                        List<Comment> commentList = apiResponse.result;

                        ArrayList<CommentItem> commentItemList = new ArrayList<>();

                        HashMap<String, Comment> store = new HashMap<>();
                        for (Comment comment : commentList) {
                            store.put(comment.getTid(), comment);
                        }

                        CommentItem commentItem;
                        for (Comment comment : commentList) {
                            Log.d("tid:" + comment.getTid() + ",pid:" + comment.getPid()
                                    + "," + comment.getContent());
                            StringBuilder sb = new StringBuilder();

                            commentItem = new CommentItem();
                            commentItem.copy(comment);
                            Comment parent = store.get(comment.getPid());
                            while (parent != null) {
                                sb.append("//@");
                                sb.append(parent.getUsername());
                                sb.append(": [");
                                sb.append("CHINA");
                                sb.append("] <br/> ");
                                sb.append(parent.getContent());
                                parent = store.get(parent.getPid());
                                if (parent != null) {
                                    sb.append("<br/>");
                                }
                            }
                            commentItem.parentComment = sb.toString();
                            commentItemList.add(commentItem);
                        }
                        Log.d("commentItemList.size=" + commentItemList.size());
                        mDataList = commentItemList;
                        adapter.notifyDataSetChanged();
                        recycler.onRefreshCompleted();
                    }

                    @Override
                    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMills) {

                    }
                }, true);
    }

    class SampleViewHolder extends BaseViewHolder {

        @Bind(R.id.comment_name)
        TextView mNameView;
        @Bind(R.id.comment_ref)
        TextView mParentContent;
        @Bind(R.id.comment_more)
        ImageView mMoreView;
        @Bind(R.id.comment_content)
        TextView mContentView;
        @Bind(R.id.comment_time)
        TextView mDateView;
        @Bind(R.id.comment_reason)
        TextView mReasonView;//评论数
        @Bind(R.id.comment_score)
        TextView mScoreView;

        public SampleViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onBindViewHolder(int position) {
            CommentItem commentItem = mDataList.get(position);
            if (TextUtils.isEmpty(commentItem.username)) {
                mNameView.setText("匿名用户");
            } else {
                mNameView.setText(commentItem.username);
            }
            if (TextUtils.isEmpty(commentItem.parentComment)) {
                mParentContent.setVisibility(View.GONE);
            } else {
                mParentContent.setText(commentItem.parentComment);
            }
            mContentView.setText(commentItem.content);
            mReasonView.setText(commentItem.support);
            mScoreView.setText(commentItem.against);
            mDateView.setText(commentItem.createdTime);
        }

        @Override
        public void onItemClick(View view, int position) {
        }

    }
}
