package com.yuri.cnbeta.http;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Yuri on 2016/4/8.
 */
public class HttpConfigure {

    public static final String BASE_URL = "http://www.cnbeta.com/";

    public static final String NEWS_LIST_URL = BASE_URL + "more";

    private static final String ENDPOINT = "http://api.cnbeta.com/capi";
    private static final String APP_KEY = "10000";
    private static final String FORMAT = "json";
    private static final String V = "1.0";

    private static final String EXTRA = "mpuffgvbvbttn3Rc";
    private static final String KEY_METHOD = "method";
    private static final String NEWS_LISTS = "Article.Lists";


    private static Map<String, String> initParamMap() {
        Map<String, String> map = new TreeMap<>();
        map.put("app_key", APP_KEY);
        map.put("format", FORMAT);
        map.put("v", V);
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return map;
    }

    public static String buildArtistUrl() {
        Map<String, String> paramMap = initParamMap();
        paramMap.put(KEY_METHOD, NEWS_LISTS);
        String urlString = buildUrl(paramMap);
        return urlString;
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
