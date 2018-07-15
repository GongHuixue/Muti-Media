package com.example.android.multmedia.player;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.player.mvp.BaseActivity;
import com.example.android.multmedia.player.mvp.IMediaView;
import com.example.android.multmedia.player.mvp.MediaControlImpl;
import com.example.android.multmedia.utils.StringUtils;

public class VideoPlayerActivity extends BaseActivity<MediaControlImpl> implements IMediaView {
    private final static String TAG = VideoPlayerActivity.class.getSimpleName();

    private VideoView videoPlayer;
    private TextView tvSystemTime;
    private TextView tvVideoName;
    private TextView tvPlayTime;
    private TextView tvVideoDuration;
    private SeekBar sbPosition;

    private ImageButton ibReturn;
    private ImageButton ibPlayMode;
    private ImageButton ibPre;
    private ImageButton ibPlay;
    private ImageButton ibNext;
    private ImageButton ibFavorite;

    private int screenWidth;
    private int screenHeight;
    private int videoPosition;

    private MediaControlImpl mediaControl;

    private final static int MSG_UPDATE_TIME = 1;
    private final static int MSG_UPDATE_PROGRESS = 2;
    private final static int MSG_SHOW_HIDE_BAR = 3;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_UPDATE_TIME:
                    updateTime();
                    break;
                case MSG_UPDATE_PROGRESS:
                    updateProgress();
                    break;
                case MSG_SHOW_HIDE_BAR:
                    hideBottomTopBar();
            }
        }
    };

    @Override
    public MediaControlImpl attachMediaView() {
        if (mediaControl != null) {
            mediaControl = new MediaControlImpl(this);
            mediaControl.setVideoPlayer(videoPlayer);
        }
        return mediaControl;
    }

    @Override
    public void initView() {
        videoPlayer = (VideoView) findViewById(R.id.vv_video);
        tvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        tvVideoName = (TextView) findViewById(R.id.tv_video_name);
        tvPlayTime = (TextView) findViewById(R.id.tv_video_playtime);
        tvVideoDuration = (TextView) findViewById(R.id.tv_video_duration);

        ibReturn = (ImageButton) findViewById(R.id.ib_back);
        ibPlayMode = (ImageButton) findViewById(R.id.ib_playmode);
        ibPre = (ImageButton) findViewById(R.id.ib_pre);
        ibPlay = (ImageButton) findViewById(R.id.ib_playpause);
        ibNext = (ImageButton) findViewById(R.id.ib_next);
        ibFavorite = (ImageButton) findViewById(R.id.ib_favorite);

        initData();
    }

    public void initData() {
        Log.d(TAG, "init Data Enter");
        initPreAndNext();
        initSeekBarListener();
        initScreenHW();
        mediaControl.setVideoPlayerListener();
        Log.d(TAG, "init Data Exit");
    }

    private void initPreAndNext() {
        if(videoPosition == 0 ) {
            ibPre.setImageResource(R.drawable.btn_pre_normal);
            ibPre.setEnabled(false);
        }else {
            ibPre.setImageResource(R.drawable.btn_pre_pressed);
            ibPre.setEnabled(true);
        }

        if(videoPosition == -1) {
            ibNext.setImageResource(R.drawable.btn_next_normal);
            ibNext.setEnabled(false);
        }else {
            ibNext.setImageResource(R.drawable.btn_next_normal);
            ibNext.setEnabled(true);
        }
    }

    private void initSeekBarListener() {
        /*set video seek bar change listener*/
        VideoSeekBarListener videoSeekBarListener = new VideoSeekBarListener();
        sbPosition.setOnSeekBarChangeListener(videoSeekBarListener);
    }

    private void initScreenHW() {
        WindowManager windowManager = this.getWindowManager();
        DisplayMetrics outmetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outmetrics);

        screenWidth = outmetrics.widthPixels;
        screenHeight = outmetrics.heightPixels;
        Log.d(TAG, "width = " + screenWidth + ", height = " + screenHeight);
    }

    private class VideoSeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()) {
                case R.id.sb_position:
                    if(fromUser) {
                        mediaControl.getVideoPlayer().seekTo(progress);
                    }
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            handler.removeMessages(MSG_SHOW_HIDE_BAR);
        }


        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            handler.sendEmptyMessageAtTime(MSG_SHOW_HIDE_BAR, 3000);
        }
    }

    private void updateTime() {
        tvSystemTime.setText(StringUtils.getSystemTime());
        handler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, 1000);
    }

    private void updateProgress() {
        tvPlayTime.setText(StringUtils.formatMediaTime(mediaControl.getVideoPlayer().getCurrentPosition()));
        sbPosition.setProgress(mediaControl.getVideoPlayer().getCurrentPosition());
        handler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, 1000);

    }

    private void hideBottomTopBar() {

    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_audio_player;
    }

    @Override
    public void showTopBottomBar() {

    }

    @Override
    public void hideTopBottomBar() {

    }

    @Override
    public void showLoadingProgress() {
        showLoadingProgressDialog();
    }

    @Override
    public void hideLoadingProgress() {
        hideLoadingProgressDialog();
    }
}
