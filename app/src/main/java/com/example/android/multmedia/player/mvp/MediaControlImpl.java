package com.example.android.multmedia.player.mvp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.VideoView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.playedlist.personaldb.GreenDaoManager;
import com.example.android.multmedia.playedlist.personaldb.MediaDb;
import com.example.android.multmedia.player.AudioPlayerActivity;
import com.example.android.multmedia.player.PhotoPlayerActivity;
import com.example.android.multmedia.player.VideoPlayerActivity;
import com.example.android.multmedia.player.photo.PhotoViewPager;
import com.example.android.multmedia.services.MediaStoreService;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.BaseItem;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.VideoItem;
import java.io.IOException;
import java.util.ArrayList;

import static android.content.Context.BIND_AUTO_CREATE;
import static com.example.android.multmedia.player.MediaPlayConstants.*;

public class MediaControlImpl extends BaseControl<IMediaView> implements IMediaPlayControl{
    private final static String TAG = MediaControlImpl.class.getSimpleName();
    private Context mContext;
    private VideoPlayerActivity videoPlayerActivity;
    private VideoView videoPlayer;
    private ArrayList<VideoItem> videoList = new ArrayList<>();

    /*init audio*/
    private MediaPlayer audioPlayer;
    private AudioPlayerActivity audioPlayerActivity;
    private ArrayList<AudioItem> audioList = new ArrayList<>();

    /*init photo*/
    private PhotoViewPager photoPlayer;
    private PhotoPlayerActivity photoPlayerActivity;
    private ArrayList<PhotoItem> photoList = new ArrayList<>();

    private int mediaPosition = 0;
    private volatile Boolean isPlaying = false;
    private MediaType mediaType;
    private Handler mainHandler;

    private boolean mBound = false;
    private MediaStoreService mediaService;
    private boolean isFavorite = false;

    private static GreenDaoManager daoManager = GreenDaoManager.getSingleInstance();

    private BaseItem mediaItem;

