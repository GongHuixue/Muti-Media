package com.example.android.multmedia.player.mvp;

import android.media.MediaPlayer;
import android.widget.VideoView;

import java.lang.ref.WeakReference;

public class MediaControlImpl extends BaseControl<IMediaView> implements IMediaPlayControl{

    private WeakReference<VideoView> videoPlayer;

    public MediaControlImpl(IMediaView view) {
        attachView(view);
    }

    public void setVideoPlayer(VideoView player) {
        videoPlayer = new WeakReference<VideoView>(player);
    }

    public VideoView getVideoPlayer() {
        if(videoPlayer.get() != null) {
            return videoPlayer.get();
        }
        return null;
    }

    public void setVideoPlayerListener() {
        if (getVideoPlayer() != null) {
            getVideoPlayer().setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });

            getVideoPlayer().setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    getVideoPlayer().start();
                }
            });

            getVideoPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    getVideoPlayer().stopPlayback();
                }
            });
        }
    }

    @Override
    public void loadMediaData(){
        mvpView.showLoadingProgress();
    }
    @Override
    public void playPreMedia(){

    }
    @Override
    public void playMedia(){

    }
    @Override
    public void pauseMedia(){

    }
    @Override
    public void playNextMedia(){

    }
    @Override
    public void playModeChanged(){

    }
    @Override
    public void isFavorite(){

    }
}
