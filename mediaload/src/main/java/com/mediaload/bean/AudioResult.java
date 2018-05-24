package com.mediaload.bean;

import java.util.List;

public class AudioResult extends BaseResult {
    private List<AudioItem> items;

    public AudioResult() {
    }

    public AudioResult(List<AudioItem> items) {
        this.items = items;
    }

    public AudioResult(long totalSize, List<AudioItem> items) {
        super(totalSize);
        this.items = items;
    }

    public List<AudioItem> getItems() {
        return items;
    }

    public void setItems(List<AudioItem> items) {
        this.items = items;
    }
}
