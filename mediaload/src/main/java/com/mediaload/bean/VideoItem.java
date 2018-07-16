package com.mediaload.bean;

import java.io.Serializable;

public class VideoItem extends BaseItem implements Serializable {
    private final static long serialVersionUID = 1L;
    private long duration;
    private boolean checked;

    public VideoItem() {
        VIEW_TYPE = 0;
    }

    public VideoItem(int id, String displayName, String path, long size, long modified, long duration) {
        super(id, displayName, path, size, modified);
        this.duration = duration;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
