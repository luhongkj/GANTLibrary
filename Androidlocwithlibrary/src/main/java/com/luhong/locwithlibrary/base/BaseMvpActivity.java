package com.luhong.locwithlibrary.base;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.lang.reflect.ParameterizedType;

/**
 * Created by ITMG on 2018/8/10.
 */

public abstract class BaseMvpActivity<P extends BasePresenter> extends BaseActivity implements BaseMvpView {
    protected P mPresenter;
    protected abstract void fetchData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
            Class<P> clazz = (Class<P>) parameterizedType.getActualTypeArguments()[0];
            mPresenter = clazz.newInstance();
            mPresenter.attachView(this);
            fetchData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) mPresenter.detachView();
        super.onDestroy();
    }

}
