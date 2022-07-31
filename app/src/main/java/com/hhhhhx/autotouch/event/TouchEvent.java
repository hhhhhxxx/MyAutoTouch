package com.hhhhhx.autotouch.event;

import com.hhhhhx.autotouch.bean.TimesTip;
import com.hhhhhx.autotouch.bean.TouchPoint;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class TouchEvent {

    public static final int ACTION_START = 1;
    public static final int ACTION_PAUSE = 2;
    public static final int ACTION_CONTINUE = 3;
    public static final int ACTION_STOP = 4;

    public static final int ACTION_START_SS = 5;

    public static final int ACTION_TAP = 123;


    private int action;

    public TouchPoint getTouchPoint() {
        return touchPoint;
    }

    private List<TouchPoint> tList;
    private TouchPoint touchPoint;

    public TouchEvent(int action, TouchPoint touchPoint) {
        this.action = action;
        this.touchPoint = touchPoint;
    }

    private TouchEvent(int action) {
        this.action = action;
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


    public static void postStartSSAction() {
        postAction(new TouchEvent(ACTION_START_SS));
    }

    public static void postTapAction(TouchPoint touchPoint) {
        postAction(new TouchEvent(ACTION_TAP,touchPoint));
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
