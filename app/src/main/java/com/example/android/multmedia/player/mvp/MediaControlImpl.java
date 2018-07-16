package com.example.android.multmedia.player.mvp;

import android.media.MediaPlayer;
import android.widget.VideoView;

import com.example.android.multmedia.player.VideoPlayerActivity;

import java.lang.ref.WeakReference;

import static com.example.android.multmedia.player.MediaPlayConstants.*;

public class MediaControlImpl extends BaseControl<IMediaView> implements IMediaPlayControl{
    private final static String TAG = MediaControlImpl.class.getSimpleName();
    private VideoPlayerActivity videoPlayerActivity;
    private WeakReference<VideoView> videoPlayer;
    private volatile Boolean isPlaying = false;

    public MediaControlImpl(IMediaView view) {
        attachView(view);
        videoPlayerActivity = (VideoPlayerActivity) getActivityView();
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
                    videoPlayerActivity.setVideoHW(mp.getVideoWidth(), mp.getVideoHeight());
                    playMedia();
                }
            });

            getVideoPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    getVideoPlayer().stopPlayback();
                    pauseMedia();
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
        isPlaying = true;
        videoPlayerActivity.getMainThreadHandler().sendEmptyMessage(MSG_UPDATE_CONTROL_BAR);
    }
    @Override
    public void pauseMedia(){
        isPlaying = false;
        videoPlayerActivity.getMainThreadHandler().sendEmptyMessage(MSG_UPDATE_CONTROL_BAR);
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
    @Override
    public boolean isPlaying(){
        return isPlaying;
    }
}
