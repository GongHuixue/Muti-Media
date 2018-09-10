package com.example.android.multmedia.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class BasePlayerActivity extends FragmentActivity {
    private FragmentActivity activity;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        initView();
        activity = this;
    }

    public abstract int getLayoutResID();

    public abstract void initView();

//    public abstract void initMediaPlayer();
}
