package com.example.android.multmedia.player;

import android.media.MediaPlayer;
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
import com.mediaload.bean.VideoItem;

import java.util.ArrayList;

import static com.example.android.multmedia.player.MediaPlayConstants.*;

public class VideoPlayerActivity extends BaseActivity<MediaControlImpl> implements IMediaView , View.OnClickListener{
    private final static String TAG = VideoPlayerActivity.class.getSimpleName();

    private VideoView videoPlayer;
    private TextView tvSystemTime;
    private TextView tvVideoName;
    private TextView tvPlayTime;
    private TextView tvVideoDuration;
    private SeekBar sbPosition;
    private View viewAlpha;

    private ImageButton ibReturn;
    private ImageButton ibPlayMode;
    private ImageButton ibPre;
    private ImageButton ibPlay;
    private ImageButton ibNext;
    private ImageButton ibFavorite;

    private RelativeLayout rlTopBar;
    private LinearLayout llBottomBar;

    private float startY;
    private float startX;
    private float currentAlpha;
    private int screenWidth;
    private int screenHeight;
    private int videoWidth;
    private int videoHeight;
    private int topHeight;
    private int bottomHeight;
    private int videoPosition;

    private boolean isFullScreen = true;
    private boolean isFavorite = false;
    private boolean isTopBottomBarShow = false;
    private volatile int playMode = SEQUENCE_PLAY; //default play mode.

    private ArrayList<VideoItem> videoList;
    private VideoItem video;
    private VideoItem currentVideo;

    private MediaControlImpl mediaControl;
    private GestureDetector gestureDetector;


    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Log.d(TAG, "Main Thread Handle Msg = " + msg.what + ", Msg.arg1 = " + msg.arg1);
            switch (msg.what) {
                case MSG_UPDATE_TIME:
                    updateTime();
                    break;
                case MSG_UPDATE_PROGRESS:
                    updateProgress();
                    break;
                case MSG_SHOW_HIDE_BAR:
                    hideTopBottomBar(topHeight, bottomHeight);
                    break;
                case MSG_UPDATE_CONTROL_BAR:
                    if(msg.arg1 == PLAY_STATE_PLAYING) {
                        if(msg.obj != null) {
                            currentVideo = (VideoItem) msg.obj;
                            tvVideoDuration.setText(StringUtils.formatMediaTime(currentVideo.getDuration()));
                            //获取时长,实时更新播放进度
                            sbPosition.setMax((int)currentVideo.getDuration());
                        }
                        ibPlay.setImageResource(R.drawable.btn_pause_normal);
                        updateProgress();
                    }else if (msg.arg1 == PLAY_STATE_PAUSE) {
                        ibPlay.setImageResource(R.drawable.btn_play_normal);
                    }else if (msg.arg1 == PLAY_STATE_END) {
                        handler.removeMessages(MSG_UPDATE_PROGRESS);
                        ibPlay.setImageResource(R.drawable.btn_play_normal);
                        sbPosition.setProgress(sbPosition.getMax());
                        tvPlayTime.setText(StringUtils.formatMediaTime(videoPlayer.getDuration()));
                    }

                    if(mediaControl.isFavorite()) {
                        ibFavorite.setImageResource(R.drawable.btn_favorite_pressed);
                    }else {
                        ibFavorite.setImageResource(R.drawable.btn_favorite_normal);
                    }
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

    public void updateSeekBar(int progress) {
        sbPosition.setSecondaryProgress(progress);
    }

    public void mediaPlayerPrepared() {

    }

    public int getPlayMode() {
        return playMode;
    }

    @Override
    public MediaControlImpl attachMediaView() {
        Log.d(TAG, "attachMediaView");
        if (mediaControl == null) {
            mediaControl = new MediaControlImpl(this, MediaType.VIDEO);
            videoPlayer = mediaControl.getVideoPlayer();
        }
        return mediaControl;
    }

    @Override
    public void initView() {
        Log.d(TAG, "initView");
        tvSystemTime = (TextView) findViewById(R.id.tv_system_time);
        tvVideoName = (TextView) findViewById(R.id.tv_video_name);
        tvPlayTime = (TextView) findViewById(R.id.tv_av_playtime);
        tvVideoDuration = (TextView) findViewById(R.id.tv_av_duration);
        sbPosition = (SeekBar)findViewById(R.id.sb_position);
        viewAlpha = (View) findViewById(R.id.view_alpha);

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
        getVideoDataFromIntent();
        initPreAndNext();
        initSeekBarListener();
        initScreenHW();
        mediaControl.setVideoPlayerListener(videoList);

        /*start play the video*/
        mediaControl.setVideoPath(video.getPath(), videoPosition);
        gestureDetector = new GestureDetector(this, new GestureListener());
        handler.sendEmptyMessageDelayed(MSG_SHOW_HIDE_BAR, FIVE_SECOND_TIMER);
        Log.d(TAG, "init Data Exit");
    }

    private void getVideoDataFromIntent() {
        /*get video list and selected video*/
        videoList = (ArrayList<VideoItem>)getIntent().getSerializableExtra(INTENT_VIDEO_LIST);
        videoPosition = getIntent().getIntExtra(INTENT_MEDIA_POSITION, 0);
        video = videoList.get(videoPosition);
        Log.d(TAG, "Video Position = " + videoPosition);

        if(video != null) {
            Log.d(TAG, "Video Duration = " + video.getDuration());

            tvVideoName.setText(video.getDisplayName());
            tvVideoDuration.setText(StringUtils.formatMediaTime(video.getDuration()));
        }
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
                        videoPlayer.seekTo(progress);
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
            handler.sendEmptyMessageDelayed(MSG_SHOW_HIDE_BAR, FIVE_SECOND_TIMER);
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
        handler.sendEmptyMessageDelayed(MSG_UPDATE_TIME, ONE_SECOND_TIMER);
    }

    private void updateProgress() {
        Log.d(TAG, "Current played time = " + videoPlayer.getCurrentPosition());
        tvPlayTime.setText(StringUtils.formatMediaTime(videoPlayer.getCurrentPosition()));
        sbPosition.setProgress(videoPlayer.getCurrentPosition());
        handler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, ONE_SECOND_TIMER);
    }

