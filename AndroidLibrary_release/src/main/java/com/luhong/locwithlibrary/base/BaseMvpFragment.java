package com.luhong.locwithlibrary.base;

import android.os.Bundle;

import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.lang.reflect.ParameterizedType;


public abstract class BaseMvpFragment<P extends BasePresenter> extends BaseFragmentTow implements BaseMvpView {

    protected P mPresenter;
    protected abstract void fetchData();
    protected boolean isVisibleToUser;
    protected boolean isViewInitiated;
    protected boolean isDataInitiated;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        initFetchData();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
            Class<P> clazz = (Class<P>) parameterizedType.getActualTypeArguments()[0];
            mPresenter = clazz.newInstance();

            isViewInitiated = true;
        } catch (Exception e) {
            e.printStackTrace();
            isViewInitiated = false;
        }
        if (mPresenter != null) mPresenter.attachView(this);
        initFetchData();
    }

    private void initFetchData() {
        if (isVisibleToUser && isViewInitiated && !isDataInitiated) {
            fetchData();
            isDataInitiated = true;
        }
    }

    @Override
    public void onDestroy() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroy();
    }
}
