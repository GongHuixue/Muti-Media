package com.example.android.multmedia.browser;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView TvTitle;
    private TextView TvNum;


    @Override
    public int getLayoutResID() {
        return R.layout.activity_audio_browser;
    }

    @Override
    public void initView() {
        TvTitle = (TextView)findViewById(R.id.txt_title);
        TvTitle.setText(R.string.audio);
        TvNum = (TextView)findViewById(R.id.txt_number);
        mRecyclerView = (RecyclerView) findViewById(R.id.audio_recycler_view);
        mediaLoad.loadAudios(AudioBrowserActivity.this, new OnAudioLoadCallBack() {
            @Override
            public void onResult(AudioResult result) {
                TvNum.setText("" + result.getItems().size());
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

        /*short click*/
        mAudioRvAdapter.setOnItemClickListener(new BrowserRvAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(AudioBrowserActivity.this, "short click " + position, Toast.LENGTH_SHORT).show();
            }
        });

        /*long click*/
        mAudioRvAdapter.setOnItemLongClickListener(new BrowserRvAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                Toast.makeText(AudioBrowserActivity.this, "long click " + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        horizontalDivider = new DividerItemDecoration(this, LinearLayoutManager.HORIZONTAL);
        mRecyclerView.addItemDecoration(horizontalDivider);

        mRecyclerView.setAdapter(mAudioRvAdapter);

    }
}
