package com.example.android.multmedia.playedlist;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.BrowserRvAdapter;
import com.example.android.multmedia.base.BaseBrowserActivity;
import com.example.android.multmedia.browser.AudioBrowserActivity;
import com.example.android.multmedia.notification.INotificationListener;
import com.example.android.multmedia.notification.NotificationHandler;
import com.example.android.multmedia.notification.NotificationInfoObject;
import com.example.android.multmedia.playedlist.personaldb.GreenDaoManager;
import com.example.android.multmedia.player.VideoPlayerActivity;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.VideoItem;

import java.util.ArrayList;
import java.util.Observable;

import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_AUDIO_LIST;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_MEDIA_POSITION;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_PHOTO_LIST;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_VIDEO_LIST;
import static com.example.android.multmedia.player.MediaPlayConstants.UPDATE_AUDIO_DATA;
import static com.example.android.multmedia.player.MediaPlayConstants.UPDATE_PHOTO_DATA;
import static com.example.android.multmedia.player.MediaPlayConstants.UPDATE_VIDEO_DATA;
import static com.example.android.multmedia.utils.Constant.AUDIO_LOADED_COMPLETED_ID;
import static com.example.android.multmedia.utils.Constant.PHOTO_LOADED_COMPLETED_ID;
import static com.example.android.multmedia.utils.Constant.VIDEO_LOADED_COMPLETED_ID;

public class FavoriteActivity extends BaseBrowserActivity implements INotificationListener{
    private final static String TAG = FavoriteActivity.class.getSimpleName();
    private GreenDaoManager daoManager = GreenDaoManager.getSingleInstance();
    private LoadMediaTask loadMediaTask = new LoadMediaTask();
    private int total = 0;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "msg id = " + msg.what);
            switch (msg.what) {
                case UPDATE_VIDEO_DATA:
                    mVideoRvAdapter.notifyDataSetChanged();
                    break;
                case UPDATE_PHOTO_DATA:
                    mPhotoRvAdapter.notifyDataSetChanged();
                    break;
                case UPDATE_AUDIO_DATA:
                    mAudioRvAdapter.notifyDataSetChanged();
                    break;
            }
            total = mVideoList.size() + mPhotoList.size() + mAudioList.size();
            //update total favorite media num.
            mTvMediaNum.setText("" + total);
        }
    };

    @Override
    public int getLayoutResID() {
        return R.layout.activity_favorite;
    }

    @Override
    public void initView() {
        loadRvLayout();
        initTvView();
        initVideoRv();
        initPhotoRv();
        initAudioRv();

        NotificationHandler.getInstance().registerForNotification(this);
        progressDialog = new ProgressDialog(FavoriteActivity.this);
        /*start load favorite media*/
        loadMediaTask.execute();
    }

    private void initTvView() {
        mTvMediaTitle.setText("Favorite");
        mTvVideo.setText("Most Favorite Video Files");
        mTvPhoto.setText("Most Favorite Photo Files");
        mTvAudio.setText("Most Favorite Audio Files");
    }

    @Override
    public void update(Observable observable, Object data) {
        Log.d(TAG, "update: action " + ((NotificationInfoObject) data).actionID + ", message " + ((NotificationInfoObject) data).message);
        final int l_eventID = ((NotificationInfoObject) data).actionID;

        switch (l_eventID) {
            case VIDEO_LOADED_COMPLETED_ID:
                mHandler.sendEmptyMessage(UPDATE_VIDEO_DATA);
                break;
            case AUDIO_LOADED_COMPLETED_ID:
                mHandler.sendEmptyMessage(UPDATE_AUDIO_DATA);
                break;
            case PHOTO_LOADED_COMPLETED_ID:
                mHandler.sendEmptyMessage(UPDATE_PHOTO_DATA);
                break;
        }
    }


    private class LoadMediaTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            showProgressLoading();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            hideProgressLoading();
            if(total == 0) {
                showMediaToast("No favorite media file, Maybe you can select favorite media file first");
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            mVideoList.addAll(daoManager.getFavoriteVideo());
            mPhotoList.addAll(daoManager.getFavoritePhoto());
            mAudioList.addAll(daoManager.getFavoriteAudio());

            Log.d(TAG, "video_size = " + mVideoList.size() +
                    ", photo_size = " + mPhotoList.size() +
                    ", audio_size = " + mAudioList.size());
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
