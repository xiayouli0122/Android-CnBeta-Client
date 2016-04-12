package com.yuri.cnbeta.view.ui;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.linroid.filtermenu.library.FilterMenu;
import com.linroid.filtermenu.library.FilterMenuLayout;
import com.yuri.cnbeta.BuildConfig;
import com.yuri.cnbeta.R;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.view.widgets.FixViewPager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Yuri on 2016/4/11.
 */
public class ImageActivity extends Activity implements ViewPager.OnPageChangeListener {

    public static final String IMAGE_URLS = "image_urls";
    public static final String CURRENT_POS = "current";
    private static final String imageNumFormate = " %d / %d ";

    private FixViewPager mFixViewPager;

    private TextView imagenum;
    private String[] imageSrcs;
    private int pos;
    private List<View> views;
    private List<ImageItem> mImageItems;
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
        FilterMenuLayout filtermenu = (FilterMenuLayout) findViewById(R.id.filter_menu);
        this.imagenum = (TextView) findViewById(R.id.image_num);
        this.mFixViewPager = (FixViewPager) findViewById(R.id.pager);
        attachMenu(filtermenu);

        views = new ArrayList<>(imageSrcs.length);
        mImageItems = new ArrayList<>();
        for (String url : imageSrcs) {
            Log.d("url:" + url);
            FrameLayout view = new FrameLayout(this);
            //View view = LayoutInflater.from(this).inflate(R.layout.image_item, pager, false);
            ImageView imageView = new ImageView(this);
            FrameLayout.LayoutParams pvparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(pvparams);
            view.addView(imageView);
//            ProgressWheel progress = new ProgressWheel(this);
//            progress.setRimWidth(width);
//            progress.setBarWidth(width);
//            progress.setBarColor(Color.parseColor("#fff0f4e2"));
//            progress.setRimColor(Color.parseColor("#44000000"));
//            FrameLayout.LayoutParams pgparams = new FrameLayout.LayoutParams(progressWidth, progressWidth);
//            pgparams.gravity = Gravity.CENTER;
//            progress.setLayoutParams(pgparams);
//            progress.spin();
//            view.addView(progress);
            mImageItems.add(new ImageItem(url, imageView));
            views.add(view);
        }

        PagerAdapter mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return imageSrcs.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(views.get(position));
                return views.get(position);
            }
        };
        mFixViewPager.setAdapter(mPagerAdapter);
        mFixViewPager.setOnPageChangeListener(this);
        mFixViewPager.setPageTransformer(true, new MyPageTransformer());
        mFixViewPager.setCurrentItem(pos);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        loadAndShowPos(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    private class MyPageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);
            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when
                // moving to the left page
                view.setAlpha(1);
                view.setTranslationX(0);
            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);
                // Counteract the default slide transition
                view.setTranslationX(pageWidth / 2 * -position);
            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }

    }


    private void loadAndShowPos(int pos) {
        mImageItems.get(pos).displayImage(true);
        if (preload_image) {
            if (pos > 0) {
                mImageItems.get(pos - 1).displayImage(false);
            }
            if (pos < mImageItems.size() - 1) {
                mImageItems.get(pos + 1).displayImage(false);
            }
        }
        imagenum.setText(String.format(Locale.CHINA, imageNumFormate, pos + 1, imageSrcs.length));
    }

    private FilterMenu attachMenu(FilterMenuLayout layout) {
        return new FilterMenu.Builder(this)
                .addItem(R.mipmap.ic_save)
                .addItem(R.mipmap.ic_share)
                .addItem(R.mipmap.ic_reflush)
                .withListener(new FilterMenu.OnMenuChangeListener() {
                    @Override
                    public void onMenuItemClick(View view, int position) {
//                        if (position != 2) {
//                            String image_url = imageSrcs[mFixViewPager.getCurrentItem()];
//                            //UIL
//                            Glide.with(ImageActivity.class).load(image_url).downloadOnly();
//                            File imageFile = ImageLoader.getInstance().getDiskCache().get(image_url);
//                            if (imageFile != null) {
//                                switch (position) {
//                                    case 0:
//                                        saveImage(image_url, imageFile);
//                                        break;
//                                    case 1:
//                                        shareImage(image_url, imageFile);
//                                        break;
//                                }
//                            } else {
//                                Toast.makeText(ImageViewActivity.this, "图片还未下载完成", Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            mImageItems.get(pager.getCurrentItem()).displayImage(true);
//                        }
                    }

                    @Override
                    public void onMenuCollapse() {

                    }

                    @Override
                    public void onMenuExpand() {

                    }
                })
                .attach(layout)
                .build();
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

        public ImageItem(String imageSrc, ImageView imageview) {
            this.imageSrc = imageSrc;
            this.imageview = imageview;
//            this.progress = progress;
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
