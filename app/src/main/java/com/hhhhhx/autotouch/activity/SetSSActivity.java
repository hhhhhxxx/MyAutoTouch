package com.hhhhhx.autotouch.activity;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hhhhhx.autotouch.Data;
import com.hhhhhx.autotouch.R;
import com.hhhhhx.autotouch.adapter.TouchPointAdapterPlus;
import com.hhhhhx.autotouch.bean.BrushSetting;
import com.hhhhhx.autotouch.event.TouchEvent;
import com.hhhhhx.autotouch.bean.TouchPoint;
import com.hhhhhx.autotouch.utils.SpUtils;
import com.hhhhhx.autotouch.utils.ToastUtil;

import java.util.ArrayList;

public class SetSSActivity extends Activity {

    private static final String TAG = "SetSSActivity";

    private Spinner spinner;
    private EditText doTimesET;
    private EditText treatTimesET;
    private EditText forSecondET;
    private EditText forBreakSecondET;

    private Button bt_save;
    private Button bt_back;

    private RadioGroup rg0;
    private RadioButton s0;
    private RadioButton a0;

    private RadioGroup rg1;
    private RadioGroup rg2;
    private RadioGroup rg3;
    private RadioGroup rg4;

    private RadioButton s1;
    private RadioButton s2;
    private RadioButton s3;
    private RadioButton s4;

    private RadioButton a1;
    private RadioButton a2;
    private RadioButton a3;
    private RadioButton a4;

    private View detail;

    private String mode1;
    private String mode2;
    private String mode3;
    private String mode4;

    private boolean way;

    private String[] ctype = new String[]{"1行刷怪","2行刷怪","3行刷怪","4行刷怪","5行刷怪"};
    private int selectIndex = 0;

    private BrushSetting brushSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setss_page);

        doTimesET = findViewById(R.id.et_doTimes);
        treatTimesET = findViewById(R.id.et_treatTimes);
        forSecondET = findViewById(R.id.et_forSecond);
        forBreakSecondET = findViewById(R.id.et_forBreakSecond);

        bt_save = findViewById(R.id.bt_save);
        bt_back = findViewById(R.id.bt_back);

        rg1 =findViewById(R.id.rg1);
        rg2 =findViewById(R.id.rg2);
        rg3 =findViewById(R.id.rg3);
        rg4 =findViewById(R.id.rg4);

        s1 =findViewById(R.id.s1);
        s2 =findViewById(R.id.s2);
        s3 =findViewById(R.id.s3);
        s4 =findViewById(R.id.s4);

        a1 =findViewById(R.id.a1);
        a2 =findViewById(R.id.a2);
        a3 =findViewById(R.id.a3);
        a4 =findViewById(R.id.a4);

        rg0 =findViewById(R.id.rg0);
        s0 =findViewById(R.id.s0);
        a0 =findViewById(R.id.a0);

        detail =findViewById(R.id.detail);

        //数据绑定自定义下拉布局文件，并且为每列设置下拉列布局，相当Listview
        ArrayAdapter<String> adpter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,ctype);
        adpter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        //获取Spinner组件,
        spinner = (Spinner) findViewById(R.id.Spinner);
        spinner.setAdapter(adpter);
        spinner.setOnItemSelectedListener(new MySelectedListener());

        bt_save.setOnClickListener(view -> {
            int d = Integer.parseInt(doTimesET.getText().toString().trim());
            int t = Integer.parseInt(treatTimesET.getText().toString().trim());
            int f = Integer.parseInt(forSecondET.getText().toString().trim());
            int fb = Integer.parseInt(forBreakSecondET.getText().toString().trim());

            if(d < 0 || t <0 || f<0 || fb <0) {
                ToastUtil.show("输入有误");
                return;
            }
            BrushSetting brushSetting = new BrushSetting(d, t, selectIndex + 1, f, fb,way,mode1+mode2+mode3+mode4);
            SpUtils.setBrushSetting(getApplication(),brushSetting);
            ToastUtil.show("保存成功");
        });

        bt_back.setOnClickListener(view -> {
            finish();
        });

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                String s = radbtn.getText().toString();
                if(s.equals("技能")) {
                    mode1 = "1";
                } else{
                    mode1 = "0";
                }
            }
        });
        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                String s = radbtn.getText().toString();
                if(s.equals("技能")) {
                    mode2 = "1";
                } else{
                    mode2 = "0";
                }
            }
        });
        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                String s = radbtn.getText().toString();
                if(s.equals("技能")) {
                    mode3 = "1";
                } else{
                    mode3 = "0";
                }
            }
        });
        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                String s = radbtn.getText().toString();
                if(s.equals("技能")) {
                    mode4 = "1";
                } else{
                    mode4 = "0";
                }
            }
        });

        rg0.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                String s = radbtn.getText().toString();
                if(s.equals("精确技能")) {
                    way = true;
                    detail.setVisibility(View.VISIBLE);
                } else{
                    way = false;
                    detail.setVisibility(View.GONE);
                }
            }
        });

        init();
    }

    public void init() {
        BrushSetting brushSetting = SpUtils.getBrushSetting(getApplicationContext());

        Log.d(TAG, "init: "+brushSetting.toString());

        doTimesET.setText(String.valueOf(brushSetting.getDoTimes()));
        treatTimesET.setText(String.valueOf(brushSetting.getTreatTimes()));
        forSecondET.setText(String.valueOf(brushSetting.getForSecond()));
        forBreakSecondET.setText(String.valueOf(brushSetting.getForBreakSecond()));
        spinner.setSelection(brushSetting.getRowNumber()-1);

        String mode = brushSetting.getMode();

        if (mode.charAt(0) == '1') {
            rg1.check(R.id.s1);
            mode1 = "1";
        } else {
            rg1.check(R.id.a1);
            mode1 = "0";
        }

        if (mode.charAt(1) == '1') {
            rg2.check(R.id.s2);
            mode2 = "1";
        } else {
            rg2.check(R.id.a2);
            mode2 = "0";
        }

        if (mode.charAt(2) == '1') {
            rg3.check(R.id.s3);
            mode3 = "1";
        } else {
            rg3.check(R.id.a3);
            mode3 = "0";
        }

        if (mode.charAt(3) == '1') {
            rg4.check(R.id.s4);
            mode4 = "1";
        } else {
            rg4.check(R.id.a4);
            mode4 = "0";
        }

        way = brushSetting.isWay();

        if(way) {
            rg0.check(R.id.s0);
            detail.setVisibility(View.VISIBLE);
        } else {
            rg0.check(R.id.a0);
            detail.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    class MySelectedListener implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            selectIndex = i;
        }
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }
}

