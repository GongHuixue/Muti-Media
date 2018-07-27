package com.example.android.multmedia.player.photo;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.mediaload.bean.PhotoItem;

import java.util.ArrayList;

public class PhotoViewAdapter extends PagerAdapter {
    private static final String TAG = PhotoViewAdapter.class.getSimpleName();
    private Context context;
    private ArrayList<PhotoItem> photoList = new ArrayList<>();

    public PhotoViewAdapter(Context context, ArrayList<PhotoItem> photos) {
        this.context = context;
        this.photoList.clear();
        this.photoList.addAll(photos);
    }

    @Override
    public int getCount() {
        return photoList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.d(TAG, "instantiateItem");
        PhotoView photoView = new PhotoView(context);
        Glide.with(context)
                .load("file://" + photoList.get(position).getPath())
                .into(photoView);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "instantiateItem");
                ((FragmentActivity)context).finish();
            }
        });

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
