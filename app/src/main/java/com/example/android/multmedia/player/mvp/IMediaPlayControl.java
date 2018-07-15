package com.example.android.multmedia.player.mvp;

public interface IMediaPlayControl {
    void loadMediaData();
    void playPreMedia();
    void playMedia();
    void pauseMedia();
    void playNextMedia();
    void playModeChanged();
    void isFavorite();
}
