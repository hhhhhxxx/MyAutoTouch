package com.hhhhhx.autotouch;

import android.util.Log;

import com.hhhhhx.autotouch.bean.BrushSetting;
import com.hhhhhx.autotouch.bean.Color;
import com.hhhhhx.autotouch.bean.ForBreakTask;
import com.hhhhhx.autotouch.bean.ForBreakTask_0;
import com.hhhhhx.autotouch.bean.ForTask;
import com.hhhhhx.autotouch.bean.IfTask;
import com.hhhhhx.autotouch.bean.Task;
import com.hhhhhx.autotouch.bean.TouchPoint;
import com.hhhhhx.autotouch.bean.WaitTask;

import java.util.ArrayList;

public class Data {


    private static final String TAG = "Data" ;
    private static ArrayList<TouchPoint> attackRow = null;

    private static ArrayList<Task> allTask = null;
    private static ArrayList<Task> treatTask = null;


    private BrushSetting brushSetting;

    public Data(BrushSetting brushSetting) {
        this.brushSetting = brushSetting;
        if (attackRow == null) {
            setRow();
        }
        if (treatTask == null) {
            treat();
        }
        allTask = null;
    }

    private void setRow() {
        attackRow = new ArrayList<>();
        attackRow.add(new TouchPoint("1行刷怪", 875, 156, 2));
        attackRow.add(new TouchPoint("2行刷怪", 997, 234, 2));
        attackRow.add(new TouchPoint("3行刷怪", 879, 318, 2));
        attackRow.add(new TouchPoint("4行刷怪", 1000, 392, 2));
        attackRow.add(new TouchPoint("5行刷怪", 880, 483, 2));
    }

    private ArrayList<Task> init() {
        allTask = new ArrayList<>();
        // 人物 x=138-y=915
        TouchPoint t1 = new TouchPoint("人物", 138, 915, 1);

        // 选择行数    颜色：底部旗子黄 保证进入了战斗 防止对话框
        Color outColor = new Color(-1592733, 294, 1763);
        ArrayList<TouchPoint> rowfbList = new ArrayList<TouchPoint>();
        TouchPoint touchPoint = attackRow.get(brushSetting.getRowNumber() - 1);
        rowfbList.add(touchPoint);
        // 有这个颜色就一直点
        ForBreakTask_0 rowForBreak_0 = new ForBreakTask_0(outColor, rowfbList);


        // 手动小按钮 x=1024-y=1086, rgb: (148,0,0) colorValue:-7077888 切换的红色
        Color color = new Color(-7077888, 1024, 1086);
        ArrayList<TouchPoint> waitList1 = new ArrayList<>();
        // 手动小按钮 x=1024-y=1086, rgb: (148,0,0) colorValue:-7077888
        TouchPoint t2 = new TouchPoint("切换自、手动", 1024, 1086, 0.5);
        // 手动 x=541-y=727
        TouchPoint t3 = new TouchPoint("手动操作", 545, 727, 1);
        waitList1.add(t2);
        waitList1.add(t3);
        WaitTask switchHand = new WaitTask(color, 1, waitList1);


        // 技能 x=636-y=540, rgb: (255,251,255) colorValue:-1025 颜色：文字的白色
        Color skillColor = new Color(-1025, 636, 540);
        ArrayList<TouchPoint> waitList2 = new ArrayList<>();

        // 和技能一样位置
        TouchPoint s1 = new TouchPoint("点击技能", 636, 540, 0.5);
        TouchPoint s2 = new TouchPoint("选择技能", 636, 540, 0.5);
        TouchPoint s3 = new TouchPoint("选择技能", 636, 540, 0.5);
        waitList2.add(s1);
        waitList2.add(s2);
        waitList2.add(s3);
        WaitTask doSkill = new WaitTask(skillColor, 1, waitList2);

        // 技能左边的空白 x=515-y=554, rgb: (173,158,82) colorValue:-5398958
        TouchPoint skillLeftTP = new TouchPoint("技能左边的空白", 515, 554, 0.5);
        ArrayList<TouchPoint> skillFBList = new ArrayList<TouchPoint>();
        skillFBList.add(skillLeftTP);
        ForBreakTask skillForBreak = new ForBreakTask(skillColor,skillFBList);

        // 普通攻击
        ArrayList<TouchPoint> attackWaitList = new ArrayList<>();
        //x=461-y=469
        TouchPoint a1 = new TouchPoint("点击攻击", 461, 469, 0.5);
        TouchPoint a2 = new TouchPoint("选择攻击人", 636, 540, 0.5);
        attackWaitList.add(a1);
        attackWaitList.add(a2);
        WaitTask doAttack = new WaitTask(skillColor, 1, attackWaitList);

        // 等放完技能
        ArrayList<TouchPoint> waitList4 = new ArrayList<>();
        WaitTask waitSkill = new WaitTask(skillColor, 1, waitList4);


        // 开始无脑点
        // 手动小按钮 x=1024-y=1086, rgb: (148,0,0) colorValue:-7077888
        Color color3 = new Color(-7077888, 1024, 1086);
        ArrayList<TouchPoint> waitList3 = new ArrayList<>();
        // 手动小按钮 x=1024-y=1086, rgb: (148,0,0) colorValue:-7077888
        TouchPoint t7 = new TouchPoint("切换自、手动", 1024, 1086, 0.5);
        //  自动操作 x=424-y=574
        TouchPoint t8 = new TouchPoint("自动操作", 424, 574, 0.5);
        waitList3.add(t7);
        waitList3.add(t8);
        WaitTask switchAuto = new WaitTask(color, 1, waitList3);


        // 提前量
        ArrayList forList = new ArrayList<TouchPoint>();

        //  自动操作 x=424-y=574
        TouchPoint t9 = new TouchPoint("自动操作", 424, 574, 0.5);
        // 两个0.5当一秒
        forList.add(t9);
        forList.add(t9);
        ForTask autoAttackFor = new ForTask(brushSetting.getForSecond(), forList);

        // 退出标记 x=294-y=1763, rgb: (231,178,99) colorValue:-1592733

        Color color4 = new Color(-1592733, 294, 1763);
        ArrayList forList2 = new ArrayList<TouchPoint>();
        for (int i = 0; i < brushSetting.getForBreakSecond() * 2; i++) {
            forList2.add(t9);
        }
        ForBreakTask autoAttackForBreak = new ForBreakTask(color4, forList2);

        allTask.add(t1);
        allTask.add(rowForBreak_0);

        if(brushSetting.isWay()) {
            allTask.add(switchHand);
            String mode = getBrushSetting().getMode();
            for (int i = 0; i < 4; i++) {
                char c = mode.charAt(i);
                if(c == '1') {
                    allTask.add(doSkill);
                    //allTask.add(waitSkill);
                    allTask.add(skillForBreak);
                } else {
                    allTask.add(doAttack);
                    allTask.add(skillForBreak);
                    //allTask.add(waitSkill);
                }
            }
            allTask.add(switchAuto);
        }
        allTask.add(autoAttackFor);
        allTask.add(autoAttackForBreak);
        return allTask;
    }


