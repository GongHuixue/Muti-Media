package com.example.android.multmedia.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.multmedia.R;

import java.util.List;

/**
 * Created by huixue.gong on 2018/4/2.
 */

public class FragListItemAdapter extends ArrayAdapter<BaseFragment.FragListItem> {
    private int resourceId;

    public FragListItemAdapter(Context context, int textViewResourceId, List<BaseFragment.FragListItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseFragment.FragListItem mediaItem = getItem(position);
        View view;

        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }

        ImageView MediaItemImage = view.findViewById(R.id.media_item_image);
        TextView MediaItemName = view.findViewById(R.id.media_item_name);
        MediaItemImage.setImageResource(mediaItem.getImageId());
        MediaItemName.setText(mediaItem.getItemName());

        return view;
    }
}
