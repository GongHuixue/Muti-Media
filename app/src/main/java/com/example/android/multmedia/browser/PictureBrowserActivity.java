package com.example.android.multmedia.browser;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private ImageButton IbDel;
    private ArrayList<String> mSelectList = new ArrayList<>();
    private DeleteMediaTask mDeleteTask;


    @Override
    public int getLayoutResID() {
        return R.layout.activity_picture_browser;
    }

    @Override
    public void initView() {
        TvTitle = (TextView) findViewById(R.id.txt_title);
        TvTitle.setText(R.string.picture);
        TvNum = (TextView) findViewById(R.id.txt_number);
        IbReturn = (ImageButton) findViewById(R.id.ib_back);
        IbReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        IbDel = (ImageButton) findViewById(R.id.btn_del);
        IbDel.setVisibility(View.GONE);
        IbDel.setEnabled(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.picture_recycler_view);
        mediaLoad.loadPhotos(PictureBrowserActivity.this, new OnPhotoLoadCallBack() {
            @Override
            public void onResult(PhotoResult result) {
                TvNum.setText("" + result.getItems().size());
                if (result.getItems().size() > 0) {
                    mPictureItems.clear();
                    mTempList.clear();
                    mPictureItems.addAll(result.getItems());
                    mTempList.addAll(result.getItems());
                    mPictureRvAdapter.notifyDataSetChanged();
                    browserMediaFile.saveMediaFile(mTempList, PHOTO_LIST);
                }
            }
        });

        mPictureRvAdapter = new BrowserRvAdapter<PhotoItem>(mPictureItems, PictureBrowserActivity.this);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        mPictureRvAdapter.setOnItemClickListener(new BrowserRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //clear select media and hide del button.
                mSelectList.clear();
                hideDelButton();

                Intent intent = new Intent(PictureBrowserActivity.this, PhotoPlayerActivity.class);
                intent.putExtra(INTENT_MEDIA_POSITION, position);
                intent.putExtra(INTENT_PHOTO_LIST, mPictureItems);

                startActivity(intent);
            }
        });

        mPictureRvAdapter.setOnItemLongClickListener(new BrowserRvAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(boolean selected, String path) {
                if (selected) {
                    if (!mSelectList.contains(path)) {
                        mSelectList.add(path);
                    }
                } else {
                    if (mSelectList.contains(path)) {
                        mSelectList.remove(path);
                    }
                }

                if (mSelectList.size() == 0) {
                    hideDelButton();
                } else {
                    showDelButton();
                }
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
        mSelectList.clear();
        if (mDeleteTask != null) {
            mDeleteTask.cancel(true);
        }
    }

    private void hideDelButton() {
        IbDel.setVisibility(View.GONE);
        IbDel.setEnabled(false);
    }

    private void showDelButton() {
        IbDel.setVisibility(View.VISIBLE);
        IbDel.setEnabled(true);
        IbDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialog for confirm
                showDelDialog();
            }
        });
    }

    private void showDelDialog() {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(PictureBrowserActivity.this);
        mDialog.setMessage("Are you sure to delete these media file ?");
        mDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mDeleteTask = new DeleteMediaTask();
                mDeleteTask.execute();
            }
        });

        mDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //nothing to do
            }
        });
        mDialog.show();
    }

    private class DeleteMediaTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            mSelectList.clear();
            mPictureRvAdapter.initCheckedList();
            hideDelButton();
            TvNum.setText("" + mPictureItems.size());

            mPictureRvAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver mContentResolver = PictureBrowserActivity.this.getContentResolver();
            String where;
            MediaScanner mediaScanner = new MediaScanner(PictureBrowserActivity.this);
            for (int i = 0; i < mSelectList.size(); i++) {
                for (int j = 0; j < mPictureItems.size(); j++) {
                    if (mSelectList.get(i).equals(mPictureItems.get(j).getPath())) {
                        mPictureItems.remove(j);
                        break;
                    } else {
                        continue;
                    }
                }

                for (int k = 0; k < mTempList.size(); k++) {
                    if (mSelectList.get(i).equals(mTempList.get(k).getPath())) {
                        mTempList.remove(k);
                        break;
                    } else {
                        continue;
                    }
                }

                where = MediaStore.Images.Media.DATA + "='" + mSelectList.get(i) + "'";
                mContentResolver.delete(uri, where, null);
            }
            //after delete, notify to provider
            mediaScanner.scan(mSelectList);

            browserMediaFile.saveMediaFile(mTempList, PHOTO_LIST);
            return null;
        }
    }
}
