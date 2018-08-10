package com.example.android.multmedia.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.android.multmedia.adpter.BrowserRvAdapter;
import com.example.android.multmedia.playedlist.personaldb.BrowserMediaFile;
import com.mediaload.MediaLoad;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.VideoItem;

import java.util.ArrayList;

public abstract class BaseBrowserActivity extends FragmentActivity {
    /*mediaload instance, user for load audio/video/picture*/
    public MediaLoad mediaLoad = MediaLoad.getMediaLoad();
    public BrowserMediaFile bowserFile = new BrowserMediaFile();

    public RecyclerView mRecyclerView;

    /*These are used for played list activity*/
    public RecyclerView mVideoRv, mAudioRv, mPhotoRv;
    public TextView mediaTv;

    public BrowserRvAdapter<VideoItem> mVideoRvAdapter;
    public ArrayList<VideoItem> mVideoList;
    public BrowserRvAdapter<PhotoItem> mPhotoRvAdapter;
    public ArrayList<PhotoItem> mPhotoList;
    public BrowserRvAdapter<AudioItem> mAudioRvAdapter;
    public ArrayList<AudioItem> mAudioList;

    /*recycler view divider*/
    public DividerItemDecoration verticalDivider;
    public DividerItemDecoration horizontalDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        initView();
    }

    public void showActionBar() {

    }

    public void hideActionBar() {

    }

    public abstract int getLayoutResID();
    public abstract void initView();
}
