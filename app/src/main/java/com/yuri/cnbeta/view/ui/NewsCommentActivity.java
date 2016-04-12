package com.yuri.cnbeta.view.ui;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;
import com.yuri.cnbeta.R;
import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.request.JsonRequest;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.http.response.Comment;
import com.yuri.cnbeta.model.CommentItem;
import com.yuri.cnbeta.view.adapter.BaseViewHolder;
import com.yuri.cnbeta.view.ui.core.BaseListActivity;
import com.yuri.cnbeta.view.widgets.ExtendPopMenu;
import com.yuri.cnbeta.view.widgets.textdrawable.TextDrawable;
import com.yuri.cnbeta.view.widgets.textdrawable.util.ColorGenerator;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

                        if (commentList.size() == 0) {
                            mEmptyViewTV.setText("暂无评论");
                            mEmptyViewTV.setVisibility(View.VISIBLE);
                            return;
                        } else {
                            mEmptyViewTV.setVisibility(View.GONE);
                        }

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
                                if (TextUtils.isEmpty(parent.getUsername())) {
                                    sb.append("匿名用户");
                                } else {
                                    sb.append(parent.getUsername());
                                }
                                sb.append(": [");
                                sb.append("unknow");
                                sb.append("]");
                                sb.append("\n");
                                sb.append(parent.getContent());
                                parent = store.get(parent.getPid());
                                if (parent != null) {
                                    sb.append("\n");
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

            Log.d("m:" + mMoreView);
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
            mNameView.setText(userName);

            mImageView.setImageDrawable(mTextBuilder.build(String.valueOf(userName.charAt(0)), mColorGenerator.getRandomColor()));
            mAddressView.setText("火星网友");
            if (TextUtils.isEmpty(commentItem.parentComment)) {
                mParentContent.setVisibility(View.GONE);
            } else {
                mParentContent.setText(commentItem.parentComment);
            }
            mContentView.setText(commentItem.content);
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
