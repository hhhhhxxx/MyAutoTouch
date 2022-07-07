package com.hhhhhx.autotouch.bean;

public class BrushSetting {
    private int doTimes;
    private int treatTimes;
    private int rowNumber;
    private int forSecond;
    private int forBreakSecond;
    private boolean way;
    private String mode;

    @Override
    public String toString() {
        return "BrushSetting{" +
                "doTimes=" + doTimes +
                ", treatTimes=" + treatTimes +
                ", rowNumber=" + rowNumber +
                ", forSecond=" + forSecond +
                ", forBreakSecond=" + forBreakSecond +
                ", way=" + way +
                ", mode='" + mode + '\'' +
                '}';
    }

    public BrushSetting() {
        // 默认
        this.doTimes = 0;
        this.treatTimes = 5;
        this.rowNumber = 5;
        this.forSecond = 10;
        this.forBreakSecond = 5;
        this.mode = "0000";
        this.way = false;
    }

    public BrushSetting(int doTimes, int treatTimes, int rowNumber, int forSecond, int forBreakSecond, boolean way, String mode) {
        this.doTimes = doTimes;
        this.treatTimes = treatTimes;
        this.rowNumber = rowNumber;
        this.forSecond = forSecond;
        this.forBreakSecond = forBreakSecond;
        this.way = way;
        this.mode = mode;
    }

    public int getDoTimes() {
        return doTimes;
    }

    public void setDoTimes(int doTimes) {
        this.doTimes = doTimes;
    }

    public int getTreatTimes() {
        return treatTimes;
    }

    public void setTreatTimes(int treatTimes) {
        this.treatTimes = treatTimes;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public int getForSecond() {
        return forSecond;
    }

    public void setForSecond(int forSecond) {
        this.forSecond = forSecond;
    }

    public int getForBreakSecond() {
        return forBreakSecond;
    }

    public void setForBreakSecond(int forBreakSecond) {
        this.forBreakSecond = forBreakSecond;
    }

    public boolean isWay() {
        return way;
    }

    public void setWay(boolean way) {
        this.way = way;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
