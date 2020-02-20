package com.jokerwan.lib.bean;

import java.io.Serializable;

/**
 * 媒体实体类
 */
public class MediaFile implements Serializable {

    private String path;
    private long duration;
    private long dateToken;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getDateToken() {
        return dateToken;
    }

    public void setDateToken(long dateToken) {
        this.dateToken = dateToken;
    }
}

