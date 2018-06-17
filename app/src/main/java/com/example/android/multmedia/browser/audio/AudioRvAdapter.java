package com.example.android.multmedia.browser.audio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.android.multmedia.R;

import java.util.ArrayList;
import java.util.List;


public class AudioRvAdapter extends RecyclerView.Adapter<AudioRvAdapter.AudioViewHolder> {
    private final static String TAG = AudioRvAdapter.class.getSimpleName();
    private Context context;
    //private List<String> audioItem = new ArrayList<>();

    public AudioRvAdapter(Context context/*, List<String> audioList*/) {
        Log.d(TAG, "AudioRvAdapter");
        this.context = context;
        //this.audioItem.addAll(audioList);
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
        //String textItem = audioItem.get(position);
        holder.mAudioName.setText("Yesterday once more");
        holder.mAudioSinger.setText("Fany");
        holder.mAudioLength.setText("5:00");
        holder.mAudioIcon.setImageResource(R.drawable.ic_tab_audio);
    }

    @Override
    public int getItemCount() {
        return 30; /*audioItem.size();*/
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
