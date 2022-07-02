package com.hhhhhx.autotouch.dialog;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;

import com.hhhhhx.autotouch.R;
import com.hhhhhx.autotouch.activity.ItemActivity;
import com.hhhhhx.autotouch.bean.ItemEvent;
import com.hhhhhx.autotouch.bean.TouchPoint;
import com.hhhhhx.autotouch.utils.ToastUtil;

import java.util.ArrayList;

public class AddPointDialog extends BaseServiceDialog implements View.OnClickListener {

    private static final String TAG = "AddPointDialog++";
    private EditText etName;
    private EditText etTime;
    private Group groupInput;
    private TextView tvHint;
    private int x;
    private int y;

    public AddPointDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_add_point;
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
        Log.d(TAG, "onInited: ");
        etName = findViewById(R.id.et_name);
        etTime = findViewById(R.id.et_time);
        groupInput = findViewById(R.id.gl_input);
        tvHint = findViewById(R.id.tv_hint);
        findViewById(R.id.bt_commit).setOnClickListener(this);
        findViewById(R.id.bt_cancel).setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            x = (int) event.getRawX();
            y = (int) event.getRawY();
            tvHint.setVisibility(View.GONE);
            groupInput.setVisibility(View.VISIBLE);
            Log.d(TAG, "onTouchEvent: "+x+"-"+y);
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_commit:
                String name = null;
                double second = 0;
                try {
                    name = etName.getText().toString().trim();
                    second = Double.parseDouble(etTime.getText().toString().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (TextUtils.isEmpty(name) ||  second <= 0) {
                    ToastUtil.show("名字或秒数错误");
                    return;
                }
                TouchPoint touchPoint = new TouchPoint(name, x, y, second);

                ItemEvent.postItemAddAction(touchPoint);
                dismiss();
                Intent intent = new Intent(getContext(), ItemActivity.class);
                v.getContext().startActivity(intent);
                break;
            case R.id.bt_cancel:
                dismiss();
                break;
        }
    }
}
