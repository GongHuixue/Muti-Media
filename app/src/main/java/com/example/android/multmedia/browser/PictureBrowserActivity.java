package com.example.android.multmedia.browser;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.TextView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.BrowserRvAdapter;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.PhotoResult;
import com.mediaload.callback.OnPhotoLoadCallBack;

import java.util.ArrayList;
import java.util.List;

public class PictureBrowserActivity extends BaseBrowserActivity {
    private final static String TAG = PictureBrowserActivity.class.getSimpleName();
    private BrowserRvAdapter<PhotoItem> mPictureRvAdapter;
    private List<PhotoItem> mPictureItems = new ArrayList<>();


    @Override
    public int getLayoutResID() {
        return R.layout.activity_picture_browser;
    }

    @Override
    public void initView() {
        Log.d(TAG, "PictureBrowserActivity Enter");
        final TextView pictureNum = (TextView) findViewById(R.id.picture_num);
        mRecyclerView = (RecyclerView) findViewById(R.id.picture_recycler_view);
        mediaLoad.loadPhotos(PictureBrowserActivity.this, new OnPhotoLoadCallBack() {
            @Override
            public void onResult(PhotoResult result) {
                pictureNum.setText("Picture Files: " + result.getItems().size());
                if(result.getItems().size()> 0 ) {
                    mPictureItems.clear();
                    mPictureItems.addAll(result.getItems());
                    mPictureRvAdapter.notifyDataSetChanged();
                }
            }
        });

        mPictureRvAdapter = new BrowserRvAdapter<PhotoItem>(mPictureItems, PictureBrowserActivity.this) ;

        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        /*add horizontal and vertical divider line*/
        verticalDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mRecyclerView.addItemDecoration(verticalDivider);
        mRecyclerView.addItemDecoration(horizontalDivider);

        mRecyclerView.setAdapter(mPictureRvAdapter);
    }
}
