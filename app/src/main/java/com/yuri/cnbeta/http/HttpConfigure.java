package com.yuri.cnbeta.http;

import com.google.gson.reflect.TypeToken;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Created by Yuri on 2016/4/8.
 */
public class HttpConfigure {

    public static final String BASE_URL = "http://www.cnbeta.com";

    public static final String NEWS_LIST_URL = BASE_URL + "more";

    private static final String ENDPOINT = "http://api.cnbeta.com/capi";
    private static final String APP_KEY = "10000";
    private static final String FORMAT = "json";
    private static final String V = "1.0";

    private static final String EXTRA = "mpuffgvbvbttn3Rc";
    private static final String NEWS_LISTS = "Article.Lists";
    private static final String NEWS_CONTENT = "Article.NewsContent";
    private static final String NEWS_COMMENT = "Article.Comment";
    private static final String DO_COMMENT = "Article.DoCmt";
    private static final String RECOMMEND_COMMENT = "Article.RecommendComment";
    private static final String TOP_TEN = "Article.Top10";
    private static final String DAY_RANK = "Article.TodayRank";

    private static final String KEY_METHOD = "method";
    private static final String KEY_END_SID = "end_sid";
    private static final String KEY_TOPICID = "topicid";
    private static final String KEY_RANK_TYPE = "type";
    private static final String KEY_SID = "sid";
    private static final String KEY_PAGE = "page";
    private static final String KEY_PAGE_SIZE = "pageSize";
    private static final String KEY_OP = "op";
    private static final String KEY_TID = "tid";
    private static final String KEY_PID = "pid";

    private static final String ARTICLE_URL = BASE_URL + "/articles/%s.htm";

    public static final Pattern ARTICLE_PATTERN = Pattern.compile("http://www\\.cnbeta\\.com/articles/(\\d+)\\.htm");

    private static final String VALUE_PAGE_SIZE = "20";

    private static Map<String, String> initParamMap() {
        Map<String, String> map = new TreeMap<>();
        map.put("app_key", APP_KEY);
        map.put("format", FORMAT);
        map.put("v", V);
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return map;
    }

    /**
     * 获取当月top 10新闻列表
     * @return
     */
    public static String getMonthlyTopTen() {
        Map<String, String> paramMap = initParamMap();
        paramMap.put(KEY_METHOD, TOP_TEN);
        return buildUrl(paramMap);
    }

    /**
     * 每日新闻
     * @param rankType 排序方式
     * @return
     */
    public static String getDialyRank(String rankType) {
        Map<String, String> paramMap = initParamMap();
        paramMap.put(KEY_METHOD, DAY_RANK);
        paramMap.put(KEY_RANK_TYPE, rankType);
        return buildUrl(paramMap);
    }

    /**
     * 获取主题资讯数据
     * @param topicid 主题id，详见assets/news_topics.txt
     * @return
     */
    public static String getTopicNews(String topicid) {
        Map<String, String> paramMap = initParamMap();
        paramMap.put(KEY_METHOD, NEWS_LISTS);
        paramMap.put(KEY_TOPICID, topicid);
        return buildUrl(paramMap);
    }

    /**
     * 获取更多主题资讯数据
     * @param endSid 当前列表最后一条数据的sid
     * @param topicid 主题id
     * @return
     */
    public static String getMoreTpicNews(String endSid, String topicid) {
        Map<String, String> paramMap = initParamMap();
        paramMap.put(KEY_METHOD, NEWS_LISTS);
        paramMap.put(KEY_END_SID, endSid);
        paramMap.put(KEY_TOPICID, topicid);
        return buildUrl(paramMap);
    }

    /**
     * 获取热门评论url
     * @return url
     */
    public static String hotComments() {
        Map<String, String> paramMap = initParamMap();
        paramMap.put(KEY_METHOD, RECOMMEND_COMMENT);
        return buildUrl(paramMap);
    }

    public static String voteComment(String op, String sid, String tid) {
        Map<String, String> paramMap = initParamMap();
        paramMap.put(KEY_METHOD, DO_COMMENT);
        paramMap.put(KEY_OP, op);
        paramMap.put(KEY_SID, sid);
        paramMap.put(KEY_TID, tid);
        return buildUrl(paramMap);
    }

    public static String writeComment(String sid, String pid) {
        Map<String, String> paramMap = initParamMap();
        paramMap.put(KEY_METHOD, DO_COMMENT);
        paramMap.put(KEY_SID, sid);
        paramMap.put("content", "oooooooo");
        /**
         * null pid means publish a comment,NOT null means reply a comment
         */
        if (pid != null) {
            paramMap.put(KEY_PID, pid);
        }
        return buildUrl(paramMap);
    }

    public static String newsComment(String page, String sid) {
        Map<String, String> paramMap = initParamMap();
        paramMap.put(KEY_METHOD, NEWS_COMMENT);
        paramMap.put(KEY_PAGE, page);
        paramMap.put(KEY_PAGE_SIZE, VALUE_PAGE_SIZE);
        paramMap.put(KEY_SID, sid);
        return buildUrl(paramMap);
    }

    public static String newsContent(String sid) {
        Map<String, String> paramMap = initParamMap();
        paramMap.put(KEY_METHOD, NEWS_CONTENT);
        paramMap.put(KEY_SID, sid);
        String url = buildUrl(paramMap);
        return url;
    }

    public static String buildArtileUrl(String sid) {
        return String.format(Locale.CHINA, ARTICLE_URL, sid);
    }

    /**
     * 获取最新资讯列表
     * @return
     */
    public static String getLatestNews() {
        Map<String, String> paramMap = initParamMap();
        paramMap.put(KEY_METHOD, NEWS_LISTS);
        return buildUrl(paramMap);
    }

    /**
     * 获取更多最新资讯列表
     * @param lastSid 上一次列表最后一条新闻的sid
     * @return
     */
    public static String getMoreLatestNews(String lastSid) {
        Map<String, String> paramMap = initParamMap();
        paramMap.put(KEY_METHOD, NEWS_LISTS);
        paramMap.put(KEY_END_SID, lastSid);
        return buildUrl(paramMap);
    }

    private static String buildUrl(Map<String, String> map) {
        String paramString = assembleParam(map);
        String sign = generateMd5(paramString);
        return ENDPOINT + "?" + paramString + "&sign=" + sign;
    }

    private static String assembleParam(Map<String, String> map) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append("&");
        }
        builder.append(EXTRA);
        return builder.toString();
    }

    public static String generateMd5(String input) {
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance("MD5").digest(input.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            int i = (b & 0xFF);
            if (i < 0x10) hex.append('0');
            hex.append(Integer.toHexString(i));
        }
        return hex.toString();
    }
}
