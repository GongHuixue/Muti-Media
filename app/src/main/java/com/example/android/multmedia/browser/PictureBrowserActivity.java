package com.example.android.multmedia.browser;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.BrowserRvAdapter;
import com.example.android.multmedia.base.BaseBrowserActivity;
import com.example.android.multmedia.player.PhotoPlayerActivity;
import com.mediaload.bean.BaseItem;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.PhotoResult;
import com.mediaload.callback.OnPhotoLoadCallBack;

import java.util.ArrayList;

import static com.example.android.multmedia.utils.Constant.PHOTO_LIST;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_MEDIA_POSITION;
import static com.example.android.multmedia.player.MediaPlayConstants.INTENT_PHOTO_LIST;

public class PictureBrowserActivity extends BaseBrowserActivity {
    private final static String TAG = PictureBrowserActivity.class.getSimpleName();
    private BrowserRvAdapter<PhotoItem> mPictureRvAdapter;
    private ArrayList<PhotoItem> mPictureItems = new ArrayList<>();
    private ArrayList<BaseItem> mTempList = new ArrayList<>();
    private TextView TvTitle;
    private TextView TvNum;
    private ImageButton IbReturn;


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
        IbReturn = (ImageButton)findViewById(R.id.ib_back);
        IbReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.picture_recycler_view);
        mediaLoad.loadPhotos(PictureBrowserActivity.this, new OnPhotoLoadCallBack() {
            @Override
            public void onResult(PhotoResult result) {
                TvNum.setText("" + result.getItems().size());
                if(result.getItems().size()> 0 ) {
                    mPictureItems.clear();
                    mTempList.clear();
                    mPictureItems.addAll(result.getItems());
                    mTempList.addAll(result.getItems());
                    mPictureRvAdapter.notifyDataSetChanged();
                    browserMediaFile.saveMediaFile(mTempList, PHOTO_LIST);
                }
            }
        });

        mPictureRvAdapter = new BrowserRvAdapter<PhotoItem>(mPictureItems, PictureBrowserActivity.this) ;
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        mPictureRvAdapter.setOnItemClickListener(new BrowserRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(PictureBrowserActivity.this, PhotoPlayerActivity.class);
                intent.putExtra(INTENT_MEDIA_POSITION, position);
                intent.putExtra(INTENT_PHOTO_LIST, mPictureItems);

                startActivity(intent);
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

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPictureItems.clear();
        mTempList.clear();
    }
}
