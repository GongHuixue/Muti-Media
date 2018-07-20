package com.example.android.multmedia.player.audio;

public class Lcr implements Comparable<Lcr> {
    private String content;
    private int time;

    public Lcr(String content, int time) {
        this.content = content;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public int getTime() {
        return time;
    }


    @Override
    public int compareTo(Lcr lcr) {
        return this.time - lcr.time;
    }
}

