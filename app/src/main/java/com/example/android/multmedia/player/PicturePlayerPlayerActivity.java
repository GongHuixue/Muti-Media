package com.example.android.multmedia.player;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.RecyclerViewAdapter;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.PhotoResult;
import com.mediaload.callback.OnPhotoLoadCallBack;

import java.util.ArrayList;
import java.util.List;

public class PicturePlayerPlayerActivity extends BasePlayerActivity {
    private final static String TAG = PicturePlayerPlayerActivity.class.getSimpleName();
    private RecyclerViewAdapter<PhotoItem> mPictureRvAdapter;
    private List<PhotoItem> mPictureItems = new ArrayList<>();


    @Override
    public int getLayoutResID() {
        return R.layout.activity_picture_player;
    }

    @Override
    public void initView() {
        Log.d(TAG, "PicturePlayerPlayerActivity Enter");
        final TextView pictureNum = (TextView) findViewById(R.id.picture_num);
        mRecyclerView = (RecyclerView) findViewById(R.id.picture_recycler_view);
        mediaLoad.loadPhotos(PicturePlayerPlayerActivity.this, new OnPhotoLoadCallBack() {
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

        mPictureRvAdapter = new RecyclerViewAdapter<PhotoItem>(mPictureItems, PicturePlayerPlayerActivity.this) {
            @Override
            public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
                PhotoItem mPhoto = mPictureItems.get(position);
                Glide.with(PicturePlayerPlayerActivity.this)
                        .load("file://" + mPhoto.getPath())
                        .thumbnail(0.1f)
                        .into(holder.mImageView);

                //holder.mTextView.setText(mPhoto.getDisplayName());
            }
        };
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        /*add horizontal and vertical divider line*/
        verticalDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mRecyclerView.addItemDecoration(verticalDivider);
        mRecyclerView.addItemDecoration(horizontalDivider);

        mRecyclerView.setAdapter(mPictureRvAdapter);
    }
}
