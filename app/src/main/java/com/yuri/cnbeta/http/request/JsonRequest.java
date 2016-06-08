package com.yuri.cnbeta.http.request;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yolanda.nohttp.Headers;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.rest.JsonObjectRequest;
import com.yolanda.nohttp.rest.RestRequest;
import com.yolanda.nohttp.tools.HeaderParser;
import com.yuri.cnbeta.http.response.ApiResponse;
import com.yuri.xlog.Log;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

/**
 * Created by Yuri on 2016/4/8.
 */
public class JsonRequest extends RestRequest<ApiResponse> {

    private Type mType;

    public JsonRequest(String url, Type type) {
        super(url);
        mType = type;
        Log.net("url:%s", url);
    }

    @Override
    public ApiResponse parseResponse(String url, Headers responseHeaders, byte[] responseBody) {
        String jsonString = parseResponseString(url, responseHeaders, responseBody);
        Log.json(jsonString);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();
        return gson.fromJson(jsonString, mType);
    }

    public String parseResponseString(String url, Headers responseHeaders, byte[] responseBody) {
        String result = null;
        if (responseBody != null && responseBody.length > 0) {
            try {
                String charset = HeaderParser.parseHeadValue(responseHeaders.getContentType(), "charset", "");
                result = new String(responseBody, charset);
            } catch (UnsupportedEncodingException e) {
                Logger.w("Charset error in ContentType returned by the server：" + responseHeaders.getValue(Headers.HEAD_KEY_CONTENT_TYPE, 0));
                result = new String(responseBody);
            }
        }
        return result;
    }

    @Override
    public String getAccept() {
        return JsonObjectRequest.ACCEPT;
    }
}
