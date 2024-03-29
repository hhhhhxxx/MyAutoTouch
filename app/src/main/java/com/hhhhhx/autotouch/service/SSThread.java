package com.hhhhhx.autotouch.service;

import android.graphics.Path;
import android.util.Log;

import com.hhhhhx.autotouch.activity.GBData;
import com.hhhhhx.autotouch.bean.BrushSetting;
import com.hhhhhx.autotouch.bean.Color;
import com.hhhhhx.autotouch.bean.ForBreakTask;
import com.hhhhhx.autotouch.bean.ForBreakTask_0;
import com.hhhhhx.autotouch.bean.ForTask;
import com.hhhhhx.autotouch.bean.IfTask;
import com.hhhhhx.autotouch.bean.Task;
import com.hhhhhx.autotouch.bean.TaskValue;
import com.hhhhhx.autotouch.bean.TimesTip;
import com.hhhhhx.autotouch.bean.TouchPoint;
import com.hhhhhx.autotouch.bean.WaitTask;
import com.hhhhhx.autotouch.event.TouchEvent;
import com.hhhhhx.autotouch.event.UIEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SSThread extends Thread {
    private static final String TAG = "SSThread";
    private boolean isClose = false;
    private boolean isPause = false;
    private ArrayList<Task> taskList;
    private ArrayList<Task> treakList;
    private BrushSetting brushSetting;

    public SSThread(ArrayList<Task> taskList, ArrayList<Task> treakList,BrushSetting brushSetting) {
        this.taskList = taskList;
        this.treakList = treakList;
        this.brushSetting = brushSetting;
    }
    // 暂停线程
    public synchronized void onThreadPause() {
        isPause = true;
    }
    // 线程等待,不提供给外部调用
    private void onThreadWait() {
        try {
            synchronized (this) {
                this.wait();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //  线程继续运行
    public synchronized void onThreadResume() {
        isPause = false;
        this.notify();
    }
    //  关闭线程
    public synchronized void closeThread() {
        try {
            notify();
            setClose(true);
            interrupt();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isClose() {
        return isClose;
    }

    public void setClose(boolean isClose) {
        this.isClose = isClose;
    }

    /*@Override
    public void run() {
        Log.d(TAG, "在run里面");

        Path path = new Path();

        // 临时参数
        int flag = TaskValue.NEXT_TASK;
        double offset = 0.25;
        double touchDelay = TaskValue.INIT_DELAY;

        int fRepeatTime = 0;
        boolean isWait = true;
        Color nowColor = null;
        double waitInterval = 1;

        int doTimes = 0;
        int treatTimes = brushSetting.getTreatTimes();
        boolean isTreat = false;

        // 新数据结构

        // 迭代器
        Iterator tIter = this.taskList.iterator();
        // 子操作迭代器
        Iterator fIter = null;

        Object nowTask = null;
        TouchPoint tNowTask = null;
        ForTask fNowTask = null;
        WaitTask wNowTask = null;
        ForBreakTask fbNowTask = null;
        ForBreakTask_0 fb0NowTask = null;
        IfTask ifNowTask = null;


        long st = System.currentTimeMillis();

        UIEvent.postUIUpdateAction(new TimesTip(doTimes,brushSetting.getDoTimes()));
        // 开局缓一下
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while (!isClose) {
            if (!isPause) {

                if (doTimes < brushSetting.getDoTimes() || isTreat == true) {

                    if (flag == TaskValue.NEXT_TASK && tIter.hasNext()) {
                        nowTask = tIter.next();
                        if (nowTask instanceof ForTask) {
                            Log.d(TAG, "run: 辨认为for");
                            flag = TaskValue.FOR_TASK;
                        } else if (nowTask instanceof TouchPoint) {
                            Log.d(TAG, "run: 辨认为TouchPoint");
                            flag = TaskValue.NORMAL_TASK;
                        } else if (nowTask instanceof WaitTask) {
                            Log.d(TAG, "run: 辨认为Wait");
                            flag = TaskValue.WAIT_TASK;
                        } else if (nowTask instanceof ForBreakTask) {
                            Log.d(TAG, "run: 辨认为ForBreak");
                            flag = TaskValue.FORBREAK_TASK;
                        } else if (nowTask instanceof IfTask) {
                            Log.d(TAG, "run: 辨认为If");
                            flag = TaskValue.IF_TASK;
                        } else if (nowTask instanceof ForBreakTask_0) {
                            Log.d(TAG, "run: 辨认为forBreak_0");
                            flag = TaskValue.FORBREAK_TASK_0;
                        }
                        touchDelay = TaskValue.INIT_DELAY;
                    } else if (flag == TaskValue.NEXT_TASK && !tIter.hasNext()) {
                        // 上一个task不是补药
                        if (isTreat == false) {
                            doTimes++;
                        }

                        UIEvent.postUIUpdateAction(new TimesTip(doTimes,brushSetting.getDoTimes()));

                        // 补药时间
                        if (treatTimes != 0 && isTreat == false && doTimes % treatTimes == 0) {
                            isTreat = true;
                            tIter = treakList.iterator();
                        } else {
                            isTreat = false;
                            tIter = taskList.iterator();
                        }
                        continue;
                    }

                    if (flag == TaskValue.FOR_TASK) {
                        //  for 初始化
                        if (touchDelay <= 0 && touchDelay == TaskValue.INIT_DELAY) {
                            fNowTask = (ForTask) nowTask;
                            fIter = fNowTask.getTouchPointList().iterator();
                            fRepeatTime = fNowTask.getRepeatTime();
                            touchDelay = 0.0;
                        }

                        if (fRepeatTime != 0) {
                            //  一个循环250
                            if (touchDelay <= 0 && fIter.hasNext()) {
                                TouchPoint nextPoint = (TouchPoint) fIter.next();
                                TouchEvent.postTapAction(nextPoint);
//                                Tag(path, nextPoint);
                                touchDelay = nextPoint.getDelay();
                            } else if (touchDelay <= 0 && !fIter.hasNext()) {
                                fRepeatTime--;
                                if (fRepeatTime != 0) {
                                    fIter = fNowTask.getTouchPointList().iterator();
                                }
                                continue;
                            }
                            if (touchDelay > 0) {
                                touchDelay -= offset;
                            }
                        } else {
                            // 结束
                            flag = TaskValue.NEXT_TASK;
                            continue;
                        }

                    } else if (flag == TaskValue.NORMAL_TASK) {

                        if (touchDelay == TaskValue.INIT_DELAY) {
                            tNowTask = (TouchPoint) nowTask;
                            TouchEvent.postTapAction(tNowTask);
//                            Tag(path, tNowTask);
                            touchDelay = tNowTask.getDelay();
                        }
                        //  一个循环250
                        if (touchDelay <= 0) {
                            flag = TaskValue.NEXT_TASK;
                            continue;
                        } else {
                            touchDelay -= offset;
                        }

                    } else if (flag == TaskValue.WAIT_TASK) {
                        //  第一次进入 初始化
                        if (touchDelay == TaskValue.INIT_DELAY) {
                            wNowTask = (WaitTask) nowTask;
                            fIter = wNowTask.getTouchPointList().iterator();

                            nowColor = wNowTask.getColor();
                            waitInterval = wNowTask.getWaitInterval();
                            isWait = true;
                            // 缓一下 让屏幕更清晰 不收开始操作影响
                            touchDelay = 0.0;
                        }

                        if (!isWait) {
                            //  成功 不用等待
                            if (touchDelay <= 0 && fIter.hasNext()) {
                                TouchPoint nextPoint = (TouchPoint) fIter.next();
                                TouchEvent.postTapAction(nextPoint);
//                                Tag(path, nextPoint);
                                touchDelay = nextPoint.getDelay();
                            } else if (touchDelay <= 0 && !fIter.hasNext()) {
                                // 结束
                                flag = TaskValue.NEXT_TASK;
                                continue;
                            }
                            if (touchDelay > 0) {
                                touchDelay -= offset;
                            }
                        } else {
                            // 没有出现标记颜色重新等待 或者第一次进入
                            if (touchDelay <= 0) {
                                if (GBData.isSameColor(nowColor)) {
                                    isWait = false;
                                    continue;
                                } else {
                                    Log.d(TAG, "run: wait辨色等待");
                                    touchDelay = waitInterval;
                                }
                            }
                            touchDelay -= offset;
                        }
                    } else if (flag == TaskValue.FORBREAK_TASK) {
                        //  forBreak 初始化
                        if (touchDelay <= 0 && touchDelay == TaskValue.INIT_DELAY) {
                            fbNowTask = (ForBreakTask) nowTask;
                            fIter = fbNowTask.getTouchPointList().iterator();
                            nowColor = fbNowTask.getColor();
                            touchDelay = 0.0;
                            // 开局测一下
                            if (GBData.isSameColor(nowColor)) {
                                flag = TaskValue.NEXT_TASK;
                                continue;
                            }
                        }

                        if (touchDelay <= 0 && fIter.hasNext()) {
                            TouchPoint nextPoint = (TouchPoint) fIter.next();
                            TouchEvent.postTapAction(nextPoint);
//                            Tag(path, nextPoint);
                            touchDelay = nextPoint.getDelay();
                        } else if (touchDelay <= 0 && !fIter.hasNext()) {
                            // 辨色 然后退出循环 否则无限执行
                            if (GBData.isSameColor(nowColor)) {
                                flag = TaskValue.NEXT_TASK;
                                continue;
                            } else {
                                fIter = fbNowTask.getTouchPointList().iterator();
                            }
                        }
                        if (touchDelay > 0) {
                            touchDelay -= offset;
                        }
                    } else if (flag == TaskValue.FORBREAK_TASK_0) {
                        //  forBreak 初始化
                        if (touchDelay <= 0 && touchDelay == TaskValue.INIT_DELAY) {
                            fb0NowTask = (ForBreakTask_0) nowTask;
                            fIter = fb0NowTask.getTouchPointList().iterator();
                            nowColor = fb0NowTask.getColor();
                            touchDelay = 0.0;
                            // 开局测一下
                            if (!GBData.isSameColor(nowColor)) {
                                flag = TaskValue.NEXT_TASK;
                                continue;
                            }
                        }

                        if (touchDelay <= 0 && fIter.hasNext()) {
                            TouchPoint nextPoint = (TouchPoint) fIter.next();
                            TouchEvent.postTapAction(nextPoint);
//                            Tag(path, nextPoint);
                            touchDelay = nextPoint.getDelay();
                        } else if (touchDelay <= 0 && !fIter.hasNext()) {
                            // 辨色 然后退出循环 否则无限执行
                            if (!GBData.isSameColor(nowColor)) {
                                flag = TaskValue.NEXT_TASK;
                                continue;
                            } else {
                                fIter = fb0NowTask.getTouchPointList().iterator();
                            }
                        }
                        if (touchDelay > 0) {
                            touchDelay -= offset;
                        }
                    } else if (flag == TaskValue.IF_TASK) {
                        if (touchDelay <= 0 && touchDelay == TaskValue.INIT_DELAY) {
                            ifNowTask = (IfTask) nowTask;

                            nowColor = ifNowTask.getColor();
                            if (GBData.isSameColor(nowColor)) {
                                fIter = ifNowTask.getTrueList().iterator();
                            } else {
                                fIter = ifNowTask.getFalseList().iterator();
                            }
                            touchDelay = 0.0;
                        }

                        if (touchDelay <= 0 && fIter.hasNext()) {
                            TouchPoint nextPoint = (TouchPoint) fIter.next();
                            TouchEvent.postTapAction(nextPoint);
//                            Tag(path, nextPoint);
                            touchDelay = nextPoint.getDelay();
                        } else if (touchDelay <= 0 && !fIter.hasNext()) {
                            flag = TaskValue.NEXT_TASK;
                            continue;
                        }
                        if (touchDelay > 0) {
                            touchDelay -= offset;
                        }
                    }

                } else {
                    // 结束
                    long et = System.currentTimeMillis();
                    Log.d(TAG, "allTime: " + (et - st));
                    setClose(true);// 大for
                    continue;
                }

                // ------------循环间隔---------------
                try {
                    Thread.sleep((long) (1000 * offset));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Log.d(TAG, "线程等待");
                onThreadWait();
            }
        }
        Log.d(TAG, "run: 线程结束");
        TouchEvent.postStopAction();
    }*/
    @Override
    public void run() {
        while (true) {
            list.add(new TouchPoint("123",1,1,1.0));

        }
    }

    static List<TouchPoint> list = new ArrayList<>();
}
