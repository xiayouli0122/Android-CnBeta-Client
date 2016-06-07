package com.yuri.cnbeta.newsdetial;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.model.listener.HttpResultListener;
import com.yuri.xlog.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 新闻详情
 * Created by Yuri on 2016/6/7.
 */
public class NewsDetailModel extends NewsDetailContract.Model {

    @Override
    public void getDetailData(final String sid, final HttpResultListener listener) {
        //通过这个API得到的资讯详情数据，如果详情中包含视频模块，则可能会丢失视频相关数据
        //官方客户端就是使用的api获取数据的，然后你发现详情页本来有视频的却都无法显示
//        String contentUrl = HttpConfigure.newsContent(sid);
        //所以我们采用另外一种方法，抓取htm，并解析其中的数据，以拿到一个资讯详情的数据（感谢ywwxhz）
        //但是这个解析拿不到评论条数
        //方法二
//        String url = HttpConfigure.buildArtileUrl(sid);
        //去获取cnbeta移动版网页的数据
        String url = HttpConfigure.buildMobileViewUrl(sid);
        Log.d("contentUrl:" + url);
        RequestMethod requestMethod = RequestMethod.GET;
        Request<String> request = NoHttp.createStringRequest(url, requestMethod);
        addRequest(url, request);
        request(request, new HttpListener<String>() {
            @Override
            public void onSuccess(int what, Response<String> response) {
                Content content = new Content();
                content.sid = sid;
//                parseHtml(response.get(), content);
                parseMobileHtml(response.get(), content);
                if (listener != null) {
                    listener.onSuccess(content);
                }
            }

            @Override
            public void onFailed(int what, String errorMsg) {
                if (listener != null) {
                    listener.onFail(errorMsg);
                }
            }
        });
    }

    /**
     * 解析移动端网页数据
     * @param html html数据
     * @param item content
     */
    private void parseMobileHtml(String html, Content item) {
        Log.d();
        Document document = Jsoup.parse(html);
        Elements newsHeadlines = document.select("body");
        String title = newsHeadlines.select(".article-tit").html();
        Elements select = document.select(".article-holder");
        String source = select.select("span").first().text().trim();
        String commentnum = newsHeadlines.select(".commnum").text();
        String time = newsHeadlines.select(".time").text();
        String homeText = newsHeadlines.select(".article-summ").html();
        String bodyText = newsHeadlines.select(".articleCont").html();
        item.title = title;
        item.source = source;
        item.comments = commentnum;
        item.time = time;
        item.hometext = homeText;
        item.bodytext = bodyText;
    }

    private static final Pattern SN_PATTERN = Pattern.compile("SN:\"(.{5})\"");

    /**
     * 解析PC端网页数据
     * @param htm html数据
     * @param item content
     */
    private void parseHtml(String htm, Content item) {
        Log.d();
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
