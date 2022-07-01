package com.zhang.autotouch.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.RequiresApi;

import com.zhang.autotouch.R;
import com.zhang.autotouch.TouchEventManager;
import com.zhang.autotouch.bean.TouchEvent;
import com.zhang.autotouch.bean.TouchPoint;
import com.zhang.autotouch.utils.DensityUtil;
import com.zhang.autotouch.utils.WindowUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 无障碍服务-自动点击
 *
 * @date 2019/9/6 16:23
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class AutoTouchService extends AccessibilityService {

    private final String TAG = "++AutoTouchService+++";
    //自动点击事件
    private TouchPoint autoTouchPoint;
    // 改成传一个集合
    private ArrayList<TouchPoint> touchPointList;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper());
    private WindowManager windowManager;
    private TextView tvTouchPoint;
    //倒计时
    private double countDownTime;
    private DecimalFormat floatDf = new DecimalFormat("#0.0");
    //修改点击文本的倒计时
    private Runnable touchViewRunnable;
    private TouchThread touchThread;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        handler = new Handler();
        // 接收时间
        EventBus.getDefault().register(this);
        windowManager = WindowUtils.getWindowManager(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReciverTouchEvent(TouchEvent event) {
        Log.d(TAG, "onReciverTouchEvent: " + event.toString());
        TouchEventManager.getInstance().setTouchAction(event.getAction());
        handler.removeCallbacks(autoTouchRunnable);
        switch (event.getAction()) {
            case TouchEvent.ACTION_START:
                autoTouchPoint = event.getTouchPoint();
                onAutoClick();
                break;
            case TouchEvent.ACTION_CONTINUE:
                Log.d(TAG, "onReciverTouchEvent: 继续");
                if (touchThread != null) {
                    touchThread.onThreadResume();
                }
                break;
            case TouchEvent.ACTION_PAUSE:
                // removeCallbacks方法是删除指定的Runnable对象，使线程对象停止运行
                Log.d(TAG, "onReciverTouchEvent: 暂停");
                if (touchThread != null) {
                    touchThread.onThreadPause();
                }
                break;
            case TouchEvent.ACTION_STOP:
                touchThread.closeThread();
//                handler.removeCallbacks(autoTouchRunnable);
//                handler.removeCallbacks(touchViewRunnable);
//                removeTouchView();
                autoTouchPoint = null;
                break;
        }
    }

    /**
     * 执行自动点击
     */
    private void onAutoClick() {
        if (autoTouchPoint != null) {
            //  postDelayed的作用是延迟多少毫秒后开始运行
//            showTouchView();
            // 实际上在主线程 会堵塞点击 不符合需求
//            handler.post(autoTouchRunnable);
            touchThread = new TouchThread();
            touchThread.start();
            Log.d(TAG, "有到这里吗1");
        }
    }

    class TouchThread extends Thread {
        private boolean isClose = false;
        private boolean isPause = false;
        private double offset = 0.25;

        private TouchThread() {
        }

        /**
         * 暂停线程
         */
        public synchronized void onThreadPause() {
            isPause = true;
        }

        /**
         * 线程等待,不提供给外部调用
         */
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

        @Override
        public void run() {
            Log.d(TAG, "在run里面");

            double touchDelay = 0.0;

            Path path = new Path();
            touchPointList = new ArrayList<>();
            touchPointList.add(autoTouchPoint);
            touchPointList.add(autoTouchPoint);

            Iterator iter = touchPointList.iterator();

            iter.hasNext();

            while (!isClose) {
                if (!isPause) {
                    //  一个循环250
                    if (touchDelay <= 0 && iter.hasNext()) {
                        TouchPoint nextPoint = (TouchPoint) iter.next();
                        Tag(path, nextPoint);
                        touchDelay = nextPoint.getDelay();
                    } else if (touchDelay <= 0 && !iter.hasNext()) {
                        setClose(true);
                    } else {
                        touchDelay -= offset;
                    }
                    try {
                        Thread.sleep((long) (1000L * offset));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "线程等待");
                    onThreadWait();
                }
            }
        }
    }

    private Runnable autoTouchRunnable = new Runnable() {
        @Override
        public void run() {
//            Log.d(TAG, "onAutoClick: " + "x=" + autoTouchPoint.getX() + " y=" + autoTouchPoint.getY());
            Log.d(TAG, "在run里面");

            Path path = new Path();

            touchPointList = new ArrayList<>();
            touchPointList.add(autoTouchPoint);
            touchPointList.add(autoTouchPoint);

            for (TouchPoint touchPoint : touchPointList) {
                showTouchView();
                Tag(path, touchPoint);
                try {
                    Thread.sleep(getDelayTime(touchPoint));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            TouchEvent.postStopAction();
        }
    };

    // 模拟点击
    private void Tag(Path path, TouchPoint touchPoint) {

        path.moveTo(touchPoint.getX(), touchPoint.getY());

        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder.addStroke(
                new GestureDescription.StrokeDescription(path, 0, 100))
                .build();

        dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d("AutoTouchService", "滑动结束" + gestureDescription.getStrokeCount());
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d("AutoTouchService", "滑动取消");
            }
        }, null);
    }


    private long getDelayTime(TouchPoint touchPoint) {
//        int random = (int) (Math.random() * (30 - 1) + 1);
//        return autoTouchEvent.getDelay() * 1000L + random;
        return (long) touchPoint.getDelay() * 1000L;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
    }

    @Override
    public void onInterrupt() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        removeTouchView();
    }

    /**
     * 显示倒计时
     */
    private void showTouchView() {
        if (autoTouchPoint != null) {
            //创建触摸点View
            if (tvTouchPoint == null) {
                tvTouchPoint = (TextView) LayoutInflater.from(this).inflate(R.layout.window_touch_point, null);
            }
            //显示触摸点View
            if (windowManager != null && !tvTouchPoint.isAttachedToWindow()) {
                int width = DensityUtil.dip2px(this, 40);
                int height = DensityUtil.dip2px(this, 40);
                WindowManager.LayoutParams params = WindowUtils.newWmParams(width, height);
                params.gravity = Gravity.START | Gravity.TOP;
                params.x = autoTouchPoint.getX() - width / 2;
                params.y = autoTouchPoint.getY() - width;
                params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                windowManager.addView(tvTouchPoint, params);
            }
            //开启倒计时
            countDownTime = autoTouchPoint.getDelay();
            if (touchViewRunnable == null) {
                touchViewRunnable = new Runnable() {
                    @Override
                    public void run() {
                        handler.removeCallbacks(touchViewRunnable);
                        Log.d("触摸倒计时", countDownTime + "");
                        if (countDownTime > 0) {
                            float offset = 0.1f;
                            tvTouchPoint.setText(floatDf.format(countDownTime));
                            countDownTime -= offset;
                            handler.postDelayed(touchViewRunnable, (long) (1000L * offset));
                        } else {
                            removeTouchView();
                        }
                    }
                };
            }
            handler.post(touchViewRunnable);
        }
    }

    private void removeTouchView() {
        if (windowManager != null && tvTouchPoint.isAttachedToWindow()) {
            windowManager.removeView(tvTouchPoint);
        }
    }
}
