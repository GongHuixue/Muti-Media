package com.mediaload.callback;

import android.database.Cursor;
import android.support.v4.content.Loader;

import com.mediaload.loader.ILoader;

public abstract class OnLoadCallBack implements ILoader{
    public abstract void onLoadFinish(Loader<Cursor> loader, Cursor data);

    public void onLoaderReset(){ }
}