    private ArrayList<Task> treat() {
        treatTask = new ArrayList<>();

        // x=945-y=922, rgb: (33,0,0) colorValue:-14614528
        TouchPoint functionTP = new TouchPoint("功能", 945, 922, 1);

        // x=768-y=163
        TouchPoint goodsTP = new TouchPoint("物品", 768, 163, 1);


        // x=396-y=542, rgb: (247,0,0) colorValue:-589824
        Color treatColor = new Color(-589824, 396, 542);
        ArrayList<TouchPoint> list1 = new ArrayList<>();
        TouchPoint quickTreatTP = new TouchPoint("快速治疗", 396, 542, 2);
        list1.add(quickTreatTP);
        WaitTask quickWait = new WaitTask(treatColor, 1, list1);

        // 一直点 等快速治疗响应成功  红色被遮挡
        ForBreakTask_0 quickForBreak_0 = new ForBreakTask_0(treatColor, list1);

        // 无需恢复 确定 x=459-y=778, rgb: (222,195,41) colorValue:-2178263
        Color isNoTreatColor = new Color(-2178263, 459, 778);
        ArrayList<TouchPoint> confirmList = new ArrayList<>();
        TouchPoint cTp = new TouchPoint("确认", 459, 778, 1);
        confirmList.add(cTp);
        confirmList.add(cTp);
        ForBreakTask_0 isNoTreatForBreak_0 = new ForBreakTask_0(isNoTreatColor, confirmList);

        // 返回 x=996-y=1427, rgb: (214,190,41) colorValue:-2703831
        Color backButtonColor = new Color(-2703831, 996, 1427);
        TouchPoint backTP = new TouchPoint("返回", 996, 1427, 2);
        ArrayList<TouchPoint> backTPList = new ArrayList<>();
        backTPList.add(backTP);
        ForBreakTask_0 backForBreak_0 = new ForBreakTask_0(backButtonColor,backTPList);

        //  背包与外面不同的颜色 x=258-y=1162, rgb: (222,138,16) colorValue:-2192880
        Color mainColor = new Color(-2192880, 258, 1162);
        ArrayList<TouchPoint> list3 = new ArrayList<>();
        TouchPoint roleTP = new TouchPoint("人物", 138, 915, 1);
        list3.add(roleTP);
        WaitTask mainWait = new WaitTask(mainColor, 1, list3);


        treatTask.add(functionTP);
        treatTask.add(goodsTP);
        treatTask.add(quickWait);
        treatTask.add(quickForBreak_0);
        treatTask.add(isNoTreatForBreak_0);
        treatTask.add(backForBreak_0);
        treatTask.add(mainWait);
        return treatTask;
    }

    public ArrayList<Task> getAllTask() {
        if (allTask == null) {
            return init();
        }
        return allTask;
    }

    public ArrayList<Task> getTreatTask() {
        if (treatTask == null) {
            return treat();
        }
        return treatTask;
    }

    public BrushSetting getBrushSetting() {
        return brushSetting;
    }

    public void setBrushSetting(BrushSetting brushSetting) {
        this.brushSetting = brushSetting;
        allTask = null;
    }
}
