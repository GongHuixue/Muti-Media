package com.example.android.multmedia.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.BrowserRvAdapter;
import com.example.android.multmedia.playedlist.FavoriteActivity;
import com.example.android.multmedia.playedlist.personaldb.BrowserMediaFile;
import com.example.android.multmedia.player.AudioPlayerActivity;
import com.example.android.multmedia.player.PhotoPlayerActivity;
import com.example.android.multmedia.player.VideoPlayerActivity;
import com.mediaload.MediaLoad;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.VideoItem;

import java.util.ArrayList;

import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_AUDIO_LIST;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_MEDIA_POSITION;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_PHOTO_LIST;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_VIDEO_LIST;

public abstract class BaseBrowserActivity extends FragmentActivity {
    private Context mContext;
    public ProgressDialog progressDialog;
    /*mediaload instance, user for load audio/video/picture*/
    public MediaLoad mediaLoad = MediaLoad.getMediaLoad();
    public BrowserMediaFile browserMediaFile = new BrowserMediaFile();


    public RecyclerView mRecyclerView;

    /*These are used for played list activity*/
    public RecyclerView mVideoRv, mAudioRv, mPhotoRv;
    protected ImageButton IbReturn;
    protected LinearLayout linearLayout;
    protected LayoutInflater inflater;
    protected TextView mTvVideo, mTvPhoto, mTvAudio, mTvMediaTitle, mTvMediaNum;

    public BrowserRvAdapter<VideoItem> mVideoRvAdapter;
    public ArrayList<VideoItem> mVideoList = new ArrayList<>();
    public BrowserRvAdapter<PhotoItem> mPhotoRvAdapter;
    public ArrayList<PhotoItem> mPhotoList = new ArrayList<>();
    public BrowserRvAdapter<AudioItem> mAudioRvAdapter;
    public ArrayList<AudioItem> mAudioList = new ArrayList<>();

    /*recycler view divider*/
    public DividerItemDecoration verticalDivider;
    public DividerItemDecoration horizontalDivider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        mContext = this;
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

    public void initVideoRv() {
        /*init Video Recycle View*/
        mVideoRvAdapter = new BrowserRvAdapter<VideoItem>(mVideoList, mContext);
        mVideoRv.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));

        /*short click*/
        mVideoRvAdapter.setOnItemClickListener(new BrowserRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, VideoPlayerActivity.class);
                intent.putExtra(INTENT_MEDIA_POSITION, position);
                intent.putExtra(INTENT_VIDEO_LIST, mVideoList);

                startActivity(intent);            }
        });
        /*long click*/
        mVideoRvAdapter.setOnItemLongClickListener(new BrowserRvAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(mContext, "long click " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        verticalDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mVideoRv.addItemDecoration(horizontalDivider);
        mVideoRv.addItemDecoration(verticalDivider);
        mVideoRv.setAdapter(mVideoRvAdapter);
    }

    public void initPhotoRv() {
        /*init Photo Recycle View*/
        mPhotoRvAdapter = new BrowserRvAdapter<PhotoItem>(mPhotoList, mContext);
        mPhotoRv.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        /*short click*/
        mPhotoRvAdapter.setOnItemClickListener(new BrowserRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, PhotoPlayerActivity.class);
                intent.putExtra(INTENT_MEDIA_POSITION, position);
                intent.putExtra(INTENT_PHOTO_LIST, mPhotoList);

                startActivity(intent);
            }
        });
        /*long click*/
        mPhotoRvAdapter.setOnItemLongClickListener(new BrowserRvAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(mContext, "long click " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        verticalDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mPhotoRv.addItemDecoration(horizontalDivider);
        mPhotoRv.addItemDecoration(verticalDivider);
        mPhotoRv.setAdapter(mPhotoRvAdapter);
    }

    public void initAudioRv() {
        /*init Audio Recycle View*/
        mAudioRvAdapter = new BrowserRvAdapter<AudioItem>(mAudioList, mContext);
        mAudioRv.setLayoutManager(new LinearLayoutManager(mContext));

        /*short click*/
        mAudioRvAdapter.setOnItemClickListener(new BrowserRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, AudioPlayerActivity.class);
                intent.putExtra(INTENT_MEDIA_POSITION, position);
                intent.putExtra(INTENT_AUDIO_LIST, mAudioList);

                startActivity(intent);
            }
        });
        /*long click*/
        mAudioRvAdapter.setOnItemLongClickListener(new BrowserRvAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(mContext, "long click " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mAudioRv.addItemDecoration(horizontalDivider);
        mAudioRv.setAdapter(mAudioRvAdapter);
    }

    public void loadRvLayout() {
        inflater = LayoutInflater.from(mContext);

        IbReturn = (ImageButton)findViewById(R.id.ib_back);
        IbReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTvMediaTitle = (TextView)findViewById(R.id.txt_title);
        mTvMediaNum = (TextView)findViewById(R.id.txt_number);

        linearLayout = (LinearLayout)findViewById(R.id.play_video_rv);
        mTvVideo = (TextView)findViewById(R.id.tv_video_submenu);
        mVideoRv = (RecyclerView)findViewById(R.id.video_rv);
        //inflater.inflate(R.layout.played_video_rv, linearLayout, true);

        linearLayout = (LinearLayout)findViewById(R.id.play_photo_rv);
        mTvPhoto = (TextView)findViewById(R.id.tv_photo_submenu);
        mPhotoRv = (RecyclerView)findViewById(R.id.photo_rv);
        //inflater.inflate(R.layout.played_photo_rv, linearLayout, true);

        linearLayout = (LinearLayout)findViewById(R.id.play_audio_rv);
        mTvAudio = (TextView)findViewById(R.id.tv_audio_submenu);
        mAudioRv = (RecyclerView)findViewById(R.id.audio_rv);
        //inflater.inflate(R.layout.played_audio_rv, linearLayout, true);
    }

    public abstract int getLayoutResID();
    public abstract void initView();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mVideoList != null) {
            mVideoList.clear();
        }
        if(mPhotoList != null) {
            mPhotoList.clear();
        }
        if(mAudioList != null) {
            mAudioList.clear();
        }
        mContext = null;
    }
}
