package com.luhong.locwithlibrary.model;

import com.luhong.locwithlibrary.entity.SportEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.List;

public interface SportRecordContract
{

    interface View extends BaseMvpView
    {
        void onSportRecordSuccess(List<SportEntity.TrackList> dataList);

        void onUpdateSportSuccess(int position, String sportName);
    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void getSportRecord(String queryMonth, int pageSize, int pageNo);

        public abstract void updateSport(int position, String sportId, String sportName);
    }

}
