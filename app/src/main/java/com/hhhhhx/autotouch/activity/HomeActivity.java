package com.hhhhhx.autotouch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hhhhhx.autotouch.R;
import com.hhhhhx.autotouch.adapter.TouchPointAdapterPlus;
import com.hhhhhx.autotouch.event.TouchEvent;
import com.hhhhhx.autotouch.bean.TouchPoint;
import com.hhhhhx.autotouch.utils.SpUtils;

import java.util.ArrayList;

public class HomeActivity extends Activity {

    private static final String TAG = "HomeActivity";
    private RecyclerView rvPoints;
    private TouchPointAdapterPlus touchPointAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        rvPoints = findViewById(R.id.rv);
        touchPointAdapter = new TouchPointAdapterPlus();
        touchPointAdapter.setOnItemClickListener(new TouchPointAdapterPlus.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ArrayList<TouchPoint> tList) {
                Intent intent = new Intent(HomeActivity.this, ItemActivity.class);
                intent.putExtra("listIndex",position);
                Log.d(TAG, "position: "+position);
                startActivity(intent);
            }
        });
        rvPoints.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvPoints.setAdapter(touchPointAdapter);

        findViewById(R.id.addScriptBtn).setOnClickListener(view -> {
            ArrayList<TouchPoint> tList = new ArrayList<>();

            SpUtils.addTouchPointsList(getApplicationContext(),tList);
            if (touchPointAdapter != null) {
                ArrayList<ArrayList<TouchPoint>> tListList = SpUtils.getTouchPointsListList(getApplicationContext());
                touchPointAdapter.setTouchPointListList(tListList);
            }
        });

        findViewById(R.id.setSS).setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this,SetSSActivity.class));
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        //??????????????????????????????

//        SpUtils.clear(getApplicationContext());
//        SpUtils.clearAll(getApplicationContext());

        TouchEvent.postPauseAction();
        if (touchPointAdapter != null) {
            ArrayList<ArrayList<TouchPoint>> tListList = SpUtils.getTouchPointsListList(getApplicationContext());
            touchPointAdapter.setTouchPointListList(tListList);
        }
    }
}
