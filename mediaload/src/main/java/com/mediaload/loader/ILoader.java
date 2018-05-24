package com.mediaload.loader;

import android.net.Uri;

public interface ILoader {
    String[] getSelectProjection();
    Uri getQueryUri();
    String getSortOrderSql();
    String getSelections();
    String[] getSelectionsArgs();
}
