package com.yuri.cnbeta.newsdetial;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yuri.cnbeta.R;
import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.utils.ToastUtil;
import com.yuri.cnbeta.view.ui.ImageActivity;
import com.yuri.cnbeta.view.ui.NewsCommentActivity;
import com.yuri.cnbeta.view.ui.base.BaseActivity;
import com.yuri.cnbeta.view.widgets.AVLoadingIndicatorView.AVLoadingIndicatorView;
import com.yuri.xlog.Log;

import java.lang.reflect.Type;
import java.util.Locale;
import java.util.regex.Matcher;

/**
 *  <t>新闻详情Activity</t>
 *  <p>从cnbeta服务器api接口中获取指定新闻的数据详情，然后根据web页面模板动态生成一个web页面显示在webview上</p>
 * Created by Yuri on 2016/4/11.
 */
public class NewsDetailActivity extends BaseActivity implements NewsDetailContract.View {

    private static final String EXTRA_SID = "extra_sid";
    private static final String EXTRA_TOPIC_LOGO = "extra_topic_logo";

    private String mSID;
    private String mTopicLogoUrl;

    private WebView mWebview;
    private FloatingActionButton mActionButton;
    private AVLoadingIndicatorView mLoadingView;
    private FrameLayout mFrameLayout;

    private static final String WEB_TEMPLATE = "<!DOCTYPE html><html><head><title></title><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no\"/>" +
            "<link  rel=\"stylesheet\" href=\"file:///android_asset/style.css\" type=\"text/css\"/><style>.title{color: #%s;}%s</style>" +
            "<script>var config = {\"enableImage\":%s,\"enableFlashToHtml5\":%s};</script>" +
            "<script src=\"file:///android_asset/BaseTool.js\"></script>" +
            "<script src=\"file:///android_asset/ImageTool.js\"></script>" +
            "<script src=\"file:///android_asset/VideoTool.js\"></script></head>" +
            "<body><div><div class=\"title\">%s</div><div class=\"from\">%s<span style=\"float: right\">%s</span></div><div id=\"introduce\">%s<div class=\"clear\"></div></div><div id=\"content\">%s</div><div class=\"clear foot\">-- The End --</div></div>" +
            "<script src=\"file:///android_asset/loder.js\"></script></body></html>";
    private static String NIGHT = "body{color:#9bafcb}#introduce{background-color:#262f3d;color:#616d80}.content blockquote{background-color:#262f3d;color:#616d80}";
    private static String LIGHT = "#introduce{background-color:#F1F1F1;color: #444;}";

    private Content mContent;

    private NewsDetailPresenter mPresenter;

