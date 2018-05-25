package com.mediaload;


import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.mediaload.callback.OnAudioLoadCallBack;
import com.mediaload.callback.OnLoadCallBack;
import com.mediaload.callback.OnPhotoLoadCallBack;
import com.mediaload.callback.OnVideoLoadCallBack;
import com.mediaload.loader.AbsLoaderCallBack;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class MediaLoad {
    private final String TAG = MediaLoad.class.getSimpleName();
    private Map<String, LoadTask> mTask = new HashMap<>();
    private Map<String, Integer> mLoadId = new HashMap<>();

    private static MediaLoad mediaLoadInstance;

    private final int MSG_LOAD_START = 101;
    private final int MSG_LOAD_STOP = 102;
    private final int MSG_LOAD_FINISH = 103;

    private final int MEDIA_LOAD_ID = 1000;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String activityName;
            LoadTask loadTask;
            Log.d(TAG, "Handle Msg = " + msg.what + ", FragmentActivity = " + (String)msg.obj);
            switch (msg.what) {
                case MSG_LOAD_START:
                    activityName = (String)msg.obj;
                    if(!TextUtils.isEmpty(activityName)) {
                        loadTask = mTask.get(activityName);
                        if(loadTask != null) {
                            queueLoad(loadTask.activityWeakReference.get(), loadTask.onLoadCallBack);
                        }
                    }
                    break;
                case MSG_LOAD_STOP:
                    activityName = (String)msg.obj;
                    if(!TextUtils.isEmpty(activityName)) {
                        loadTask = mTask.get(activityName);
                        int loadId = mLoadId.get(activityName);

                        if((loadTask != null) && (loadTask.activityWeakReference.get() != null)){
                            loadTask.activityWeakReference.get().getSupportLoaderManager().destroyLoader(loadId);
                        }
                        mTask.clear();
                        mLoadId.clear();
                    }
                    break;
                case MSG_LOAD_FINISH:
                    mTask.clear();
                    mLoadId.clear();
                    break;
                default:
                    break;
            }
        }
    };

    private int createLoadId(FragmentActivity activity) {
        String activityName = activity.getClass().getSimpleName();
        int id;
        if(!mLoadId.containsKey(activityName)) {
            id = MEDIA_LOAD_ID;
            mLoadId.put(activityName, id);
        }else {
            int preId = mLoadId.get(activityName);
            preId++;
            mLoadId.put(activityName, preId);
            id = preId;
        }
        return id;
    }

    private MediaLoad() {

    }

    public static MediaLoad getMediaLoad(){
        if(mediaLoadInstance == null) {
            mediaLoadInstance = new MediaLoad();
        }
        return mediaLoadInstance;
    }

    public void loadPhotos(FragmentActivity activity, OnPhotoLoadCallBack loadCallBack) {
        startLoad(activity, loadCallBack);
    }

    public void loadVideos(FragmentActivity activity, OnVideoLoadCallBack loadCallBack) {
        startLoad(activity, loadCallBack);
    }

    public void loadAudios(FragmentActivity activity, OnAudioLoadCallBack loadCallBack) {
        startLoad(activity, loadCallBack);
    }

    private synchronized void startLoad(FragmentActivity activity, OnLoadCallBack loadCallBack) {
        String activityName = activity.getClass().getSimpleName();
        LoadTask task = new LoadTask(new WeakReference<FragmentActivity>(activity), loadCallBack);

        if(!mTask.containsKey(activityName)) {
            mTask.put(activityName, task);

            /*send start load message*/
            Message message = Message.obtain();
            message.what = MSG_LOAD_START;
            message.obj = activityName;
            mHandler.sendMessage(message);
        }
    }

    public void stopLoad(FragmentActivity activity) {

    }

    private void queueLoad(final FragmentActivity activity, OnLoadCallBack loadCallBack) {
        loadMedia(activity, new AbsLoaderCallBack(activity, loadCallBack){
            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
                super.onLoadFinished(loader, data);
                Message message = Message.obtain();
                message.what = MSG_LOAD_FINISH;
                message.obj = activity.getClass().getSimpleName();
                mHandler.sendMessage(message);
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                super.onLoaderReset(loader);
                LoadTask task = mTask.get(activity.getClass().getSimpleName());
                if(task != null) {
                    mTask.clear();
                }
            }
        });
    }

    private void loadMedia(FragmentActivity activity, AbsLoaderCallBack absLoaderCallBack) {
        activity.getSupportLoaderManager().restartLoader(createLoadId(activity), null, absLoaderCallBack);
    }

    public static class LoadTask{
        public WeakReference<FragmentActivity> activityWeakReference;
        public OnLoadCallBack onLoadCallBack;

        public LoadTask(WeakReference<FragmentActivity> activity, OnLoadCallBack loadCallBack) {
            this.activityWeakReference = activity;
            this.onLoadCallBack = loadCallBack;
        }
    }
}
