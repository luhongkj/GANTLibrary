package com.luhong.locwithlibrary.model;

import android.app.Activity;
import android.text.TextUtils;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2018/8/15 0015.
 */

public class UploadModel implements IUploadModel {

    @Override
    public Observable<BaseResponse<UrlEntity>> uploadFile(Activity context, String filePath) {
        Map<String, String> fileParams = new HashMap<>();
        if (!TextUtils.isEmpty(filePath)) {
            fileParams.put("file", filePath);
            return ApiClient.getInstance().uploadForPath(null, fileParams);
        } else {
            return null;
        }
    }

    @Override
    public Observable<BaseResponse<UrlEntity>> uploadFile(Activity context, File file) {
        Map<String, File> fileParams = new HashMap<>();
        if (file != null) {
            fileParams.put("file", file);
            return ApiClient.getInstance().uploadForFile(null, fileParams);
        } else
            return null;
    }
}
