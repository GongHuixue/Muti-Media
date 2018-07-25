package com.example.android.multmedia.player.photo;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.mediaload.bean.PhotoItem;

import java.util.ArrayList;

public class PhotoViewAdapter extends PagerAdapter {
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
        PhotoView photoView = new PhotoView(context);
        Glide.with(context)
                .load("file://" + photoList.get(position).getPath())
                .into(photoView);
        container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
