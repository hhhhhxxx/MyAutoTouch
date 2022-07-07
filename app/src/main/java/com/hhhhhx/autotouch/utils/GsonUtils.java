package com.hhhhhx.autotouch.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GsonUtils {

    private static final String TAG = "GsonUtils";
    private static Gson gson;

    static {
        gson = new Gson();
    }

    public static <T> T jsonToBean(String gsonString, Class<T> cls) {
        T t = null;
        if (gson != null) {
            t = gson.fromJson(gsonString, cls);
        }
        return t;
    }

    public static String beanToJson(Object object) {
        String gsonString = null;
        if (gson != null) {
            gsonString = gson.toJson(object);
        }
        return gsonString;
    }

    /**
     * 转成list
     * 解决泛型问题
     *
     * @param json json
     * @param cls  类
     * @param <T>  T
     * @return T列表
     */
    public static <T> List<T> jsonToList(String json, Class<T> cls) {
        Gson gson = new Gson();
        List<T> list = new ArrayList<>();
        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        for (final JsonElement elem : array) {
            list.add(gson.fromJson(elem, cls));
        }
        return list;
    }

    public static <T> ArrayList<ArrayList<T>> jsonToListList(String json, Class<T> cls) {
        Gson gson = new Gson();
        ArrayList<ArrayList<T>> list = new ArrayList<>();

        JsonArray array = new JsonParser().parse(json).getAsJsonArray();
        while (array.remove(null));

        for (final JsonElement elem : array) {
            if(elem == null) {
                continue;
            } else {
                ArrayList jsonInnerList = gson.fromJson(elem, ArrayList.class);
                ArrayList<T> innerList = new ArrayList<>();
                for (Object obj : jsonInnerList) {
                    innerList.add(gson.fromJson(gson.toJson(obj),cls));
                }
                list.add(innerList);
            }
        }
        return list;
    }
}
