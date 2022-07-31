package com.hhhhhx.autotouch.service;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.hhhhhx.autotouch.Data;
import com.hhhhhx.autotouch.R;
import com.hhhhhx.autotouch.bean.BrushSetting;
import com.hhhhhx.autotouch.bean.Task;
import com.hhhhhx.autotouch.bean.TimesTip;
import com.hhhhhx.autotouch.event.TouchEvent;
import com.hhhhhx.autotouch.event.TouchEventManager;
import com.hhhhhx.autotouch.event.UIEvent;
import com.hhhhhx.autotouch.utils.DensityUtil;
import com.hhhhhx.autotouch.utils.SpUtils;
import com.hhhhhx.autotouch.utils.ToastUtil;
import com.hhhhhx.autotouch.utils.WindowUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * 悬浮窗
 */
public class SwitchFloatingService extends Service {

    private static final String TAG = "++SwitchFloatingService++";
    private WindowManager mWindowManager;
    private View mFloatingView;
    private WindowManager.LayoutParams floatLayoutParams;

    private Button switchBtn;
    private Button stopBtn;
    private TextView tv_tip;
    private int flag = 1;

    private Data ssData;
    private SSThread ssThread;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingView = creatView(R.layout.layout_switch);

        EventBus.getDefault().register(this);

        //设置WindowManger布局参数以及相关属性
        int dWidth = DensityUtil.dip2px(this, 200);
        int dHeight = DensityUtil.dip2px(this, 50);

        floatLayoutParams = WindowUtils.newWmParams(dWidth, dHeight);
        //初始化位置
        floatLayoutParams.gravity = Gravity.TOP | Gravity.START;
        floatLayoutParams.x = 0;
        floatLayoutParams.y = WindowUtils.getScreenHeight(this) - DensityUtil.dip2px(this, 350);
        //获取WindowManager对象
        mWindowManager = WindowUtils.getWindowManager(this);

        addViewToWindow(mFloatingView, floatLayoutParams);

        mFloatingView.setVisibility(View.GONE);
        //FloatingView的拖动事件
        mFloatingView.setClickable(true);
        mFloatingView.setOnTouchListener(new View.OnTouchListener() {
            private int x;
            private int y;
            //是否在移动
            private boolean isMoving;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        isMoving = false;
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        int nowX = (int) event.getRawX();
                        int nowY = (int) event.getRawY();
                        int moveX = nowX - x;
                        int moveY = nowY - y;
                        if (Math.abs(moveX) > 0 || Math.abs(moveY) > 0) {
                            isMoving = true;
                            floatLayoutParams.x += moveX;
                            floatLayoutParams.y += moveY;
                            //更新View的位置
                            mWindowManager.updateViewLayout(mFloatingView, floatLayoutParams);
                            x = nowX;
                            y = nowY;
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (!isMoving) {
//                            onShowSelectDialog();
                            return true;
                        }
                        break;
                }
                return false;
            }
        });

        switchBtn = mFloatingView.findViewById(R.id.switchBtn);
        stopBtn = mFloatingView.findViewById(R.id.stopBtn);
        tv_tip = mFloatingView.findViewById(R.id.tv_tip);


        switchBtn.setOnClickListener(view -> {
            if (flag % 2 != 0) {
                pauseTask();
            } else {
                conTask();
            }
            flag++;
        });

        stopBtn.setOnClickListener(view -> {
            TouchEvent.postStopAction();
            stopTask();
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReciverTOuchEvent(TouchEvent event) {
        Log.d(TAG, "控制中心: " + event.toString());

        switch (event.getAction()) {
            case TouchEvent.ACTION_START_SS:
                startTask();
                break;
            case TouchEvent.ACTION_CONTINUE:
                conTask();
                break;
            case TouchEvent.ACTION_PAUSE:
                pauseTask();
                break;
            case TouchEvent.ACTION_STOP:
                stopTask();
                break;
        }
    }

    public void startTask() {
        mFloatingView.setVisibility(View.VISIBLE);
        flag = 1;
        switchBtn.setText("暂停");
        tv_tip.setText("拖动");
        onAutoClick();
    }
    public void conTask() {
        if (ssThread != null) {
            ssThread.onThreadResume();
        }
        switchBtn.setText("暂停");
        ToastUtil.show("脚本继续");
    }
    public void pauseTask() {
        if (ssThread != null) {
            ssThread.onThreadPause();
        }
        switchBtn.setText("继续");
        ToastUtil.show("脚本暂停");
    }
    public void stopTask() {
        ssThread.closeThread();
        mFloatingView.setVisibility(View.GONE);
    }

    /**
     * 执行自动点击
     */
    private void onAutoClick() {
        // 准备数据
        BrushSetting brushSetting = SpUtils.getBrushSetting(SwitchFloatingService.this);
        ssData = new Data(brushSetting);


        ArrayList<Task> t1 = ssData.getAllTask();
        ArrayList<Task> t2 = ssData.getTreatTask();

        ssThread = new SSThread(t1, t2,brushSetting);
        ssThread.start();
        UIEvent.postUIStartAction();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReciverUIEvent(UIEvent event) {
        switch (event.getAction()) {
            case UIEvent.UI_UPDATE:
                TimesTip timesTip = event.getTimesTip();
                if(timesTip != null) {
                    tv_tip.setText(timesTip.getSSTip());
                }
                break;
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        removeViewFromWinddow(mFloatingView);
        EventBus.getDefault().unregister(this);
    }

    private void hideDialog(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private void addViewToWindow(View view, WindowManager.LayoutParams params) {
        if (mWindowManager != null) {
            mWindowManager.addView(view, params);
        }
    }

    private void removeViewFromWinddow(View view) {
        if (mWindowManager != null && view != null && view.isAttachedToWindow()) {
            mWindowManager.removeView(view);
        }
    }

    private <T extends View> T creatView(int layout) {
        return (T) LayoutInflater.from(this).inflate(layout, null);
    }
}

