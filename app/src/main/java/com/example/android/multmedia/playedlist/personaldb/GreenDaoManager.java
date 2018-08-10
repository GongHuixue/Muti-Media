package com.example.android.multmedia.playedlist.personaldb;

import android.os.HandlerThread;
import android.os.Handler;
import android.os.Message;

import com.example.android.multmedia.GlobalApplication;
import com.example.android.multmedia.greendao.MediaDbDao;
import com.mediaload.bean.BaseItem;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class GreenDaoManager {
    private static GreenDaoManager singleInstance;

    private MediaDbDao mediaDbDao = ((GlobalApplication)GlobalApplication.getGlobalContext()).getMediaDbDao();
    private MediaDb media;  //return the query result.
    private QueryBuilder<MediaDb> queryBuilder = mediaDbDao.queryBuilder();
    private Query<MediaDb> query = queryBuilder.build();

    private Handler mHandler;
    private HandlerThread mHT;

    private GreenDaoManager() {
        createHandlerThread();
    }

    /*The thread was used for operate database*/
    private void createHandlerThread() {
        if(mHT == null) {
            mHT = new HandlerThread("GreenDaoManager_HT");
        }
        mHT.start();

        mHandler = new Handler(mHT.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
    }

    private void destroyHandlerThread() {
        if(mHT != null) {
            mHT.quit();
            mHT.interrupt();
            mHT = null;
            mHandler = null;
        }
    }

    public static synchronized GreenDaoManager getSingleInstance() {
        if(singleInstance == null) {
            singleInstance = new GreenDaoManager();
        }
        return singleInstance;
    }

    public void insertIfNotExist(BaseItem mediaItem) {
        media = new MediaDb();
        media.setMediaPath(mediaItem.getPath());
        media.setMediaName(mediaItem.getDisplayName());
        media.setSize(mediaItem.getSize());
        media.setMediaId(mediaItem.getId());
        media.setCreateTime(mediaItem.getModified());
        media.setMediaType(mediaItem.getViewType());
        media.setPlayedCounts(1);
        media.setPlayedTime(System.currentTimeMillis());
        mediaDbDao.insert(media);
    }

    public void delete() {

    }

    public void updatePlayedTimes(String path) {
        media = getMedia(path);
        if(media != null) {
            media.setPlayedTime(System.currentTimeMillis());
            media.setPlayedCounts(media.getPlayedCounts() + 1);
        }
    }

    public void updateFavorite(BaseItem mediaItem, Boolean isFavor) {
        if(mediaItem != null) {
            media = getMedia(mediaItem.getPath());
            media.setIsFavor(isFavor);
        }
    }

    public MediaDb getMedia(String path) {
        if(path != null) {
            query = queryBuilder.where(MediaDbDao.Properties.MediaPath.eq(path)).build();
            query.setParameter(0, path);
            media = query.unique();
        }
        return media;
    }

    public boolean queryByPath(String path) {
        boolean fileExist = false;
        if(path != null) {
            //MediaDb media = mediaDbDao.queryBuilder().where(MediaDbDao.Properties.MediaPath.eq(path)).unique();
            query = queryBuilder.where(MediaDbDao.Properties.MediaPath.eq(path)).build();
            query.setParameter(0, path);
            media = query.unique();
            if(media != null) {
                fileExist = true;
            }else {
                fileExist = false;
            }
        }
        return fileExist;
    }

    public List<MediaDb> queryByMediaType(int mediaType) {
        List<MediaDb> mediaList = mediaDbDao.queryBuilder()
                .where(MediaDbDao.Properties.MediaType.eq(mediaType))
                .orderDesc(MediaDbDao.Properties.PlayedTime)
                .list();

        return mediaList;
    }

    public List<MediaDb> queryFavorite(boolean isFavorite) {
        List<MediaDb> favoriteList = mediaDbDao.queryBuilder()
                .where(MediaDbDao.Properties.IsFavor.eq(isFavorite))
                .orderDesc(MediaDbDao.Properties.PlayedTime)
                .list();
        return favoriteList;
    }

    public List<MediaDb> queryLastPlayed() {
        List<MediaDb> lastPlayed = mediaDbDao.queryBuilder()
                .orderDesc(MediaDbDao.Properties.PlayedTime)
                .limit(50)
                .build()
                .list();
        return lastPlayed;
    }


    public MediaDbDao getMediaDbDao() {
        return mediaDbDao;
    }
}
