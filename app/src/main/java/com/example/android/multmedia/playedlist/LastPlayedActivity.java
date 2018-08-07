package com.example.android.multmedia.playedlist;

import android.app.Activity;
import android.os.Bundle;

import com.example.android.multmedia.R;
import com.example.android.multmedia.base.BaseBrowserActivity;

public class LastPlayedActivity extends BaseBrowserActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_played);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_last_played;
    }

    @Override
    public void initView() {

    }
}
