package com.luhong.locwithlibrary.contract;

import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.List;

public interface SafeguardContract {

    interface View extends BaseMvpView {
        void onSafeguardHomeSuccess(List<SafeguardEntity> dataList);

        void onSafeguardMineSuccess(BasePageEntity<SafeguardEntity> pageList);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void getSafeguardHome();

        public abstract void getSafeguardMine();
    }
}
