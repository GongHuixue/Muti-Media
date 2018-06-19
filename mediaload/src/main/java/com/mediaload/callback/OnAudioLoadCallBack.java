package com.mediaload.callback;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;

import com.mediaload.bean.AudioItem;
import com.mediaload.bean.AudioResult;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Audio.AudioColumns.DURATION;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.SIZE;
import static android.provider.MediaStore.Audio.AlbumColumns.ALBUM;

public abstract class OnAudioLoadCallBack extends BaseLoaderCallBack<AudioResult> {
    /*the album uri*/
    public static final Uri ALBUM_URI = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
    /*The title of album*/
    public static final int TITLE = 0;
    /*The picture of album*/
    public static final int IMAGE = 1;

    public static final String[] ALBUM_COLUMNS = new String[]{
            MediaStore.Audio.Albums.ALBUM,          //专辑标题
            MediaStore.Audio.Albums.ALBUM_ART       //专辑图片
    };

    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data, Context context) {
        List<AudioItem> result = new ArrayList<>();
        AudioItem item;
        Cursor albumCursor = null;
        long sum_size = 0;
        while (data.moveToNext()) {
            item = new AudioItem();
            int audioId = data.getInt(data.getColumnIndexOrThrow(_ID));
            String name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));
            String album = data.getString(data.getColumnIndexOrThrow(ALBUM));
            long duration = data.getLong(data.getColumnIndexOrThrow(DURATION));
            long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
            long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
            item.setId(audioId);
            item.setDisplayName(name);
            item.setPath(path);
            item.setAlbum(album);
            item.setDuration(duration);
            item.setSize(size);
            item.setModified(modified);
            if(context != null) {
                try {
                    albumCursor = context.getContentResolver().query(ALBUM_URI,
                            ALBUM_COLUMNS,
                            ALBUM_COLUMNS[TITLE],
                            new String[]{item.getAlbum()},
                            null);
                    if( (albumCursor != null) && (albumCursor.getCount() > 0) ) {
                        for (albumCursor.moveToFirst(); !albumCursor.isAfterLast(); albumCursor.moveToNext()) {
                            String albumPath = albumCursor.getString(IMAGE);
                            if (TextUtils.isEmpty(path)) {
                                item.setImage(new String());
                            } else {
                                item.setImage(albumPath);
                            }
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(albumCursor != null) {

                    }
                }
            }
            result.add(item);
            sum_size += size;
        }
        onResult(new AudioResult(sum_size,result));
    }

    @Override
    public Uri getQueryUri() {
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    }

    @Override
    public String[] getSelectProjection() {
        String[] PROJECTION = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.MediaColumns.SIZE,
                MediaStore.Audio.Media.DATE_MODIFIED
        };
        return PROJECTION;
    }
}
