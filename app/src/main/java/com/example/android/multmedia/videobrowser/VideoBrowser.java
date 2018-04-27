package com.example.android.multmedia.videobrowser;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.app.LoaderManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.multmedia.MainActivity;
import com.example.android.multmedia.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huixue.gong on 2018/4/10.
 */

public class VideoBrowser implements LoaderManager.LoaderCallbacks<Cursor> {
    private final static String TAG = VideoBrowser.class.getSimpleName();

    private Context mContext;
    private MainActivity mActivity;
    private View mRetView;
    private RecyclerView mView;
    private VideoBrowserAdapter mAdapter;
    private Cursor mCursor;
    private int mNumColumns;
    private static VideoBrowser mVbInstance = null;
    private int mSortOrder = VbConstants.DEFAULT_VB_SORT_ORDER;

    private VideoBrowser(Context context) {
        mContext = context;
        mActivity = (MainActivity) context;
    }

    public static VideoBrowser getVbInstance(Context context) {
        if ((mVbInstance == null) && (context != null)){
            mVbInstance = new VideoBrowser(context);
        }
        return mVbInstance;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle) {
        Log.d(TAG, "onCreateLoader");

        Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.Video.Media.DATA,
                MediaStore.Video.Media._ID, MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.DATE_TAKEN, MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.DURATION };
        String mntPath = null;
        String selection = MediaStore.Video.Media.DATA + " LIKE '" + mntPath+"/%'"
                +" AND "+ MediaStore.Video.Media.DATA + " NOT LIKE '" + mntPath + "/%.divx'";

        return new CursorLoader(mContext, uri, projection, selection, null,
                VbConstants.ORDER_BY[mSortOrder]);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished...");
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset");
    }

    /*get recycler view layout*/
    public View getRecyclerViewLayout() {
        /*
        if(mCursor != null && mCursor.getCount() == 0) {
            return null;
        }

        if(mNumColumns != VbConstants.COLUMNS_GRID_EXPANDED) {
            mActivity.getLoaderManager().restartLoader(VbConstants.VB_LOADER_ID, null, this);

            mRetView = mActivity.getLayoutInflater().inflate(R.layout.activity_video_browser, null);
            mView = mRetView.findViewById(R.id.vid_recycler_view);
        }*/
        mAdapter = new VideoBrowserAdapter(getData());

        mRetView = mActivity.getLayoutInflater().inflate(R.layout.activity_video_browser, null);
        mView = mRetView.findViewById(R.id.vid_recycler_view);
        mView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mView.setItemAnimator(new DefaultItemAnimator());
        mView.setAdapter(mAdapter);

        return mRetView;
    }

    /*get recycler view id*/
    public RecyclerView getRecyclerViewInstance() {
        return mView;
    }

    private ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        String temp = " TextView ";

        for(int i = 0; i < 50; i++) {
            data.add(temp + i);
        }
        return data;
    }

}
