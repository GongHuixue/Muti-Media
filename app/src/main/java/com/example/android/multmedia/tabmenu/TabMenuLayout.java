package com.example.android.multmedia.tabmenu;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class TabMenuLayout extends LinearLayout implements View.OnClickListener {
    private ArrayList<TableItem> tableItems;
    private OnTabClickListener tabClickLister;
    private Context mContext;
    private View mSelectView;
    private int tabCount;

    public TabMenuLayout(Context context) {
        this(context, null);
    }

    public TabMenuLayout(Context context, AttributeSet atts) {
        this(context, atts, 0);
    }

    public TabMenuLayout(Context context, AttributeSet atts, int defStyle) {
        super(context, atts, defStyle);
        mContext = context;
        setOrientation(HORIZONTAL);
    }

    public void setCurrentTab(int i) {
        if (i < tabCount && i >= 0) {
            View view = getChildAt(i);
            if (mSelectView != view) {
                view.setSelected(true);
                if (mSelectView != null) {
                    mSelectView.setSelected(false);
                }
                mSelectView = view;
            }
        }
    }

    public void initData(ArrayList<TableItem> tabs, OnTabClickListener listener) {
        tableItems = tabs;
        tabClickLister = listener;

        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        Point point = getDefaultHW(mContext);

        if (tabs != null && (tabs.size() > 0)) {
            tabCount = tabs.size();
            TabView mTabView;

            for (int i = 0; i < tabs.size(); i++) {
                mTabView = new TabView(getContext());
                mTabView.setTag(tabs.get(i));
                mTabView.initTabItemData(tabs.get(i));
                mTabView.setOnClickListener(this);
                addView(mTabView, params);
            }
            setCurrentTab(0); //set the first highlight as default;
        } else {
            throw new IllegalArgumentException("tabs is null");
        }
    }

    private Point getDefaultHW(Context context) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = getContext().getResources().getDisplayMetrics().widthPixels;
        int height = getContext().getResources().getDisplayMetrics().heightPixels;
        return new Point(width, height);
    }

    @Override
    public void onClick(View view) {
        tabClickLister.onTabClick((TableItem) view.getTag());
    }

    public interface OnTabClickListener {
        void onTabClick(TableItem tableItem);
    }
}
