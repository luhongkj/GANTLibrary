package com.luhong.locwithlibrary.contract;

import android.app.Activity;

import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.Map;

public interface SafeguardEditContract
{

    interface View extends BaseMvpView
    {

        void onUploadSuccess(int dataType, UrlEntity resultEntity);

        void onSafeguardMineSuccess(BasePageEntity<SafeguardEntity> pageList);

        void onSaveSafeguardSuccess(Object resultEntity);

        void onUpdateSafeguardSuccess(Object resultEntity);

        void onGetSafeguardInfoSuccess(SafeguardEntity resultEntity);
    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void uploadFile(Activity context, int dataType, String filePath/*List<PicEntity> pathList*/);

        public abstract void getSafeguardMine();

        public abstract void saveSafeguard(Map<String, Object> bodyParams);

        public abstract void updateSafeguard(Map<String, Object> bodyParams);

        public abstract void getSafeguardInfo(Map<String, Object> bodyParams);
    }

}
