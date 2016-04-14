package com.yuri.cnbeta;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.yolanda.nohttp.Logger;
import com.yolanda.nohttp.NoHttp;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.utils.Emoticons;

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
        initUmengConfig();

        //aa init
        ActiveAndroid.initialize(this);
        Emoticons.init(this);
    }

    public static Application getInstance() {
        return mInstance;
    }

    private void initUmengConfig() {
        //微信    wx12342956d1cab4f9,a5ae111de7d9ea137e88a5e02c07c94d
        PlatformConfig.setWeixin("wx54a7c3475c53229c", "ad14e541a0f0b5d5e8820a2f274f9267");
        //豆瓣RENREN平台目前只能在服务器端配置
        //新浪微博
//        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        //易信
//        PlatformConfig.setYixin("yxc0614e80c9304c11b0391514d09f13bf");
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setQQZone("1105329752", "jyc8t8wE1PKnmKWd");
//        PlatformConfig.setTwitter("3aIN7fuF685MuZ7jtXkQxalyi", "MK6FEYG63eWcpDFgRYw4w9puJhzDl0tyuqWjZ3M7XJuuG7mMbO");
//        PlatformConfig.setAlipay("2015111700822536");
//        PlatformConfig.setLaiwang("laiwangd497e70d4", "d497e70d4c3e4efeab1381476bac4c5e");
//        PlatformConfig.setPinterest("1439206");
        com.umeng.socialize.utils.Log.LOG = Log.isDebug;
    }

}
