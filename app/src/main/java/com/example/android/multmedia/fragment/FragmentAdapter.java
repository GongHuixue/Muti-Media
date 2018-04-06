package com.example.android.multmedia.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.multmedia.tabmenu.TableItem;

import java.util.ArrayList;

/**
 * Created by huixue.gong on 2018/4/2.
 */

public class FragmentAdapter extends FragmentPagerAdapter {
    private ArrayList<TableItem> tableItems;
    private BaseFragment fragment;
    public FragmentAdapter(FragmentManager fm, ArrayList<TableItem>tabs) {
        super(fm);
        this.tableItems = tabs;
    }

    @Override
    public Fragment getItem(int arg0) {
        try {
            fragment = tableItems.get(arg0).tagFragmentClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return tableItems.size();
    }
}