package com.yuri.cnbeta.view.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.yuri.cnbeta.R;
import com.yuri.cnbeta.log.Log;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    private FragmentManager mFragmentManager;

    private static final String MAIN_TAG = "mainFragment";
    private static final String FAVORITE_TAG = "favoriteFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mFragmentManager = getSupportFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.fl_content_main, new MainFragment(), MAIN_TAG).commit();
        navigationView.getMenu().findItem(R.id.nav_camera).setChecked(true);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Fragment mainFragment = mFragmentManager.findFragmentByTag(MAIN_TAG);
            Fragment favoriteFragment = mFragmentManager.findFragmentByTag(FAVORITE_TAG);
            if (favoriteFragment != null) {
                mFragmentManager.beginTransaction().hide(favoriteFragment).commit();
            }

            if (mainFragment == null) {
                mFragmentManager.beginTransaction().add(R.id.fl_content_main, new MainFragment(),
                        MAIN_TAG).commit();
            } else {
                mFragmentManager.beginTransaction().show(mainFragment).commit();
            }
        } else if (id == R.id.nav_gallery) {
            Fragment mainFragment = mFragmentManager.findFragmentByTag(MAIN_TAG);
            Fragment favoriteFragment = mFragmentManager.findFragmentByTag(FAVORITE_TAG);
            if (mainFragment != null) {
                mFragmentManager.beginTransaction().hide(mainFragment).commit();
            }

            ViewPager viewPager;
            if (favoriteFragment == null) {
                mFragmentManager.beginTransaction().add(R.id.fl_content_main, new FavoriteFragment(),
                        FAVORITE_TAG).commit();
            } else {
                mFragmentManager.beginTransaction().show(favoriteFragment).commit();
            }
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
