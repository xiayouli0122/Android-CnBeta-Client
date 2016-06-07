package com.yuri.cnbeta.view.widgets;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;

import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yuri.cnbeta.R;
import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.request.JsonRequest;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.cnbeta.model.bean.CommentItem;
import com.yuri.cnbeta.view.adapter.BaseListAdapter;
import com.yuri.xlog.Log;

import java.lang.reflect.Type;

public class ExtendPopMenu extends PopupMenu {
    public int SUPPORT = 1;
    public int AGAINST = 2;
    public int REPORT = 3;
    private int action;
    private CommentItem commentItem;
    private Context mContext;
    private BaseListAdapter adapter;
    private String token;

    public ExtendPopMenu(Context context, View anchor) {
        super(context, anchor);
        this.mContext = context;
        inflate(R.menu.menu_comment);
        setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.comment_support:
                        Log.d("support");
                        action = SUPPORT;
                        Type type = new TypeToken<ApiResponse<String>>() {}.getType();
                        String supportUrl = HttpConfigure.voteComment("support", commentItem.sid, commentItem.tid);
                        //against
                        final Request<ApiResponse> request = new JsonRequest(supportUrl, type);
                        CallServer.getInstance().add(mContext, 0, request, new HttpListener<ApiResponse>() {
                            @Override
                            public void onSuccess(int what, Response<ApiResponse> response) {
                                ApiResponse<String> apiResponse = response.get();
                                Log.d(apiResponse.result);
                            }

                            @Override
                            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMills) {

                            }
                        }, true);
//                        NetKit.getInstance().setCommentAction("support", commentItem.getSid()+"", commentItem.getTid(), token, chandler);
                        break;
                    case R.id.comment_against:
                        Log.d("against");
                        action = AGAINST;
                        Type type1 = new TypeToken<ApiResponse<String>>() {}.getType();
                        String againstUrl = HttpConfigure.voteComment("against", commentItem.sid, commentItem.tid);
                        //against
                        final Request<ApiResponse> request1 = new JsonRequest(againstUrl, type1);
                        CallServer.getInstance().add(mContext, 0, request1, new HttpListener<ApiResponse>() {
                            @Override
                            public void onSuccess(int what, Response<ApiResponse> response) {
                                ApiResponse<String> apiResponse = response.get();
                                Log.d(apiResponse.result);
                            }

                            @Override
                            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMills) {

                            }
                        }, true);
                        break;
                    case R.id.comment_report:
                        action = REPORT;
                        Log.d("report");
                        //举报
                        Type type2 = new TypeToken<ApiResponse<String>>() {}.getType();
                        String reporttUrl = HttpConfigure.voteComment("report", commentItem.sid, commentItem.tid);
                        //against
                        final Request<ApiResponse> request2 = new JsonRequest(reporttUrl, type2);
                        CallServer.getInstance().add(mContext, 0, request2, new HttpListener<ApiResponse>() {
                            @Override
                            public void onSuccess(int what, Response<ApiResponse> response) {
                                ApiResponse<String> apiResponse = response.get();
                                Log.d(apiResponse.result);
                            }

                            @Override
                            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMills) {

                            }
                        }, true);
                        break;
                    case R.id.comment_replay:
                        Log.d("reply");
                        //回复还没实现
//                        Type type3 = new TypeToken<ApiResponse<String>>() {}.getType();
//                        String writeCommentUrl = HttpConfigure.writeComment(commentItem.sid, commentItem.pid);
//                        final Request<ApiResponse> request3 = new JsonRequest(writeCommentUrl, type3);
//                        CallServer.getInstance().add(mContext, 0, request3, new HttpListener<ApiResponse>() {
//                            @Override
//                            public void onSuccess(int what, Response<ApiResponse> response) {
//                                ApiResponse<String> apiResponse = response.get();
//                                Log.d(apiResponse.result);
//                            }
//
//                            @Override
//                            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMills) {
//
//                            }
//                        }, true);
                        break;
                }
                return true;
            }
        });

    }

    public void setCommentItem(CommentItem commentItem) {
        this.commentItem = commentItem;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setAdapter(BaseListAdapter adapter) {
        this.adapter = adapter;
    }
}