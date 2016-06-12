package com.yuri.cnbeta.view.ui.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.yuri.cnbeta.R;

public abstract class BaseActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    protected Toolbar mToolbar;
    protected TextView mToolBarTitleView;
    public static final int MODE_BACK = 0;
    public static final int MODE_DRAWER = 1;
    public static final int MODE_NONE = 2;
    public static final int MODE_HOME = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpContentView();
        setUpView();
        setUpData();
    }

    protected abstract void setUpContentView();

    protected abstract void setUpView();

    protected abstract void setUpData();


    @Override
    public void setContentView(int layoutResID) {
        setContentView(layoutResID, -1, -1, MODE_BACK);
    }

    public void setContentView(int layoutResID, int titleResId) {
        setContentView(layoutResID, titleResId, -1, MODE_BACK);
    }

    public void setContentView(int layoutResID, int titleResId, int mode) {
        setContentView(layoutResID, titleResId, -1, mode);
    }

    public void setContentView(int layoutResId, String title, int mode) {
        super.setContentView(layoutResId);
        setUpToolbar(title, -1, mode);
    }

    public void setContentView(int layoutResID, int titleResId, int menuId, int mode) {
        super.setContentView(layoutResID);
        setUpToolbar(titleResId, menuId, mode);
    }

    protected void setUpToolbar(String title, int menuId, int mode) {
        if (mode != MODE_NONE) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mToolbar.setTitle(title);

            if (mode == MODE_BACK) {
//                mToolbar.setnavigationicon(r.drawable.ic_toolbar_back);
            }
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationBtnClicked();
                }
            });

            setUpMenu(menuId);
        }
    }

    protected void setUpToolbar(int titleResId, int menuId, int mode) {
        if (mode != MODE_NONE) {
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mToolbar.setTitle("");

            if (mode == MODE_BACK) {
//                mToolbar.setnavigationicon(r.drawable.ic_toolbar_back);
            }
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNavigationBtnClicked();
                }
            });

            setUpTitle(titleResId);
            setUpMenu(menuId);
        }
    }

    protected void setUpMenu(int menuId) {
        if (mToolbar != null) {
            mToolbar.getMenu().clear();
            if (menuId > 0) {
                mToolbar.inflateMenu(menuId);
                mToolbar.setOnMenuItemClickListener(this);
            }
        }
    }

    protected void setUpTitle(int titleResId) {
        if (titleResId > 0 && mToolBarTitleView != null) {
            mToolBarTitleView.setText(titleResId);
        }
    }

    protected void setUpTitle(String title) {
        mToolbar.setTitle(title);
    }

    protected void onNavigationBtnClicked() {
        finish();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
