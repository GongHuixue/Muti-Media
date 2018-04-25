package com.example.android.multmedia;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.multmedia.fragment.BaseFragment;

/**
 * Created by huixue.gong on 2018/4/4.
 */

public class PanelViewManager implements AdapterView.OnItemClickListener{
    private static final String TAG = PanelViewManager.class.getSimpleName();
    private static PanelViewManager mInstance;
    private MainActivity mActivity;
    private int currentActiveFragId;
    private BaseFragment currentActiveFrag;

    private PanelViewManager(Context context) {
        this.mActivity = (MainActivity)context;
    }

    public static synchronized PanelViewManager getPanelViewManager(Context context) {
        if(mInstance == null) {
            mInstance = new PanelViewManager(context);
        }
        return mInstance;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick view = " + view + ", parent = " + parent + ", position = " + position);
        ListView listView = (ListView) parent;
        BaseFragment.FragListItem fragListItem;
        View panelView = null;

        try {
            fragListItem = (BaseFragment.FragListItem) listView.getItemAtPosition(position);
        }catch (Exception ex) {
            Log.e(TAG, "get List Item failed " + ex.getMessage());
            return;
        }

        String listItemName = fragListItem.getItemName();

        Log.d(TAG, "Current Selected List Item is " + listItemName);

        if(listItemName.equalsIgnoreCase(Constant.AUDIO_FILES)) {
            Log.d(TAG, "Launch Audio Recycler View");
            //panelView = getVideoBrowserView();
        }else if (listItemName.equalsIgnoreCase(Constant.VIDEO_FILES)) {
            Log.d(TAG, "Launch Video Recycler View");
            panelView = getVideoBrowserView(position);
        }else if(listItemName.equalsIgnoreCase(Constant.PICTURE_FILES)) {
            Log.d(TAG, "Launch Picture Recycler View");
        }else if(listItemName.equalsIgnoreCase(Constant.FAVORITE_FILES)) {
            Log.d(TAG, "Launch Favorite Recycler View");
        }else if(listItemName.equalsIgnoreCase(Constant.POPULAR_FILES)) {
            Log.d(TAG, "Launch Popular Recycler View");
        }

        if(panelView != null) {
            //LinearLayout linearLayout = (LinearLayout)findViewById(R.id.media_recycler_layout);
        }
    }

    public void setActiveFragment(int fragmentId, BaseFragment fragment) {
        currentActiveFragId = fragmentId;
        currentActiveFrag = fragment;
    }

    public BaseFragment getActiveFragment() {
        return currentActiveFrag;
    }

    private Utility getUtility() {
        return mActivity.getUtility();
    }

    public View getVideoBrowserView(int position) {
        View recyclerView = null;
        boolean isVideoScaned = getUtility().isMediaScanned(mActivity);
        if(!isVideoScaned) {
            /*if the media had not scanned, show the loading progress bar*/
            return getUtility().getProgressBarView(mActivity);
        }
        recyclerView = getUtility().getVideoRecyclerView();
        return recyclerView;
    }
}
