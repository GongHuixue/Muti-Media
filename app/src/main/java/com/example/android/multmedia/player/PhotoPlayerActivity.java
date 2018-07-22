package com.example.android.multmedia.player;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.android.multmedia.R;
import com.example.android.multmedia.base.BaseBrowserActivity;
import com.example.android.multmedia.player.mvp.BaseActivity;
import com.example.android.multmedia.player.mvp.IMediaView;
import com.example.android.multmedia.player.mvp.MediaControlImpl;
import com.example.android.multmedia.utils.StringUtils;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.PhotoItem;
import com.xiuyukeji.pictureplayerview.PicturePlayerView;

import java.util.ArrayList;

import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_MEDIA_POSITION;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_PHOTO_LIST;
import static com.example.android.multmedia.player.MediaPlayConstants.MSG_UPDATE_CONTROL_BAR;
import static com.example.android.multmedia.player.MediaPlayConstants.MSG_UPDATE_PROGRESS;
import static com.example.android.multmedia.player.MediaPlayConstants.PLAY_STATE_END;
import static com.example.android.multmedia.player.MediaPlayConstants.PLAY_STATE_PAUSE;
import static com.example.android.multmedia.player.MediaPlayConstants.PLAY_STATE_PLAYING;
import static com.example.android.multmedia.player.MediaPlayConstants.SEQUENCE_PLAY;

public class PhotoPlayerActivity extends BaseActivity<MediaControlImpl> implements IMediaView, View.OnClickListener{
    private final static String TAG = PhotoPlayerActivity.class.getSimpleName();
    private ImageButton ibReturn;
    private ImageButton ibPlay;
    private ImageButton ibFavorite;

    private boolean isFavorite = false;
    private int playMode = SEQUENCE_PLAY;
    private PicturePlayerView photoPlayer;
    private MediaControlImpl mediaControl;

    private ArrayList<PhotoItem> photoList;
    private PhotoItem currentPhoto;
    private int position;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_photo_player;
    }

    @Override
    public void initView() {
        ibReturn = (ImageButton) findViewById(R.id.ib_back);
        ibPlay = (ImageButton) findViewById(R.id.ib_playpause);
        ibFavorite = (ImageButton) findViewById(R.id.ib_favorite);

        ibReturn.setOnClickListener(this);
        ibPlay.setOnClickListener(this);
        ibFavorite.setOnClickListener(this);
    }

    @Override
    public void initData() {
        getPhotoDataFromIntent();
    }

    public void getPhotoDataFromIntent() {
        photoList = (ArrayList<PhotoItem>)getIntent().getSerializableExtra(INTENT_PHOTO_LIST);
        position = getIntent().getIntExtra(INTENT_MEDIA_POSITION, 0);
        currentPhoto = photoList.get(position);

    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "Main Thread Handle Msg = " + msg.what + ", Msg.arg1 = " + msg.arg1);
            switch (msg.what) {
                case MSG_UPDATE_CONTROL_BAR:

                    if(msg.arg1 == PLAY_STATE_PLAYING) {
                        if(msg.obj != null) {
                        }
                        ibPlay.setImageResource(R.drawable.btn_pause_normal);

                    }else if (msg.arg1 == PLAY_STATE_PAUSE) {
                        ibPlay.setImageResource(R.drawable.btn_play_normal);
                    }else if (msg.arg1 == PLAY_STATE_END) {
                        handler.removeMessages(MSG_UPDATE_PROGRESS);
                        ibPlay.setImageResource(R.drawable.btn_play_normal);
                    }
                    break;
            }
        }
    };

    public Handler getMainThreadHandler() {
        return handler;
    }

    @Override
    public MediaControlImpl attachMediaView() {
        if(mediaControl == null) {
            mediaControl = new MediaControlImpl(this, MediaPlayConstants.MediaType.PHOTO);
            photoPlayer = mediaControl.getPhotoPlayer();
        }
        return mediaControl;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                finish();
                break;
            case R.id.ib_playpause:
                if (mediaControl.isPlaying()) {
                    ibPlay.setImageResource(R.drawable.btn_pause_normal);
                    mediaControl.pauseMedia();
                } else {
                    ibPlay.setImageResource(R.drawable.btn_play_normal);
                    mediaControl.playMedia();
                }
                break;
            case R.id.ib_favorite:
                if (isFavorite == false) {
                    ibFavorite.setImageResource(R.drawable.btn_favorite_normal);
                } else {
                    ibFavorite.setImageResource(R.drawable.btn_favorite_pressed);
                }
                isFavorite = !isFavorite;
                break;
        }
    }


    @Override
    public void showTopBottomBar() {

    }
    @Override
    public void hideTopBottomBar(int topHeight, int bottomHeight) {

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

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        handler = null;
        mediaControl.resetMediaData();
        super.onDestroy();
    }
}
