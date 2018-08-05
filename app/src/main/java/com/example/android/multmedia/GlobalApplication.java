package com.example.android.multmedia;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.android.multmedia.greendao.DaoMaster;
import com.example.android.multmedia.greendao.DaoSession;

public class GlobalApplication extends Application {
    private static Context mContext;
    private static Application application;

    private DaoMaster.DevOpenHelper mHelper;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private SQLiteDatabase mediaDB;


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
        mHelper = new DaoMaster.DevOpenHelper(this, "media_db", null);
        mediaDB = mHelper.getWritableDatabase();
        mDaoMaster = new DaoMaster(mediaDB);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoMaster getDaoMaster() {
        return mDaoMaster;
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public SQLiteDatabase getMediaDB() {
        return mediaDB;
    }
}
