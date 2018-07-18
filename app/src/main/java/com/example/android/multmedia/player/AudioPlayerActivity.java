package com.example.android.multmedia.player;

import com.example.android.multmedia.R;
import com.example.android.multmedia.player.mvp.BaseActivity;
import com.example.android.multmedia.player.mvp.IMediaView;
import com.example.android.multmedia.player.mvp.MediaControlImpl;

public class AudioPlayerActivity extends BaseActivity<MediaControlImpl> implements IMediaView {
    private MediaControlImpl mediaControl;

    public MediaControlImpl attachMediaView() {
        if(mediaControl != null) {
            mediaControl = new MediaControlImpl(this, MediaPlayConstants.MediaType.AUDIO);
        }
        return mediaControl;
    }

    @Override
    public void initView() {

    }

    @Override
    public void initData() {

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
