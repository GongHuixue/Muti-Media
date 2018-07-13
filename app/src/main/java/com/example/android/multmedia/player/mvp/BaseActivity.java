package com.example.android.multmedia.player.mvp;

import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;

public abstract class BaseActivity extends FragmentActivity {
    public FragmentActivity fragmentActivity;
    public ProgressDialog progressDialog;

    public void showLoadingProgress() {

    }

    public void hideLoadingProgress() {

    }
}
