package com.luhong.locwithlibrary.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

public class ResUtils {

    public static View inflate(Context context, int viewId, ViewGroup root) {
        return LayoutInflater.from(context).inflate(viewId, root, false);
    }

    public static List<String> stringArrayToList(Context context, int arrayId) {
        return Arrays.asList(context.getResources().getStringArray(arrayId));
    }

    public static String getString(Context context, String strName) {
        int id = getStringId(context, strName);
        if (id == 0) return strName;
        return context.getString(id);
    }

    public static int getStringId(Context context, String strName) {
        return context.getApplicationContext().getResources().getIdentifier(strName, "string", context.getPackageName());
    }

    public static int getResId(Context context, String resName, String defType) {
        return context.getResources().getIdentifier(resName, defType, context.getPackageName());
    }

    public static int getDrawableId(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }

    public static int getMipmapId(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "mipmap", context.getPackageName());
    }

    public static String resToStr(Context context, int strId) {
        return context.getString(strId);
    }

    @SuppressWarnings("deprecation")
    public static int resToColor(Context context, int colorId) {
        return context.getResources().getColor(colorId);
    }

    public static Drawable resToDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }

    public static String getResourceName(Context context, int resId) {
        return context.getResources().getResourceName(resId);
    }

    public static Typeface getTypeface(Context context, String filePath) {
        return Typeface.createFromAsset(context.getAssets(), filePath);
    }

}
