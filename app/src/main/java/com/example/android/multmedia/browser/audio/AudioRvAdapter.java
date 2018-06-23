package com.example.android.multmedia.browser.audio;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.android.multmedia.R;
import com.mediaload.bean.AudioItem;

import java.util.ArrayList;
import java.util.List;


public class AudioRvAdapter extends RecyclerView.Adapter<AudioRvAdapter.AudioViewHolder> {
    private final static String TAG = AudioRvAdapter.class.getSimpleName();
    private Context context;
    private List<AudioItem> audioItem = new ArrayList<>();

    public AudioRvAdapter(Context context, List<AudioItem> audioList) {
        Log.d(TAG, "AudioRvAdapter");
        this.context = context;
        this.audioItem.addAll(audioList);
    }

    public void setAudioList(List<AudioItem> audioList) {
        this.audioItem.addAll(audioList);
    }

    @Override
    public AudioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View contentView = LayoutInflater.from(context).inflate(R.layout.audio_list_item, parent, false);
        return new AudioViewHolder(contentView);
    }

    @Override
    public void onBindViewHolder(AudioViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        AudioItem audio = audioItem.get(position);
        Glide.with(context)
                .loadFromMediaStore(audio.getAlbumIconUri(audio.getAlbumId()))
                .asBitmap()
                .placeholder(R.drawable.record)
                .into(holder.mAudioIcon);

        holder.mAudioName.setText(audio.getDisplayName());
        holder.mAudioSinger.setText(audio.getSinger());
        holder.mAudioLength.setText(audio.getDurationString());

        /*bitmap = audio.getBitmap(context, audio.getId(), audio.getAlbumId());
        holder.mAudioName.setText(audio.getDisplayName());
        holder.mAudioSinger.setText(audio.getSinger());
        holder.mAudioLength.setText(audio.getDurationString());
        holder.mAudioIcon.setImageBitmap(bitmap);*/
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "SIZE = " + audioItem.size());
        return audioItem.size();
    }

    public class AudioViewHolder extends RecyclerView.ViewHolder{
        public ImageView mAudioIcon;
        public TextView mAudioName;
        public TextView mAudioSinger;
        public TextView mAudioLength;


        public AudioViewHolder(View itemView) {
            super(itemView);
            mAudioIcon = (ImageView) itemView.findViewById(R.id.audio_icon);
            mAudioName = (TextView) itemView.findViewById(R.id.audio_name);
            mAudioSinger = (TextView) itemView.findViewById(R.id.audio_singer);
            mAudioLength = (TextView) itemView.findViewById(R.id.audio_length);
        }
    }
}
