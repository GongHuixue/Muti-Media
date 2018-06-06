package com.example.android.multmedia.player;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.TextView;

import com.example.android.multmedia.R;
import com.mediaload.bean.PhotoResult;
import com.mediaload.callback.OnPhotoLoadCallBack;

public class PicturePlayerPlayerActivity extends BasePlayerActivity {

    @Override
    public int getLayoutResID(){
        return R.layout.activity_picture_player;
    }

    @Override
    public void initView() {
        final TextView pictureNum = (TextView) findViewById(R.id.picture_num);
        mediaLoad.loadPhotos(PicturePlayerPlayerActivity.this, new OnPhotoLoadCallBack() {
            @Override
            public void onResult(PhotoResult result) {
                pictureNum.setText("Picture Files: " + result.getItems().size());
            }
        });
    }
}
