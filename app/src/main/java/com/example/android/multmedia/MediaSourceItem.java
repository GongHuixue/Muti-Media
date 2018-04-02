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

public class MediaSourceItem{
    private int imageId;
    private String itemName;

    public MediaSourceItem(String name, int image) {
        this.itemName = name;
        this.imageId = image;
    }

    public int getImageId() {
        return imageId;
    }

    public String getItemName() {
        return itemName;
    }
}
