package com.example.android.multmedia.player;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.videobrowser.VideoBrowser;
import com.mediaload.bean.VideoResult;
import com.mediaload.callback.OnVideoLoadCallBack;

import java.util.ArrayList;

public class VideoPlayerPlayerActivity extends BasePlayerActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private VideoBrowser videoBrowser;


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_player);
////        mRecyclerView = (RecyclerView) findViewById(R.id.video_recycler_view);
////        mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
////        mRecyclerView.setLayoutManager(mLayoutManager);
////        mAdapter = new VideoBrowserAdapter(getData());
////        mRecyclerView.setAdapter(mAdapter);
//    }

    @Override
    public int getLayoutResID(){
        return R.layout.activity_video_player;
    }

    @Override
    public void initView() {
        final TextView videoNum = (TextView) findViewById(R.id.video_num);
        mediaLoad.loadVideos(VideoPlayerPlayerActivity.this, new OnVideoLoadCallBack() {
            @Override
            public void onResult(VideoResult result) {
                videoNum.setText("Video Files: " + result.getItems().size());
            }
        });
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
