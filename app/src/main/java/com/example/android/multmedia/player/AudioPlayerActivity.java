package com.example.android.multmedia.player;

import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.android.multmedia.R;
import com.example.android.multmedia.player.mvp.BaseActivity;
import com.example.android.multmedia.player.mvp.IMediaView;
import com.example.android.multmedia.player.mvp.MediaControlImpl;
import com.example.android.multmedia.utils.StringUtils;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.VideoItem;

import java.util.ArrayList;

import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_MEDIA_POSITION;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_VIDEO_LIST;
import static com.example.android.multmedia.player.MediaPlayConstants.MSG_UPDATE_PROGRESS;
import static com.example.android.multmedia.player.MediaPlayConstants.SEQUENCE_PLAY;
import static com.example.android.multmedia.player.MediaPlayConstants.SINGLE_PLAY;

public class AudioPlayerActivity extends BaseActivity<MediaControlImpl> implements IMediaView, View.OnClickListener {
    private final static String TAG = AudioPlayerActivity.class.getSimpleName();
    private ImageButton ibReturn;
    private ImageButton ibPlayMode;
    private ImageButton ibPre;
    private ImageButton ibPlay;
    private ImageButton ibNext;
    private ImageButton ibFavorite;

    private RelativeLayout rlTopBar;
    private LinearLayout llBottomBar;


    private AudioItem audio;
    private AudioItem currentAudio;
    private ArrayList<AudioItem> audioList;
    private int position;

    private volatile int playMode = SEQUENCE_PLAY; //default play mode.
    private boolean isFavorite = false;


    private MediaControlImpl mediaControl;

    public MediaControlImpl attachMediaView() {
        if(mediaControl != null) {
            mediaControl = new MediaControlImpl(this, MediaPlayConstants.MediaType.AUDIO);
        }
        return mediaControl;
    }

    @Override
    public void initView() {
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

    }

    @Override
    public void initData() {
        getAudioDataFromIntent();

    }

    private void getAudioDataFromIntent() {
        /*get video list and selected video*/
        audioList = (ArrayList<AudioItem>)getIntent().getSerializableExtra(INTENT_VIDEO_LIST);
        position = getIntent().getIntExtra(INTENT_MEDIA_POSITION, 0);
        audio = audioList.get(position);
        Log.d(TAG, "Video Position = " + position);

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
}
