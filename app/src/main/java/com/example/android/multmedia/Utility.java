package com.example.android.multmedia;

import android.content.Context;

import com.example.android.multmedia.fragment.FragItemClickListener;

/**
 * Created by huixue.gong on 2018/4/4.
 */

public class Utility {
    private Context mContext;
    private MainActivity mActivity;
    FragItemClickListener mFragItemClick;

    public Utility(Context context, FragItemClickListener itemClick) {
        this.mContext = context;
        this.mActivity = (MainActivity)context;
        this.mFragItemClick = itemClick;

    }
}
