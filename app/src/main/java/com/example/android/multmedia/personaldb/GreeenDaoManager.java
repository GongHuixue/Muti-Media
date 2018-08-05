package com.example.android.multmedia.personaldb;

import android.database.sqlite.SQLiteDatabase;

import com.example.android.multmedia.GlobalApplication;
import com.example.android.multmedia.greendao.DaoMaster;
import com.example.android.multmedia.greendao.DaoSession;
import com.example.android.multmedia.greendao.MediaDbDao;

public class GreeenDaoManager {

    private SQLiteDatabase mediaDb = ((GlobalApplication)GlobalApplication.getGlobalContext()).getMediaDB();
    private DaoMaster daoMaster = ((GlobalApplication)GlobalApplication.getGlobalContext()).getDaoMaster();
    private DaoSession daoSession = ((GlobalApplication)GlobalApplication.getGlobalContext()).getDaoSession();

    public void insert() {

    }

    public void delete() {

    }

    public void update() {

    }

    public void search() {

    }

    public SQLiteDatabase getMediaDb() {
        return mediaDb;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public MediaDbDao getMediaDbDao() {
        return daoSession.getMediaDbDao();
    }
}
