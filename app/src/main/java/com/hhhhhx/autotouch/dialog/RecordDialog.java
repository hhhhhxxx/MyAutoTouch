package com.hhhhhx.autotouch.dialog;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.hhhhhx.autotouch.R;

/**
 * 录制模式Dialog
 */
public class RecordDialog extends BaseServiceDialog {
    private Button cancelBtn;

    public RecordDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_record;
    }

    @Override
    protected int getWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected int getHeight() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected void onInited() {
        cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(view -> {
            RecordDialog.this.dismiss();
        });
    }

    @Override
    public void show() {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();
            // 对话框背景不变暗
            params.dimAmount = 0f;
            //允许点击穿透 屏蔽点击
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        super.show();
    }

    @Override
    public boolean dispatchTouchEvent(@NonNull MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            Log.d("录制坐标", "x=" + ev.getRawX() + "y=" + ev.getRawY());
        }
        return super.dispatchTouchEvent(ev);
    }
}
