package com.yuri.cnbeta.view.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.Request;
import com.yolanda.nohttp.Response;
import com.yuri.cnbeta.R;
import com.yuri.cnbeta.http.CallServer;
import com.yuri.cnbeta.http.HttpConfigure;
import com.yuri.cnbeta.http.HttpListener;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.http.response.Content;
import com.yuri.cnbeta.presenter.NewsDetailPresenter;
import com.yuri.cnbeta.utils.ToastUtil;
import com.yuri.cnbeta.utils.Utils;
import com.yuri.cnbeta.view.INewsDetailView;
import com.yuri.cnbeta.view.ui.core.BaseActivity;
import com.yuri.cnbeta.view.widgets.AVLoadingIndicatorView.AVLoadingIndicatorView;

import java.util.Locale;
import java.util.regex.Matcher;

/**
 *  <t>新闻详情Activity</t>
 *  <p>从cnbeta服务器api接口中获取指定新闻的数据详情，然后根据web页面模板动态生成一个web页面显示在webview上</p>
 * Created by Yuri on 2016/4/11.
 */
public class NewsDetailActivity extends BaseActivity implements INewsDetailView {

    public static final String EXTRA_SID = "extra_sid";

    private String mSID;

    private WebView mWebview;
    private WebSettings mWebSetting;
    private FloatingActionButton mActionButton;
    private AVLoadingIndicatorView mLoadingView;

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

    public static Intent getIntent(Context context, String sid) {
        Intent intent = new Intent();
        intent.setClass(context, NewsDetailActivity.class);
        intent.putExtra(EXTRA_SID, sid);
        return intent;
    }

    @Override
    protected void setUpContentView() {
        setContentView(R.layout.activity_news_detail);
    }

    @Override
    protected void setUpView() {
        mLoadingView = (AVLoadingIndicatorView) findViewById(R.id.loading_view);
        mWebview = (WebView) findViewById(R.id.webview_details);
        mActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = NewsCommentActivity.getIntent(NewsDetailActivity.this, mContent.getSid());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void setUpData() {

        mPresenter = new NewsDetailPresenter(getApplicationContext(), this);

        mSID = getIntent().getStringExtra(EXTRA_SID);
        Log.d("sid:" + mSID);

        mWebSetting = mWebview.getSettings();
        mWebSetting.setSupportZoom(false);
        mWebSetting.setAllowFileAccess(true);
        mWebSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
        mWebSetting.setJavaScriptEnabled(true);
        mWebSetting.setDomStorageEnabled(true);
        mWebSetting.setLoadsImagesAutomatically(true);
        WebView.setWebContentsDebuggingEnabled(true);
        mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        mWebSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        mWebSetting.setTextZoom(100);

        mWebview.addJavascriptInterface(new JavaScriptInterface(this), "Interface");
        mWebview.setWebChromeClient(new VideoWebChromeClient());
        mWebview.setWebViewClient(new MyWebViewClient());

        mLoadingView.start();


        mPresenter.getData(mSID);
    }

    private void bindData() {
        int titleColor = getResources().getColor(R.color.colorPrimary);
        String colorString = Integer.toHexString(titleColor);
        String theme = LIGHT;
        boolean showImage = true;
        boolean convertFlashToHtml5 = false;
        String date = Utils.getDate(Long.parseLong(mContent.inputtime) * 1000);
        String data = String.format(Locale.CHINA, WEB_TEMPLATE, colorString.substring(2, colorString.length()),
                theme, showImage, convertFlashToHtml5, mContent.getTitle(), mContent.getSource(),
                date, mContent.getHometext(), mContent.getBodytext());
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
        setUpTitle(mContent.getTitle());
        bindData();
    }

    @Override
    public void showError(String message) {
        Log.d(message);
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
        private static final String TAG = "WebView ImageLoader";
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
                Matcher sidMatcher = HttpConfigure.ARTICLE_PATTERN.matcher(url);
                if (sidMatcher.find()) {
                    intent = NewsDetailActivity.getIntent(NewsDetailActivity.this, mContent.getSid());
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
                }
            }
            return super.shouldInterceptRequest(view, url);
        }
    }

    private void onShowHtmlVideoView(View html5VideoView) {
    }

    private void onHideHtmlVideoView(View html5VideoView) {
    }

    private class JavaScriptInterface {
        Context mContext;
        private Handler myHandler;

        JavaScriptInterface(Context c) {
            mContext = c;
            myHandler = new Handler();
        }

        @JavascriptInterface
        public void showImage(String pos, final String[] imageSrcs) {
            Log.d("pos:" + pos + ",imageSrcs:" + imageSrcs);
            final int posi;
            try {
                posi = Integer.parseInt(pos);
                myHandler.post(new Runnable() {
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
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    Request<String> stringRequest = NoHttp.createStringRequest(requestUrl);
                    CallServer.getInstance().add(getApplicationContext(), 0, stringRequest, new HttpListener<String>(){

                        @Override
                        public void onSuccess(int what, Response<String> response) {
                            Log.d("" + response.get());
                        }

                        @Override
                        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMills) {

                        }
                    }, true);
//                    NetKit.getAsyncClient().get(requestUrl, new JsonHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            try {
//                                mWebView.loadUrl("javascript:VideoTool.VideoCallBack(\"" + hoder_id + "\",\"" + response.getJSONObject("data").getString("url_high_mp4") + "\",\"" + response.getJSONObject("data").getString("hor_big_pic") + "\")");
//                            } catch (Exception e) {
//                                Toolkit.showCrouton(mActivity, "搜狐视频加载失败", Style.ALERT);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                            Toolkit.showCrouton(mActivity, "搜狐视频加载失败", Style.ALERT);
//                            if (MyApplication.getInstance().getDebug()) {
//                                Toast.makeText(getActivity(), throwable.toString(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
                }
            });
        }

        @JavascriptInterface
        public void showMessage(final String message, final String type) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
//                    Toolkit.showCrouton(mActivity, message, CroutonStyle.getStyle(type));
                    ToastUtil.showToast(getApplicationContext(), message);
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.cancelRequestBySign(NewsDetailActivity.class);
        mPresenter = null;
    }
}
