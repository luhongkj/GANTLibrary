package com.luhong.locwithlibrary.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 数据本地保存
 * Created by ITMG on 2020-01-13.
 */
public class SPUtils
{
    public static final String FILE_NAME = "share_data";

    /**
     * 基本数据类型
     *
     * @param
     * @param key
     * @param object
     */
    public static void put(Context context, String key, Object object)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();

        if (object instanceof String)
        {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer)
        {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean)
        {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float)
        {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long)
        {
            editor.putLong(key, (Long) object);
        } else
        {
            editor.putString(key, object.toString());
        }
        SharedPreferencesCompat.apply(editor);
    }

    public static String getString(Context context, String key, String defaultObject)
    {
        return (String) get(context, key, defaultObject);
    }

    public static String getString(Context context, String key)
    {
        return getString(context, key, null);
    }

    public static boolean getBoolean(Context context, String key, boolean defaultObject)
    {
        return (boolean) get(context, key, defaultObject);
    }

    /**
     * 基本数据类型
     *
     * @param
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object get(Context context, String key, Object defaultObject)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if (defaultObject instanceof String)
        {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer)
        {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean)
        {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float)
        {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long)
        {
            return sp.getLong(key, (Long) defaultObject);
        } else
        {
            return sp.getString(key, null);
        }
        //		return null;
    }

    private static Gson gson = new GsonBuilder().create();

    /**
     * 保存实现序列化的对象
     *
     * @param
     * @param key
     * @param object
     * @return
     */
    public static boolean putObject(Context context, String key, Object object)
    {
        SharedPreferences share = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = share.edit();
        if (object == null)
        {
            editor.remove(key);
            editor.apply();
            editor.commit();
            return false;
        }
        String objectStr = gson.toJson(object);
        editor.putString(key, objectStr);
        editor.commit();
        return true;
    }

    /**
     * 获取对象
     *
     * @param context
     * @param key
     * @return
     */
    public static <T> T getObject(Context context, String key, Type typeOfT)
    {
        SharedPreferences sharePre = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        try
        {
            String wordBase64 = sharePre.getString(key, "");
            if (wordBase64 == null || wordBase64.equals(""))
            {
                return null;
            }
            return gson.fromJson(wordBase64, typeOfT);
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param
     * @param key
     */
    public static void removeByKey(Context context, String key)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.remove(key);
        SharedPreferencesCompat.apply(editor);
    }

    /**
     * 清除所有数据
     *
     * @param
     */
    public static void clear(Context context)
    {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        editor.clear();
        SharedPreferencesCompat.apply(editor);
    }


    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private static class SharedPreferencesCompat
    {
        private static final Method sApplyMethod = findApplyMethod();

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        @SuppressWarnings({"unchecked", "rawtypes"})
        private static Method findApplyMethod()
        {
            try
            {
                Class clz = Editor.class;
                return clz.getMethod("apply");
            } catch (NoSuchMethodException e)
            {
            }

            return null;
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        private static void apply(Editor editor)
        {
            try
            {
                if (sApplyMethod != null)
                {
                    sApplyMethod.invoke(editor);
                    return;
                }
            } catch (Exception e)
            {
                Logger.error("反射invoke出错=" + e);
            }
            editor.commit();
        }
    }

}
