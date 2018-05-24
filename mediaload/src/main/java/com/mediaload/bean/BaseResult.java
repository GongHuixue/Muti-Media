package com.mediaload.bean;

import java.io.Serializable;

public class BaseResult implements Serializable{
    private long totalSize;

    public BaseResult() {
    }

    public BaseResult(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }
}
