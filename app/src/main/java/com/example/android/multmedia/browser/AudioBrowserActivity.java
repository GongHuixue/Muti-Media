package com.example.android.multmedia.browser;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.BrowserRvAdapter;
import com.mediaload.bean.AudioItem;
import com.mediaload.bean.AudioResult;
import com.mediaload.callback.OnAudioLoadCallBack;

import java.util.ArrayList;
import java.util.List;

public class AudioBrowserActivity extends BaseBrowserActivity {
    private final static String TAG = AudioBrowserActivity.class.getSimpleName();
    private BrowserRvAdapter<AudioItem> mAudioRvAdapter;
    private List<AudioItem> mAudioItems = new ArrayList<>();


    @Override
    public int getLayoutResID() {
        return R.layout.activity_audio_browser;
    }

    @Override
    public void initView() {
        //actionBar = getActionBar();
        /*get total audio nums*/
        final TextView audioNum = (TextView) findViewById(R.id.audio_num);
        mRecyclerView = (RecyclerView) findViewById(R.id.audio_recycler_view);
        mediaLoad.loadAudios(AudioBrowserActivity.this, new OnAudioLoadCallBack() {
            @Override
            public void onResult(AudioResult result) {
                audioNum.setText("Audio Files: " + result.getItems().size());
                Log.d(TAG, "Total file size = " + result.getItems().size());
                if(result.getItems().size() > 0) {
                    mAudioItems.clear();
                    mAudioItems.addAll(result.getItems());
                    mAudioRvAdapter.notifyDataSetChanged();
                }
            }
        });

        mAudioRvAdapter = new BrowserRvAdapter<AudioItem>(mAudioItems, AudioBrowserActivity.this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(AudioBrowserActivity.this));

        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mRecyclerView.addItemDecoration(horizontalDivider);

        mRecyclerView.setAdapter(mAudioRvAdapter);

    }
}
