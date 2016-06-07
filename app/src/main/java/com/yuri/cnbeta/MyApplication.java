package com.yuri.cnbeta;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yuri.cnbeta.utils.Emoticons;
import com.yuri.xlog.Log;
import com.yuri.xlog.Settings;

/**
 * Created by Yuri on 2016/4/8.
 */
public class MyApplication extends Application {

    private static Application mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        NoHttp.initialize(this);
        //开启MoHttp Log
        Logger.setTag("CnBetaNet");
        Logger.setDebug(true);

        //aa init
        ActiveAndroid.initialize(this);
        Emoticons.init(this);

        //Log
        Log.initialize(
                Settings.getInstance()
                .isDebug(BuildConfig.DEBUG)
                .isShowMethodLink(true)
                .isShowThreadInfo(true)
                .setAppTag("CnBetaApp")
                .setNetTag("CnBetaNet")
        );
    }

    public static Application getInstance() {
        return mInstance;
    }

}
