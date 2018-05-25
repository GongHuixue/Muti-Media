package com.example.android.multmedia.player;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.mediaload.MediaLoad;

public abstract class BasePlayerActivity extends FragmentActivity {
    public MediaLoad mediaLoad = MediaLoad.getMediaLoad();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        initView();
    }

    public abstract int getLayoutResID();
    public abstract void initView();
}
