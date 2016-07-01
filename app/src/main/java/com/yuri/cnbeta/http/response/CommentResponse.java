package com.yuri.cnbeta.http.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 详细见
 */
public class CommentResponse {
    public String state;
    public String message;
    public HttpComment result;
    public static class HttpComment {
        public List<CommentSimpleItem> hotlist = new ArrayList<>();
        public List<CommentSimpleItem> cmntlist = new ArrayList<>();
        //pid,
        public HashMap<String, CommentDetailItem> cmntstore = new HashMap<>();
        public String comment_num;
        public String join_num;
        public String view_num;
        public String token;
        public String page;
        public String sid;
        public int open;
        public String dig_num;
        public String fav_num;
    }

    public static class CommentSimpleItem {
        public String tid;
        public String pid;
        public String sid;
        public String parent;//parent id
        public String thread;
    }

    public static class CommentDetailItem {
        public String tid;
        public String pid;
        public String sid;
        public String date;
        public String name;
        public String host_name;
        public String comment;
        public String score;
        public String reason;
        public String userid;
        public String icon;


    }
}
