package com.luhong.locwithlibrary.model;

import android.content.Context;

import com.luhong.locwithlibrary.entity.SportEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;


public interface SportContract
{

    interface View extends BaseMvpView
    {
        void onGetSportListSuccess(SportEntity dataList);

        void onUpdateSportSuccess(Object resultEntity);
    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void getSportList(Context context);

        public abstract void updateSport(String sportId, String sportName);
    }
}
