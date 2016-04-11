package com.yuri.cnbeta.view.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.yuri.cnbeta.BuildConfig;
import com.yuri.cnbeta.R;
import com.yuri.cnbeta.view.widgets.FixViewPager;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Yuri on 2016/4/11.
 */
public class ImageActivity extends Activity{

    public static final String IMAGE_URLS = "image_urls";
    public static final String CURRENT_POS = "current";
    private static final String imageNumFormate = " %d / %d ";

    private FixViewPager mFixViewPager;

    private TextView imagenum;
    private String[] imageSrcs;
    private int pos;
    private List<View> views;
    private List<ImageItem> imageItems;
    private int screenHeight;
    private int screenWidth;
    private boolean debug;
    private boolean preload_image = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.color.gray_80);
        if (getIntent().getExtras().containsKey(IMAGE_URLS) && getIntent().getExtras().containsKey(CURRENT_POS)) {
            debug = BuildConfig.DEBUG;
            screenHeight = getResources().getDisplayMetrics().heightPixels;
            screenWidth = getResources().getDisplayMetrics().widthPixels;
            this.imageSrcs = getIntent().getStringArrayExtra(IMAGE_URLS);
            this.pos = getIntent().getIntExtra(CURRENT_POS, 0);
            if (imageSrcs.length == 0) {
                this.finish();
                return;
            }
//            TranslucentStatusHelper.TranslucentStatusBar(this);
            setContentView(R.layout.activity_image);
            initView();
            loadAndShowPos(pos);
        } else {
            this.finish();
        }
    }

    private void initView() {
//        FilterMe filtermenu = (FilterMenuLayout) findViewById(R.id.filter_menu);
//        this.imagenum = (TextView) findViewById(R.id.image_num);
//        this.pager = (FixViewPager) findViewById(R.id.pager);
    }

    private void loadAndShowPos(int pos) {

    }

    class ImageItem {
        public final int NOTSHOW = 0;
        public final int SHOWING = 1;
        public final int SHOWSUCCESS = 3;
        public final int SHOWFAILURE = 4;
        private String imageSrc;
        private ImageView imageview;
        private ProgressBar progress;
        private int showStatus = NOTSHOW;

        public ImageItem(String imageSrc, ImageView imageview, ProgressBar progressBar) {
            this.imageSrc = imageSrc;
            this.imageview = imageview;
            this.progress = progress;
            this.showStatus = NOTSHOW;
        }

        public void displayImage(boolean current) {
            if (showStatus == NOTSHOW || showStatus == SHOWFAILURE) {
                Glide.with(ImageActivity.this)
                        .load(Uri.parse(imageSrc))
                        .listener(new RequestListener<Uri, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                                showStatus = SHOWFAILURE;
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                showStatus = SHOWSUCCESS;
                                return false;
                            }
                        })
                        .into(imageview);
            } else if (showStatus == SHOWSUCCESS) {
//                if (imageview instanceof GifImageView) {
//                    Drawable drawable = ((GifImageView) imageview).getDrawable();
//                    if (drawable instanceof GifDrawable) {
//                        if (current) {
//                            ((GifDrawable) drawable).start();
//                        } else {
//                            ((GifDrawable) drawable).stop();
//                        }
//                    }
//                }
            }
        }
    }
}
