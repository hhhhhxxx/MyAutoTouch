package com.hhhhhx.autotouch.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Path;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.RequiresApi;

import com.hhhhhx.autotouch.Data;
import com.hhhhhx.autotouch.bean.BrushSetting;
import com.hhhhhx.autotouch.bean.Task;
import com.hhhhhx.autotouch.event.TouchEventManager;
import com.hhhhhx.autotouch.event.TouchEvent;
import com.hhhhhx.autotouch.bean.TouchPoint;
import com.hhhhhx.autotouch.event.UIEvent;
import com.hhhhhx.autotouch.utils.SpUtils;
import com.hhhhhx.autotouch.utils.ToastUtil;
import com.hhhhhx.autotouch.utils.WindowUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * 无障碍服务-自动点击
 *
 * @date 2019/9/6 16:23
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class AutoTouchService extends AccessibilityService {

    private final String TAG = "++AutoTouchService+++";
    private List<TouchPoint> touchPointList;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(Looper.getMainLooper());

    private Data ssData;
    private SSThread ssThread;
    private Path path;

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        handler = new Handler();
        // 接收时间
        EventBus.getDefault().register(this);
        // 复用path
        path = new Path();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReciverTouchEvent(TouchEvent event) {
        // 接收点击时间
        switch (event.getAction()) {
            case TouchEvent.ACTION_TAP:
                Tag(event.getTouchPoint());
                break;
        }
    }


    private void onAutoClick2() {
        BrushSetting brushSetting = new BrushSetting();
        brushSetting.setDoTimes(1);
        brushSetting.setTreatTimes(0);

        ArrayList<Task> tasks = new ArrayList<>();

        for (TouchPoint touchPoint : touchPointList) {
            tasks.add(touchPoint);
        }
        ssThread = new SSThread(tasks, new ArrayList<Task>(),brushSetting);
        ssThread.start();
    }



    // 模拟点击
    private void Tag(TouchPoint touchPoint) {
        path.moveTo(touchPoint.getX(), touchPoint.getY());
        GestureDescription.Builder builder = new GestureDescription.Builder();
        GestureDescription gestureDescription = builder.addStroke(
                new GestureDescription.StrokeDescription(path, 0, 100))
                .build();
        dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "触点详情: " + touchPoint.toString());
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d("AutoTouchService", "滑动取消");
            }
        }, null);
    }


    private long getDelayTime(TouchPoint touchPoint) {
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
        EventBus.getDefault().unregister(this);
    }
}
