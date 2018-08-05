package com.example.android.multmedia.personaldb;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class MediaDb {
    @Id
    private String mediaPath;
    private String mediaName;
    private long size;
    private long id;
    private long createTime;
    private int mediaType;
    private boolean isFavor;    //record the media file is favorite or not
    private long playedTime;    //record the media file playing time;
    private int playedCounts;   //record the media file played counts.


    public MediaDb() {
    }

    @Generated(hash = 170277237)
    public MediaDb(String mediaPath, String mediaName, long size, long id,
            long createTime, int mediaType, boolean isFavor, long playedTime,
            int playedCounts) {
        this.mediaPath = mediaPath;
        this.mediaName = mediaName;
        this.size = size;
        this.id = id;
        this.createTime = createTime;
        this.mediaType = mediaType;
        this.isFavor = isFavor;
        this.playedTime = playedTime;
        this.playedCounts = playedCounts;
    }
    public String getMediaPath() {
        return this.mediaPath;
    }
    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }
    public String getMediaName() {
        return this.mediaName;
    }
    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }
    public long getSize() {
        return this.size;
    }
    public void setSize(long size) {
        this.size = size;
    }
    public long getId() {
        return this.id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public int getMediaType() {
        return this.mediaType;
    }
    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
    }
    public boolean getIsFavor() {
        return this.isFavor;
    }
    public void setIsFavor(boolean isFavor) {
        this.isFavor = isFavor;
    }
    public long getPlayedTime() {
        return this.playedTime;
    }
    public void setPlayedTime(long playedTime) {
        this.playedTime = playedTime;
    }
    public int getPlayedCounts() {
        return this.playedCounts;
    }
    public void setPlayedCounts(int playedCounts) {
        this.playedCounts = playedCounts;
    }
}
