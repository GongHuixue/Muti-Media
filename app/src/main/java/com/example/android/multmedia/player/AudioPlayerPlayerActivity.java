package com.example.android.multmedia.player;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.RecyclerViewAdapter;
import com.example.android.multmedia.player.audio.AudioRvAdapter;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.AudioResult;
import com.mediaload.callback.OnAudioLoadCallBack;

import java.util.ArrayList;
import java.util.List;

public class AudioPlayerPlayerActivity extends BasePlayerActivity {
    private RecyclerViewAdapter<AudioItem> mAudioRvAdapter;
    private List<AudioItem> mAudioItems = new ArrayList<>();


    @Override
    public int getLayoutResID() {
        return R.layout.activity_audio_player;
    }

    @Override
    public void initView() {
        /*get total audio nums*/
        final TextView audioNum = (TextView) findViewById(R.id.audio_num);
        mRecyclerView = (RecyclerView) findViewById(R.id.audio_recycler_view);
        mediaLoad.loadAudios(AudioPlayerPlayerActivity.this, new OnAudioLoadCallBack() {
            @Override
            public void onResult(AudioResult result) {
                audioNum.setText("Audio Files: " + result.getItems().size());
                if(result.getItems().size() > 0) {
                    mAudioItems.clear();
                    mAudioItems.addAll(result.getItems());
                    mAudioRvAdapter.notifyDataSetChanged();
                }
            }
        });

        mAudioRvAdapter = new RecyclerViewAdapter<AudioItem>(mAudioItems, AudioPlayerPlayerActivity.this) {
            @Override
            public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
                holder.mImageView.setImageResource(R.drawable.ic_tab_audio);
                holder.mTextView.setText("Audio");
            }
        };
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));

        /*add horizontal and vertical divider line*/
        verticalDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mRecyclerView.addItemDecoration(verticalDivider);
        mRecyclerView.addItemDecoration(horizontalDivider);

        mRecyclerView.setAdapter(mAudioRvAdapter);
    }
}
