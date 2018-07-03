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
import java.io.InputStream;
import java.util.Locale;

public class AudioItem extends BaseItem {
    private final static String TAG = AudioItem.class.getSimpleName();
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

    RoundRect roundRect = new RoundRect(300, 300, 150);

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

    public String tanslateDurationToString(long time) {
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

    public void setImage(String image, Context context) {
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
    }

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

    public static Bitmap getBitmap(Context context, long audioId, long albumId) {
        Bitmap bm = null;
        Uri uri;
        if((audioId < 0 ) && (albumId < 0)) {
            throw new IllegalArgumentException("must specify an albumId and songId");
        }

        try{
            if(albumId < 0) {
                uri = Uri.parse("content://media/external/audio/media/" + audioId + "/albumart");
                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }
            }else {
                uri = ContentUris.withAppendedId(sArtworkUri, albumId);
//                InputStream inputStream;
//                try {
//                    inputStream = context.getContentResolver().openInputStream(uri);
//                }catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                    return null;
//                }
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inPreferredConfig = Bitmap.Config.RGB_565;
//                return BitmapFactory.decodeStream(inputStream, null, options);

                ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r");

                if(pfd != null) {
                    Log.d(TAG, "pfd not null");
                    FileDescriptor fd = pfd.getFileDescriptor();
                    bm = BitmapFactory.decodeFileDescriptor(fd);
                }else {
                    Log.d(TAG, "pfg get failed");
                    return null;
                }
            }
        }catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if(bm == null) {
            Drawable drawable = context.getResources().getDrawable(R.drawable.music, null);
            BitmapDrawable bitmapDrawable = (BitmapDrawable)drawable;
            bm = bitmapDrawable.getBitmap();
        }

        Bitmap.createScaledBitmap(bm, 150, 150, true);
        return bm;
    }
}
