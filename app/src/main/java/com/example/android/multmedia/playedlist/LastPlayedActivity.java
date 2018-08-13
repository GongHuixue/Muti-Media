package com.example.android.multmedia.playedlist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.BrowserRvAdapter;
import com.example.android.multmedia.base.BaseBrowserActivity;
import com.example.android.multmedia.notification.INotificationListener;
import com.example.android.multmedia.notification.NotificationHandler;
import com.example.android.multmedia.notification.NotificationInfoObject;
import com.example.android.multmedia.playedlist.personaldb.GreenDaoManager;
import com.example.android.multmedia.player.VideoPlayerActivity;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.VideoItem;

import java.util.Observable;

import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_AUDIO_LIST;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_MEDIA_POSITION;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_PHOTO_LIST;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_VIDEO_LIST;
import static com.example.android.multmedia.utils.Constant.AUDIO_LOADED_COMPLETED_ID;
import static com.example.android.multmedia.utils.Constant.PHOTO_LOADED_COMPLETED_ID;
import static com.example.android.multmedia.utils.Constant.VIDEO_LOADED_COMPLETED_ID;

public class LastPlayedActivity extends BaseBrowserActivity implements INotificationListener{
    private final static String TAG = LastPlayedActivity.class.getSimpleName();
    private GreenDaoManager daoManager = GreenDaoManager.getSingleInstance();
    private LoadMediaTask loadMediaTask = new LoadMediaTask();
    private TextView mTvVideo, mTvPhoto, mTvAudio;
    @Override
    public int getLayoutResID() {
        return R.layout.activity_last_played;
    }

    @Override
    public void initView() {
        mTvVideo = (TextView)findViewById(R.id.media_tv_submenu);
        mVideoRv = (RecyclerView)findViewById(R.id.media_rv);
        mTvPhoto = (TextView)findViewById(R.id.media_tv_submenu);
        mPhotoRv = (RecyclerView)findViewById(R.id.media_rv);
        mTvAudio = (TextView)findViewById(R.id.media_tv_submenu);
        mAudioRv = (RecyclerView)findViewById(R.id.media_rv);

        mTvVideo.setText("Last Played Video Files");
        mTvPhoto.setText("Last Played Photo Files");
        mTvAudio.setText("Last Played Audio Files");
        progressDialog = new ProgressDialog(LastPlayedActivity.this);


        /*init Video Recycle View*/
        mVideoRvAdapter = new BrowserRvAdapter<VideoItem>(mVideoList, LastPlayedActivity.this);
        mVideoRv.setLayoutManager(new LinearLayoutManager(LastPlayedActivity.this));

        /*short click*/
        mVideoRvAdapter.setOnItemClickListener(new BrowserRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(LastPlayedActivity.this, VideoPlayerActivity.class);
                intent.putExtra(INTENT_MEDIA_POSITION, position);
                intent.putExtra(INTENT_VIDEO_LIST, mVideoList);

                startActivity(intent);            }
        });
        /*long click*/
        mVideoRvAdapter.setOnItemLongClickListener(new BrowserRvAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(LastPlayedActivity.this, "long click " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mVideoRv.addItemDecoration(horizontalDivider);
        mVideoRv.setAdapter(mVideoRvAdapter);

        /*init Photo Recycle View*/
        mPhotoRvAdapter = new BrowserRvAdapter<PhotoItem>(mPhotoList, LastPlayedActivity.this);
        mPhotoRv.setLayoutManager(new LinearLayoutManager(LastPlayedActivity.this));

        /*short click*/
        mPhotoRvAdapter.setOnItemClickListener(new BrowserRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(LastPlayedActivity.this, VideoPlayerActivity.class);
                intent.putExtra(INTENT_MEDIA_POSITION, position);
                intent.putExtra(INTENT_PHOTO_LIST, mPhotoList);

                startActivity(intent);            }
        });
        /*long click*/
        mPhotoRvAdapter.setOnItemLongClickListener(new BrowserRvAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(LastPlayedActivity.this, "long click " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mPhotoRv.addItemDecoration(horizontalDivider);
        mPhotoRv.setAdapter(mPhotoRvAdapter);

        /*init Audio Recycle View*/
        mAudioRvAdapter = new BrowserRvAdapter<AudioItem>(mAudioList, LastPlayedActivity.this);
        mAudioRv.setLayoutManager(new LinearLayoutManager(LastPlayedActivity.this));

        /*short click*/
        mAudioRvAdapter.setOnItemClickListener(new BrowserRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(LastPlayedActivity.this, VideoPlayerActivity.class);
                intent.putExtra(INTENT_MEDIA_POSITION, position);
                intent.putExtra(INTENT_AUDIO_LIST, mAudioList);

                startActivity(intent);            }
        });
        /*long click*/
        mAudioRvAdapter.setOnItemLongClickListener(new BrowserRvAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(LastPlayedActivity.this, "long click " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mAudioRv.addItemDecoration(horizontalDivider);
        mAudioRv.setAdapter(mAudioRvAdapter);

        NotificationHandler.getInstance().registerForNotification(this);
        showProgressLoading();
        /*start load favorite media*/
        loadMediaTask.execute();
    }


    @Override
    public void update(Observable observable, Object data) {
        Log.d(TAG, "update: action " + ((NotificationInfoObject) data).actionID + ", message " + ((NotificationInfoObject) data).message);
        final int l_eventID = ((NotificationInfoObject) data).actionID;

        hideProgressLoading();

        switch (l_eventID) {
            case VIDEO_LOADED_COMPLETED_ID:
                mVideoRvAdapter.notifyDataSetChanged();
                break;
            case AUDIO_LOADED_COMPLETED_ID:
                mPhotoRvAdapter.notifyDataSetChanged();
                mAudioRvAdapter.notifyDataSetChanged();
                break;
            case PHOTO_LOADED_COMPLETED_ID:
                mPhotoRvAdapter.notifyDataSetChanged();
                break;
        }
    }

    private class LoadMediaTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            mVideoList = daoManager.getLastPlayedVideo();
            mPhotoList = daoManager.getLastPlayedPhoto();
            mAudioList = daoManager.getLastPlayedAudio();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadMediaTask.cancel(true);
        NotificationHandler.getInstance().unregisterForNotification(this);
    }
}
