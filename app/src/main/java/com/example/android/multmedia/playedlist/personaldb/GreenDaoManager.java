package com.example.android.multmedia.playedlist.personaldb;

import android.os.HandlerThread;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.android.multmedia.GlobalApplication;
import com.example.android.multmedia.greendao.DaoSession;
import com.example.android.multmedia.greendao.MediaDbDao;
import com.example.android.multmedia.notification.NotificationHandler;
import com.example.android.multmedia.player.MediaPlayConstants;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.BaseItem;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.VideoItem;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.multmedia.player.MediaPlayConstants.AUDIO_TYPE;
import static com.example.android.multmedia.player.MediaPlayConstants.PHOTO_TYPE;
import static com.example.android.multmedia.player.MediaPlayConstants.VIDEO_TYPE;
import static com.example.android.multmedia.utils.Constant.*;

public class GreenDaoManager {
    private final static String TAG = GreenDaoManager.class.getSimpleName();
    private static GreenDaoManager singleInstance;
    private MediaDb media;  //return the query result.

    private Handler mHandler;
    private HandlerThread mHT;

    private BrowserMediaFile browserMediaFile = new BrowserMediaFile();
    private NotificationHandler ntf = NotificationHandler.getInstance();

    private GreenDaoManager() {
        createHandlerThread();
    }

