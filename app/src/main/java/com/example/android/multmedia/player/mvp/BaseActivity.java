package com.example.android.multmedia.player.mvp;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.example.android.multmedia.services.MediaStoreService;

public abstract class BaseActivity<P extends BaseControl> extends FragmentActivity {
    private final static String TAG = BaseActivity.class.getSimpleName();
    public FragmentActivity fragmentActivity;
    public ProgressDialog progressDialog;
    public P mediaView;

//    private boolean mBound = false;
//    private MediaStoreService mediaService;
//
//    private ServiceConnection mediaServiceCon = new ServiceConnection() {
//        @Override
//        public void onServiceConnected(ComponentName name, IBinder service) {
//            MediaStoreService.MediaServiceBinder binder = (MediaStoreService.MediaServiceBinder) service;
//            mediaService = binder.getMediaService();
//            mBound = true;
//        }
//
//        @Override
//        public void onServiceDisconnected(ComponentName name) {
//            mBound = false;
//        }
//    };
//
//    public MediaStoreService getMediaService() {
//        return mediaService;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        Log.d(TAG, "OnCreate");
        /*The following init flow can't changed*/
        initView();
        mediaView = attachMediaView();
        initData();
        this.fragmentActivity = this;
        //bindMediaService();
        Log.d(TAG, "OnCreate exit");
    }

//    private void bindMediaService() {
//        final Intent intent = new Intent(this, MediaStoreService.class);
//        bindService(intent, mediaServiceCon, BIND_AUTO_CREATE);
//    }
//
//    private void unbindMediaService() {
//        if(mBound == true) {
//            unbindService(mediaServiceCon);
//        }
//        mediaService = null;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaView != null) {
            mediaView.detachView();
        }
        this.fragmentActivity = null;
        //unbindMediaService();
    }

    public abstract P attachMediaView();

    public abstract int getLayoutResID();

    public abstract void initView();

    public abstract void initData();


    public void showLoadingProgressDialog() {
        progressDialog = new ProgressDialog(fragmentActivity);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    public void hideLoadingProgressDialog() {
        if ((progressDialog != null) && (progressDialog.isShowing())) {
            progressDialog.dismiss();
        }
    }
}
