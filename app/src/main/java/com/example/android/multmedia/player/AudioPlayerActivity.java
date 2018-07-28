package com.example.android.multmedia.player;

import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.player.audio.LcrView;
import com.example.android.multmedia.player.mvp.BaseActivity;
import com.example.android.multmedia.player.mvp.IMediaView;
import com.example.android.multmedia.player.mvp.MediaControlImpl;
import com.example.android.multmedia.utils.StringUtils;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.VideoItem;

import java.util.ArrayList;

import static com.example.android.multmedia.player.MediaPlayConstants.*;


public class AudioPlayerActivity extends BaseActivity<MediaControlImpl> implements IMediaView, View.OnClickListener {
    private final static String TAG = AudioPlayerActivity.class.getSimpleName();
    private TextView tvMusicName;
    private TextView tvMusicSinger;
    private TextView tvMusicPlayTime;
    private TextView tvMusicDuration;

    private ImageButton ibReturn;
    private ImageButton ibPlayMode;
    private ImageButton ibPre;
    private ImageButton ibPlay;
    private ImageButton ibNext;
    private ImageButton ibFavorite;
    private SeekBar sbPosition;
    private ImageView ivFrame;
    private AnimationDrawable animationDrawable;
    private LcrView tvLcr;

    private volatile int playMode = SEQUENCE_PLAY; //default play mode.
    private MediaPlayer audioPlayer;

    private AudioItem currentAudio;
    private ArrayList<AudioItem> audioList;
    private int position;

    private boolean isFavorite = false;


