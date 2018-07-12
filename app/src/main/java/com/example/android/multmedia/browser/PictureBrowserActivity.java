package com.example.android.multmedia.browser;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.BrowserRvAdapter;
import com.example.android.multmedia.base.BaseBrowserActivity;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.PhotoResult;
import com.mediaload.callback.OnPhotoLoadCallBack;

import java.util.ArrayList;
import java.util.List;

public class PictureBrowserActivity extends BaseBrowserActivity {
    private final static String TAG = PictureBrowserActivity.class.getSimpleName();
    private BrowserRvAdapter<PhotoItem> mPictureRvAdapter;
    private List<PhotoItem> mPictureItems = new ArrayList<>();
    private TextView TvTitle;
    private TextView TvNum;


    @Override
    public int getLayoutResID() {
        return R.layout.activity_picture_browser;
    }

    @Override
    public void initView() {
        Log.d(TAG, "PictureBrowserActivity Enter");
        TvTitle = (TextView)findViewById(R.id.txt_title);
        TvTitle.setText(R.string.picture);
        TvNum = (TextView)findViewById(R.id.txt_number);
        mRecyclerView = (RecyclerView) findViewById(R.id.picture_recycler_view);
        mediaLoad.loadPhotos(PictureBrowserActivity.this, new OnPhotoLoadCallBack() {
            @Override
            public void onResult(PhotoResult result) {
                TvNum.setText("" + result.getItems().size());
                if(result.getItems().size()> 0 ) {
                    mPictureItems.clear();
                    mPictureItems.addAll(result.getItems());
                    mPictureRvAdapter.notifyDataSetChanged();
                }
            }
        });

        mPictureRvAdapter = new BrowserRvAdapter<PhotoItem>(mPictureItems, PictureBrowserActivity.this) ;
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        mPictureRvAdapter.setOnItemClickListener(new BrowserRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(PictureBrowserActivity.this, "short click " + position, Toast.LENGTH_SHORT).show();
            }
        });

        mPictureRvAdapter.setOnItemLongClickListener(new BrowserRvAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(PictureBrowserActivity.this, "long click " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        /*add horizontal and vertical divider line*/
        verticalDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mRecyclerView.addItemDecoration(verticalDivider);
        mRecyclerView.addItemDecoration(horizontalDivider);

        mRecyclerView.setAdapter(mPictureRvAdapter);
    }
}
