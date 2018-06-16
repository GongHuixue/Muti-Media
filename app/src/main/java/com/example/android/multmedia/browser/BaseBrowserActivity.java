package com.example.android.multmedia.browser;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.mediaload.MediaLoad;

public abstract class BaseBrowserActivity extends FragmentActivity {
    /*mediaload instance, user for load audio/video/picture*/
    public MediaLoad mediaLoad = MediaLoad.getMediaLoad();

    public RecyclerView mRecyclerView;
    /*recycler view divider*/
    public DividerItemDecoration verticalDivider;
    public DividerItemDecoration horizontalDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        initView();
    }

    public abstract int getLayoutResID();
    public abstract void initView();
}
