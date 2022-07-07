package com.hhhhhx.autotouch.bean;

import java.util.List;

public class IfTask implements Task {
    private Color color;
    private List<TouchPoint> trueList;
    private List<TouchPoint> falseList;

    public IfTask(Color color, List<TouchPoint> trueList, List<TouchPoint> falseList) {
        this.color = color;
        this.trueList = trueList;
        this.falseList = falseList;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<TouchPoint> getTrueList() {
        return trueList;
    }

    public void setTrueList(List<TouchPoint> trueList) {
        this.trueList = trueList;
    }

    public List<TouchPoint> getFalseList() {
        return falseList;
    }

    public void setFalseList(List<TouchPoint> falseList) {
        this.falseList = falseList;
    }

    @Override
    public int getTaskType() {
        return TaskValue.IF_TASK;
    }
}
