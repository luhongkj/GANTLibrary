package com.luhong.locwithlibrary.contract;

import android.app.Activity;

import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

public interface UploadContract {
    interface View extends BaseMvpView {
        void onUploadSuccess(UrlEntity resultEntity, int position);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void uploadFile(Activity context, int position, String filePath/*List<PicEntity> pathList*/);
    }
}
