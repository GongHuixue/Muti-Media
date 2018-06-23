package com.example.android.multmedia;

import android.app.Application;
import android.content.Context;

public class GlobalApplication extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getGlobalContext() {
        return mContext;
    }
}
