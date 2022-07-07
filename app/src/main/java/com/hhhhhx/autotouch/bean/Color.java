package com.hhhhhx.autotouch.bean;

public class Color {
    private int color;
    private int x;
    private int y;

    public Color(int color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public int getColor() {
        return color;
    }
    public void setColor(int color) {
        this.color = color;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
}
