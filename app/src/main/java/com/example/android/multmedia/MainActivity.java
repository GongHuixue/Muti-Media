package com.example.android.multmedia;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.multmedia.fragment.*;
import com.example.android.multmedia.tabmenu.TabMenuLayout;
import com.example.android.multmedia.tabmenu.TableItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements TabMenuLayout.OnTabClickListener{
    private static final String TAG = MainActivity.class.getSimpleName();
    private TabMenuLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<TableItem> tabs;
    private BaseFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

    }

    private void initView() {
        mTabLayout = (TabMenuLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager)findViewById(R.id.viewpager);

        tabs = new ArrayList<>();
        tabs.add(new TableItem(R.drawable.selector_tab_media, R.string.media, MediaFragment.class));
        tabs.add(new TableItem(R.drawable.selector_tab_video, R.string.video, VideoFragment.class));
        tabs.add(new TableItem(R.drawable.selector_tab_audio, R.string.audio, AudioFragment.class));
        tabs.add(new TableItem(R.drawable.selector_tab_picture, R.string.picture, PictureFragment.class));

        mTabLayout.initData(tabs, this);
        mTabLayout.setCurrentTab(0);


    }


    @Override
    public void onTabClick(TableItem tabItem) {
        int item = tabs.indexOf(tabItem);
        Log.i(TAG, "onTabClick: item=" +item);
        mViewPager.setCurrentItem(item);
    }

    public class FragmentAdapter extends FragmentPagerAdapter {
        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {
            try {
                return tabs.get(index).tagFragmentClass.getClass();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            return fragment;
        }
    }
}
