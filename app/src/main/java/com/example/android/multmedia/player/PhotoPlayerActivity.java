package com.example.android.multmedia.player;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.android.multmedia.R;
import com.example.android.multmedia.player.mvp.BaseActivity;
import com.example.android.multmedia.player.mvp.IMediaView;
import com.example.android.multmedia.player.mvp.MediaControlImpl;
import com.example.android.multmedia.player.photo.PhotoViewAdapter;
import com.example.android.multmedia.player.photo.PhotoViewPager;
import com.mediaload.bean.PhotoItem;
import com.xiuyukeji.pictureplayerview.PicturePlayerView;

import java.util.ArrayList;

import static com.example.android.multmedia.player.MediaPlayConstants.*;

public class PhotoPlayerActivity extends BaseActivity<MediaControlImpl> implements IMediaView, View.OnClickListener {
    private final static String TAG = PhotoPlayerActivity.class.getSimpleName();
    private ImageButton ibReturn;
    private ImageButton ibPlay;
    private ImageButton ibFavorite;

    private boolean isFavorite = false;
    private boolean isBottomBarShow = false;
    private int bottomHeight;
    private int playMode = SEQUENCE_PLAY;
    private PhotoViewPager photoPlayer;
    private MediaControlImpl mediaControl;

    private ArrayList<PhotoItem> photoList;
    private PhotoItem currentPhoto;
    private PhotoViewAdapter photoViewAdapter;
    private int position;

    private LinearLayout llBottomBar;
    private GestureDetector gestureDetector;

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

        llBottomBar = (LinearLayout) findViewById(R.id.ll_video_bottom);
        llBottomBar.measure(0, 0);
        bottomHeight = llBottomBar.getMeasuredHeight();
    }

    @Override
    public void initData() {
        getPhotoDataFromIntent();
        gestureDetector = new GestureDetector(this, new GestureListener());
        mediaControl.playMedia();

        photoViewAdapter = new PhotoViewAdapter(this, photoList);
        photoPlayer.setAdapter(photoViewAdapter);
        photoPlayer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                position = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        photoPlayer.setCurrentItem(position);
    }

    public void getPhotoDataFromIntent() {
        photoList = (ArrayList<PhotoItem>) getIntent().getSerializableExtra(INTENT_PHOTO_LIST);
        position = getIntent().getIntExtra(INTENT_MEDIA_POSITION, 0);
        currentPhoto = photoList.get(position);

    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.d(TAG, "Main Thread Handle Msg = " + msg.what + ", Msg.arg1 = " + msg.arg1);
            switch (msg.what) {
                case MSG_SHOW_HIDE_BAR:
                    if(isBottomBarShow == true) {
                        hideTopBottomBar(0, bottomHeight);
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
        if (mediaControl == null) {
            mediaControl = new MediaControlImpl(this, MediaPlayConstants.MediaType.PHOTO);
            //photoPlayer = mediaControl.getPhotoPlayer();
        }
        return mediaControl;
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (isBottomBarShow) {
                hideTopBottomBar(0, bottomHeight);
                handler.removeMessages(MSG_SHOW_HIDE_BAR);
            } else {
                showTopBottomBar();
            }
            return super.onSingleTapConfirmed(e);
        }
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
        isBottomBarShow = true;
        llBottomBar.setTranslationY(0);
        handler.sendEmptyMessageDelayed(MSG_SHOW_HIDE_BAR, CONTROL_BAR_UPDATE);
    }

    @Override
    public void hideTopBottomBar(int topHeight, int bottomHeight) {
        llBottomBar.setTranslationY(bottomHeight);
        isBottomBarShow = false;
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
