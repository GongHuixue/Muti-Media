package com.example.android.multmedia.videobrowser;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.multmedia.R;

import java.util.ArrayList;

/**
 * Created by huixue.gong on 2018/4/27.
 */

public class VideoBrowserAdapter extends RecyclerView.Adapter<VideoBrowserAdapter.VideoViewHolder>{
    private ArrayList<String> mData;
    private Context mContext;
    private Cursor mCursor;

    public VideoBrowserAdapter(ArrayList<String> data) {
        this.mData = data;
    }

    public VideoBrowserAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @Override
    public int getItemCount() {
        return (mData == null) ? 0 : mData.size();
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_recycler_view_item, parent, false);
        VideoViewHolder viewHolder = new VideoViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.mTv.setText(mData.get(position));
    }

    public class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView mTv;

        public VideoViewHolder (View itemView) {
            super(itemView);
            mTv = (TextView) itemView.findViewById(R.id.recycler_view_item);
        }
    }
}
