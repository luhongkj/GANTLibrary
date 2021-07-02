package com.luhong.locwithlibrary.presenter;

import android.text.TextUtils;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.entity.SportEntity;
import com.luhong.locwithlibrary.model.ISportModel;
import com.luhong.locwithlibrary.model.SportModel;
import com.luhong.locwithlibrary.model.SportRecordContract;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.utils.ToastUtil;

/**
 * Created by ITMG on 2019/8/16.
 */
public class SportRecordPresenter extends SportRecordContract.Presenter {
    private ISportModel sportModel;

    public SportRecordPresenter() {
    }

    @Override
    public void getSportRecord(String queryMonth, int pageSize, int pageNo) {
        if (sportModel == null) sportModel = new SportModel();
        ApiClient.getInstance().doSubscribe(sportModel.getSportList(queryMonth, pageSize, pageNo), new BaseObserver<SportEntity>() {
            @Override
            protected void onSuccess(SportEntity resultEntity, String msg) {
                if (!TextUtils.isEmpty(msg)) ToastUtil.show(msg);
                if (isViewAttached()) {
                    if (resultEntity == null || resultEntity.getTrackList() == null) {
                        baseMvpView.onSportRecordSuccess(null);
                    } else
                        baseMvpView.onSportRecordSuccess(resultEntity.getTrackList());
                }
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });
    }

    @Override
    public void updateSport(int position, String sportId, String sportName) {
        if (sportModel == null) sportModel = new SportModel();
        ApiClient.getInstance().doSubscribe(sportModel.updateSport(sportId, sportName), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                ToastUtil.show("修改成功");
                if (isViewAttached()) baseMvpView.onUpdateSportSuccess(position, sportName);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });
    }
}
