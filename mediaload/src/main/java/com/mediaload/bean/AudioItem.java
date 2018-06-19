package com.mediaload.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.mediaload.R;

public class AudioItem extends BaseItem {
    private long duration;
    private boolean checked;
    private String album;
    private String image;
    private Bitmap bitmap; //album picture;
    private Bitmap round;   //round album picture

    RoundRect roundRect = new RoundRect(300, 300, 150);

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbum() {
        return album;
    }

    public void setImage(String image) {
        this.image = image;
        setBitmap(image);
        setRound(bitmap);
    }

    public void setRound(Bitmap bitmap) {
        this.round = roundRect.Transformation(bitmap);
    }

    public void setBitmap(String image) {
        if (TextUtils.isEmpty(image)) {
            bitmap = BitmapFactory.decodeResource
                    (GlobalApplication.getContext().getResources(), R.drawable.music);
        } else {
            try {
                bitmap = RoundRect.lessenUriImage(image);
            }catch (Exception e){
                bitmap = BitmapFactory.decodeResource
                        (GlobalApplication.getContext().getResources(), R.drawable.music);
            }
        }
    }
}
