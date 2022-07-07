package com.hhhhhx.autotouch.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hhhhhx.autotouch.R;
import com.hhhhhx.autotouch.activity.HomeActivity;
import com.hhhhhx.autotouch.activity.SetSSActivity;
import com.hhhhhx.autotouch.adapter.TouchPointAdapterShow;
import com.hhhhhx.autotouch.event.TouchEventManager;
import com.hhhhhx.autotouch.event.TouchEvent;
import com.hhhhhx.autotouch.bean.TouchPoint;
import com.hhhhhx.autotouch.utils.DensityUtil;
import com.hhhhhx.autotouch.utils.DialogUtils;
import com.hhhhhx.autotouch.utils.SpUtils;
import com.hhhhhx.autotouch.utils.ToastUtil;

import java.util.ArrayList;

public class MenuDialog extends BaseServiceDialog implements View.OnClickListener {

    private static final String TAG = "菜单对话框";

    private RecyclerView rvPoints;

    private AddPointDialog addPointDialog;
    private Listener listener;
    private TouchPointAdapterShow touchPointAdapter;
    private RecordDialog recordDialog;

    public MenuDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_menu;
    }

    @Override
    protected int getWidth() {
        return DensityUtil.dip2px(getContext(), 350);
    }

    @Override
    protected int getHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    @Override
    protected void onInited() {
        setCanceledOnTouchOutside(true);
        findViewById(R.id.bt_exit).setOnClickListener(this);
        findViewById(R.id.bt_add).setOnClickListener(this);
        findViewById(R.id.bt_record).setOnClickListener(this);
        // 我自己加的
//        findViewById(R.id.bt_clear).setOnClickListener(this);
//        findViewById(R.id.bt_test).setOnClickListener(this);
        findViewById(R.id.bt_execute).setOnClickListener(this);

        rvPoints = findViewById(R.id.rv);
        touchPointAdapter = new TouchPointAdapterShow();
        touchPointAdapter.setOnItemClickListener(new TouchPointAdapterShow.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList<TouchPoint> tList) {
                dismiss();
                TouchEvent.postStartAction(tList);
                ToastUtil.show("已开启触控点：");
            }
        });
        rvPoints.setLayoutManager(new LinearLayoutManager(getContext()));
        rvPoints.setAdapter(touchPointAdapter);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (TouchEventManager.getInstance().isPaused()) {
                    TouchEvent.postContinueAction();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        //如果正在触控，则暂停
        TouchEvent.postPauseAction();
        if (touchPointAdapter != null) {
            ArrayList<ArrayList<TouchPoint>> tListList = SpUtils.getTouchPointsListList(getContext());
//            Log.d(TAG, GsonUtils.beanToJson(touchPoints));
            touchPointAdapter.setTouchPointListList(tListList);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                DialogUtils.dismiss(addPointDialog);
                addPointDialog = new AddPointDialog(getContext());
                addPointDialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
//                        MenuDialog.this.show();
                    }
                });
                addPointDialog.show();
                dismiss();
                break;
            case R.id.bt_record:
                dismiss();
                if (listener != null) {
                    listener.onFloatWindowAttachChange(false);
                    if (recordDialog == null) {
                        recordDialog = new RecordDialog(getContext());
                        recordDialog.setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                listener.onFloatWindowAttachChange(true);
                                MenuDialog.this.show();
                            }
                        });
                    }
                    recordDialog.show();
                }
                break;
            case R.id.bt_exit:
                TouchEvent.postStopAction();
                if (listener != null) {
                    listener.onExitService();
                }
                break;
            case R.id.bt_execute:
                MenuDialog.this.dismiss();
                TouchEvent.postStartSSAction();
                ToastUtil.show("已开启刷熟：");
                break;

        }
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public interface Listener {
        /**
         * 悬浮窗显示状态变化
         *
         * @param attach
         */
        void onFloatWindowAttachChange(boolean attach);

        /**
         * 关闭辅助
         */
        void onExitService();
    }
}
