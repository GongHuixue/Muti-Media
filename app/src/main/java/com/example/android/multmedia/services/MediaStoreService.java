package com.example.android.multmedia.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class MediaStoreService extends Service {
    private final static String TAG = MediaStoreService.class.getSimpleName();

    private MediaServiceBinder IBinder = new MediaServiceBinder();
    public MediaStoreService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return IBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class MediaServiceBinder extends Binder {
        public MediaStoreService getMediaService() {
            return MediaStoreService.this;
        }
    }

    public void getFavoriteMedia() {

    }

    public void getPopularMedia() {

    }

    public void getPlayedMedia() {

    }

}
