package com.example.android.multmedia.player;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.player.mvp.BaseActivity;
import com.example.android.multmedia.player.mvp.IMediaView;
import com.example.android.multmedia.player.mvp.MediaControlImpl;
import com.example.android.multmedia.utils.StringUtils;

import static com.example.android.multmedia.player.MediaPlayConstants.*;

public class VideoPlayerActivity extends BaseActivity<MediaControlImpl> implements IMediaView , View.OnClickListener{
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

    private RelativeLayout rlTopBar;
    private LinearLayout llBottomBar;

    private int screenWidth;
    private int screenHeight;
    private int videoWidth;
    private int videoHeight;
    private int topHeight;
    private int bottomHeight;
    private int videoPosition;

    private boolean isFullScreen = false;
    private boolean isTopBottomBarShow = false;

    private MediaControlImpl mediaControl;
    private GestureDetector gestureDetector;


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PLAY_PRE:
                    break;
                case PLAY:
                    break;
                case PAUSE:
                    break;
                case NEXT:
                    break;
                case MSG_UPDATE_TIME:
                    updateTime();
                    break;
                case MSG_UPDATE_PROGRESS:
                    updateProgress();
                    break;
                case MSG_SHOW_HIDE_BAR:
                    showTopBottomBar();
                    break;
                case MSG_UPDATE_CONTROL_BAR:
                    updateMediaControlBar();
                    break;
            }
        }
    };

    public Handler getMainThreadHandler() {
        return handler;
    }

    public void setVideoHW(int width, int height) {
        videoWidth = width;
        videoHeight = height;
    }

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

        ibReturn.setOnClickListener(this);
        ibPlayMode.setOnClickListener(this);
        ibPre.setOnClickListener(this);
        ibPlay.setOnClickListener(this);
        ibNext.setOnClickListener(this);
        ibFavorite.setOnClickListener(this);

        rlTopBar = (RelativeLayout) findViewById(R.id.rl_video_top);
        rlTopBar.measure(0, 0);
        topHeight = rlTopBar.getMeasuredHeight();
        llBottomBar = (LinearLayout) findViewById(R.id.ll_video_bottom);
        llBottomBar.measure(0,0);
        bottomHeight = llBottomBar.getMeasuredHeight();
    }

    @Override
    public void initData() {
        Log.d(TAG, "init Data Enter");
        initPreAndNext();
        initSeekBarListener();
        initScreenHW();
        mediaControl.setVideoPlayerListener();
        gestureDetector = new GestureDetector(this, new GestureListener());
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

    private void setVideoViewSize() {
        if(isFullScreen) {
            videoPlayer.getLayoutParams().width = videoWidth * screenHeight/videoHeight;
            videoPlayer.getLayoutParams().height = screenHeight;
        }else {
            videoPlayer.getLayoutParams().width = screenWidth;
            videoPlayer.getLayoutParams().height = screenHeight;
        }

        isFullScreen = !isFullScreen;
        videoPlayer.requestLayout();
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

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            setVideoViewSize();
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if(isTopBottomBarShow) {
                hideTopBottomBar(topHeight, bottomHeight);
                handler.removeMessages(MSG_SHOW_HIDE_BAR);
            }else {
                showTopBottomBar();
            }
            return super.onSingleTapConfirmed(e);
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

    private void updateMediaControlBar() {
        if(mediaControl.isPlaying() == true) {
            ibPlay.setImageResource(R.drawable.btn_pause_normal);
            tvVideoDuration.setText(""/*StringUtils.formatMediaTime(video.getTime())*/);
            //获取时长,实时更新播放进度
            sbPosition.setMax((int) 0 /*video.getTime()*/);
            updateProgress();
        }else {
            handler.removeMessages(MSG_UPDATE_PROGRESS);
            tvPlayTime.setText(""/*StringUtils.formatMediaTime(vvVideo.getDuration())*/);
            ibPlay.setImageResource(R.drawable.btn_play_normal);
            sbPosition.setProgress(sbPosition.getMax());
        }
    }

    /*Media Play Control Bar Listener*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                break;
            case R.id.ib_playmode:
                break;
            case R.id.ib_pre:
                break;
            case R.id.ib_playpause:
                break;
            case R.id.ib_next:
                break;
            case R.id.ib_favorite:
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_audio_player;
    }

    @Override
    public void showTopBottomBar() {
        rlTopBar.setTranslationY(0);
        llBottomBar.setTranslationY(0);
        isTopBottomBarShow = true;
    }

    @Override
    public void hideTopBottomBar(int topHeight, int bottomHeight) {
        rlTopBar.setTranslationY(-topHeight);
        llBottomBar.setTranslationY(bottomHeight);
        isTopBottomBarShow = false;
    }

    @Override
    public void showLoadingProgress() {

    }

    @Override
    public void hideLoadingProgress() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateTime();
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
