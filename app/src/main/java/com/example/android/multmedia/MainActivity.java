package com.example.android.multmedia;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.multmedia.adpter.FragmentAdapter;
import com.example.android.multmedia.fragment.*;
import com.example.android.multmedia.player.videoplayer.VideoPlayerActivity;
import com.example.android.multmedia.tabmenu.TabMenuLayout;
import com.example.android.multmedia.tabmenu.TableItem;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements TabMenuLayout.OnTabClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private TabMenuLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<TableItem> tabs;
    private ProgressDialog progressDialog;
    private BaseFragment fragment;
    FragmentAdapter fgAdapter;
    private PanelViewManager mPanelViewManager;
    private Utility mUtility;
    private Intent intent;


    private volatile int currentFragment = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate Enter");
        initView();

        mPanelViewManager = PanelViewManager.getPanelViewManager(this);
        mUtility = new Utility(this, mPanelViewManager);
    }

    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Log.d(TAG, "Page Selected " + position);
            mTabLayout.setCurrentTab(position);
            handlePageChanged(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void initView() {
        mTabLayout = (TabMenuLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        progressDialog = new ProgressDialog(MainActivity.this);

        tabs = new ArrayList<>();
        tabs.add(new TableItem(R.drawable.selector_tab_media, R.string.media, MediaFragment.class));
        tabs.add(new TableItem(R.drawable.selector_tab_video, R.string.video, VideoFragment.class));
        tabs.add(new TableItem(R.drawable.selector_tab_audio, R.string.audio, AudioFragment.class));
        tabs.add(new TableItem(R.drawable.selector_tab_picture, R.string.picture, PictureFragment.class));

        mTabLayout.initData(tabs, this);
        mTabLayout.setCurrentTab(0); //set media fragment as default.

        fgAdapter = new FragmentAdapter(getSupportFragmentManager(), tabs);
        mViewPager.setAdapter(fgAdapter);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.addOnPageChangeListener(pageChangeListener);
    }

    @Override
    public void onTabClick(TableItem tabItem) {
        int item = tabs.indexOf(tabItem);
        Log.i(TAG, "onTabClick: item=" + item);
        mViewPager.setCurrentItem(item);
    }

    public Utility getUtility() {
        return mUtility;
    }

    private void handlePageChanged(int pageIndex) {
        Log.d(TAG, "handlePageChanged Index = " + pageIndex + ", currentFragment = " + currentFragment);
        currentFragment = pageIndex;
        fragment = (BaseFragment) fgAdapter.getItem(pageIndex);
        Log.d(TAG, "Current fragment is " + fragment);
        mPanelViewManager.setActiveFragment(currentFragment, (BaseFragment) fgAdapter.getItem(currentFragment));
    }

    public void showProgessLoading() {
        progressDialog.setTitle("");
        progressDialog.setMessage("File is loading, please wait ......");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.show();
    }

    public void hideProgressLoading() {
        progressDialog.hide();
    }

    public void startActivity(String activityName) {
        if(activityName.equalsIgnoreCase(Constant.AUDIO_FILES)) {
            Log.d(TAG, "Launch Audio Recycler View");
            //panelView = getVideoBrowserView();
        }else if (activityName.equalsIgnoreCase(Constant.VIDEO_FILES)) {
            Log.d(TAG, "Launch Video Recycler View");
//            panelView = getVideoBrowserView(position);
            intent = new Intent(MainActivity.this, VideoPlayerActivity.class);
            startActivity(intent);

        }else if(activityName.equalsIgnoreCase(Constant.PICTURE_FILES)) {
            Log.d(TAG, "Launch Picture Recycler View");
        }else if(activityName.equalsIgnoreCase(Constant.FAVORITE_FILES)) {
            Log.d(TAG, "Launch Favorite Recycler View");
        }else if(activityName.equalsIgnoreCase(Constant.POPULAR_FILES)) {
            Log.d(TAG, "Launch Popular Recycler View");
        }
    }
}
