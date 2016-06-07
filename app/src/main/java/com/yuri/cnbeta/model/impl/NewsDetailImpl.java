package com.yuri.cnbeta.model.impl;

import android.content.Context;

import com.activeandroid.query.Select;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yuri.cnbeta.db.NewsItem;
import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.model.NewsDetailModel;
import com.yuri.cnbeta.model.listener.HttpResultListener;
import com.yuri.cnbeta.view.ui.NewsDetailActivity;
import com.yuri.xlog.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yuri on 2016/4/13.
 */
public class NewsDetailImpl extends BaseNetImpl implements NewsDetailModel {

    @Override
    public void getData(Context context, final String sid, final HttpResultListener listener) {
        //通过这个API得到的资讯详情数据，如果详情中包含视频模块，则可能会丢失视频相关数据
        //所以我们采用另外一种方法，抓取htm，并解析其中的数据，以拿到一个资讯详情的数据（感谢ywwxhz）
//        String contentUrl = HttpConfigure.newsContent(sid);
//        Log.d("contentUrl:" + contentUrl);
//
//        Type type = new TypeToken<ApiResponse<Content>>() {}.getType();
//        Request<ApiResponse> request = new JsonRequest(contentUrl, type);
//        request.setCancelSign(NewsDetailActivity.class);
//
//        CallServer.getInstance().add(context, 1, request, new HttpListener<ApiResponse>() {
//            @Override
//            public void onSuccess(int what, Response<ApiResponse> response) {
//                ApiResponse<Content> apiResponse = response.get();
//                Content content = apiResponse.result;
//                if (listener != null) {
//                    listener.onSuccess(content);
//                }
//            }
//
//            @Override
//            public void onFailed(int what, String url, Object tag, Exception exception,
//                                 int responseCode, long networkMills) {
//                if (listener != null) {
//                    listener.onFail(exception.getMessage());
//                }
//            }
//        }, true);

        //方法二
        String url = HttpConfigure.buildArtileUrl(sid);
        Log.d("contentUrl:" + url);

        RequestMethod requestMethod = RequestMethod.GET;
        Request<String> request2 = NoHttp.createStringRequest(url, requestMethod);
        request2.setCancelSign(NewsDetailActivity.class);
        CallServer.getInstance().add(context, 0, request2, new HttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                Content content = new Content();
                content.sid = sid;
                parseHtml(response.get(), content);
                if (listener != null) {
                    listener.onSuccess(content);
                }
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMills) {
                if (listener != null) {
                    listener.onFail(exception.getMessage());
                }
            }
        }, true);
    }

    @Override
    public boolean isFavorited(String sid) {
        NewsItem newsItem = new Select().from(NewsItem.class).where("sid=?", sid).executeSingle();
        return newsItem != null;
    }

    @Override
    public boolean doFavorite(Content content, String topicLogo) {
        if (isFavorited(content.sid)) {
            return true;
        }
        NewsItem newsItem = new NewsItem();
        newsItem.sid = content.sid;
        newsItem.aid = content.aid;
        newsItem.bodytext = content.bodytext;
        newsItem.comments = content.comments;
        newsItem.counter = content.counter;
        newsItem.hometext = content.hometext;
        newsItem.source = content.source;
        newsItem.title = content.title;
        newsItem.topic = content.topic;
        newsItem.time = content.time;
        newsItem.topicLogo = topicLogo;
        if (newsItem.save() == -1) {
            //insert error
            return false;
        }
        return true;
    }

    public static final Pattern SN_PATTERN = Pattern.compile("SN:\"(.{5})\"");
    private void parseHtml(String htm, Content item) {
        Log.d("html");
        Document doc = Jsoup.parse(htm);
        Elements newsHeadlines = doc.select(".body");
        item.title = newsHeadlines.select("#news_title").html().replaceAll("<.*?>", "");
        item.source = newsHeadlines.select(".where").html();
        item.time = newsHeadlines.select(".date").html();
        Elements introduce = newsHeadlines.select(".introduction");
        introduce.select("div").remove();
        item.hometext = introduce.html();
        Elements content = newsHeadlines.select(".content");
        content.select(".tigerstock").remove();
        Elements scripts = content.select("script");
        for (int i=0;i<scripts.size();i++){
            Element script = scripts.get(i);
            Element SiblingScript = script.nextElementSibling();
            String _script;
            if(SiblingScript!=null&&SiblingScript.tag()== Tag.valueOf("script")){
                i++;
                _script = script.toString().replaceAll(",?\"?(width|height)\"?:?\"(.*)?\"","");
                _script += SiblingScript.toString();
                _script = _script.replaceAll("\"|'","'");
                SiblingScript.remove();
            }else{
                _script = script.toString().replaceAll(",?\"(width|height)\":\"\\d+\"","").replaceAll("\"|'","'");
            }
            Element element = new Element(Tag.valueOf("iframe"),"");
            element.attr("contentScript",_script);
            element.attr("ignoreHolder","true");
            element.attr("style","width:100%");
            element.attr("allowfullscreen ","true");
            element.attr("onload","VideoTool.onloadIframeVideo(this)");
            script.replaceWith(element);
        }
//        if(cacheImage){
//            Elements images = content.select("img");
//            for(Element image:images){
//                Bitmap img = ImageLoader.getInstance().loadImageSync(image.attr("src"), MyApplication.getDefaultDisplayOption());
//                if(img!=null) {
//                    img.recycle();
//                }
//            }
//        }
        item.bodytext = content.html();
        Matcher snMatcher = SN_PATTERN.matcher(htm);
        if (snMatcher.find()) {
            String sn = snMatcher.group(1);
        }
//            item.setSN(snMatcher.group(1));
//        Log.d(item.toString());
//        if(item.getContent()!=null&&item.getContent().length()>0){
//            if(shouldCache) {
//                FileCacheKit.getInstance().put(item.getSid() + "", Toolkit.getGson().toJson(item));
//            }
//            return true;
//        }else{
//            return false;
//        }
    }
}
