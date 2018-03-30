package com.example.android.multmedia.fragment;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by huixue.gong on 2018/3/29.
 */

public abstract class BaseFragment extends Fragment {
    protected boolean isViewInit;
    protected boolean isVisiable;
    protected boolean isDataInit;

    public abstract void fetchData();

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


    /*public boolean prepareFetchData() {
        return prepareFetchData(false);
    }*/

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisiable && isViewInit && (!isDataInit || forceUpdate)) {
            fetchData();
            isDataInit = true;
            return true;
        }
        return false;
    }
}
