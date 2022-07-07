package com.hhhhhx.autotouch.bean;

public class TouchPoint implements Task {
    private String name;
    private int x;
    private int y;
    private double delay;

    public TouchPoint(String name, int x, int y, double delay) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.delay = delay;
    }

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getDelay() {
        return delay;
    }

    @Override
    public String toString() {
        return "TouchPoint{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", delay=" + delay +
                '}';
    }


    @Override
    public int getTaskType() {
        return TaskValue.NORMAL_TASK;
    }
}
