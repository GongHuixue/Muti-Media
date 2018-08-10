package com.example.android.multmedia.browser;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.BrowserRvAdapter;
import com.example.android.multmedia.base.BaseBrowserActivity;
import com.example.android.multmedia.player.VideoPlayerActivity;
import com.mediaload.bean.BaseItem;
import com.mediaload.bean.VideoItem;
import com.mediaload.bean.VideoResult;
import com.mediaload.callback.OnVideoLoadCallBack;

import java.util.ArrayList;

import static com.example.android.multmedia.utils.Constant.VIDEO_LIST;

import static com.example.android.multmedia.player.MediaPlayConstants.*;

public class VideoBrowserActivity extends BaseBrowserActivity {
    final static String TAG = VideoBrowserActivity.class.getSimpleName();
    private BrowserRvAdapter<VideoItem> mVideoRvAdapter;
    private ArrayList<VideoItem> mVideoItems = new ArrayList<>();
    private ArrayList<BaseItem> mTempList = new ArrayList<>();
    private TextView TvTitle;
    private TextView TvNum;
    private ImageButton IbReturn;

    @Override
    public int getLayoutResID(){
        return R.layout.activity_video_browser;
    }

    @Override
    public void initView() {
        TvTitle = (TextView)findViewById(R.id.txt_title);
        TvTitle.setText(R.string.video);
        TvNum = (TextView)findViewById(R.id.txt_number);
        IbReturn = (ImageButton)findViewById(R.id.ib_back);
        IbReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView = (RecyclerView) findViewById(R.id.video_recycler_view);
        mediaLoad.loadVideos(VideoBrowserActivity.this, new OnVideoLoadCallBack() {
            @Override
            public void onResult(VideoResult result) {
                TvNum.setText("" + result.getItems().size());
                if(result.getItems().size() > 0) {
                    mVideoItems.clear();
                    mTempList.clear();
                    mVideoItems.addAll(result.getItems());
                    mTempList.addAll(result.getItems());
                    mVideoRvAdapter.notifyDataSetChanged();
                    browserMediaFile.saveMediaFile(mTempList, VIDEO_LIST);
                }
            }
        });

        mVideoRvAdapter = new BrowserRvAdapter<VideoItem>(mVideoItems, VideoBrowserActivity.this) ;
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        /*short click*/
        mVideoRvAdapter.setOnItemClickListener(new BrowserRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(VideoBrowserActivity.this, VideoPlayerActivity.class);
                intent.putExtra(INTENT_MEDIA_POSITION, position);
                intent.putExtra(INTENT_VIDEO_LIST, mVideoItems);

                startActivity(intent);

            }
        });

        /*long click*/
        mVideoRvAdapter.setOnItemLongClickListener(new BrowserRvAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(VideoBrowserActivity.this, "long click " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        /*add horizontal and vertical divider line*/
        verticalDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mRecyclerView.addItemDecoration(verticalDivider);
        mRecyclerView.addItemDecoration(horizontalDivider);

        mRecyclerView.setAdapter(mVideoRvAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoItems.clear();
        mTempList.clear();
    }
}
