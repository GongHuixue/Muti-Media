package com.example.android.multmedia;

import android.content.Context;
import android.content.Intent;
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
    private BaseFragment currentActiveFrag;
    private int currentActiveFragId;

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

        try {
            fragListItem = (BaseFragment.FragListItem) listView.getItemAtPosition(position);
        }catch (Exception ex) {
            Log.e(TAG, "get List Item failed " + ex.getMessage());
            return;
        }

        String listItemName = fragListItem.getItemName();

        Log.d(TAG, "Current Selected List Item is " + listItemName);

        mActivity.launchActivity(listItemName);
    }

    public void setActiveFragment(int fragmentId, BaseFragment fragment) {
        currentActiveFragId = fragmentId;
        currentActiveFrag = fragment;
    }

    public BaseFragment getActiveFragment() {
        return currentActiveFrag;
    }
}
