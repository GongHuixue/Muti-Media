package com.example.android.multmedia.videobrowser;

import android.provider.MediaStore;

public class VbConstants {
    public static final int VIEWMODE_GRID = 1;

    public static final int VIEWMODE_LIST = 2;
    
    public static final int VIEWMODE_EMPTY = -1;

    public static final int DEFAULT_VIEW_MODE = VIEWMODE_GRID;

    public static final int VB_LOADER_ID = 201;

    public static final int COLUMNS_GRID_COLLAPSED = 3;

    public static final int COLUMNS_GRID_EXPANDED = 4;
    
    public static int RELATIVE_ITEM_COUNT=50;
    
    public static final int SORT_ORDER_ASC_NAME = 0;

    public static final int DEFAULT_VB_SORT_ORDER = SORT_ORDER_ASC_NAME;
    public static final int VISIBLE_COLS_COLLAPSED_VIEW = 3;   
    public static final int VISIBLE_COLS_EXPANDED_VIEW = 4;
    public static final int VISIBLE_ROWS = 3;
    public static final String[] ORDER_BY = {            
            MediaStore.Video.Media.TITLE, MediaStore.Video.Media.TITLE + " DESC"
    };

    public static final int THUMB_WIDTH = 104;

    public static final int THUMB_HEIGHT = 68;

    /*
     * Max cache size for storing thumbnails - 5MB
     */
    public static final int MAX_CACHE_SIZE = 5 * 1024 * 1024;
}
