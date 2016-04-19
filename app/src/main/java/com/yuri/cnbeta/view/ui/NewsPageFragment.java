package com.yuri.cnbeta.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.yuri.cnbeta.R;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.model.bean.NewsType;
import com.yuri.cnbeta.model.bean.Topic;
import com.yuri.cnbeta.view.ui.core.BaseFragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by Yuri on 2016/4/18.
 */
public class NewsPageFragment extends BaseFragment {

    public static final String NEWS_TYPE = "news_page_news_type";
    private NewsType mNewsType;

    private static final String[] RANK_TYPES = {"comments", "dig", "counter"};
    private static final String[] RANK_TITLES = {"评论", "点击量", "阅读量"};
    private Topic mTopic;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private NewsPagerAdapter mPagerAdapter;



    public NewsPageFragment() {
    }

    public static NewsPageFragment getInstance(NewsType newsType) {
        NewsPageFragment fragment = new NewsPageFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(NEWS_TYPE, newsType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNewsType = (NewsType) getArguments().getSerializable(NEWS_TYPE);
        Log.d("newsType:" + mNewsType);

        if (mNewsType == NewsType.TOPIC) {
            try {
                //读取预设好的主题数据
                InputStream inputStream = getResources().getAssets().open("news_topics.txt");

                int size = inputStream.available();

                byte[] buffer = new byte[size];
                inputStream.read(buffer);
                inputStream.close();

                String topicJson = new String(buffer);

                mTopic = new Gson().fromJson(topicJson, Topic.class);
            } catch (IOException e) {
                e.printStackTrace();

                Log.e(e.getMessage());
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_page, container, false);
        mTabLayout = (TabLayout) rootView.findViewById(R.id.news_page_tablayout);
        mViewPager = (ViewPager) rootView.findViewById(R.id.news_page_viewpager);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPagerAdapter = new NewsPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        if (isDialy()) {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
            mViewPager.setOffscreenPageLimit(3);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            mViewPager.setOffscreenPageLimit(1);
        }
    }

    public class NewsPagerAdapter extends FragmentStatePagerAdapter {

        SparseArray<MainFragment> fragments = new SparseArray<>();

        public NewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (isDialy()) {
                return RANK_TITLES[position];
            } else {
                return mTopic.topics.get(position).title;
            }
        }

        @Override
        public Fragment getItem(int position) {
            String param;
            MainFragment fragment;
            if (isDialy()) {
                param = RANK_TYPES[position];
            } else {
                if (mTopic == null) {
                    return null;
                }
                param = mTopic.topics.get(position).id;
            }
            fragment = MainFragment.getInstance(mNewsType, param);
            fragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            fragments.remove(position);
        }

        @Override
        public int getCount() {
            if (isDialy()) {
                return RANK_TYPES.length;
            } else {
                if (mTopic != null) {
                    return mTopic.topics.size();
                }
            }
            return 0;
        }

        public MainFragment getFragment(int position) {
            return fragments.get(position);
        }
    }

    private boolean isDialy() {
        return mNewsType == NewsType.DAILY;
    }

    @Override
    public void goTop() {
        mPagerAdapter.getFragment(mViewPager.getCurrentItem()).goTop();
    }
}