    private MediaControlImpl mediaControl;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "Main Thread Handle Msg = " + msg.what + ", Msg.arg1 = " + msg.arg1);
            switch (msg.what) {
                case MSG_UPDATE_PROGRESS:
                    updateProgress();
                    break;
                case MSG_UPDATE_CONTROL_BAR:
                    updateAudioInfo();
                    updateProgress();
                    if(msg.arg1 == PLAY_STATE_PLAYING) {
                        if(msg.obj != null) {
                            currentAudio = (AudioItem) msg.obj;
                            tvMusicDuration.setText(StringUtils.formatMediaTime(currentAudio.getDuration()));
                            //获取时长,实时更新播放进度
                            sbPosition.setMax((int)currentAudio.getDuration());
                        }
                        ibPlay.setImageResource(R.drawable.btn_pause_normal);
                        animationDrawable.start();
                    }else if (msg.arg1 == PLAY_STATE_PAUSE) {
                        ibPlay.setImageResource(R.drawable.btn_play_normal);
                        tvMusicPlayTime.setText(StringUtils.formatMediaTime(audioPlayer.getCurrentPosition()));
                        sbPosition.setProgress(audioPlayer.getCurrentPosition());
                        animationDrawable.stop();
                    }else if (msg.arg1 == PLAY_STATE_END) {
                        handler.removeMessages(MSG_UPDATE_PROGRESS);
                        ibPlay.setImageResource(R.drawable.btn_play_normal);
                        sbPosition.setProgress(sbPosition.getMax());
                        tvMusicPlayTime.setText(StringUtils.formatMediaTime(audioPlayer.getDuration()));
                        animationDrawable.stop();
                    }
                    break;
            }
        }
    };

    public Handler getMainThreadHandler() {
        return handler;
    }


    public MediaControlImpl attachMediaView() {
        if(mediaControl == null) {
            mediaControl = new MediaControlImpl(this, MediaType.AUDIO);
            audioPlayer = mediaControl.getAudioPlayer();
        }
        return mediaControl;
    }

    @Override
    public void initView() {
        tvMusicName = (TextView) findViewById(R.id.tv_audio_name);
        tvMusicSinger = (TextView) findViewById(R.id.tv_singer_name);
        tvMusicPlayTime = (TextView) findViewById(R.id.tv_av_playtime);
        tvMusicDuration = (TextView) findViewById(R.id.tv_av_duration);

        ibReturn = (ImageButton) findViewById(R.id.ib_back);
        ibPlayMode = (ImageButton) findViewById(R.id.ib_playmode);
        ibPre = (ImageButton) findViewById(R.id.ib_pre);
        ibPlay = (ImageButton) findViewById(R.id.ib_playpause);
        ibNext = (ImageButton) findViewById(R.id.ib_next);
        ibFavorite = (ImageButton) findViewById(R.id.ib_favorite);
        ivFrame = (ImageView) findViewById(R.id.iv_zhen);
        sbPosition = (SeekBar) findViewById(R.id.sb_position);
        tvLcr = (LcrView) findViewById(R.id.tv_lyric);

        ibReturn.setOnClickListener(this);
        ibPlayMode.setOnClickListener(this);
        ibPre.setOnClickListener(this);
        ibPlay.setOnClickListener(this);
        ibNext.setOnClickListener(this);
        ibFavorite.setOnClickListener(this);

        ivFrame.setImageResource(R.drawable.music_zhen);
        animationDrawable = (AnimationDrawable) ivFrame.getDrawable();
    }

    @Override
    public void initData() {
        getAudioDataFromIntent();
        initSeekBarListener();

        mediaControl.setAudioPlayerListener(audioList);
        /*start play music*/
        mediaControl.setAudioPath(currentAudio.getPath(), position);
    }

    private void getAudioDataFromIntent() {
        /*get video list and selected video*/
        audioList = (ArrayList<AudioItem>)getIntent().getSerializableExtra(INTENT_AUDIO_LIST);
        position = getIntent().getIntExtra(INTENT_MEDIA_POSITION, 0);
        currentAudio = audioList.get(position);
        Log.d(TAG, "audio Position = " + position);

        updateAudioInfo();
    }

    private void updateAudioInfo() {
        tvMusicDuration.setText(StringUtils.formatMediaTime(currentAudio.getDuration()));
        tvMusicName.setText(currentAudio.getDisplayName());
        tvMusicSinger.setText(currentAudio.getSinger());
    }

    private void initSeekBarListener() {
        /*set video seek bar change listener*/
        AudioSeekBarListener audioSeekBarListener = new AudioSeekBarListener();
        sbPosition.setOnSeekBarChangeListener(audioSeekBarListener);
        tvLcr.loadLrc("没有找到歌词文件。");
    }

    public void mediaPlayerPrepared() {
        
    }

    private void updateProgress() {
        Log.d(TAG, "Current played time = " + audioPlayer.getCurrentPosition());
        tvMusicPlayTime.setText(StringUtils.formatMediaTime(audioPlayer.getCurrentPosition()));
        sbPosition.setProgress(audioPlayer.getCurrentPosition());
        tvLcr.updateLrcView(audioPlayer.getCurrentPosition(), audioPlayer.getDuration());

        handler.sendEmptyMessageDelayed(MSG_UPDATE_PROGRESS, ONE_SECOND_TIMER);
    }


    public int getPlayMode() {
        return playMode;
    }
    private class AudioSeekBarListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            switch (seekBar.getId()) {
                case R.id.sb_position:
                    if(fromUser) {
                        audioPlayer.seekTo(progress);
                    }
                    break;
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
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
                }else {
                    mediaControl.playMedia();
                    ibPlay.setImageResource(R.drawable.btn_pause_normal);
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
                break;
        }
    }


    @Override
    public int getLayoutResID() {
        return R.layout.activity_audio_player;
    }

    @Override
    public void showTopBottomBar(){

    }
    @Override
    public void hideTopBottomBar(int top, int bottom) {

    }
    @Override
    public void showLoadingProgress() {
        showLoadingProgressDialog();
    }
    @Override
    public void hideLoadingProgress() {
        hideLoadingProgressDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(audioPlayer != null) {
            updateProgress();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        handler = null;
        audioPlayer = null;
        mediaControl.resetMediaData();
        super.onDestroy();
    }
}
