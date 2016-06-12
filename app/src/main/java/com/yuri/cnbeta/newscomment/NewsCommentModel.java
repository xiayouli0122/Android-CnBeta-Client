package com.yuri.cnbeta.newscomment;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.request.JsonRequest;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.cnbeta.http.response.Comment;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.xlog.Log;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Yuri on 2016/4/13.
 */
public class NewsCommentModel extends NewsCommentContract.Model {

    /**
     * 获取新闻评论
     *
     * @param page     页数
     * @param sid      新闻的sid
     * @param listener 结果回调
     */
    @Override
    void getNewsComment(int page, final String sid, final HttpListResultListener<CommentItem> listener) {
        final String commentUrl = HttpConfigure.newsComment(page + "", sid);
        Log.d("commentUrl:" + commentUrl);
        Type type = new TypeToken<ApiResponse<List<Comment>>>(){}.getType();
        Request<ApiResponse> jsonRequest = new JsonRequest(commentUrl, type);
        addRequest(commentUrl, jsonRequest);

        request(jsonRequest, new HttpListener<ApiResponse>() {
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
                    int index = commentList.indexOf(parent) + 1;
                    String parentComment = "";
                    while (parent != null) {
                        sb.append("//@");
                        if (TextUtils.isEmpty(parent.getUsername())) {
                            sb.append("匿名用户");
                        } else {
                            sb.append(parent.getUsername());
                        }
                        sb.append(": [");
                        sb.append("unknow");
                        sb.append("] " + index + "楼");
                        sb.append("\n");
                        sb.append(parent.getContent());

                        if (parentComment.equals("")) {
                            parentComment = sb.toString();
                            sb.delete(0, sb.length());
                        } else {
                            sb.append("\n");
                            parentComment = sb.toString() + parentComment;
                            sb.delete(0, sb.length());
                        }
                        parent = store.get(parent.getPid());
                        index = commentList.indexOf(parent) + 1;
                    }
                    commentItem.parentComment = parentComment;
                    commentItemList.add(commentItem);
                }
                Log.d("commentItemList.size=" + commentItemList.size());
                if (listener != null) {
                    listener.onSuccess(commentItemList);
                }
            }

            @Override
            public void onFailed(int what, String errorMsg) {
                if (listener != null) {
                    listener.onFail(errorMsg);
                }
            }
        });
    }
}
