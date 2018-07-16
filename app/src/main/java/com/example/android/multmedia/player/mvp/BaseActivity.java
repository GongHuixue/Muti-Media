package com.example.android.multmedia.player.mvp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public abstract class BaseActivity<P extends BaseControl> extends FragmentActivity {
    public FragmentActivity fragmentActivity;
    public ProgressDialog progressDialog;
    public P mediaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        /*The following init flow can't changed*/
        initView();
        mediaView = attachMediaView();
        initData();
        this.fragmentActivity = this;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaView != null) {
            mediaView.detachView();
        }
        this.fragmentActivity = null;
    }

    public abstract P attachMediaView();
    public abstract int getLayoutResID();
    public abstract void initView();
    public abstract void initData();


    public void showLoadingProgressDialog() {
        progressDialog = new ProgressDialog(fragmentActivity);
        progressDialog.setMessage("Loading");
        progressDialog.show();
    }

    public void hideLoadingProgressDialog() {
        if( (progressDialog != null) && (progressDialog.isShowing()) ) {
            progressDialog.dismiss();
        }
    }
}
