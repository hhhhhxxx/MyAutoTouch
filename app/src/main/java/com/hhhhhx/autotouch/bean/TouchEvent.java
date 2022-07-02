package com.hhhhhx.autotouch.bean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class TouchEvent {

    public static final int ACTION_START = 1;
    public static final int ACTION_PAUSE = 2;
    public static final int ACTION_CONTINUE = 3;
    public static final int ACTION_STOP = 4;

    // 编辑触点用到
    public static final int ITEM_DELETE = 5;
    public static final int ITEM_ADD = 6;

    private int action;
    private List<TouchPoint> tList;

    private TouchEvent(int action) {
        this(action, null);
    }

    private TouchEvent(int action, List<TouchPoint> tList) {
        this.action = action;
        this.tList = tList;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public List<TouchPoint> getTouchPointList() {
        return tList;
    }

    public static void postStartAction(List<TouchPoint> tList) {
        postAction(new TouchEvent(ACTION_START, tList));
    }

    public static void postPauseAction() {
        postAction(new TouchEvent(ACTION_PAUSE));
    }

    public static void postContinueAction() {
        postAction(new TouchEvent(ACTION_CONTINUE));
    }

    public static void postStopAction() {
        postAction(new TouchEvent(ACTION_STOP));
    }

    private static void postAction(TouchEvent touchEvent) {
        // EventBus是一个Android事件发布/订阅框架
        EventBus.getDefault().post(touchEvent);
    }

    @Override
    public String toString() {
        return "TouchEvent{" +
                "action=" + action +
                ", tList=" + tList +
                '}';
    }
}
