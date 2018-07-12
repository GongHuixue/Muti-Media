package com.example.android.multmedia.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.example.android.multmedia.Constant;
import com.example.android.multmedia.R;
import com.example.android.multmedia.adpter.FragListItemAdapter;

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
        View view = inflater.inflate(R.layout.fragment_list_item, container, false);
        fragList = new ArrayList<>();
        fragListItemAdapter = new FragListItemAdapter(this.getContext(),
                R.layout.media_item, fragList);
        initFragmentData();
        ListView listView = (ListView) view.findViewById(R.id.media_item_list_view);
        listView.setAdapter(fragListItemAdapter);
        listView.setOnItemClickListener(onItemClickListener);
        return view;
    }

    private void initFragmentData() {
        FragListItem favourites = new FragListItem(Constant.FAVORITE_FILES, R.drawable.favorite);
        FragListItem popular = new FragListItem(Constant.POPULAR_FILES, R.drawable.popular);
        FragListItem lastPlayed = new FragListItem(Constant.LASTED_FILES, R.drawable.play);
        FragListItem onlineMedia = new FragListItem(Constant.ONLINE_MEDIA, R.drawable.online);
        FragListItem settings = new FragListItem(Constant.SETTINGS, R.drawable.setting);

        fragList.add(favourites);
        fragList.add(popular);
        fragList.add(lastPlayed);
        fragList.add(onlineMedia);
        fragList.add(settings);
    }
}
