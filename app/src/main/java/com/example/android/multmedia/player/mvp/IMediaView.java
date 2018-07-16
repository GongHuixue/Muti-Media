package com.example.android.multmedia.player.mvp;

public interface IMediaView {
    void showTopBottomBar();
    void hideTopBottomBar(int top, int bottom);
    void showLoadingProgress();
    void hideLoadingProgress();
}
