package com.example.android.multmedia.browser;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.BrowserRvAdapter;
import com.mediaload.bean.VideoItem;
import com.mediaload.bean.VideoResult;
import com.mediaload.callback.OnVideoLoadCallBack;

import java.util.ArrayList;
import java.util.List;

public class VideoBrowserActivity extends BaseBrowserActivity {
    private BrowserRvAdapter<VideoItem> mVideoRvAdapter;
    private List<VideoItem> mVideoItems = new ArrayList<>();

    @Override
    public int getLayoutResID(){
        return R.layout.activity_video_browser;
    }

    @Override
    public void initView() {
        final TextView videoNum = (TextView) findViewById(R.id.video_num);
        mRecyclerView = (RecyclerView) findViewById(R.id.video_recycler_view);
        mediaLoad.loadVideos(VideoBrowserActivity.this, new OnVideoLoadCallBack() {
            @Override
            public void onResult(VideoResult result) {
                videoNum.setText("Video Files: " + result.getItems().size());
                if(result.getItems().size() > 0) {
                    mVideoItems.clear();
                    mVideoItems.addAll(result.getItems());
                    mVideoRvAdapter.notifyDataSetChanged();
                }
            }
        });

        mVideoRvAdapter = new BrowserRvAdapter<VideoItem>(mVideoItems, VideoBrowserActivity.this) ;
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        /*add horizontal and vertical divider line*/
        verticalDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mRecyclerView.addItemDecoration(verticalDivider);
        mRecyclerView.addItemDecoration(horizontalDivider);

        mRecyclerView.setAdapter(mVideoRvAdapter);
    }

}
