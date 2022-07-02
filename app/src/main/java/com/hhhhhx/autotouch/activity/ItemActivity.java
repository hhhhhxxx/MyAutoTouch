package com.hhhhhx.autotouch.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hhhhhx.autotouch.R;
import com.hhhhhx.autotouch.adapter.TouchPointAdapterItem;
import com.hhhhhx.autotouch.bean.ItemEvent;
import com.hhhhhx.autotouch.bean.TouchEvent;
import com.hhhhhx.autotouch.bean.TouchPoint;
import com.hhhhhx.autotouch.dialog.AddPointDialog;
import com.hhhhhx.autotouch.dialog.MenuDialog;
import com.hhhhhx.autotouch.utils.GsonUtils;
import com.hhhhhx.autotouch.utils.SpUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class ItemActivity extends Activity {
    private AddPointDialog addPointDialog;

    private static final String TAG = "ItemActivity";
    private RecyclerView rvPoints;
    private TouchPointAdapterItem touchPointAdapter;

    private int listIndex = -1;
    private  ArrayList<TouchPoint> touchPointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.script_page);

        EventBus.getDefault().register(this);

        rvPoints = findViewById(R.id.rv);
        touchPointAdapter = new TouchPointAdapterItem();
        touchPointAdapter.setOnItemClickListener(new TouchPointAdapterItem.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, TouchPoint touchPoint) {

                Log.d(TAG, "onItemClick: "+view.getTag());
            }
        });
        rvPoints.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvPoints.setAdapter(touchPointAdapter);

        findViewById(R.id.addPoint).setOnClickListener(view -> {
            addPointDialog = new AddPointDialog(getApplicationContext());
            addPointDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {

                }
            });
            addPointDialog.show();
            moveTaskToBack(false);
        });

        findViewById(R.id.saveBtn).setOnClickListener(view -> {
            if(listIndex >=0) {
                SpUtils.setTouchPointsList(getApplicationContext(),touchPointList,listIndex);
            } else {
                Log.d(TAG, "saveBtn: 保存失败");
            }
            finish();
        });

        findViewById(R.id.cancelBtn).setOnClickListener(view -> {
            finish();
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        //如果正在触控，则暂停
        TouchEvent.postPauseAction();
        Intent intent = getIntent();

        Log.d(TAG, "获取之前: "+ GsonUtils.beanToJson(touchPointList));

        listIndex = intent.getIntExtra("listIndex",-1);
        
        if(touchPointList != null && listIndex >=0) {
            touchPointAdapter.setTouchPointListList(touchPointList);
        } else if (touchPointAdapter != null && touchPointList == null && listIndex >= 0) {
            ArrayList<ArrayList<TouchPoint>> tListList = SpUtils.getTouchPointsListList(getApplicationContext());
            touchPointList = SpUtils.getTouchPointsList(getApplicationContext(), listIndex);
            touchPointAdapter.setTouchPointListList(touchPointList);
        } else {
            finish();
            Log.d(TAG, "onStart: touchList内存丢失");
        }

        Log.d(TAG, "获取之后: "+ GsonUtils.beanToJson(touchPointList));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReciverItemEvent(ItemEvent event) {
        Log.d(TAG, "onReciverTouchEvent: " + event.toString());
        
        switch (event.getAction()) {
            case ItemEvent.ITEM_DELETE:
                delItemInTouchPointList(event.getDelPosition());
                break;
            case ItemEvent.ITEM_ADD:
                addItemInTouchPointList(event.getTouchPoint());
                break;
        }
    }
    
    private void delItemInTouchPointList(int position) {
        if(touchPointList != null && position >= 0 && position < touchPointList.size()) {
            touchPointList.remove(position);
            touchPointAdapter.setTouchPointListList(touchPointList);
        } else {
            Log.d(TAG, "delItemInTouchPointList: 删除失败");
        }
    }
    private void addItemInTouchPointList(TouchPoint touchPoint) {
        touchPointList.add(touchPoint);
        touchPointAdapter.setTouchPointListList(touchPointList);
    }

}
