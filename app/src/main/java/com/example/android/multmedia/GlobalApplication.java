package com.example.android.multmedia;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.multmedia.greendao.DaoMaster;
import com.example.android.multmedia.greendao.DaoSession;
import com.example.android.multmedia.greendao.MediaDbDao;
import com.example.android.multmedia.playedlist.personaldb.MediaDb;

import org.greenrobot.greendao.query.QueryBuilder;

public class GlobalApplication extends Application {
    private static Context mContext;
    private static Application application;

    private DaoMaster.DevOpenHelper mHelper;
    private DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;
    private SQLiteDatabase mediaDB;
    private static MediaDbDao mediaDbDao;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        application = this;

        createDataBase();
    }

    public static Context getGlobalContext() {
        return mContext;
    }

    public void createDataBase() {
        mHelper = new DaoMaster.DevOpenHelper(this, "media.db", null);
        mediaDB = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mediaDB);
        mDaoSession = mDaoMaster.newSession();
        mediaDbDao = mDaoSession.getMediaDbDao();

        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES = true;
    }

    public static MediaDbDao getMediaDbDao() {
        return mediaDbDao;
    }

    public static DaoSession getDaoSession() {
        return mDaoSession;
    }
}
