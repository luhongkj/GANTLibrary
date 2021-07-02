package com.luhong.locwithlibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Build;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * Created by ITMG on 2019-12-11.
 */
public class BitmapUtils {

    /**
     * 获取view截图
     *
     * @param v
     * @return
     */
    public static Bitmap loadBitmapFromView(View v) {
        if (null == v) {
            return null;
        }
        try {
            v.setDrawingCacheEnabled(true);
            v.buildDrawingCache();
            if (Build.VERSION.SDK_INT >= 11) {
                v.measure(View.MeasureSpec.makeMeasureSpec(v.getWidth(), View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(v.getHeight(), View.MeasureSpec.EXACTLY));
                v.layout((int) v.getX(), (int) v.getY(), (int) v.getX() + v.getMeasuredWidth(), (int) v.getY() + v.getMeasuredHeight());
            } else {
                v.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            }
            Bitmap b = Bitmap.createBitmap(v.getDrawingCache(), 0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());

            v.setDrawingCacheEnabled(false);
            v.destroyDrawingCache();
            return b;
        } catch (Exception e) {
            Logger.error("获取view截图出错=" + e);
            return null;
        }
    }

    /**
     * 合并轨迹
     *
     * @param backBitmap
     * @param frontBitmap
     * @return
     */
    public static Bitmap mergeTrackBitmap(Bitmap backBitmap, Bitmap frontBitmap) {
        if (backBitmap == null || frontBitmap == null) {
            return null;
        }
        int backW = backBitmap.getWidth();
        int backH = backBitmap.getHeight();

        int frontW = frontBitmap.getWidth();
        int frontH = frontBitmap.getHeight();

        frontBitmap = imageScale(frontBitmap, backW, frontH * backW / frontW);

        Bitmap bitmap = Bitmap.createBitmap(backW, backH, backBitmap.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Matrix matrix = new Matrix();
//        matrix.postScale(backW, backH);
        canvas.drawBitmap(backBitmap, matrix, null);

        int left = backW - frontW > 0 ? ((backW - frontW) / 2) : 0;
        RectF rectF = new RectF(left, backH - frontH, frontW - left, backH);
        canvas.drawBitmap(frontBitmap, null, rectF, null);

//        if (backBitmap != null) backBitmap.recycle();
        if (frontBitmap != null) frontBitmap.recycle();
        return bitmap;
    }


    public static Bitmap decodeResource(Context context, int resId) {
        return BitmapFactory.decodeResource(context.getResources(), resId);
    }

    public static Bitmap decodeFile(String filePath) {
        return BitmapFactory.decodeFile(filePath);
    }

    /**
     * 压缩图片
     *
     * @param bitmap    被压缩的图片
     * @param sizeLimit 大小限制
     * @return 压缩后的图片
     */
    public static Bitmap compressBitmap(Bitmap bitmap, long sizeLimit) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        // 循环判断压缩后图片是否超过限制大小
        while (baos.toByteArray().length / 1024 > sizeLimit) {
            // 清空baos
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
        }
        Bitmap newBitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(baos.toByteArray()), null, null);
        return newBitmap;
    }

    /**
     * 调整图片大小
     *
     * @param bitmap 源
     * @param dst_w  输出宽度
     * @param dst_h  输出高度
     * @return
     */
    public static Bitmap imageScale(Bitmap bitmap, int dst_w, int dst_h) {
        int src_w = bitmap.getWidth();
        int src_h = bitmap.getHeight();
        float scale_w = ((float) dst_w) / src_w;
        float scale_h = ((float) dst_h) / src_h;
        Matrix matrix = new Matrix();
        matrix.postScale(scale_w, scale_h);
        Bitmap dstbmp = Bitmap.createBitmap(bitmap, 0, 0, src_w, src_h, matrix, true);
        return dstbmp;
    }

    /**
     * 获取缩略图
     * @param bitmap
     * @param sizeWidth
     * @return
     */
    public static Bitmap getThumbBitmap(Bitmap bitmap, double sizeWidth) {
        Bitmap imgBitmap = bitmap;
        int h = imgBitmap.getHeight();
        int w = imgBitmap.getWidth();
        if ((w > h) && (w > sizeWidth)) {
            double ratio = sizeWidth / w;
            w = (int) sizeWidth;
            h = (int) (ratio * h);
        } else if ((h > w) && (h > sizeWidth)) {
            double ratio = sizeWidth / h;
            h = (int) sizeWidth;
            w = (int) (ratio * w);
        }
        Bitmap scaled = Bitmap.createScaledBitmap(imgBitmap, w, h, true);
        return scaled;
    }
}
