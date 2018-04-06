package com.example.android.multmedia.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.multmedia.R;

import java.util.ArrayList;

/**
 * Created by huixue.gong on 2018/3/29.
 */

public class VideoFragment extends BaseFragment{
    @Override
    public void fetchData() {

    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_fragment, container, false);
        fragList = new ArrayList<>();
        fragListItemAdapter = new FragListItemAdapter(this.getContext(),
                R.layout.media_item, fragList);
        initFragmentData();
        ListView listView = view.findViewById(R.id.media_item_list_view);
        listView.setAdapter(fragListItemAdapter);
        return view;
    }

    private void initFragmentData() {
        fragListItem = new FragListItem("All Video Files", R.drawable.ic_tab_video);
        fragList.add(fragListItem);
    }
}
