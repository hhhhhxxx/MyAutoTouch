package com.hhhhhx.autotouch.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.hhhhhx.autotouch.R;

import java.util.ArrayList;

public class HomeActivity extends Activity {

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);


        findViewById(R.id.setSS).setOnClickListener(view -> {
            startActivity(new Intent(HomeActivity.this,SetSSActivity.class));
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        //如果正在触控，则暂停
//        SpUtils.clear(getApplicationContext());
//        SpUtils.clearAll(getApplicationContext());
    }
}
