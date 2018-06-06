package com.example.android.multmedia.player;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.example.android.multmedia.R;
import com.mediaload.bean.AudioResult;
import com.mediaload.bean.PhotoResult;
import com.mediaload.callback.OnAudioLoadCallBack;
import com.mediaload.callback.OnPhotoLoadCallBack;

public class AudioPlayerPlayerActivity extends BasePlayerActivity {

    @Override
    public int getLayoutResID(){
        return R.layout.activity_audio_player;
    }

    @Override
    public void initView() {
        final TextView audioNum = (TextView) findViewById(R.id.audio_num);
        mediaLoad.loadAudios(AudioPlayerPlayerActivity.this, new OnAudioLoadCallBack() {
            @Override
            public void onResult(AudioResult result) {
                audioNum.setText("Audio Files: " + result.getItems().size());
            }
        });
    }
}
