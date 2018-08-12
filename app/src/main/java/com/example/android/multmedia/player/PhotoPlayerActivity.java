package com.example.android.multmedia.player;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.example.android.multmedia.R;
import com.example.android.multmedia.player.mvp.BaseActivity;
import com.example.android.multmedia.player.mvp.IMediaView;
import com.example.android.multmedia.player.mvp.MediaControlImpl;
import com.example.android.multmedia.player.photo.PhotoViewPager;
import com.github.chrisbanes.photoview.PhotoView;
import com.mediaload.bean.PhotoItem;

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
    private int currentPosition;

    private LinearLayout llBottomBar;

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

        llBottomBar = (LinearLayout) findViewById(R.id.ll_photo_bottom);
        llBottomBar.measure(0, 0);
        bottomHeight = llBottomBar.getMeasuredHeight();
    }

    @Override
    public void initData() {
        getPhotoDataFromIntent();
        photoViewInit();

        mediaControl.setPhotoListListener(photoList);

        //start play selected photo.
        mediaControl.setPhotoPath(currentPosition);
        showTopBottomBar();
        handler.sendEmptyMessageDelayed(MSG_SHOW_HIDE_BAR, FIVE_SECOND_TIMER);
    }

    private void getPhotoDataFromIntent() {
        photoList = (ArrayList<PhotoItem>) getIntent().getSerializableExtra(INTENT_PHOTO_LIST);
        currentPosition = getIntent().getIntExtra(INTENT_MEDIA_POSITION, 0);
        currentPhoto = photoList.get(currentPosition);
    }

    private void photoViewInit() {
        photoViewAdapter = new PhotoViewAdapter(this, photoList);
        photoPlayer.setAdapter(photoViewAdapter);
        photoPlayer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.d(TAG, "onPageScrolled " + position);
            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                mediaControl.setPhotoPath(currentPosition);
                /*update favorite icon*/
                if(mediaControl.isFavorite()) {
                    ibFavorite.setImageResource(R.drawable.btn_favorite_pressed);
                }else {
                    ibFavorite.setImageResource(R.drawable.btn_favorite_normal);
                }

                Log.d(TAG, "onPageSelected = " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.d(TAG, "onPageScrollStateChanged " + state);
            }
        });
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Log.d(TAG, "Main Thread Handle Msg = " + msg.what + ", Msg.arg1 = " + msg.arg1);
            switch (msg.what) {
                case MSG_SHOW_HIDE_BAR:
                    if(isBottomBarShow == true) {
                        hideTopBottomBar(0, bottomHeight);
                    }
                    break;
                case MSG_UPDATE_CONTROL_BAR:
                    if (mediaControl.isPlaying()) {
                        ibPlay.setImageResource(R.drawable.btn_pause_normal);
                    } else {
                        ibPlay.setImageResource(R.drawable.btn_play_normal);
                    }
                case PLAY:
                    if(mediaControl.isPlaying()) {
                        mediaControl.playMedia();
                    }else {
                        handler.removeMessages(PLAY);
                    }
                    break;
                case PAUSE:
                    showTopBottomBar();
                    break;
            }
        }
    };

    public Handler getMainThreadHandler() {
        return handler;
    }

    private Runnable photoRunnable = new Runnable() {
        @Override
        public void run() {
            mediaControl.playMedia();
        }
    };

    @Override
    public MediaControlImpl attachMediaView() {
        if (mediaControl == null) {
            mediaControl = new MediaControlImpl(this, MediaPlayConstants.MediaType.PHOTO);
            photoPlayer = mediaControl.getPhotoPlayer();
        }
        return mediaControl;
    }

    private class PhotoViewAdapter extends PagerAdapter {
        private Context context;
        private ArrayList<PhotoItem> photoList = new ArrayList<>();

        public PhotoViewAdapter(Context context, ArrayList<PhotoItem> photos) {
            this.context = context;
            this.photoList.clear();
            this.photoList.addAll(photos);
        }

        @Override
        public int getCount() {
            return photoList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(context);
            handler.removeMessages(MSG_SHOW_HIDE_BAR);
            Glide.with(context)
                    .load("file://" + photoList.get(position).getPath())
                    .into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);

            photoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "instantiateItem onClick");
                    if(isBottomBarShow) {
                        hideTopBottomBar(0, bottomHeight);
                    }else {
                        showTopBottomBar();
                    }
                }
            });

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
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
                    handler.removeMessages(PLAY);
                    ibPlay.setImageResource(R.drawable.btn_play_normal);
                    mediaControl.pauseMedia();
                    handler.removeCallbacksAndMessages(null);
                } else {
                    ibPlay.setImageResource(R.drawable.btn_pause_normal);
                    handler.postDelayed(photoRunnable, ONE_SECOND_TIMER);
                    break;
                }
                handler.sendEmptyMessage(MSG_UPDATE_CONTROL_BAR);
                break;
            case R.id.ib_favorite:
                if (isFavorite == false) {
                    ibFavorite.setImageResource(R.drawable.btn_favorite_pressed);
                } else {
                    ibFavorite.setImageResource(R.drawable.btn_favorite_normal);
                }
                isFavorite = !isFavorite;
                mediaControl.setMediaFavorite(isFavorite);
                break;
        }
    }

    @Override
    public void showTopBottomBar() {
        isBottomBarShow = true;
        llBottomBar.setTranslationY(0);
        handler.sendEmptyMessageDelayed(MSG_SHOW_HIDE_BAR, FIVE_SECOND_TIMER);
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

        mediaControl.resetMediaData();
        handler.removeCallbacksAndMessages(null);
        handler = null;
        super.onDestroy();
    }
}
