package com.luhong.locwithlibrary.presenter;

import android.content.Context;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.entity.SportLocationEntity;
import com.luhong.locwithlibrary.entity.SportLocationPageEntity;
import com.luhong.locwithlibrary.model.ISportModel;
import com.luhong.locwithlibrary.model.SportModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;

import java.util.List;

/**
 * 轨迹详情
 * Created by ITMG on 2019/8/16.
 */
public class TrackDetailPresenter {
    private ISportModel sportModel;

    public TrackDetailPresenter() {
    }


    public void getSportLocations(Context context, String sportId, TrackDetailPresenter.View presenter) {
        if (sportModel == null) sportModel = new SportModel();
        ApiClient.getInstance().doSubscribe(sportModel.getSportLocations(sportId), new BaseObserver<List<SportLocationEntity>>() {
            @Override
            protected void onSuccess(List<SportLocationEntity> resultEntity, String msg) {
                presenter.onTrackDetailSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                presenter.onFailure(0, errResult);
            }
        });
    }

    public void getSportLocationsNew(Context context, String sportId, TrackDetailPresenter.View view) {
        if (sportModel == null) sportModel = new SportModel();
        ApiClient.getInstance().doSubscribe(sportModel.getSportLocationsNew(sportId), new BaseObserver<SportLocationPageEntity<SportLocationEntity>>() {
            @Override
            protected void onSuccess(SportLocationPageEntity<SportLocationEntity> resultEntity, String msg) {
                view.onTrackDetailNewSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                view.onFailure(0, errResult);
            }
        });
    }

    public interface View {
        void onTrackDetailSuccess(List<SportLocationEntity> dataList);

        void onTrackDetailNewSuccess(SportLocationPageEntity<SportLocationEntity> dataList);

        void onFailure(int errCode, String errResult);
    }

    public abstract class Presenter {
        public abstract void getSportLocations(Context context, String sportId, TrackDetailPresenter.View view);

        public abstract void getSportLocationsNew(Context context, String sportId, TrackDetailPresenter.View view);
    }
}
