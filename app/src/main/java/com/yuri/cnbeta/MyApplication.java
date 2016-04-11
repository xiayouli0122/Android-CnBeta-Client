package com.yuri.cnbeta;

import android.app.Application;

import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yuri.cnbeta.log.Log;

/**
 * Created by Yuri on 2016/4/8.
 */
public class MyApplication extends Application {

    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        NoHttp.init(this);
        //开启MoHttp Log
        Logger.setTag("YuriNet");
        Logger.setDebug(true);
    }

    public static Application getInstance() {
        return mInstance;
    }

}
