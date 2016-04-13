package com.yuri.cnbeta.model.impl;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;
import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.request.JsonRequest;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.cnbeta.http.response.Comment;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.model.CommentItem;
import com.yuri.cnbeta.model.INewsComment;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.model.listener.HttpResultListener;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yuri on 2016/4/13.
 */
public class NewsCommentImpl extends BaseNetImpl implements INewsComment {

    @Override
    public void getNewsComment(Context context, int page, final String sid, final HttpListResultListener listener) {
        final String commentUrl = HttpConfigure.newsComment(page + "", sid);
        Log.d("commentUrl:" + commentUrl);
        Type type = new TypeToken<ApiResponse<List<Comment>>>(){}.getType();
        Request<ApiResponse> jsonRequest = new JsonRequest(commentUrl, type);
        CallServer.getInstance().add(context, 0,
                jsonRequest, new HttpListener<ApiResponse>() {
                    @Override
                    public void onSuccess(int what, Response<ApiResponse> response) {
                        ApiResponse<List<Comment>> apiResponse = response.get();
                        List<Comment> commentList = apiResponse.result;
                        List<CommentItem> commentItemList = new ArrayList<>();

                        if (commentList.size() == 0) {
                            if (listener != null) {
                                listener.onSuccess(commentItemList);
                            }
                            return;
                        }

                        HashMap<String, Comment> store = new HashMap<>();
                        for (Comment comment : commentList) {
                            store.put(comment.getTid(), comment);
                        }

                        CommentItem commentItem;
                        for (Comment comment : commentList) {
//                            Log.d("tid:" + comment.getTid() + ",pid:" + comment.getPid()
//                                    + "," + comment.getContent());
                            StringBuilder sb = new StringBuilder();

                            commentItem = new CommentItem();
                            commentItem.copy(comment);
                            commentItem.sid = sid;
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
                        if (listener != null) {
                            listener.onSuccess(commentItemList);
                        }
                    }

                    @Override
                    public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMills) {
                        if (listener != null) {
                            listener.onFail(exception.getMessage());
                        }
                    }
                }, true);
    }
}