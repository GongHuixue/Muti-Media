package com.example.android.multmedia.browser;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.RecyclerViewAdapter;
import com.example.android.multmedia.browser.audio.AudioRvAdapter;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.AudioResult;
import com.mediaload.callback.OnAudioLoadCallBack;

import java.util.ArrayList;
import java.util.List;

public class AudioBrowserActivity extends BaseBrowserActivity {
    private RecyclerViewAdapter<AudioItem> mAudioRvAdapter;
    private AudioRvAdapter mAudioAdapter;
    private List<AudioItem> mAudioItems = new ArrayList<>();


    @Override
    public int getLayoutResID() {
        return R.layout.activity_audio_browser;
    }

    @Override
    public void initView() {
        /*get total audio nums*/
        final TextView audioNum = (TextView) findViewById(R.id.audio_num);
        mRecyclerView = (RecyclerView) findViewById(R.id.audio_recycler_view);
        mediaLoad.loadAudios(AudioBrowserActivity.this, new OnAudioLoadCallBack() {
            @Override
            public void onResult(AudioResult result) {
                audioNum.setText("Audio Files: " + result.getItems().size());
                if(result.getItems().size() > 0) {
                    mAudioItems.clear();
                    mAudioItems.addAll(result.getItems());
                    mAudioAdapter.notifyDataSetChanged();
                }
            }
        });

        /*mAudioRvAdapter = new RecyclerViewAdapter<AudioItem>(mAudioItems, AudioBrowserActivity.this) {
            @Override
            public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
                holder.mImageView.setImageResource(R.drawable.ic_tab_audio);
                holder.mTextView.setText("Audio");
            }
        };
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));*/

        mAudioAdapter = new AudioRvAdapter(AudioBrowserActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AudioBrowserActivity.this));

        /*add horizontal and vertical divider line*/
//        verticalDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
//        mRecyclerView.addItemDecoration(verticalDivider);
        mRecyclerView.addItemDecoration(horizontalDivider);

        mRecyclerView.setAdapter(mAudioAdapter);
    }
}
