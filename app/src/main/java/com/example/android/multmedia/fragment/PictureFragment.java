package com.example.android.multmedia.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.multmedia.MediaSourceItem;
import com.example.android.multmedia.MediaSourceItemAdapter;
import com.example.android.multmedia.R;

import java.util.ArrayList;

/**
 * Created by huixue.gong on 2018/3/29.
 */

public class PictureFragment extends BaseFragment{
    private ArrayList<MediaSourceItem> pictureList;
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
        View view = inflater.inflate(R.layout.picture_fragment, container, false);
        pictureList = new ArrayList<>();
        adapter = new MediaSourceItemAdapter(this.getContext(),
                R.layout.media_item, pictureList);
        initFragmentData();
        ListView listView = view.findViewById(R.id.media_item_list_view);
        listView.setAdapter(adapter);
        return view;
    }

    private void initFragmentData() {
        MediaSourceItem allPictures = new MediaSourceItem("All Picture Files", R.drawable.ic_tab_picture);
        pictureList.add(allPictures);
    }
}
