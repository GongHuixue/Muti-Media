package com.mediaload.bean;

import java.io.Serializable;

public class BaseItem implements Serializable {
    private long id;
    private String displayName;
    private String path;
    private long size;
    private long modified;

    public int VIEW_TYPE = 0;

    public BaseItem() {
    }

    public int getViewType() {
        return VIEW_TYPE;
    }

    public BaseItem(int id, String displayName, String path) {
        this(id, displayName, path, 0);
    }

    public BaseItem(int id, String displayName, String path, long size) {
        this(id, displayName, path, size, 0);
    }

    public BaseItem(int id, String displayName, String path, long size, long modified) {
        this.id = id;
        this.displayName = displayName;
        this.path = path;
        this.size = size;
        this.modified = modified;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getModified() {
        return modified;
    }

    public void setModified(long modified) {
        this.modified = modified;
    }
}
