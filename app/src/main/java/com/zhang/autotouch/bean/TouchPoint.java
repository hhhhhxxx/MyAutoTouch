package com.zhang.autotouch.bean;

public class TouchPoint {
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
}
