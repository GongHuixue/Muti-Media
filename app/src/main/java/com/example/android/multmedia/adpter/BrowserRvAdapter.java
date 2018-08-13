package com.example.android.multmedia.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.android.multmedia.R;
import com.example.android.multmedia.browser.AudioBrowserActivity;
import com.example.android.multmedia.browser.PictureBrowserActivity;
import com.example.android.multmedia.browser.VideoBrowserActivity;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.BaseItem;
import com.mediaload.bean.PhotoItem;
import com.mediaload.bean.VideoItem;

import java.util.List;

/**
 * Created by huixue.gong on 2018/4/8.
 */

public class BrowserRvAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener{
    private final static String TAG = BrowserRvAdapter.class.getSimpleName();

    protected List<T> mMediaList;
    protected Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    private static final int VIDEO_BROWSER = 0;
    private static final int PICTURE_BROWSER = 1;
    private static final int AUDIO_BROWSER = 2;

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public interface OnItemLongClickListener{
        boolean onItemLongClick(View view,int position);
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }

    public BrowserRvAdapter(List<T> mediaList, Context context) {
        this.mMediaList = mediaList;
        this.mContext = context;
    }

    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (Integer) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        return mOnItemLongClickListener != null && mOnItemLongClickListener.onItemLongClick(v, (Integer)v.getTag());
    }

    @Override
    public int getItemViewType(int position) {
        Log.d(TAG, "getItemViewType position = " + position);
        int viewType = -1;
        if(mMediaList.size() > 0) {

            viewType = ((BaseItem) mMediaList.get(position)).getViewType();

            Log.d(TAG, "getItemViewType View Type = " + viewType);
        }
        return viewType;
    }

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        View mediaView;
        public ImageView mImageView;
        public TextView mTextView;

        public VideoViewHolder(View view) {
            super(view);
            mediaView = view;
            mImageView = (ImageView) view.findViewById(R.id.image_item);
            mTextView = (TextView) view.findViewById(R.id.text_item);
        }
    }

    public static class AudioViewHolder extends RecyclerView.ViewHolder {
        View mediaView;
        public ImageView mAudioIcon;
        public TextView mAudioName;
        public TextView mAudioSinger;
        public TextView mAudioLength;

        public AudioViewHolder(View view) {
            super(view);
            mediaView = view;
            mAudioIcon = (ImageView) itemView.findViewById(R.id.audio_icon);
            mAudioName = (TextView) itemView.findViewById(R.id.audio_name);
            mAudioSinger = (TextView) itemView.findViewById(R.id.audio_singer);
            mAudioLength = (TextView) itemView.findViewById(R.id.audio_length);
        }
    }

    public static class PictureViewHolder extends RecyclerView.ViewHolder {
        View mediaView;
        public ImageView mImageView;
        public TextView mTextView;

        public PictureViewHolder(View view) {
            super(view);
            mediaView = view;
            mImageView = (ImageView) view.findViewById(R.id.image_item);
            mTextView = (TextView) view.findViewById(R.id.text_item);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder, viewType = " + viewType);
        View root;
        RecyclerView.ViewHolder viewHolder;
        if((viewType == VIDEO_BROWSER) ){
            root = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
            viewHolder = new VideoViewHolder(root);
            root.setOnClickListener(this);
            root.setOnLongClickListener(this);
        }else if((viewType == PICTURE_BROWSER) ) {
            root = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
            viewHolder = new PictureViewHolder(root);
            root.setOnClickListener(this);
            root.setOnLongClickListener(this);
        }else if((viewType == AUDIO_BROWSER)){
            root = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_list_item, parent, false);
            viewHolder = new AudioViewHolder(root);
            root.setOnClickListener(this);
            root.setOnLongClickListener(this);
        }else {
            return null;
        }

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if(mMediaList != null) {
            Log.d(TAG, "getItemCount = " + mMediaList.size());
            return mMediaList.size();
        }else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder != null) {
            if (holder instanceof VideoViewHolder) {
                VideoItem videoItem = (VideoItem) mMediaList.get(position);
                Glide.with(mContext)
                        .load("file://" + videoItem.getPath())
                        .centerCrop()
                        .thumbnail(0.1f)
                        .into(((VideoViewHolder) holder).mImageView);
                ((VideoViewHolder) holder).mediaView.setTag(position);
            } else if (holder instanceof PictureViewHolder) {
                PhotoItem photoItem = (PhotoItem) mMediaList.get(position);
                Glide.with(mContext)
                        .load("file://" + photoItem.getPath())
                        .centerCrop()
                        .thumbnail(0.1f)
                        .into(((PictureViewHolder) holder).mImageView);
                ((PictureViewHolder) holder).mediaView.setTag(position);
            } else if (holder instanceof AudioViewHolder) {
                AudioItem audioItem = (AudioItem) mMediaList.get(position);
                Glide.with(mContext)
                        .loadFromMediaStore(audioItem.getAlbumIconUri(audioItem.getAlbumId()))
                        .asBitmap()
                        .placeholder(R.drawable.record)
                        .into(((AudioViewHolder) holder).mAudioIcon);
                ((AudioViewHolder)holder).mediaView.setTag(position);
                ((AudioViewHolder)holder).mAudioName.setText(audioItem.getDisplayName());
                ((AudioViewHolder)holder).mAudioSinger.setText(audioItem.getSinger());
                ((AudioViewHolder)holder).mAudioLength.setText(audioItem.getDurationString());
            }
        }
    }

    /*short click for play*/
    //public abstract void onItemClickListener();
    /*long click for add/del*/
    //public abstract void onItemLongClickListener();
}
