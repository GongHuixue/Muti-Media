package com.example.android.multmedia.playedlist;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.base.BaseBrowserActivity;

public class LastPlayedActivity extends BaseBrowserActivity {

    @Override
    public int getLayoutResID() {
        return R.layout.activity_last_played;
    }

    @Override
    public void initView() {
        mVideoRv = (RecyclerView)findViewById(R.id.video_rv);
        mPhotoRv = (RecyclerView)findViewById(R.id.photo_rv);
        mAudioRv = (RecyclerView)findViewById(R.id.music_rv);
    }
}
