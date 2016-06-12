package com.yuri.cnbeta.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment implements ListFragmentExtra{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void goTop() {

    }
}
