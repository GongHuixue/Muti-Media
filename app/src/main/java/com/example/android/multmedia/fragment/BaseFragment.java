package com.example.android.multmedia.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;

import com.example.android.multmedia.PanelViewManager;
import com.example.android.multmedia.adpter.FragListItemAdapter;

import java.util.ArrayList;

/**
 * Created by huixue.gong on 2018/3/29.
 */

public abstract class BaseFragment extends android.support.v4.app.Fragment {
    private final static String TAG = BaseFragment.class.getSimpleName();
    protected boolean isViewInit;
    protected boolean isVisiable;
    protected boolean isDataInit;

    protected AdapterView.OnItemClickListener onItemClickListener;
    protected ArrayList<FragListItem> fragList;
    protected FragListItem fragListItem;
    protected FragListItemAdapter fragListItemAdapter;

    public abstract void fetchData();
    public abstract BaseFragment getFragment();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onItemClickListener = PanelViewManager.getPanelViewManager(context);
    }

    @Override
    public void onCreate(Bundle savedInsanceState) {
        super.onCreate(savedInsanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.isViewInit = true;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisiable = isVisibleToUser;
        prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisiable && isViewInit && (!isDataInit || forceUpdate)) {
            fetchData();
            isDataInit = true;
            return true;
        }
        return false;
    }

    public class FragListItem {
        private int imageId;
        private String itemName;

        public FragListItem(String name, int image) {
            this.itemName = name;
            this.imageId = image;
        }

        public int getImageId() {
            return imageId;
        }

        public String getItemName() {
            return itemName;
        }
    }
}
