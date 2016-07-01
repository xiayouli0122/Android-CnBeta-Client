package com.yuri.cnbeta.newscomment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.linroid.filtermenu.library.FilterMenu;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.request.JsonRequest;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.cnbeta.http.response.CommentResponse;
import com.yuri.cnbeta.http.response.HttpCommentItem;
import com.yuri.cnbeta.model.listener.HttpListResultListener;
import com.yuri.cnbeta.model.listener.HttpResultListener;
import com.yuri.cnbeta.model.listener.HttpSimpleResultListener;
import com.yuri.xlog.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
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
    void getNewsComment(int page, final String sid, String sn, final HttpListResultListener<HttpCommentItem> listener) {

        //use new way
        String commentUrl = HttpConfigure.COMMENT_URL;
        Request<String> request = NoHttp.createStringRequest(commentUrl);
        request.addHeader("Referer", "http://www.cnbeta.com/");
        request.addHeader("Origin", "http://www.cnbeta.com");
        request.addHeader("X-Requested-With", "XMLHttpRequest");
        request.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
        Log.d("sid:%s,sn:%s", sid, sn);
        request.add("op", ("1," + sid + "," + sn));

        request(request, new HttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                String json = response.get();
                Type type = new TypeToken<CommentResponse>() {}.getType();
                GsonBuilder gsonBuilder = new GsonBuilder();
                Gson gson = gsonBuilder.create();
                CommentResponse result = gson.fromJson(json, type);

//                File file = new File("/sdcard/test.txt");
//                try {
//                    file.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    FileWriter writer = new FileWriter(file);
//                    writer.write(response.get());
//                    writer.flush();
//                    writer.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

                Log.d("result:" + result.state);
                if (result.state.equals("success")) {
//                    Log.object(result.result);

                    if (result.result.open == 0) {
                        //评论已关闭
                        listener.onFail("评论已关闭");
                        return;
                    }

                    //所有评论列表
                    List<CommentResponse.CommentSimpleItem> cmntlist = result.result.cmntlist;
                    HashMap<String, CommentResponse.CommentDetailItem> cmntstore = result.result.cmntstore;

                    List<HttpCommentItem> commentItemList = new ArrayList<>();

                    HttpCommentItem commentItem;

                    CommentResponse.CommentSimpleItem currentSimpleItem;
                    CommentResponse.CommentDetailItem currentDetailItem;

                    for (int i = 0; i < cmntlist.size(); i++) {
                        //首先拿到第一个简约列表
                        currentSimpleItem = cmntlist.get(i);
                        //再拿到他的详细列表
                        currentDetailItem = cmntstore.get(currentSimpleItem.tid);
                        commentItem = new HttpCommentItem();
                        commentItem.copy(currentDetailItem);

                        //确认他的引用回复
                        //根据parent判断
                        String parentId = currentSimpleItem.parent;
                        int index;
                        String refContent = "";
                        while (!TextUtils.isEmpty(parentId)) {
                            //得到parent的simpleitem
                            currentSimpleItem = getParent(parentId, cmntlist);
                            currentDetailItem = cmntstore.get(currentSimpleItem.tid);
                            index = getIndex(currentSimpleItem.tid, cmntlist);
                            StringBuilder parentSb = new StringBuilder();
                            parentSb.append("//@");
                            parentSb.append(currentDetailItem.name);
                            parentSb.append(": [");
                            parentSb.append(currentDetailItem.host_name);
                            parentSb.append("]" + index + "楼" + "\n");
                            parentSb.append(currentDetailItem.comment);
                            parentId = currentSimpleItem.parent;
                            if (!refContent.equals("")) {
                                parentSb.append("\n");
                            }
                            refContent = parentSb + refContent;
                        }
                        commentItem.refContent = refContent;
                        commentItemList.add(commentItem);
                    }

                    //热门评论
//                    List<HttpCommentItem> hotcmntlist = result.result.hotlist;
//                    for (HttpCommentItem item : hotcmntlist) {
//                        StringBuilder sb = new StringBuilder();
//                        item.copy(cmntstore.get(item.tid));
//                        HttpCommentItem parent = cmntstore.get(item.pid);
//                        int index = cmntlist.indexOf(parent) + 1;
//                        while (parent != null) {
//                            sb.append("//@");
//                            sb.append(parent.name);
//                            sb.append(": [");
//                            sb.append(parent.host_name);
//                            sb.append("] " + index + "楼" +
//                                    "\n");
//                            sb.append(parent.comment);
//                            parent = cmntstore.get(parent.pid);
//                            if (parent != null) {
//                                sb.append("\n");
//                            }
//                        }
//                        item.refContent = sb.toString();
//                    }

                    listener.onSuccess(commentItemList);
                } else {
                    listener.onFail("");
                }
            }

            @Override
            public void onFailed(int what, String errorMsg) {
                listener.onFail(errorMsg);
            }
        });