    /*The thread was used for operate database*/
    private void createHandlerThread() {
        if (mHT == null) {
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
        if (mHT != null) {
            mHT.quit();
            mHT.interrupt();
            mHT = null;
            mHandler = null;
        }
    }

    public static synchronized GreenDaoManager getSingleInstance() {
        if (singleInstance == null) {
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
        ((GlobalApplication) GlobalApplication.getGlobalContext()).getMediaDbDao().insertOrReplace(media);
    }

    public void delete() {

    }

    public void updatePlayedTimes(String path) {
        media = getMedia(path);
        if (media != null) {
            media.setPlayedTime(System.currentTimeMillis());
            media.setPlayedCounts(media.getPlayedCounts() + 1);
            ((GlobalApplication) GlobalApplication.getGlobalContext()).getMediaDbDao().update(media);
        }
    }

    public void updateFavorite(BaseItem mediaItem, Boolean isFavor) {
        if (mediaItem != null) {
            media = getMedia(mediaItem.getPath());
            if (media != null) {
                media.setIsFavor(isFavor);
                ((GlobalApplication) GlobalApplication.getGlobalContext()).getMediaDbDao().update(media);
            } else {
                Log.d(TAG, "Current file not exist in database");
            }
        }
    }

    private MediaDb getMedia(String path) {
        if (path != null) {
            Log.d(TAG, "getMedia path = " + path);
            media = ((GlobalApplication) GlobalApplication.getGlobalContext()).getMediaDbDao()
                    .queryBuilder()
                    .where(MediaDbDao.Properties.MediaPath.eq(path))
                    .unique();
            /*clear greendao cache*/
            ((GlobalApplication) GlobalApplication.getGlobalContext()).getDaoSession().clear();

            ((GlobalApplication) GlobalApplication.getGlobalContext()).getMediaDbDao().detachAll();
        }
        return media;
    }

    public boolean queryByPath(String path) {
        boolean fileExist = false;
        if (path != null) {
            media = ((GlobalApplication) GlobalApplication.getGlobalContext()).getMediaDbDao()
                    .queryBuilder()
                    .where(MediaDbDao.Properties.MediaPath.eq(path))
                    .unique();

            if (media != null) {
                fileExist = true;
            } else {
                fileExist = false;
            }
            /*clear greendao cache*/
            ((GlobalApplication) GlobalApplication.getGlobalContext()).getDaoSession().clear();

            ((GlobalApplication) GlobalApplication.getGlobalContext()).getMediaDbDao().detachAll();
            Log.d(TAG, "queryByPath " + path + ", fileExist = " + fileExist);
        }
        return fileExist;
    }

    public Boolean queryFavorite(String mediaPath) {
        boolean isFavorite = false;
        media = ((GlobalApplication) GlobalApplication.getGlobalContext()).getMediaDbDao()
                .queryBuilder()
                .where(MediaDbDao.Properties.MediaPath.eq(mediaPath), MediaDbDao.Properties.IsFavor.eq(true))
                .build()
                .unique();

        if (media != null) {
            isFavorite = true;
        }
        Log.d(TAG, "queryFavorite " + mediaPath + ", isFavorite = " + isFavorite);
        return isFavorite;
    }

    public List<MediaDb> queryFavoriteByType(int mediaType) {
        List<MediaDb> favoriteList = ((GlobalApplication) GlobalApplication.getGlobalContext()).getMediaDbDao()
                .queryBuilder()
                .where(MediaDbDao.Properties.IsFavor.eq(true), MediaDbDao.Properties.MediaType.eq(mediaType)).list();
        return favoriteList;
    }

    public List<MediaDb> queryPopularByType(int mediaType) {
        List<MediaDb> popularList = ((GlobalApplication) GlobalApplication.getGlobalContext()).getMediaDbDao()
                .queryBuilder()
                .where(MediaDbDao.Properties.MediaType.eq(mediaType), MediaDbDao.Properties.PlayedCounts.ge(1)).list();
        return popularList;
    }

    public List<MediaDb> queryLastPlayedByType(int mediaType) {
        List<MediaDb> lastPlayedList = ((GlobalApplication) GlobalApplication.getGlobalContext()).getMediaDbDao()
                .queryBuilder()
                .where(MediaDbDao.Properties.MediaType.eq(mediaType))
                .orderDesc(MediaDbDao.Properties.PlayedTime)
                .list();
        return lastPlayedList;
    }


    public ArrayList<AudioItem> getFavoriteAudio() {
        ArrayList<AudioItem> audioItemArrayList = new ArrayList<>();
        ArrayList<BaseItem> baseItemArrayList = browserMediaFile.getMediaFile(AUDIO_LIST);
        List<MediaDb> mediaDbList = queryFavoriteByType(AUDIO_TYPE);
        if ((baseItemArrayList.size() > 0) && (mediaDbList.size() > 0)) {
            for (int index = 0; index < mediaDbList.size(); index++) {
                for (int i = 0; i < baseItemArrayList.size(); i++) {
                    if (mediaDbList.get(index).getMediaPath().equals(baseItemArrayList.get(i).getPath())) {
                        audioItemArrayList.add((AudioItem) baseItemArrayList.get(i));
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
        ntf.notifyAllObservers(AUDIO_LOADED_COMPLETED_ID, "");
        return audioItemArrayList;
    }

    public ArrayList<VideoItem> getFavoriteVideo() {
        ArrayList<VideoItem> audioItemArrayList = new ArrayList<>();
        ArrayList<BaseItem> baseItemArrayList = browserMediaFile.getMediaFile(VIDEO_LIST);
        List<MediaDb> mediaDbList = queryFavoriteByType(VIDEO_TYPE);
        if ((baseItemArrayList.size() > 0) && (mediaDbList.size() > 0)) {
            for (int index = 0; index < mediaDbList.size(); index++) {
                for (int i = 0; i < baseItemArrayList.size(); i++) {
                    if (mediaDbList.get(index).getMediaPath().equals(baseItemArrayList.get(i).getPath())) {
                        audioItemArrayList.add((VideoItem) baseItemArrayList.get(i));
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
        ntf.notifyAllObservers(VIDEO_LOADED_COMPLETED_ID, "");
        return audioItemArrayList;
    }

    public ArrayList<PhotoItem> getFavoritePhoto() {
        ArrayList<PhotoItem> photoItemArrayList = new ArrayList<>();
        ArrayList<BaseItem> baseItemArrayList = browserMediaFile.getMediaFile(PHOTO_LIST);
        List<MediaDb> mediaDbList = queryFavoriteByType(PHOTO_TYPE);
        if ((baseItemArrayList.size() > 0) && (mediaDbList.size() > 0)) {
            for (int index = 0; index < mediaDbList.size(); index++) {
                for (int i = 0; i < baseItemArrayList.size(); i++) {
                    if (mediaDbList.get(index).getMediaPath().equals(baseItemArrayList.get(i).getPath())) {
                        photoItemArrayList.add((PhotoItem) baseItemArrayList.get(i));
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
        ntf.notifyAllObservers(PHOTO_LOADED_COMPLETED_ID, "");
        return photoItemArrayList;
    }

    public ArrayList<AudioItem> getPopularAudio() {
        ArrayList<AudioItem> audioItemArrayList = new ArrayList<>();
        ArrayList<BaseItem> baseItemArrayList = browserMediaFile.getMediaFile(AUDIO_LIST);
        List<MediaDb> mediaDbList = queryPopularByType(AUDIO_TYPE);
        if ((baseItemArrayList.size() > 0) && (mediaDbList.size() > 0)) {
            for (int index = 0; index < mediaDbList.size(); index++) {
                for (int i = 0; i < baseItemArrayList.size(); i++) {
                    if (mediaDbList.get(index).getMediaPath().equals(baseItemArrayList.get(i).getPath())) {
                        audioItemArrayList.add((AudioItem) baseItemArrayList.get(i));
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
        ntf.notifyAllObservers(AUDIO_LOADED_COMPLETED_ID, "");
        return audioItemArrayList;
    }

    public ArrayList<VideoItem> getPopularVideo() {
        ArrayList<VideoItem> audioItemArrayList = new ArrayList<>();
        ArrayList<BaseItem> baseItemArrayList = browserMediaFile.getMediaFile(VIDEO_LIST);
        List<MediaDb> mediaDbList = queryPopularByType(VIDEO_TYPE);
        if ((baseItemArrayList.size() > 0) && (mediaDbList.size() > 0)) {
            for (int index = 0; index < mediaDbList.size(); index++) {
                for (int i = 0; i < baseItemArrayList.size(); i++) {
                    if (mediaDbList.get(index).getMediaPath().equals(baseItemArrayList.get(i).getPath())) {
                        audioItemArrayList.add((VideoItem) baseItemArrayList.get(i));
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
        ntf.notifyAllObservers(AUDIO_LOADED_COMPLETED_ID, "");
        return audioItemArrayList;
    }

    public ArrayList<PhotoItem> getPopularPhoto() {
        ArrayList<PhotoItem> photoItemArrayList = new ArrayList<>();
        ArrayList<BaseItem> baseItemArrayList = browserMediaFile.getMediaFile(PHOTO_LIST);
        List<MediaDb> mediaDbList = queryPopularByType(PHOTO_TYPE);
        if ((baseItemArrayList.size() > 0) && (mediaDbList.size() > 0)) {
            for (int index = 0; index < mediaDbList.size(); index++) {
                for (int i = 0; i < baseItemArrayList.size(); i++) {
                    if (mediaDbList.get(index).getMediaPath().equals(baseItemArrayList.get(i).getPath())) {
                        photoItemArrayList.add((PhotoItem) baseItemArrayList.get(i));
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
        ntf.notifyAllObservers(PHOTO_LOADED_COMPLETED_ID, "");
        return photoItemArrayList;
    }

    public ArrayList<AudioItem> getLastPlayedAudio() {
        ArrayList<AudioItem> audioItemArrayList = new ArrayList<>();
        ArrayList<BaseItem> baseItemArrayList = browserMediaFile.getMediaFile(AUDIO_LIST);
        List<MediaDb> mediaDbList = queryLastPlayedByType(AUDIO_TYPE);
        if ((baseItemArrayList.size() > 0) && (mediaDbList.size() > 0)) {
            for (int index = 0; index < mediaDbList.size(); index++) {
                for (int i = 0; i < baseItemArrayList.size(); i++) {
                    if (mediaDbList.get(index).getMediaPath().equals(baseItemArrayList.get(i).getPath())) {
                        audioItemArrayList.add((AudioItem) baseItemArrayList.get(i));
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
        ntf.notifyAllObservers(AUDIO_LOADED_COMPLETED_ID, "");
        return audioItemArrayList;
    }

    public ArrayList<VideoItem> getLastPlayedVideo() {
        ArrayList<VideoItem> audioItemArrayList = new ArrayList<>();
        ArrayList<BaseItem> baseItemArrayList = browserMediaFile.getMediaFile(VIDEO_LIST);
        List<MediaDb> mediaDbList = queryLastPlayedByType(VIDEO_TYPE);
        if ((baseItemArrayList.size() > 0) && (mediaDbList.size() > 0)) {
            for (int index = 0; index < mediaDbList.size(); index++) {
                for (int i = 0; i < baseItemArrayList.size(); i++) {
                    if (mediaDbList.get(index).getMediaPath().equals(baseItemArrayList.get(i).getPath())) {
                        audioItemArrayList.add((VideoItem) baseItemArrayList.get(i));
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
        ntf.notifyAllObservers(AUDIO_LOADED_COMPLETED_ID, "");
        return audioItemArrayList;
    }

    public ArrayList<PhotoItem> getLastPlayedPhoto() {
        ArrayList<PhotoItem> photoItemArrayList = new ArrayList<>();
        ArrayList<BaseItem> baseItemArrayList = browserMediaFile.getMediaFile(PHOTO_LIST);
        List<MediaDb> mediaDbList = queryLastPlayedByType(PHOTO_TYPE);
        if ((baseItemArrayList.size() > 0) && (mediaDbList.size() > 0)) {
            for (int index = 0; index < mediaDbList.size(); index++) {
                for (int i = 0; i < baseItemArrayList.size(); i++) {
                    if (mediaDbList.get(index).getMediaPath().equals(baseItemArrayList.get(i).getPath())) {
                        photoItemArrayList.add((PhotoItem) baseItemArrayList.get(i));
                        break;
                    } else {
                        continue;
                    }
                }
            }
        }
        ntf.notifyAllObservers(PHOTO_LOADED_COMPLETED_ID, "");
        return photoItemArrayList;
    }
}
