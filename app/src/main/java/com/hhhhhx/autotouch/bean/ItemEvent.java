package com.hhhhhx.autotouch.bean;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class ItemEvent {
    // 编辑触点用到
    public static final int ITEM_DELETE = 5;
    public static final int ITEM_ADD = 6;

    private int action;
    private int delPosition;
    private TouchPoint touchPoint;

    private ItemEvent(int action) {
        this(action, -1);
    }

    private ItemEvent(int action,int delPosition) {
        this.action = action;
        this.delPosition = delPosition;
    }

    private ItemEvent(int action,TouchPoint touchPoint) {
        this.action = action;
        this.touchPoint = touchPoint;
    }


    public void setAction(int action) {
        this.action = action;
    }
    public int getAction() {
        return action;
    }
    public int getDelPosition() {
        return delPosition;
    }
    public void setDelPosition(int delPosition) {
        this.delPosition = delPosition;
    }
    public TouchPoint getTouchPoint() {
        return touchPoint;
    }
    public void setTouchPoint(TouchPoint touchPoint) {
        this.touchPoint = touchPoint;
    }

    public static void postItemDelAction(int delPosition) {
        postAction(new ItemEvent(ITEM_DELETE, delPosition));
    }

    public static void postItemAddAction(TouchPoint touchPoint) {
        postAction(new ItemEvent(ITEM_ADD, touchPoint));
    }


    private static void postAction(ItemEvent itemEvent) {
        // EventBus是一个Android事件发布/订阅框架
        EventBus.getDefault().post(itemEvent);
    }

    @Override
    public String toString() {
        return "ItemEvent{" +
                "action=" + action +
                ", delPosition=" + delPosition +
                '}';
    }
}
