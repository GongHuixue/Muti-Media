package com.example.android.multmedia.player;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.widget.TextView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.player.audio.AudioRvAdapter;
import com.mediaload.bean.AudioResult;
import com.mediaload.callback.OnAudioLoadCallBack;

import java.util.ArrayList;
import java.util.List;

public class AudioPlayerPlayerActivity extends BasePlayerActivity {
    private List<String> audioList = new ArrayList<>();
    private DividerItemDecoration verticalDivider;
    private DividerItemDecoration horizontalDivider;

    @Override
    public int getLayoutResID() {
        return R.layout.activity_audio_player;
    }

    @Override
    public void initView() {
        /*get total audio nums*/
        final TextView audioNum = (TextView) findViewById(R.id.audio_num);
        mediaLoad.loadAudios(AudioPlayerPlayerActivity.this, new OnAudioLoadCallBack() {
            @Override
            public void onResult(AudioResult result) {
                audioNum.setText("Audio Files: " + result.getItems().size());
            }
        });
        /*init recycler view*/
        RecyclerView audioRecyclerView = (RecyclerView) findViewById(R.id.audio_recycler_view);
        audioRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        /*add horizontal and vertical divider line*/
        verticalDivider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        audioRecyclerView.addItemDecoration(verticalDivider);
        audioRecyclerView.addItemDecoration(horizontalDivider);

        audioRecyclerView.setAdapter(new AudioRvAdapter(this, getAudioList()));
    }

    private List<String> getAudioList() {
        for (int i = 0; i < 50; i++) {
            audioList.add("audio " + i);
        }
        return audioList;
    }

}
