package com.yuri.cnbeta;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Yuri on 2016/4/8.
 */
public interface CnbetaService {

    @GET("more")
    Object getNews(@Query("type") String type, @Query("page") String page, @Query("_") String date);
}
