package com.hhhhhx.autotouch.bean;

import java.util.List;

public class ForBreakTask_0 implements Task {
    private Color color;
    private List<TouchPoint> touchPointList;

    public ForBreakTask_0(Color color, List<TouchPoint> touchPointList) {
        this.color = color;
        this.touchPointList = touchPointList;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<TouchPoint> getTouchPointList() {
        return touchPointList;
    }

    public void setTouchPointList(List<TouchPoint> touchPointList) {
        this.touchPointList = touchPointList;
    }

    @Override
    public int getTaskType() {
        return TaskValue.FORBREAK_TASK_0;
    }
}
