package com.luhong.locwithlibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.luhong.locwithlibrary.Locwith;

import java.io.File;

public class ImageLoadUtils {
    public static void clearMemory() {
        Glide.get(Locwith.getContext()).clearMemory();
    }

    public static void loadResize(Object object, int width, int height, int errRes, ImageView imageView) {
        if (object == null) return;
        RequestOptions options = new RequestOptions().override(width, height).error(errRes);
        getLoadInstance(imageView.getContext(), object).apply(options).into(imageView);
    }

    public static void loadResize(Object object, int width, int height, int placeholderRes, int errRes, ImageView imageView) {
        if (object == null) return;
        RequestOptions options = new RequestOptions().override(width, height).placeholder(placeholderRes).error(errRes);
        getLoadInstance(imageView.getContext(), object).apply(options).into(imageView);
    }

    public static void loadRound(Object object, int cornerRadius, int placeholderRes, int errRes, ImageView imageView) {
        if (object == null) return;
        RequestOptions options = new RequestOptions().centerCrop().transform(new GlideRoundTransform(cornerRadius, GlideRoundTransform.CornerType.ALL))/*.bitmapTransform(new RoundedCorners(cornerRadius))*/.placeholder(placeholderRes).error(errRes);
        getLoadInstance(imageView.getContext(), object).apply(options).into(imageView);
    }

    public static void loadRound(Object object, int errRes, ImageView imageView) {//设置圆角图片
        loadRound(object, 4, errRes, errRes, imageView);
    }

    public static void loadRound(Object object, int cornerRadius, int errRes, ImageView imageView) {//设置圆角图片
        loadRound(object, cornerRadius, errRes, errRes, imageView);
    }

    public static void loadCircle(Object object, int placeholderRes, int errRes, ImageView imageView) {//圆形
        if (object == null) return;
        RequestOptions options = RequestOptions.bitmapTransform(new CircleCrop()).placeholder(placeholderRes).error(errRes);
        getLoadInstance(imageView.getContext(), object).apply(options).into(imageView);
    }

    public static void loadCircle(Object object, ImageView imageView) {//圆形
        loadCircle(object, 0, 0, imageView);
    }

    public static void loadCircle(Object object, int errRes, ImageView imageView) {//圆形
        loadCircle(object, errRes, errRes, imageView);
    }

    public static void load(Object object, ImageView imageView) {
        load(object, 0, imageView);
    }

    public static void load(Object object, int errRes, ImageView imageView) {
        if (object == null) return;
        RequestOptions options = new RequestOptions().placeholder(errRes).error(errRes);
        getLoadInstance(imageView.getContext(), object).apply(options).into(imageView);
    }

    public static void load(Object object, int placeholderRes, int errRes, ImageView imageView) {
        if (object == null) return;
        RequestOptions options = new RequestOptions().placeholder(placeholderRes).error(errRes);
        getLoadInstance(imageView.getContext(), object).apply(options).into(imageView);
    }

    private static RequestBuilder<Drawable> getLoadInstance(Object object) {//获取具体实例
        return getLoadInstance(Locwith.getContext(), object);
    }

    private static RequestBuilder<Drawable> getLoadInstance(Context context, Object object) {//获取具体实例
        RequestBuilder<Drawable> requestBuilder = null;
        if (object == null) return null;
        if (object instanceof String) {
            requestBuilder = Glide.with(context).load((String) object);
        } else if (object instanceof Integer) {
            requestBuilder = Glide.with(context).load((int) object);
        } else if (object instanceof File) {
            requestBuilder = Glide.with(context).load((File) object);
        } else if (object instanceof Uri) {
            requestBuilder = Glide.with(context).load((Uri) object);
//        } else if (object instanceof Byte[]) {
//            requestBuilder = Glide.with(context).load((byte[]) object);
        } else if (object instanceof Bitmap) {
            requestBuilder = Glide.with(context).load((Bitmap) object);
        }
        return requestBuilder;
    }
}
