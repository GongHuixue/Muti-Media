package com.example.android.multmedia.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.android.multmedia.MainActivity;
import com.example.android.multmedia.MediaSourceItem;
import com.example.android.multmedia.MediaSourceItemAdapter;
import com.example.android.multmedia.R;

import java.util.ArrayList;

/**
 * Created by huixue.gong on 2018/3/29.
 */

public class MediaFragment extends BaseFragment {
    private static final String TAG = MediaFragment.class.getSimpleName();
    private ArrayList<MediaSourceItem> mediaList;
    private MediaSourceItemAdapter adapter;
    @Override
    public void fetchData() {

    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.media_fragment, container, false);
        mediaList = new ArrayList<>();
        adapter = new MediaSourceItemAdapter(this.getContext(),
                R.layout.media_item, mediaList);
        initFragmentData();
        ListView listView = view.findViewById(R.id.media_item_list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MediaSourceItem mediaItem = mediaList.get(position);
                Log.d(TAG, "Current Selected Item is = " + mediaItem);
                //Toast.makeText(MainActivity.this, mediaItem.getItemName(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void initFragmentData() {
        MediaSourceItem favourites = new MediaSourceItem("Favorite", R.drawable.favourty);
        MediaSourceItem popular = new MediaSourceItem("Most Popular", R.drawable.popular);
        MediaSourceItem lastPlayed = new MediaSourceItem("Last Played", R.drawable.play);
        MediaSourceItem settings = new MediaSourceItem("Settings", R.drawable.setting);

        mediaList.add(favourites);
        mediaList.add(popular);
        mediaList.add(lastPlayed);
        mediaList.add(settings);
    }
}
