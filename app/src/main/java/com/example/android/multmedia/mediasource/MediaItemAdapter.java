package com.example.android.multmedia.mediasource;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.multmedia.R;

import java.util.List;

/**
 * Created by huixue.gong on 2018/4/8.
 */

public class MediaItemAdapter extends RecyclerView.Adapter<MediaItemAdapter.ViewHolder> {

    private List<MediaSource> mMediaList;

    public MediaItemAdapter(List<MediaSource> mediaList) {
        mMediaList = mediaList;
    }
    static class ViewHolder extends RecyclerView.ViewHolder {
        View mediaView;
        ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            mediaView = view;
            mImageView = view.findViewById(R.id.media_item_image);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.media_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MediaSource mediaSource = mMediaList.get(position);
        holder.mImageView.setImageResource(mediaSource.getImageId());
    }

    @Override
    public int getItemCount() {
        return  mMediaList.size();
    }
}
