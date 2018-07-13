package com.example.android.multmedia.player.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;

public abstract class MediaActivity<P extends BaseMedia> extends BaseActivity {
    protected P mediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mediaPlayer = createMediaPlayer();
    }

    protected abstract P createMediaPlayer();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
