package com.yuri.cnbeta.view.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuri.cnbeta.R;
import com.yuri.cnbeta.log.Log;
import com.yuri.cnbeta.model.bean.NewsType;

/**
 * Created by Yuri on 2016/4/18.
 */
public class NewsPageFragment extends Fragment {

    public static final String NEWS_TYPE = "news_page_news_type";
    private NewsType mNewsType;

    private static final String[] RANK_TYPES = {"comments", "dig", "counter"};

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
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public class NewsPagerAdapter extends FragmentStatePagerAdapter {


        public NewsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return RANK_TYPES[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = MainFragment.getInstance(mNewsType, RANK_TYPES[position]);
            return fragment;
        }

        @Override
        public int getCount() {
            return RANK_TYPES.length;
        }
    }
}
