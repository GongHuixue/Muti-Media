package com.mediaload.bean;

public class PhotoItem extends BaseItem {
    private boolean checked;

    public PhotoItem(int id, String displayName, String path) {
        super(id, displayName, path);
    }

    public PhotoItem(int id, String displayName, String path, long size) {
        super(id, displayName, path, size);
    }

    public PhotoItem(int id, String displayName, String path, long size, long modified) {
        super(id, displayName, path, size, modified);
        VIEW_TYPE = 1;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
