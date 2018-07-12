package com.example.android.multmedia.player;

import android.app.Activity;
import android.os.Bundle;

import com.example.android.multmedia.R;
import com.example.android.multmedia.base.BaseBrowserActivity;

public class VideoPlayerActivity extends BaseBrowserActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_video_player;
    }

    @Override
    public void initView() {

    }
}
