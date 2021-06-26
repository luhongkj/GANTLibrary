package com.luhong.locwithlibrary.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.CompressionPredicate;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;
import top.zibin.luban.OnRenameListener;

public class LuBanUtils {

    public static <T> void load(Context context, List<T> loadList, Consumer<List<File>> subscriber) {
        load(context, FileUtils.getRootCacheDir(), loadList, subscriber);
    }

    public static <T> void load(Context context, String targetDir, List<T> loadList, Consumer<List<File>> subscriber) {
        Observable.just(loadList)
                .observeOn(Schedulers.io())
                .map(new Function<List<T>, List<File>>() {
                    @Override
                    public List<File> apply(@NonNull List<T> list) throws Exception {
                        return Luban.with(context).setTargetDir(targetDir).load(list).get();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Logger.error("" + throwable.getMessage());
                    }
                })
                .subscribe(subscriber);
    }

    public static <T> void load(Context context, T object, OnCompressListener compressListener) {
        initLuBan(context, object).setCompressListener(compressListener).launch();
    }

    public static <T> void load(Context context, String targetDir, T object, OnCompressListener compressListener) {
        initLuBan(context, targetDir, object).setCompressListener(compressListener).launch();
    }

    public static <T> Luban.Builder initLuBan(Context context, T object) {
        return initLuBan(context, FileUtils.getRootCacheDir(), object);
    }

    public static <T> Luban.Builder initLuBan(Context context, String targetDir, T object) {
        return getLuBanBuilder(context, object)
                .ignoreBy(100)//100
                .setTargetDir(targetDir)
                .setFocusAlpha(false)
                .filter(new CompressionPredicate() {
                    @Override
                    public boolean apply(String path) {
                        return !(TextUtils.isEmpty(path) || path.toLowerCase().endsWith(".gif"));
                    }
                })
                .setRenameListener(new OnRenameListener() {
                    @Override
                    public String rename(String filePath) {
                        try {
                            MessageDigest md = MessageDigest.getInstance("MD5");
                            md.update(filePath.getBytes());
                            String newFileName = new BigInteger(1, md.digest()).toString(32);
                            String fileSuffix = filePath.substring(filePath.lastIndexOf("."));
                            return newFileName + fileSuffix;
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                        return "";
                    }
                });
    }

    private static <T> Luban.Builder getLuBanBuilder(Context context, T object) {//获取具体实例
        Luban.Builder load = null;
        if (object instanceof String) {
            load = Luban.with(context).load((String) object);
        } else if (object instanceof File) {
            load = Luban.with(context).load((File) object);
        } else if (object instanceof Uri) {
            load = Luban.with(context).load((Uri) object);
        } else {
            throw new IllegalArgumentException("Incoming data type exception, it must be String, File, Uri or Bitmap");
        }
        return load;
    }
}
