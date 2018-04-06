package com.example.android.multmedia.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.android.multmedia.R;

import java.util.ArrayList;

/**
 * Created by huixue.gong on 2018/3/29.
 */

public class MediaFragment extends BaseFragment {
    private static final String TAG = MediaFragment.class.getSimpleName();
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
        fragList = new ArrayList<>();
        fragListItemAdapter = new FragListItemAdapter(this.getContext(),
                R.layout.media_item, fragList);
        initFragmentData();
        ListView listView = view.findViewById(R.id.media_item_list_view);
        listView.setAdapter(fragListItemAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragListItem mediaItem = fragList.get(position);
                Log.d(TAG, "Current Selected Item is = " + mediaItem);
                //Toast.makeText(MainActivity.this, mediaItem.getItemName(), Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    private void initFragmentData() {
        FragListItem favourites = new FragListItem("Favorite", R.drawable.favourty);
        FragListItem popular = new FragListItem("Most Popular", R.drawable.popular);
        FragListItem lastPlayed = new FragListItem("Last Played", R.drawable.play);
        FragListItem settings = new FragListItem("Settings", R.drawable.setting);

        fragList.add(favourites);
        fragList.add(popular);
        fragList.add(lastPlayed);
        fragList.add(settings);
    }
}
