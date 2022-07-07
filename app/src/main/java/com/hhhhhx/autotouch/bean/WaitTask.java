package com.hhhhhx.autotouch.bean;

import java.util.List;

public class WaitTask implements Task{
    private Color color;
    private double waitInterval;
    private List<TouchPoint> touchPointList;

    public WaitTask(Color color, double waitInterval, List<TouchPoint> touchPointList) {
        this.color = color;
        this.waitInterval = waitInterval;
        this.touchPointList = touchPointList;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getWaitInterval() {
        return waitInterval;
    }

    public void setWaitInterval(double waitInterval) {
        this.waitInterval = waitInterval;
    }

    public List<TouchPoint> getTouchPointList() {
        return touchPointList;
    }

    public void setTouchPointList(List<TouchPoint> touchPointList) {
        this.touchPointList = touchPointList;
    }

    @Override
    public int getTaskType() {
        return TaskValue.WAIT_TASK;
    }
}
