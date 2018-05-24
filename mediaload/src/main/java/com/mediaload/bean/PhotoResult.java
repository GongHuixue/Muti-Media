package com.mediaload.bean;

import java.util.List;

public class PhotoResult extends BaseResult{
    private List<PhotoFolder> folders;
    private List<PhotoItem> items;

    public PhotoResult() {
    }

    public PhotoResult(List<PhotoFolder> folders, List<PhotoItem> items) {
        this.folders = folders;
        this.items = items;
    }

    public PhotoResult(List<PhotoFolder> folders, List<PhotoItem> items,long totalSize) {
        super(totalSize);
        this.folders = folders;
        this.items = items;
    }

    public List<PhotoFolder> getFolders() {
        return folders;
    }

    public void setFolders(List<PhotoFolder> folders) {
        this.folders = folders;
    }

    public List<PhotoItem> getItems() {
        return items;
    }

    public void setItems(List<PhotoItem> items) {
        this.items = items;
    }
}
