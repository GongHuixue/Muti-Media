package com.example.android.multmedia.personaldb;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class MediaDb {
    @Id(autoincrement = true)
    private long id;
    @NotNull
    private String mediaPath;
    private String mediaName;
    private long size;
    private long mediaId;
    private long createTime;
    private int mediaType;
    private boolean isFavor;    //record the media file is favorite or not
    private long playedTime;    //record the media file playing time;
    private int playedCounts;   //record the media file played counts.


    public MediaDb() {
    }
    @Generated(hash = 1730653658)
    public MediaDb(long id, @NotNull String mediaPath, String mediaName, long size,
            long mediaId, long createTime, int mediaType, boolean isFavor,
            long playedTime, int playedCounts) {
        this.id = id;
        this.mediaPath = mediaPath;
        this.mediaName = mediaName;
        this.size = size;
        this.mediaId = mediaId;
        this.createTime = createTime;
        this.mediaType = mediaType;
        this.isFavor = isFavor;
        this.playedTime = playedTime;
        this.playedCounts = playedCounts;
    }
    @NotNull
    public String getMediaPath() {
        return this.mediaPath;
    }
    public void setMediaPath(@NotNull String mediaPath) {
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
    public long getMediaId() {
        return this.mediaId;
    }
    public void setMediaId(long mediaId) {
        this.mediaId = mediaId;
    }
    public void setId(long id) {
        this.id = id;
    }
}
