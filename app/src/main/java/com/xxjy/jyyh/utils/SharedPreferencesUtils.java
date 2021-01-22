package com.xxjy.jyyh.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.xxjy.jyyh.app.App;

/**
 * Created by czb365 on 2017/4/18.
 */

public class SharedPreferencesUtils {

    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "youfenqi_date";
    private static SharedPreferences sp = App.getContext().getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor = sp.edit();


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param paramContext
     * @param spSaveKey
     * @param spSaveValue
     */
    public static boolean setParam(Context paramContext, String spSaveKey, Object spSaveValue) {
        return setParam(spSaveKey, spSaveValue);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param spSaveKey   sp要保存的键
     * @param spSaveValue sp要保存的值
     */
    public static boolean setParam(String spSaveKey, Object spSaveValue) {
        String type = spSaveValue.getClass().getSimpleName();
        if ("String".equals(type)) {
            editor.putString(spSaveKey, (String) spSaveValue);
        } else if ("Integer".equals(type)) {
            editor.putInt(spSaveKey, (Integer) spSaveValue);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(spSaveKey, (Boolean) spSaveValue);
        } else if ("Float".equals(type)) {
            editor.putFloat(spSaveKey, (Float) spSaveValue);
        } else if ("Long".equals(type)) {
            editor.putLong(spSaveKey, (Long) spSaveValue);
        }
        return editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param paramContext
     * @param spSaveKey
     * @param spSaveDefaultValue
     * @return
     */
    public static Object getParam(Context paramContext, String spSaveKey, Object spSaveDefaultValue) {
        return getParam(spSaveKey, spSaveDefaultValue);
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param spSaveKey          sp中存储的键
     * @param spSaveDefaultValue sp中没有该键对应的值时候的默认值
     * @return
     */
    public static Object getParam(String spSaveKey, Object spSaveDefaultValue) {
        String type = spSaveDefaultValue.getClass().getSimpleName();
        if ("String".equals(type)) {
            return sp.getString(spSaveKey, (String) spSaveDefaultValue);
        } else if ("Integer".equals(type)) {
            return sp.getInt(spSaveKey, (Integer) spSaveDefaultValue);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(spSaveKey, (Boolean) spSaveDefaultValue);
        } else if ("Float".equals(type)) {
            return sp.getFloat(spSaveKey, (Float) spSaveDefaultValue);
        } else if ("Long".equals(type)) {
            return sp.getLong(spSaveKey, (Long) spSaveDefaultValue);
        }
        return null;
    }

    public static void clear() {
        editor.clear();
        editor.commit();
    }

    public static boolean remove(String removeKey) {
        editor.remove(removeKey);
        return editor.commit();
    }

}
