package com.example.android.multmedia.playedlist;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.BrowserRvAdapter;
import com.example.android.multmedia.base.BaseBrowserActivity;
import com.example.android.multmedia.notification.INotificationListener;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.VideoItem;

import java.util.ArrayList;
import java.util.Observable;

public class FavoriteActivity extends BaseBrowserActivity implements INotificationListener{

    @Override
    public int getLayoutResID() {
        return R.layout.activity_favorite;
    }

    @Override
    public void initView() {
        mVideoRv = (RecyclerView)findViewById(R.id.video_rv);
        mPhotoRv = (RecyclerView)findViewById(R.id.photo_rv);
        mAudioRv = (RecyclerView)findViewById(R.id.music_rv);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