    /*Media Play Control Bar Listener*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_playmode:
                if(playMode == SEQUENCE_PLAY) {
                    playMode = SINGLE_PLAY;
                    ibPlayMode.setImageResource(R.drawable.btn_playmode_singlerepeat_normal);
                }else {
                    playMode = SEQUENCE_PLAY;
                    ibPlayMode.setImageResource(R.drawable.btn_playmode_all_repeat_normal);
                }
                break;
            case R.id.ib_pre:
                mediaControl.playPreMedia();
                break;
            case R.id.ib_playpause:
                if(mediaControl.isPlaying()) {
                    mediaControl.pauseMedia();
                    ibPlay.setImageResource(R.drawable.btn_play_normal);
                    handler.removeMessages(MSG_UPDATE_PROGRESS);
                }else {
                    mediaControl.playMedia();
                    ibPlay.setImageResource(R.drawable.btn_pause_normal);
                    updateProgress();
                }
                break;
            case R.id.ib_next:
                mediaControl.playNextMedia();
                break;
            case R.id.ib_favorite:
                if(isFavorite == true) {
                    ibFavorite.setImageResource(R.drawable.btn_favorite_normal);
                }else {
                    ibFavorite.setImageResource(R.drawable.btn_favorite_pressed);
                }
                isFavorite = !isFavorite;
                mediaControl.setMediaFavorite(isFavorite);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                currentAlpha = viewAlpha.getAlpha();
                break;
            case MotionEvent.ACTION_MOVE:
                float alpha = (float) (currentAlpha + (startY - event.getY()) / 100 * 0.1);
                if (alpha > 0.8f) {
                    currentAlpha = 0.8f;
                } else if (alpha < 0.0f) {
                    currentAlpha = 0.0f;
                } else {
                    currentAlpha = alpha;
                }
                viewAlpha.setAlpha(currentAlpha);
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public int getLayoutResID() {
        return R.layout.activity_video_player;
    }

    @Override
    public void showTopBottomBar() {
        rlTopBar.setTranslationY(0);
        llBottomBar.setTranslationY(0);
        handler.sendEmptyMessageDelayed(MSG_SHOW_HIDE_BAR, FIVE_SECOND_TIMER);
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
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        handler = null;
        mediaControl.resetMediaData();
        super.onDestroy();
    }
}