    public static Intent getIntent(Context context, String sid, String topicLogo) {
        Intent intent = new Intent();
        intent.setClass(context, NewsDetailActivity.class);
        intent.putExtra(EXTRA_SID, sid);
        intent.putExtra(EXTRA_TOPIC_LOGO, topicLogo);
        return intent;
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_news_detail);
        setUpMenu(R.menu.news_detail);
    }

    @Override
    protected void setUpView() {
        mLoadingView = (AVLoadingIndicatorView) findViewById(R.id.loading_view);
        mWebview = (WebView) findViewById(R.id.webview_details);
        mFrameLayout = (FrameLayout) findViewById(R.id.content);
        mActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //由于使用了ywwxhz获取咨询详情的方法，所以这里得不到评论的总数量了（详见NewsDetailImpl）
                //所以这里就默认传个0过去吧，NewsCommentActivity暂时也舍弃对评论总数量的处理
                Intent intent = NewsCommentActivity.getIntent(NewsDetailActivity.this,
                        mContent.sid, 0);
                startActivity(intent);
            }
        });
        mActionButton.setScaleX(0);
        mActionButton.setScaleY(0);
    }

    @Override
    protected void setUpData() {

        mPresenter = new NewsDetailPresenter(this);

        mSID = getIntent().getStringExtra(EXTRA_SID);
        mTopicLogoUrl = getIntent().getStringExtra(EXTRA_TOPIC_LOGO);
        Log.d("sid:" + mSID);

        WebSettings webSettings = mWebview.getSettings();
        webSettings.setSupportZoom(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setPluginState(WebSettings.PluginState.ON_DEMAND);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setTextZoom(100);

        mWebview.addJavascriptInterface(new JavaScriptInterface(this), "Interface");
        mWebview.setWebChromeClient(new VideoWebChromeClient());
        mWebview.setWebViewClient(new MyWebViewClient());

        mLoadingView.start();

        if (mPresenter.isFavorited(mSID)) {
            mToolbar.getMenu().findItem(R.id.action_favorite).setTitle("已收藏");
        }

        mPresenter.getDetailData(mSID);
    }

    private void bindData() {
        int titleColor = getResources().getColor(R.color.colorPrimary);
        String colorString = Integer.toHexString(titleColor);
        String theme = LIGHT;
        boolean showImage = true;
        boolean convertFlashToHtml5 = true;//设置为false，video将不显示以及点击无效
        String data = String.format(Locale.CHINA, WEB_TEMPLATE, colorString.substring(2, colorString.length()),
                theme, showImage, convertFlashToHtml5, mContent.title, mContent.source,
                mContent.time, mContent.hometext, mContent.bodytext);
        mWebview.loadDataWithBaseURL(HttpConfigure.BASE_URL, data, "text/html", "utf-8", null);

        mActionButton.postDelayed(new Runnable() {
            @Override
            public void run() {
                mActionButton.setVisibility(View.VISIBLE);
                mActionButton.animate()
                        .scaleX(1)
                        .scaleY(1)
                        .setDuration(500)
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .start();
                mLoadingView.stop();
                mWebview.setVisibility(View.VISIBLE);
            }
        }, 200);
    }

    @Override
    public void showData(Content content) {
        mContent = content;
        setUpTitle(mContent.title);
        bindData();
    }

    @Override
    public void showError(String message) {
        Log.d(message);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                String content = mContent.title + " "
                        + HttpConfigure.buildMobileViewUrl(mContent.sid)
                        + "\r\n(分享自Yuri CnBeta客户端)";
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, content);
                shareIntent.setType("text/plain");
                //设置分享列表的标题，并且每次都显示分享列表
                startActivity(Intent.createChooser(shareIntent, "分享到"));
                break;
            case R.id.action_favorite:
                if (mPresenter.isFavorited(mSID)) {
                    ToastUtil.showToast(getApplicationContext(), "已收藏");
                } else {
                    //bug:在页面还没出来的时候，收藏功能应该是不可用的
                    if (mPresenter.doFavorite(mContent, mTopicLogoUrl)) {
                        mToolbar.getMenu().findItem(R.id.action_favorite).setTitle("已收藏");
                        ToastUtil.showToast(getApplicationContext(), "收藏成功");
                    } else {
                        ToastUtil.showToast(getApplicationContext(), "已收藏");
                    }
                }
                break;
            case R.id.action_view_in_browser:
                Intent viewIntent = new Intent();
                viewIntent.setAction(Intent.ACTION_VIEW);
                viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                viewIntent.setData(Uri.parse(HttpConfigure.buildMobileViewUrl(mContent.sid)));
                startActivity(viewIntent);
                break;
        }
        return super.onMenuItemClick(item);
    }

    class VideoWebChromeClient extends WebChromeClient {
        private View myView = null;
        CustomViewCallback myCallback = null;

        @Override
        public void onShowCustomView(View view, CustomViewCallback customViewCallback) {
            Log.d();
            if (myCallback != null) {
                myCallback.onCustomViewHidden();
                myCallback = null;
                return;
            }
            view.setBackgroundColor(Color.BLACK);
            onShowHtmlVideoView(view);
            myView = view;
            myCallback = customViewCallback;
        }

        @Override
        public void onHideCustomView() {
            Log.d();
            if (myView != null) {
                if (myCallback != null) {
                    myCallback.onCustomViewHidden();
                    myCallback = null;
                }
                onHideHtmlVideoView(myView);
                myView = null;
            }
        }
    }

    class MyWebViewClient extends WebViewClient {
        private boolean finish = false;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            finish = false;
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                Intent intent;
                Matcher sidMatcher = HttpConfigure.MOBILE_ARTICLE_PATTERN.matcher(url);
                if (sidMatcher.find()) {
                    intent = NewsDetailActivity.getIntent(NewsDetailActivity.this, mContent.sid, "");
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            } catch (Exception ignored) {

            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d("url:" + url);
            super.onPageFinished(view, url);
            finish = true;
        }

        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
            Log.d("url:" + url);
            String prefix = MimeTypeMap.getFileExtensionFromUrl(url);
            if (!TextUtils.isEmpty(prefix)) {
                String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(prefix);
                if (mimeType != null && mimeType.startsWith("image")) {
                    boolean showImage = true;
                    if (finish || showImage) {
                        //这里试着使用Glide的缓存图片去加载

                    }
                }
            }
            return super.shouldInterceptRequest(view, url);
        }
    }

    private void onShowHtmlVideoView(View html5VideoView) {
        Log.d();
        //全屏展示 video 暂时不支持
        mFrameLayout.addView(html5VideoView);
        mWebview.setVisibility(View.GONE);
        mActionButton.setVisibility(View.GONE);
    }

    private void onHideHtmlVideoView(View html5VideoView) {
        mFrameLayout.removeView(html5VideoView);
        mWebview.setVisibility(View.VISIBLE);
        mActionButton.setVisibility(View.VISIBLE);
    }

    private class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void showImage(String pos, final String[] imageSrcs) {
            Log.d("pos:" + pos + ",imageSrcs:" + imageSrcs);
            final int posi;
            try {
                posi = Integer.parseInt(pos);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(mContext, ImageActivity.class);
                        intent.putExtra(ImageActivity.IMAGE_URLS, imageSrcs);
                        intent.putExtra(ImageActivity.CURRENT_POS, posi);
                        mContext.startActivity(intent);
                    }
                });
            } catch (Exception e) {
                android.util.Log.d(getClass().getName(), "Illegal argument");
            }
        }

        @JavascriptInterface
        public void loadSohuVideo(final String hoder_id, final String requestUrl) {
            Log.d("videoUrl:" + requestUrl);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Request<String> stringRequest = NoHttp.createStringRequest(requestUrl);
                    CallServer.getInstance().add(0, stringRequest, new HttpListener<String>(){
                        @Override
                        public void onSuccess(int what, Response<String> response) {
                            Log.json(response.get());
                            Type type = new TypeToken<VideoInfoResponse>(){}.getType();
                            VideoInfoResponse videoInfoResponse = new Gson().fromJson(response.get(), type);
                            if (videoInfoResponse.status == 200) {
                                Log.object(videoInfoResponse.data);
                                try {
                                    mWebview.loadUrl("javascript:VideoTool.VideoCallBack(" +
                                            "\"" + hoder_id + "\",\"" + videoInfoResponse.data.url_high_mp4 +
                                            "\",\"" + videoInfoResponse.data.hor_big_pic + "\")");
                                } catch (Exception e) {
                                    Log.d("@@video load fail:" + e.getMessage());
                                }
                            } else {
                                Log.e("##video load fail:" + videoInfoResponse.statusText);
                            }
                        }

                        @Override
                        public void onFailed(int what, String errorMsg) {
                            Log.d("video load fail:" + errorMsg);
                        }

                    });

                }
            });
        }

        @JavascriptInterface
        public void showMessage(final String message, final String type) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(getApplicationContext(), message);
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancelRequest();
        mPresenter = null;

        mWebview.stopLoading();
        mWebview.destroy();
    }
}
