package com.example.android.multmedia.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huixue.gong on 2018/4/8.
 */

public class BrowserRvAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> /*implements
    View.OnClickListener, View.OnLongClickListener*/{
    private final static String TAG = BrowserRvAdapter.class.getSimpleName();

    protected List<T> mMediaList;
    protected Context mContext;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private List<Boolean> mCheckedList = new ArrayList<>();

    private static final int VIDEO_BROWSER = 0;
    private static final int PICTURE_BROWSER = 1;
    private static final int AUDIO_BROWSER = 2;

    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(boolean selected, String path);
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

    private void initCheckedList() {
        mCheckedList.clear();
        for(int i = 0; i < mMediaList.size(); i++) {
            mCheckedList.add(false);
        }
    }

    @Override
    public int getItemViewType(int position) {

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
        public ImageView mImageCheck;
        public TextView mTextView;

        public VideoViewHolder(View view) {
            super(view);
            mediaView = view;
            mImageView = (ImageView) view.findViewById(R.id.image_item);
            mTextView = (TextView) view.findViewById(R.id.text_item);
            mImageCheck = (ImageView)view.findViewById(R.id.image_checked);
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
        public ImageView mImageCheck;

        public PictureViewHolder(View view) {
            super(view);
            mediaView = view;
            mImageView = (ImageView) view.findViewById(R.id.image_item);
            mTextView = (TextView) view.findViewById(R.id.text_item);
            mImageCheck = (ImageView)view.findViewById(R.id.image_checked);
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
        }else if((viewType == PICTURE_BROWSER) ) {
            root = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
            viewHolder = new PictureViewHolder(root);
        }else if((viewType == AUDIO_BROWSER)){
            root = LayoutInflater.from(parent.getContext()).inflate(R.layout.audio_list_item, parent, false);
            viewHolder = new AudioViewHolder(root);
        }else {
            return null;
        }

        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if((mMediaList != null) && (mMediaList.size() > 0)) {
            Log.d(TAG, "getItemCount = " + mMediaList.size());
            initCheckedList();
            return mMediaList.size();
        }else {
            return 0;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder != null) {
            if (holder instanceof VideoViewHolder) {
                final VideoItem videoItem = (VideoItem) mMediaList.get(position);
                Glide.with(mContext)
                        .load("file://" + videoItem.getPath())
                        .centerCrop()
                        .thumbnail(0.1f)
                        .into(((VideoViewHolder) holder).mImageView);
                ((VideoViewHolder) holder).mediaView.setTag(position);

                if(mOnItemClickListener != null) {
                    ((VideoViewHolder) holder).mediaView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initCheckedList();
                            ((VideoViewHolder) holder).mImageCheck.setVisibility(View.INVISIBLE);

                            int position = holder.getLayoutPosition();
                            mOnItemClickListener.onItemClick(((VideoViewHolder) holder).mediaView, position);
                        }
                    });
                }

                if(mOnItemLongClickListener != null) {
                    ((VideoViewHolder) holder).mediaView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if(!mCheckedList.get(position)) {
                                mCheckedList.set(position, true);
                                ((VideoViewHolder) holder).mImageCheck.setVisibility(View.VISIBLE);
                                ((VideoViewHolder) holder).mImageCheck.setBackgroundResource(R.drawable.image_select);
                            }else {
                                mCheckedList.set(position, false);
                                ((VideoViewHolder) holder).mImageCheck.setVisibility(View.INVISIBLE);
                            }
                            mCheckedList.set(position, mCheckedList.get(position));

                            //notify listener
                            mOnItemLongClickListener.onItemLongClick(mCheckedList.get(position), videoItem.getPath());
                            return true;
                        }
                    });
                }
            } else if (holder instanceof PictureViewHolder) {
                final PhotoItem photoItem = (PhotoItem) mMediaList.get(position);
                Glide.with(mContext)
                        .load("file://" + photoItem.getPath())
                        .centerCrop()
                        .thumbnail(0.1f)
                        .into(((PictureViewHolder) holder).mImageView);
                ((PictureViewHolder) holder).mediaView.setTag(position);

                if(mOnItemClickListener != null) {
                    ((PictureViewHolder) holder).mediaView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initCheckedList();
                            ((PictureViewHolder) holder).mImageCheck.setVisibility(View.INVISIBLE);

                            int position = holder.getLayoutPosition();
                            mOnItemClickListener.onItemClick(((PictureViewHolder) holder).mediaView, position);
                        }
                    });
                }

                if(mOnItemLongClickListener != null) {
                    ((PictureViewHolder) holder).mediaView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            if(!mCheckedList.get(position)) {
                                mCheckedList.set(position, true);
                                ((PictureViewHolder) holder).mImageCheck.setVisibility(View.VISIBLE);
                                ((PictureViewHolder) holder).mImageCheck.setBackgroundResource(R.drawable.image_select);
                            }else {
                                mCheckedList.set(position, false);
                                ((PictureViewHolder) holder).mImageCheck.setVisibility(View.INVISIBLE);
                            }
                            mCheckedList.set(position, mCheckedList.get(position));

                            //notify listener
                            mOnItemLongClickListener.onItemLongClick(mCheckedList.get(position), photoItem.getPath());
                            return true;
                        }
                    });
                }
            } else if (holder instanceof AudioViewHolder) {
                final AudioItem audioItem = (AudioItem) mMediaList.get(position);
                Glide.with(mContext)
                        .loadFromMediaStore(audioItem.getAlbumIconUri(audioItem.getAlbumId()))
                        .asBitmap()
                        .placeholder(R.drawable.record)
                        .into(((AudioViewHolder) holder).mAudioIcon);
                ((AudioViewHolder)holder).mediaView.setTag(position);
                ((AudioViewHolder)holder).mAudioName.setText(audioItem.getDisplayName());
                ((AudioViewHolder)holder).mAudioSinger.setText(audioItem.getSinger());
                ((AudioViewHolder)holder).mAudioLength.setText(audioItem.getDurationString());


                if(mOnItemClickListener != null) {
                    ((AudioViewHolder) holder).mediaView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int position = holder.getLayoutPosition();
                            mOnItemClickListener.onItemClick(((AudioViewHolder) holder).mediaView, position);
                        }
                    });
                }

                if(mOnItemLongClickListener != null) {
                    ((AudioViewHolder) holder).mediaView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
//                            if(!mCheckedList.get(position)) {
//                                mCheckedList.set(position, true);
//                                ((AudioViewHolder) holder).mImageCheck.setVisibility(View.VISIBLE);
//                                ((AudioViewHolder) holder).mImageCheck.setBackgroundResource(R.drawable.image_select);
//                            }else {
//                                mCheckedList.set(position, false);
//                                ((AudioViewHolder) holder).mImageCheck.setVisibility(View.INVISIBLE);
//                            }
//                            mCheckedList.set(position, mCheckedList.get(position));
//
//                            //notify listener
                            mOnItemLongClickListener.onItemLongClick(mCheckedList.get(position), audioItem.getPath());
                            return true;
                        }
                    });
                }
            }
        }
    }
}
