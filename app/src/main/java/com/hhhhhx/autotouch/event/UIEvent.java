package com.hhhhhx.autotouch.event;

import com.hhhhhx.autotouch.bean.TimesTip;
import com.hhhhhx.autotouch.bean.TouchPoint;

import org.greenrobot.eventbus.EventBus;

public class UIEvent {
    // 编辑触点用到
    public static final int UI_START = 1001;
    public static final int UI_CON = 1003;
    public static final int UI_PAUSE = 1003;
    public static final int UI_STOP = 1004;


    public static final int UI_UPDATE = 2001;


    private int action;
    private TimesTip timesTip;

    public UIEvent(int action, TimesTip timesTip) {
        this.action = action;
        this.timesTip = timesTip;
    }

    public UIEvent(int action) {
        this.action = action;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public TimesTip getTimesTip() {
        return timesTip;
    }

    public void setTimesTip(TimesTip timesTip) {
        this.timesTip = timesTip;
    }


    public static void postUIStartAction() {
        postAction(new UIEvent(UI_START));
    }

    public static void postUIUpdateAction(TimesTip timesTip) {
        postAction(new UIEvent(UI_UPDATE, timesTip));
    }
    public static void postUIStopAction() {
        postAction(new UIEvent(UI_STOP));
    }



    private static void postAction(UIEvent itemEvent) {
        // EventBus是一个Android事件发布/订阅框架
        EventBus.getDefault().post(itemEvent);
    }

}
