package com.example.android.multmedia.player.mvp;

public class BaseControl<V> {
    public V mvpView;

    public void attachView(V view) {
        this.mvpView = view;
    }

    public void detachView() {
        this.mvpView = null;
    }
}
