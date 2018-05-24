package com.mediaload.callback;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.Loader;

import com.mediaload.bean.VideoFolder;
import com.mediaload.bean.VideoItem;
import com.mediaload.bean.VideoResult;

import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_MODIFIED;
import static android.provider.MediaStore.MediaColumns.DISPLAY_NAME;
import static android.provider.MediaStore.MediaColumns.SIZE;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Video.VideoColumns.BUCKET_ID;
import static android.provider.MediaStore.Video.VideoColumns.DURATION;

public abstract class OnVideoLoadCallBack extends BaseLoaderCallBack<VideoResult> {
    @Override
    public void onLoadFinish(Loader<Cursor> loader, Cursor data) {
        List<VideoFolder> folders = new ArrayList<>();
        List<VideoItem> items = new ArrayList<>();
        VideoFolder videoFolder;
        VideoItem videoItem;
        long videoCount = 0;

        while (data.moveToNext()) {
            String folderId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
            String folderName = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
            int videoId = data.getInt(data.getColumnIndexOrThrow(_ID));
            String name = data.getString(data.getColumnIndexOrThrow(DISPLAY_NAME));
            String path = data.getString(data.getColumnIndexOrThrow(DATA));
            long duration = data.getLong(data.getColumnIndexOrThrow(DURATION));
            long size = data.getLong(data.getColumnIndexOrThrow(SIZE));
            long modified = data.getLong(data.getColumnIndexOrThrow(DATE_MODIFIED));
            videoItem = new VideoItem(videoId,name,path,size,modified,duration);
            videoFolder = new VideoFolder();
            videoFolder.setId(folderId);
            videoFolder.setName(folderName);
            if(folders.contains(videoFolder)){
                folders.get(folders.indexOf(videoFolder)).addItem(videoItem);
            }else{
                videoFolder.addItem(videoItem);
                folders.add(videoFolder);
            }
            items.add(videoItem);
            videoCount += size;
        }
        onResult(new VideoResult(folders, items, videoCount));
    }

    @Override
    public String[] getSelectProjection() {
        String[] PROJECTION = {
                MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                BUCKET_ID,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                DISPLAY_NAME,
                DURATION,
                SIZE,
                DATE_MODIFIED
        };
        return PROJECTION;
    }

    @Override
    public Uri getQueryUri() {
        return MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    }
}
