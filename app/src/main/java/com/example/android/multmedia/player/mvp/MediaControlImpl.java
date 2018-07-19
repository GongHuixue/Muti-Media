package com.example.android.multmedia.player.mvp;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.browser.VideoBrowserActivity;
import com.example.android.multmedia.player.AudioPlayerActivity;
import com.example.android.multmedia.player.PhotoPlayerActivity;
import com.example.android.multmedia.player.VideoPlayerActivity;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.VideoItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.android.multmedia.player.MediaPlayConstants.*;

public class MediaControlImpl extends BaseControl<IMediaView> implements IMediaPlayControl{
    private final static String TAG = MediaControlImpl.class.getSimpleName();
    private VideoPlayerActivity videoPlayerActivity;
    private VideoView videoPlayer;
    private ArrayList<VideoItem> videoList = new ArrayList<>();
    private AudioPlayerActivity audioPlayerActivity;
    private ArrayList<AudioItem> audioList = new ArrayList<>();
    private PhotoPlayerActivity photoPlayerActivity;
    private ArrayList<PhotoItem> photoList = new ArrayList<>();

    private int mediaPosition = 0;
    private volatile Boolean isPlaying = false;
    private MediaType mediaType;

    private Handler mainHandler;

    public MediaControlImpl(IMediaView view, MediaType type) {
        attachView(view);
        if (getActivityView() instanceof VideoPlayerActivity) {
            videoPlayerActivity = (VideoPlayerActivity) getActivityView();
            mediaType = type;
            mainHandler = videoPlayerActivity.getMainThreadHandler();
            Log.d(TAG, "Video Player init");
            videoPlayer = (VideoView) videoPlayerActivity.findViewById(R.id.vv_video);
        } else if (getActivityView() instanceof AudioPlayerActivity) {
            audioPlayerActivity = (AudioPlayerActivity) getActivityView();
            mediaType = type;
            Log.d(TAG, "Audio Player Init");
        } else if (getActivityView() instanceof PhotoPlayerActivity) {
            photoPlayerActivity = (PhotoPlayerActivity)getActivityView();
            mediaType = type;
        }
    }

    public VideoView getVideoPlayer() {
        return videoPlayer;
    }

    public MediaPlayer getAudioPlayer() {
        return new MediaPlayer();
    }

    public void setVideoPlayerListener(ArrayList<VideoItem> videoItems) {
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
                    mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
                        @Override
                        public void onBufferingUpdate(MediaPlayer mp, int percent) {
                            videoPlayerActivity.updateSeekBar(mp.getDuration()*percent/100);
                        }
                    });
                    videoPlayerActivity.setVideoHW(mp.getVideoWidth(), mp.getVideoHeight());
                    videoPlayerActivity.mediaPlayerPrepared();

                    Message msg = mainHandler.obtainMessage(MSG_UPDATE_CONTROL_BAR);
                    msg.arg1 = PLAY_STATE_PLAYING;
                    msg.obj = videoList.get(mediaPosition);
                    mainHandler.sendMessage(msg);

                    Log.d(TAG, "start play video");
                }
            });

            getVideoPlayer().setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    getVideoPlayer().stopPlayback();
                    Log.d(TAG, "video play completed");

                    Message msg = mainHandler.obtainMessage(MSG_UPDATE_CONTROL_BAR);
                    msg.arg1 = PLAY_STATE_END;
                    mainHandler.sendMessage(msg);

                    /*as play completed, need continue play media*/
                    playPauseMedia();
                }
            });
        }

        videoList.clear();
        videoList.addAll(videoItems);
    }

    /*must set video path by following api*/
    public void setVideoPath(String videoPath, int position) {
        Log.d(TAG, "setVideoPath Video Path = " + videoPath + ", position = " + position);
        getVideoPlayer().setVideoPath(videoPath);
        mediaPosition = position;
    }

    @Override
    public void playPauseMedia(){
        if(mediaType == MediaType.VIDEO) {
            if(videoPlayerActivity.getPlayMode() == SEQUENCE_PLAY) {
                playNextMedia();
            }else {
                setVideoPath(videoList.get(mediaPosition).getPath(), mediaPosition);
            }
        }else if (mediaType == MediaType.AUDIO) {

        }else if (mediaType == MediaType.PHOTO) {

        }
    }
    @Override
    public void loadMediaData(){
        mvpView.showLoadingProgress();
    }
    @Override
    public void playPreMedia(){
        if(mediaPosition == 0) {
            Log.d(TAG, "This is the first file");
        }else {
            mediaPosition = mediaPosition - 1;
            videoPlayer.stopPlayback();
            setVideoPath(videoList.get(mediaPosition).getPath(), mediaPosition);
        }
    }
    @Override
    public void playMedia(){
        isPlaying = true;
        videoPlayer.start();
    }
    @Override
    public void pauseMedia(){
        isPlaying = false;
        videoPlayer.pause();
    }
    @Override
    public void playNextMedia(){
        if(mediaPosition == (videoList.size() -1)) {
            Log.d(TAG, "This is the last file");
        }else {
            mediaPosition = mediaPosition + 1;
            videoPlayer.stopPlayback();
            setVideoPath(videoList.get(mediaPosition).getPath(), mediaPosition);
        }
    }
    @Override
    public void playModeChanged(){

    }
    @Override
    public boolean isFavorite(){
        return false;
    }
    @Override
    public boolean isPlaying(){
        return isPlaying;
    }

    @Override
    public void resetMediaData() {
        /*reset the common data*/
        isPlaying = false;
        mediaType = MediaType.NONE;
        mediaPosition = 0;

        if(mediaType == MediaType.VIDEO) {
            videoPlayerActivity = null;
            videoPlayer = null;
            videoList.clear();
            mainHandler = null;
        }else if(mediaType == MediaType.AUDIO) {
            audioPlayerActivity = null;
            audioList.clear();
        }else if(mediaType == MediaType.PHOTO) {
            photoPlayerActivity = null;
            photoList.clear();
        }
    }
}
