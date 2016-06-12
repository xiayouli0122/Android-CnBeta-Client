package com.yuri.cnbeta.view.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.yuri.cnbeta.R;
import com.yuri.cnbeta.hotcomment.HotCommentsFragment;
import com.yuri.cnbeta.news.MainFragment;
import com.yuri.cnbeta.model.bean.NewsType;
import com.yuri.cnbeta.view.ui.base.BaseFragment;
import com.yuri.xlog.Log;

/**
 * 主界面包含Fragment切换以及侧滑菜单的控制
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;

    private BaseFragment mCurrentFragment;//当前选中Fragment

    private static final String MAIN_TAG = "mainFragment";
    private static final String FAVORITE_TAG = "favoriteFragment";
    private static final String HOT_COMMENTS_TAG = "hotCommentFragment";
    private static final String MONTHLY_TOP_TEN_TAG = "topTenFragment";
    private static final String DIALY_RANK_TAG = "dialyRankFragment";
    private static final String TOPIC_TAG = "topicFragment";

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //init
        mCurrentFragment = MainFragment.getInstance(NewsType.LATEST);

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.fl_content_main, mCurrentFragment, MAIN_TAG).commit();
        navigationView.getMenu().findItem(R.id.nav_latest_news).setChecked(true);

        mToolbar.setTitle("最新资讯");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentFragment.goTop();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        String tag = null;
        String title = "";
        switch (item.getItemId()) {
            case R.id.nav_latest_news:
                tag = MAIN_TAG;
                title = "最新资讯";
                break;
            case R.id.nav_favorite:
                tag = FAVORITE_TAG;
                title = "收藏列表";
                break;
            case R.id.nav_hot_comments:
                tag = HOT_COMMENTS_TAG;
                title = "热门评论";
                break;
            case R.id.nav_monthly_top_ten:
                tag = MONTHLY_TOP_TEN_TAG;
                title = "每月TOP10";
                break;
            case R.id.nav_daily_rank:
                tag = DIALY_RANK_TAG;
                title = "每日排行榜";
                break;
            case R.id.nav_topic:
                tag = TOPIC_TAG;
                title = "资讯主题";
                break;
        }
        showFragment(tag);
        mToolbar.setTitle(title);
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(String tag) {
        BaseFragment showFragment = (BaseFragment) mFragmentManager.findFragmentByTag(tag);
        if (mCurrentFragment != null) {
            mFragmentManager.beginTransaction().hide(mCurrentFragment).commit();
        }

        if (showFragment == null) {
            switch (tag) {
                case MAIN_TAG:
                    showFragment = MainFragment.getInstance(NewsType.LATEST);
                    break;
                case FAVORITE_TAG:
                    showFragment = new FavoriteFragment();
                    break;
                case HOT_COMMENTS_TAG:
                    showFragment = new HotCommentsFragment();
                    break;
                case MONTHLY_TOP_TEN_TAG:
                    showFragment = MainFragment.getInstance(NewsType.MONTHLY);
                    break;
                case DIALY_RANK_TAG:
                    showFragment = NewsPageFragment.getInstance(NewsType.DAILY);
                    break;
                case TOPIC_TAG:
                    showFragment = NewsPageFragment.getInstance(NewsType.TOPIC);
                    break;
                default:
                    Log.e("tag:" + tag);
                    break;

            }
            mFragmentManager.beginTransaction().add(R.id.fl_content_main, showFragment,
                    tag).commit();
        } else {
            mFragmentManager.beginTransaction().show(showFragment).commit();
        }
        mCurrentFragment = showFragment;
    }
}
