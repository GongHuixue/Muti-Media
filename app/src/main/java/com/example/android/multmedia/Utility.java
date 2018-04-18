package com.example.android.multmedia;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.multmedia.videobrowser.VideoBrowser;

/**
 * Created by huixue.gong on 2018/4/4.
 */

public class Utility {
    private Context mContext;
    private MainActivity mActivity;
    AdapterView.OnItemClickListener mFragItemClick;

    public Utility(Context context, AdapterView.OnItemClickListener itemClick) {
        this.mContext = context;
        this.mActivity = (MainActivity)context;
        this.mFragItemClick = itemClick;
    }

    public View getVideoRecyclerView() {
        VideoBrowser vdBrowser = VideoBrowser.getVbInstance(mContext);
        return vdBrowser.getRecyclerViewLayout();
    }

    public boolean isMediaScanned(Context context) {
        if(context == null) {
            return false;
        }

        SharedPreferences preferences = context.getSharedPreferences(Constant.PREF, Context.MODE_PRIVATE);
        return preferences.getBoolean("", false);
    }

    public View getProgressBarView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.recycler_view_item, null);
        ProgressBar progressBar = layout.findViewById(R.id.loader);
        progressBar.setVisibility(View.VISIBLE);
        Toast.makeText(mContext, "File is Loading, please wait", Toast.LENGTH_SHORT).show();
        return layout;
    }
}