    private ServiceConnection mediaServiceCon = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MediaStoreService.MediaServiceBinder binder = (MediaStoreService.MediaServiceBinder) service;
            mediaService = binder.getMediaService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };


    public MediaControlImpl(IMediaView view, MediaType type) {
        attachView(view);
        if (getActivityView() instanceof VideoPlayerActivity) {
            videoPlayerActivity = (VideoPlayerActivity) getActivityView();
            mContext = videoPlayerActivity;
            mediaType = type;
            mainHandler = videoPlayerActivity.getMainThreadHandler();
            Log.d(TAG, "Video Player init");
            videoPlayer = (VideoView) videoPlayerActivity.findViewById(R.id.vv_video);
        } else if (getActivityView() instanceof AudioPlayerActivity) {
            audioPlayerActivity = (AudioPlayerActivity) getActivityView();
            mContext = audioPlayerActivity;
            mediaType = type;
            mainHandler = audioPlayerActivity.getMainThreadHandler();
            if(audioPlayer == null) {
                audioPlayer = new MediaPlayer();
            }
            Log.d(TAG, "Audio Player Init");
        } else if (getActivityView() instanceof PhotoPlayerActivity) {
            photoPlayerActivity = (PhotoPlayerActivity)getActivityView();
            mContext = photoPlayerActivity;
            mediaType = type;
            mainHandler = photoPlayerActivity.getMainThreadHandler();
            if(photoPlayer == null) {
                photoPlayer = (PhotoViewPager) photoPlayerActivity.findViewById(R.id.photo_view);
            }
        }

        bindMediaService();
    }

    public VideoView getVideoPlayer() {
        return videoPlayer;
    }

    public MediaPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public PhotoViewPager getPhotoPlayer() {
        return photoPlayer;
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

    public void setAudioPlayerListener(ArrayList<AudioItem> audioItems) {
        if (audioPlayer != null) {
            Log.d(TAG, "setAudioPlayerListener");
            audioPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });

            audioPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();

                    Message msg = mainHandler.obtainMessage(MSG_UPDATE_CONTROL_BAR);
                    msg.arg1 = PLAY_STATE_PLAYING;
                    msg.obj = audioList.get(mediaPosition);
                    mainHandler.sendMessage(msg);

                    Log.d(TAG, "start play audio");
                }
            });

            audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    Log.d(TAG, "audio play completed");

                    Message msg = mainHandler.obtainMessage(MSG_UPDATE_CONTROL_BAR);
                    msg.arg1 = PLAY_STATE_END;
                    mainHandler.sendMessage(msg);

                    /*as play completed, need continue play media*/
                    playPauseMedia();
                }
            });
        }

        audioList.clear();
        audioList.addAll(audioItems);
    }

    public void setPhotoListListener(ArrayList<PhotoItem> photoItems) {
        photoList.clear();
        photoList.addAll(photoItems);
    }

    /*must set video path by following api*/
    public void setVideoPath(String videoPath, int position) {
        Log.d(TAG, "setVideoPath Video Path = " + videoPath + ", position = " + position);
        getVideoPlayer().setVideoPath(videoPath);
        mediaPosition = position;
        isPlaying = true;
        mediaItem = videoList.get(position);
        if(daoManager.queryByPath(videoPath)) {
            daoManager.updatePlayedTimes(videoPath);
            isFavorite = daoManager.queryFavorite(videoPath);
        }else {
            daoManager.insertIfNotExist(mediaItem);
        }
    }

    /*must set audio path by following api*/
    public void setAudioPath(String audioPath, int position) {
        Log.d(TAG, "setAudioPath Video Path = " + audioPath + ", position = " + position);
        try {
            audioPlayer.setDataSource(audioList.get(position).getPath());
            audioPlayer.prepareAsync();
            isPlaying = true;
        }catch (IOException e) {
            e.printStackTrace();
        }
        mediaPosition = position;

        mediaItem = audioList.get(position);
        if(daoManager.queryByPath(audioPath)) {
            Log.d(TAG, "setAudioPath " + audioPath + " is exist in media.db");
            daoManager.updatePlayedTimes(audioPath);
            isFavorite = daoManager.queryFavorite(audioPath);
        }else {
            Log.d(TAG, "setAudioPath " + audioPath + " not exist in media.db and insert");
            daoManager.insertIfNotExist(mediaItem);
        }
    }

    public void setPhotoPath(int position) {
        photoPlayer.setCurrentItem(position);
        mediaPosition = position;

        mediaItem = photoList.get(position);
        if(daoManager.queryByPath(photoList.get(position).getPath())) {
            daoManager.updatePlayedTimes(photoList.get(position).getPath());
            isFavorite = daoManager.queryFavorite(photoList.get(position).getPath());
        }else {
            daoManager.insertIfNotExist(mediaItem);
        }
    }

    private void bindMediaService() {
        final Intent intent = new Intent(mContext, MediaStoreService.class);
        mContext.bindService(intent, mediaServiceCon, BIND_AUTO_CREATE);
    }

    private void unbindMediaService() {
        if(mBound == true) {
            mContext.unbindService(mediaServiceCon);
        }
        mediaService = null;
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
            if(audioPlayerActivity.getPlayMode() == SEQUENCE_PLAY) {
                playNextMedia();
            }else {
                setAudioPath(audioList.get(mediaPosition).getPath(), mediaPosition);
            }
        }else if (mediaType == MediaType.PHOTO) {
            Log.d(TAG, "for photo, not support play pre picture");
        }
    }
    @Override
    public void loadMediaData(){
        mvpView.showLoadingProgress();
    }
    @Override
    public void playPreMedia(){
        if(mediaType == MediaType.VIDEO) {
            if (mediaPosition == 0) {
                Log.d(TAG, "This is the first file");
            } else {
                mediaPosition = mediaPosition - 1;
                videoPlayer.stopPlayback();
                setVideoPath(videoList.get(mediaPosition).getPath(), mediaPosition);
            }
        }else if(mediaType == MediaType.AUDIO) {
            if(mediaPosition == 0) {
                Log.d(TAG, "This is the first file");
            }else {
                mediaPosition = mediaPosition - 1;
                audioPlayer.reset();
                setAudioPath(audioList.get(mediaPosition).getPath(), mediaPosition);
            }
        }else if(mediaType == MediaType.PHOTO) {
            Log.d(TAG, "for photo, not support play pre picture");
        }
    }
    @Override
    public void playMedia(){
        isPlaying = true;
        Log.d(TAG, "playMedia");
        if(mediaType == MediaType.VIDEO) {
            videoPlayer.start();
        }else if(mediaType == MediaType.AUDIO){
            audioPlayer.start();
        }else if(mediaType == MediaType.PHOTO) {
            int photoIndex = photoPlayer.getCurrentItem();
            Log.d(TAG, "photoIndex = " + photoIndex + ", list size = " + photoList.size());
            if(photoIndex == (photoList.size() -1)) {
                photoPlayer.setCurrentItem(0);
            }else {
                photoPlayer.setCurrentItem(photoPlayer.getCurrentItem() + 1);
            }
            mainHandler.sendEmptyMessageDelayed(PLAY, THREE_SECOND_TIMER);
        }
    }
    @Override
    public void pauseMedia(){
        isPlaying = false;
        Log.d(TAG, "pauseMedia");
        if(mediaType == MediaType.VIDEO) {
            videoPlayer.pause();
            Message msg = mainHandler.obtainMessage(MSG_UPDATE_CONTROL_BAR);
            msg.arg1 = PLAY_STATE_PAUSE;
            mainHandler.sendMessage(msg);
        }else if(mediaType == MediaType.AUDIO) {
            audioPlayer.pause();
            Message msg = mainHandler.obtainMessage(MSG_UPDATE_CONTROL_BAR);
            msg.arg1 = PLAY_STATE_PAUSE;
            mainHandler.sendMessage(msg);
        }else if(mediaType == MediaType.PHOTO) {
            mainHandler.removeMessages(PLAY);
            mainHandler.sendEmptyMessage(PAUSE);
        }
    }
    @Override
    public void playNextMedia(){
        if (mediaType == MediaType.VIDEO) {
            if (mediaPosition == (videoList.size() - 1)) {
                Log.d(TAG, "This is the last file");
            } else {
                mediaPosition = mediaPosition + 1;
                videoPlayer.stopPlayback();
                setVideoPath(videoList.get(mediaPosition).getPath(), mediaPosition);
            }
        }else if (mediaType == MediaType.AUDIO) {
            if(mediaPosition == (audioList.size() - 1)) {
                Log.d(TAG, "This is the last file");
            }else {
                mediaPosition = mediaPosition + 1;
                audioPlayer.reset();
                setAudioPath(audioList.get(mediaPosition).getPath(), mediaPosition);
            }
        }else if (mediaType == MediaType.PHOTO) {

        }
    }
    @Override
    public void playModeChanged(){

    }
    @Override
    public void setMediaFavorite(boolean isFavorite){
        if(mediaType == MediaType.VIDEO) {
            mediaItem = videoList.get(mediaPosition);
        }else if(mediaType == MediaType.AUDIO) {
            mediaItem = audioList.get(mediaPosition);
        }else if(mediaType == MediaType.PHOTO) {
            mediaItem = photoList.get(mediaPosition);
        }
        daoManager.updateFavorite(mediaItem, isFavorite);
    }
    @Override
    public boolean isPlaying(){
        Log.d(TAG, "isPlaying = " + isPlaying);
        return isPlaying;
    }

    @Override
    public boolean isFavorite(){
        Log.d(TAG, "isFavorite = " + isFavorite);
        return isFavorite;
    }

    @Override
    public void resetMediaData() {
        Log.d(TAG, "resetMediaData mediaType = " + mediaType);
        unbindMediaService();

        if(mediaType == MediaType.VIDEO) {
            videoPlayerActivity = null;
            videoPlayer = null;
            videoList.clear();
            mainHandler = null;
        }else if(mediaType == MediaType.AUDIO) {
            audioPlayerActivity = null;
            audioList.clear();
            audioPlayer.stop();
            audioPlayer.reset();
            audioPlayer.release();
            audioPlayer = null;
            Log.d(TAG, "release audio player ");
        }else if(mediaType == MediaType.PHOTO) {
            photoPlayerActivity = null;
            photoList.clear();
        }

        /*reset the common data*/
        isPlaying = false;
        mediaType = MediaType.NONE;
        mediaPosition = 0;
        mContext = null;
    }
}
