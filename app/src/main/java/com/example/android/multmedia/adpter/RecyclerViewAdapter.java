package com.example.android.multmedia.adpter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.mediasource.RecyclerViewItem;

import java.util.List;

/**
 * Created by huixue.gong on 2018/4/8.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<RecyclerViewItem> mMediaList;

    public RecyclerViewAdapter(List<RecyclerViewItem> mediaList) {
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
        RecyclerViewItem recyclerViewItem = mMediaList.get(position);
        holder.mImageView.setImageResource(recyclerViewItem.getImageId());
    }

    @Override
    public int getItemCount() {
        return  mMediaList.size();
    }
}
