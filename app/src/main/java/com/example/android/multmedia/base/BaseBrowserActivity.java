package com.example.android.multmedia.base;

import android.app.ProgressDialog;
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

    public ProgressDialog progressDialog;
    /*mediaload instance, user for load audio/video/picture*/
    public MediaLoad mediaLoad = MediaLoad.getMediaLoad();
    public BrowserMediaFile browserMediaFile = new BrowserMediaFile();


    public RecyclerView mRecyclerView;

    /*These are used for played list activity*/
    public RecyclerView mVideoRv, mAudioRv, mPhotoRv;

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

    public void showProgressLoading() {
        progressDialog.setTitle("");
        progressDialog.setMessage("File is loading, please wait ......");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    public void hideProgressLoading() {
        progressDialog.hide();
    }

    public abstract int getLayoutResID();
    public abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.cancel();
        if(mVideoList != null) {
            mVideoList.clear();
        }
        if(mPhotoList != null) {
            mPhotoList.clear();
        }
        if(mAudioList != null) {
            mAudioList.clear();
        }
    }
}
