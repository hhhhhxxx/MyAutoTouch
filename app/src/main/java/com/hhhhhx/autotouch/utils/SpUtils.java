package com.hhhhhx.autotouch.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.hhhhhx.autotouch.bean.BrushSetting;
import com.hhhhhx.autotouch.bean.TouchPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * SharedPreferences 工具类
 */
public class SpUtils {
    private static final String TAG = "SpUtils";
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";
    private static final String KEY_TOUCH_LIST = "touch_list";
    private static final String KEY_BRUSH_SETTING = "brush_setting";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context, String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.apply();
    }


    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }

        return null;
    }

    /**
     * 清除所有数据
     *
     * @param context
     */
    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear().apply();
    }

    /**
     * 清除指定数据
     *
     * @param context
     */
    public static void clearAll(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("定义的键名");
        editor.apply();
    }



    // ------------------------------------------------


    private static void setTouchPointsListList(Context context, ArrayList<ArrayList<TouchPoint>> touchPointsListList) {
        while (touchPointsListList.remove(null));
        String string = GsonUtils.beanToJson(touchPointsListList);
        setParam(context, KEY_TOUCH_LIST, string);
    }

    // 添加一行数据
    public static void addTouchPointsList(Context context, ArrayList<TouchPoint> tListList) {
        ArrayList<ArrayList<TouchPoint>> touchPointsListList = getTouchPointsListList(context);
        touchPointsListList.add(tListList);
        setTouchPointsListList(context, touchPointsListList);
    }

    // 更新一行数据
    public static void setTouchPointsList(Context context, ArrayList<TouchPoint> tListList, int listIndex) {
        ArrayList<ArrayList<TouchPoint>> touchPointsListList = getTouchPointsListList(context);
        if (listIndex < touchPointsListList.size() && listIndex >=0) {
            touchPointsListList.set(listIndex, tListList);
            setTouchPointsListList(context, touchPointsListList);
        }
    }

    //  获取所有数据
    public static ArrayList<ArrayList<TouchPoint>> getTouchPointsListList(Context context) {
        String string = (String) getParam(context, KEY_TOUCH_LIST, "");
        if (TextUtils.isEmpty(string)) {
            return new ArrayList<ArrayList<TouchPoint>>();
        }
        ArrayList<ArrayList<TouchPoint>> arrayLists = GsonUtils.jsonToListList(string, TouchPoint.class);
        arrayLists.removeIf(Objects::isNull);
        return arrayLists;
    }

    // 获取一行数据
    public static ArrayList<TouchPoint> getTouchPointsList(Context context, int listIndex) {
        ArrayList<ArrayList<TouchPoint>> touchPointsListList = getTouchPointsListList(context);
        if(listIndex < touchPointsListList.size() && listIndex >=0) {
            return touchPointsListList.get(listIndex);
        } else {
            return new ArrayList<TouchPoint>();
        }
    }

    public static boolean deleteTouchPointList(Context context,int listIndex) {
        ArrayList<ArrayList<TouchPoint>> touchPointsListList = getTouchPointsListList(context);

        if(listIndex < touchPointsListList.size()) {
            touchPointsListList.remove(listIndex);
            setTouchPointsListList(context,touchPointsListList);
            return true;
        } else {
            return false;
        }
    }
    // ------------------------------------------

    // 刷熟设置
    public static BrushSetting getBrushSetting(Context context) {
        String string = (String) getParam(context, KEY_BRUSH_SETTING, "");
        if (TextUtils.isEmpty(string)) {
            BrushSetting brushSetting = new BrushSetting();
            setBrushSetting(context,brushSetting);
            return brushSetting;
        }
        return GsonUtils.jsonToBean(string, BrushSetting.class);
    }
    public static void setBrushSetting(Context context, BrushSetting brushSetting) {
        String string = GsonUtils.beanToJson(brushSetting);
        setParam(context, KEY_BRUSH_SETTING, string);
    }
}
