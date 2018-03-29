package com.example.android.multmedia.player.fragment;

/**
 * Created by huixue.gong on 2018/3/29.
 */

public class TableItem {
    public int imageResId;
    public int lableResId;

    public Class<? extends BaseFragment> tagFragmentClass;

    public TableItem(int imageId, int lableId) {
        this.imageResId = imageId;
        this.lableResId = lableId;
    }

    public TableItem(int imageId, int lableId, Class<? extends BaseFragment> tagFragment) {
        this.imageResId = imageId;
        this.lableResId = lableId;
        this.tagFragmentClass = tagFragment;
    }
}
