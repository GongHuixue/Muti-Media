package com.example.android.multmedia.player.videoplayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.example.android.multmedia.R;
import com.example.android.multmedia.videobrowser.VideoBrowser;
import com.example.android.multmedia.videobrowser.VideoBrowserAdapter;

import java.util.ArrayList;

public class VideoPlayerActivity extends Activity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private VideoBrowser videoBrowser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        initView();
//        mRecyclerView = (RecyclerView) findViewById(R.id.video_recycler_view);
//        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new VideoBrowserAdapter(getData());
//        mRecyclerView.setAdapter(mAdapter);
    }

    public void initView() {
        videoBrowser = VideoBrowser.getVbInstance(VideoPlayerActivity.this);
        videoBrowser.startLoader();
    }

    private ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        String temp = " Item ";

        for (int i = 0; i < 50; i++ ) {
            data.add(temp + i);
        }
        return data;
    }
}
