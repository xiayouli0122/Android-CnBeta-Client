package com.yuri.cnbeta.http.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentResponse {
    public String state;
    public String message;
    public HttpComment result;
    public static class HttpComment {
        public List<HttpCommentItem> hotlist = new ArrayList<>();
        public List<HttpCommentItem> cmntlist = new ArrayList<>();
        public HashMap<String, HttpCommentItem> cmntstore = new HashMap<>();
        public int comment_num;
        public String token;
        public int view_num;
        public int page;
        public int open;
    }
}
