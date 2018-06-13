package com.example.android.multmedia.adpter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.mediasource.RecyclerViewItem;

import java.util.List;

/**
 * Created by huixue.gong on 2018/4/8.
 */

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private final static String TAG = RecyclerViewAdapter.class.getSimpleName();

    protected List<T> mMediaList;
    protected Context mContext;
    protected int mLayoutId;

    public RecyclerViewAdapter(List<T> mediaList, Context context) {
        this.mMediaList = mediaList;
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mediaView;
        public ImageView mImageView;
        public TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mediaView = view;
            mImageView = (ImageView) view.findViewById(R.id.image_item);
            mTextView = (TextView) view.findViewById(R.id.text_item);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        //View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_view_item, parent, false);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        RecyclerViewItem recyclerViewItem = mMediaList.get(position);
//        holder.mImageView.setImageResource(recyclerViewItem.getImageId());
//    }

    @Override
    public int getItemCount() {
        return  mMediaList.size();
    }
    /*short click for play*/
    //public abstract void onItemClickListener();
    /*long click for add/del*/
    //public abstract void onItemLongClickListener();
}
