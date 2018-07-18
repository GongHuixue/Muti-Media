package com.example.android.multmedia.player.mvp;

public interface IMediaPlayControl {
    void loadMediaData();
    void playPauseMedia();
    void playPreMedia();
    void playMedia();
    void pauseMedia();
    void playNextMedia();
    void playModeChanged();
    boolean isFavorite();
    boolean isPlaying();
}
