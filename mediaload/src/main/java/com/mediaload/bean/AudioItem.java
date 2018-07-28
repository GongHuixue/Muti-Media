package com.mediaload.bean;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.Log;

import com.mediaload.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.Locale;

public class AudioItem extends BaseItem {
    private final static String TAG = AudioItem.class.getSimpleName();
    private final static long serialVersionUID = 2L;
    private long duration;
    private String durationTime;
    private boolean checked;
    private long albumId;
    private String album;
    private String singer;  //audio singer;
    private String image;    //audio image;
    private Bitmap bitmap; //album picture;
    private Bitmap round;   //round album picture

    private static final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");

    //RoundRect roundRect = new RoundRect(300, 300, 150);

    public AudioItem() {
        VIEW_TYPE = 2;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getDurationString() {
        return durationTime;
    }

    public void setDurationString(String time) {
        this.durationTime = time;
    }

    public String translateDurationToString(long time) {
        double sec = time/1000.0;
        int min = (int) sec/60;
        sec = sec % 60;
        return String.format(Locale.getDefault(), "%d:%02d", min, (int)sec);
    }

    public Uri getAlbumIconUri(long albumId) {
        return ContentUris.withAppendedId(sArtworkUri, albumId);
    }

    public Uri getAlbumIconUri() {
        return ContentUris.withAppendedId(sArtworkUri, albumId);
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

    /*public void setImage(String image, Context context) {
        this.image = image;
        setBitmap(image, context);
        setRound(bitmap, context);
    }

    public void setRound(Bitmap bitmap, Context context) {
        this.round = roundRect.Transformation(bitmap, context);
    }

    public void setBitmap(String image, Context context) {
        if (TextUtils.isEmpty(image)) {
            bitmap = BitmapFactory.decodeResource
                    (context.getResources(), R.drawable.music);
        } else {
            try {
                bitmap = RoundRect.lessenUriImage(image);
            }catch (Exception e){
                bitmap = BitmapFactory.decodeResource
                        (context.getResources(), R.drawable.music);
            }
        }
    }*/

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSinger() {
        return singer;
    }

    public void setAlbumId(long id) {
        this.albumId = id;
    }

    public long getAlbumId() {
        return albumId;
    }
}
