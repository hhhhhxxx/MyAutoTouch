package com.hhhhhx.autotouch.bean;

public class TimesTip {
    private int nowTime;
    private int allTime;

    public TimesTip(int nowTime, int allTime) {
        this.nowTime = nowTime;
        this.allTime = allTime;
    }

    public int getNowTime() {
        return nowTime;
    }

    public void setNowTime(int nowTime) {
        this.nowTime = nowTime;
    }

    public int getAllTime() {
        return allTime;
    }

    public void setAllTime(int allTime) {
        this.allTime = allTime;
    }

    public String getSSTip() {
        return "进度："+this.nowTime+"/"+this.allTime;
    }
}
