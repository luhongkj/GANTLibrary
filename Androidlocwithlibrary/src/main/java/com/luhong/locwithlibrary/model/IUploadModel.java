package com.luhong.locwithlibrary.model;

import android.app.Activity;

import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.io.File;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public interface IUploadModel {
    Observable<BaseResponse<UrlEntity>> uploadFile(Activity context, String filePath);

    Observable<BaseResponse<UrlEntity>> uploadFile(Activity context, File file);
}
