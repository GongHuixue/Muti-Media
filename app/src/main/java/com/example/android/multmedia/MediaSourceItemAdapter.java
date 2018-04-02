package com.example.android.multmedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by huixue.gong on 2018/4/2.
 */

public class MediaSourceItemAdapter extends ArrayAdapter<MediaSourceItem> {
    private int resourceId;

    public MediaSourceItemAdapter(Context context, int textViewResourceId, List<MediaSourceItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MediaSourceItem mediaItem = getItem(position);
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
