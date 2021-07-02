package com.luhong.locwithlibrary.presenter;



/**
 * Presenter基类,可使用软引用
 * Created by ITMG on 2018/8/10.
 */
public abstract class BasePresenter<V extends BaseMvpView> {
    protected V baseMvpView;
//    protected SoftReference<V> baseMvpView;

    public BasePresenter() {
    }

    public void attachView(V baseMvpView) {
//        this.baseMvpView = new SoftReference<>(baseMvpView);
        this.baseMvpView = baseMvpView;
    }

    public void detachView() {
        if (baseMvpView != null) {
//            baseMvpView.clear();
            baseMvpView = null;
        }
    }

    //    protected V getMvpView() {
//        return isViewAttached() ? baseMvpView.get() : null;
//    }
//
    protected boolean isViewAttached() {
//        return baseMvpView != null && baseMvpView.get() != null;
        return baseMvpView != null;
    }

}