//        final String commentUrl = HttpConfigure.newsComment(page + "", sid);
//        Log.d("commentUrl:" + commentUrl);
//        Type type = new TypeToken<ApiResponse<List<Comment>>>(){}.getType();
//        Request<ApiResponse> jsonRequest = new JsonRequest(commentUrl, type);
//        addRequest(commentUrl, jsonRequest);
//
//        request(jsonRequest, new HttpListener<ApiResponse>() {
//            @Override
//            public void onSuccess(int what, Response<ApiResponse> response) {
//                ApiResponse<List<Comment>> apiResponse = response.get();
//                List<Comment> commentList = apiResponse.result;
//                List<CommentItem> commentItemList = new ArrayList<>();
//
//                if (commentList.size() == 0) {
//                    if (listener != null) {
//                        listener.onSuccess(commentItemList);
//                    }
//                    return;
//                }
//
//                HashMap<String, Comment> store = new HashMap<>();
//                for (Comment comment : commentList) {
//                    store.put(comment.getTid(), comment);
//                }
//                CommentItem commentItem;
//                for (Comment comment : commentList) {
////                            Log.d("tid:" + comment.getTid() + ",pid:" + comment.getPid()
////                                    + "," + comment.getContent());
//                    StringBuilder sb = new StringBuilder();
//                    commentItem = new CommentItem();
//                    commentItem.copy(comment);
//                    commentItem.sid = sid;
//                    Comment parent = store.get(comment.getPid());
//                    int index = commentList.indexOf(parent) + 1;
//                    String parentComment = "";
//                    while (parent != null) {
//                        sb.append("//@");
//                        if (TextUtils.isEmpty(parent.getUsername())) {
//                            sb.append("匿名用户");
//                        } else {
//                            sb.append(parent.getUsername());
//                        }
//                        sb.append(": [");
//                        sb.append("unknow");
//                        sb.append("] " + index + "楼");
//                        sb.append("\n");
//                        sb.append(parent.getContent());
//
//                        if (parentComment.equals("")) {
//                            parentComment = sb.toString();
//                            sb.delete(0, sb.length());
//                        } else {
//                            sb.append("\n");
//                            parentComment = sb.toString() + parentComment;
//                            sb.delete(0, sb.length());
//                        }
//                        parent = store.get(parent.getPid());
//                        index = commentList.indexOf(parent) + 1;
//                    }
//                    commentItem.parentComment = parentComment;
//                    commentItemList.add(commentItem);
//                }
//                Log.d("commentItemList.size=" + commentItemList.size());
//                if (listener != null) {
//                    listener.onSuccess(commentItemList);
//                }
//            }
//
//            @Override
//            public void onFailed(int what, String errorMsg) {
//                if (listener != null) {
//                    listener.onFail(errorMsg);
//                }
//            }
//        });
    }

    public int getIndex(String tid, List<CommentResponse.CommentSimpleItem> cmnList) {
        for (int i = 0; i < cmnList.size(); i++) {
            if (cmnList.get(i).tid.equals(tid)) {
                return i;
            }
        }
        return 0;
    }

    public CommentResponse.CommentSimpleItem getParent(String pid, List<CommentResponse.CommentSimpleItem> cmnList) {
        CommentResponse.CommentSimpleItem commentItem;
        for (int i = 0; i < cmnList.size(); i++) {
            commentItem = cmnList.get(i);
            if (commentItem.tid.equals(pid)) {
                return commentItem;
            }
        }
        return null;
    }

    @Override
    void getCodeImage(final HttpResultListener<Bitmap> listener) {
        final String url = HttpConfigure.SECOND_VIEW;
        Request<String> request = NoHttp.createStringRequest(url);
        request.setContentType("text/html");
        request.addHeader("Referer", "http://www.cnbeta.com/");
        request.addHeader("Origin", "http://www.cnbeta.com");
        request.addHeader("X-Requested-With", "XMLHttpRequest");
        request.add("refresh", 1);
        request.add("_", System.currentTimeMillis());

        request(request, new HttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                String result = response.get();
                Log.d("result:" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String url1 = jsonObject.getString("url");
                    String codeUrl = HttpConfigure.BASE_URL + url1;

                    Request<String> request = NoHttp.createStringRequest(codeUrl);
                    request.setContentType("text/html");
                    request.addHeader("Referer", "http://www.cnbeta.com/");
                    request.addHeader("Origin", "http://www.cnbeta.com");
                    request.addHeader("X-Requested-With", "XMLHttpRequest");

                    request(request, new HttpListener() {
                        @Override
                        public void onSuccess(int what, Response response) {
                            byte[] byteArray = response.getByteArray();
                            //工厂对象的decodeByteArray把字节转换成Bitmap对象
                            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
                            //设置图片
                            listener.onSuccess(bitmap);
                        }

                        @Override
                        public void onFailed(int what, String errorMsg) {

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailed(int what, String errorMsg) {

            }
        });
    }

    @Override
    void writeComment(String sid, String pid, String seccode, String content, final HttpSimpleResultListener listener) {


        final String url = HttpConfigure.writeComment(sid, pid, seccode, content);
        Type type = new TypeToken<ApiResponse>() {
        }.getType();
        Request<ApiResponse> jsonRequest = new JsonRequest(url, type);
        addRequest(url, jsonRequest);

        request(jsonRequest, new HttpListener<ApiResponse>() {
            @Override
            public void onSuccess(int what, Response<ApiResponse> response) {
                ApiResponse apiResponse = response.get();
                if (apiResponse.status.equals("error")) {
                    ApiResponse.ErrorResult result = (ApiResponse.ErrorResult) apiResponse.result;
                    listener.onFail(result.error_msg);
                }
            }

            @Override
            public void onFailed(int what, String errorMsg) {

            }
        });
    }
}
