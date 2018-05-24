package com.example.android.multmedia.tabmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.multmedia.R;

/**
 * Created by huixue.gong on 2018/3/30.
 */

public class TabView extends RelativeLayout{
    private static final String TAG = TabView.class.getSimpleName();
    private ImageView mTabImage;
    private TextView mTabText;

    public TabView(Context context) {
        super(context);
        initView(context);
    }

    public TabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TabView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        setGravity(Gravity.CENTER);
        LayoutInflater.from(context).inflate(R.layout.tab_view, this, true);
        mTabImage = (ImageView) findViewById(R.id.tab_image);
        mTabText = (TextView) findViewById(R.id.tab_lable);
    }

    public void initTabItemData(TableItem tabItem) {
        mTabImage.setImageResource(tabItem.imageResId);
        mTabText.setText(tabItem.lableResId);
    }
}
