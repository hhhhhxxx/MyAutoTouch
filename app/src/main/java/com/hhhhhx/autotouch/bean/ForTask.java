package com.hhhhhx.autotouch.bean;

import java.util.List;

public class ForTask implements Task {
    private int repeatTime;
    private List<TouchPoint> touchPointList;

    public ForTask(int repeatTime, List<TouchPoint> touchPointList) {
        this.repeatTime = repeatTime;
        this.touchPointList = touchPointList;
    }

    public int getRepeatTime() {
        return repeatTime;
    }

    public void setRepeatTime(int repeatTime) {
        this.repeatTime = repeatTime;
    }

    public List<TouchPoint> getTouchPointList() {
        return touchPointList;
    }

    public void setTouchPointList(List<TouchPoint> touchPointList) {
        this.touchPointList = touchPointList;
    }


    @Override
    public int getTaskType() {
        return TaskValue.FOR_TASK;
    }
}
