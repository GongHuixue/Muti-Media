package com.example.android.multmedia.player;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.RecyclerViewAdapter;
import com.mediaload.bean.VideoItem;
import com.mediaload.bean.VideoResult;
import com.mediaload.callback.OnVideoLoadCallBack;

import java.util.ArrayList;
import java.util.List;

public class VideoPlayerPlayerActivity extends BasePlayerActivity {
    private RecyclerViewAdapter<VideoItem> mVideoRvAdapter;
    private List<VideoItem> mVideoItems = new ArrayList<>();

    @Override
    public int getLayoutResID(){
        return R.layout.activity_video_player;
    }

    @Override
    public void initView() {
        final TextView videoNum = (TextView) findViewById(R.id.video_num);
        mRecyclerView = (RecyclerView) findViewById(R.id.video_recycler_view);
        mediaLoad.loadVideos(VideoPlayerPlayerActivity.this, new OnVideoLoadCallBack() {
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

        mVideoRvAdapter = new RecyclerViewAdapter<VideoItem>(mVideoItems, VideoPlayerPlayerActivity.this) {
            @Override
            public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
                VideoItem mVideo = mVideoItems.get(position);
                Glide.with(VideoPlayerPlayerActivity.this)
                        .load("file://" + mVideo.getPath())
                        .thumbnail(0.1f)
                        .into(holder.mImageView);
//                holder.mImageView.setImageResource(R.drawable.ic_tab_video);
//                holder.mTextView.setText("Video");
            }
        };
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        /*add horizontal and vertical divider line*/
        verticalDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mRecyclerView.addItemDecoration(verticalDivider);
        mRecyclerView.addItemDecoration(horizontalDivider);

        mRecyclerView.setAdapter(mVideoRvAdapter);
    }

}
