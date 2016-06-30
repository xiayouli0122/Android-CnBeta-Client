package com.yuri.cnbeta.http.response;

/**
 * Created by Yuri on 2016/4/8.
 */
public class ApiResponse<T> {
    public String status;
    public String message;
    public T result;

    public static class ErrorResult {
        public String error_code;
        public String error_msg;
    }
}
