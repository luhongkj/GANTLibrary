package com.luhong.locwithlibrary.base;

import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luhong.locwithlibrary.dialog.LoadingDialog;
import com.luhong.locwithlibrary.utils.Ext;

public abstract class BaseFragment extends Fragment implements BaseCycle {
    private View rootView;
    private boolean hasInited = false;
    private BaseCycleViewModel _cycleViewModel = null;
    private SparseArray<View> viewStore = new SparseArray<>();

    private BaseCycleViewModel getCycleViewModel() {
        synchronized (BaseFragment.class) {
            if (_cycleViewModel == null) {
                synchronized (BaseFragment.class) {
                    _cycleViewModel = Ext.create(this, BaseCycleViewModel.class);
                }
            }
        }
        return _cycleViewModel;
    }

    public abstract int getLayoutId();

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        BaseCycleViewModel viewModel = getCycleViewModel();

        viewModel.cycleStatus.setValue(BaseCycleViewModel.Status.INIT_OBJ);
        super.onCreate(savedInstanceState);
        viewModel.cycleStatus.observe(this, new ObserverNoneNull<BaseCycleViewModel.Status>() {
            @Override
            public void changed(@NonNull BaseCycleViewModel.Status status) {
                switch (status) {
                    case INIT_OBJ:
                        initObj();
                        break;
                    case INIT_DATA:
                        initData(savedInstanceState);
                        break;
                    case INIT_VIEW:
                        initView();
                        break;
                    case INIT_EVENT:
                        initEvent();
                        break;
                    case LOAD_DATA:
                        loadData();
                        break;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayoutId(), container, false);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        BaseCycleViewModel viewModel = getCycleViewModel();
        super.onResume();
        if (!hasInited) {
            hasInited = true;
            viewModel.cycleStatus.setValue(BaseCycleViewModel.Status.INIT_DATA);
            viewModel.cycleStatus.setValue(BaseCycleViewModel.Status.INIT_VIEW);
            viewModel.cycleStatus.setValue(BaseCycleViewModel.Status.INIT_EVENT);
            viewModel.cycleStatus.setValue(BaseCycleViewModel.Status.LOAD_DATA);
        }
    }

    @Override
    public void initObj() {

    }

    @Nullable
    @Override
    public View getView() {
        return rootView;
    }

    // 初始化传递的数据
    @Override
    public abstract void initData(@Nullable Bundle savedInstanceState);

    // 初始化界面
    @Override
    public abstract void initView();

    // 初始化各种事件
    @Override
    public abstract void initEvent();

    // 开始加载数据
    @Override
    public abstract void loadData();

    @Override
    public void onDestroy() {
        BaseCycleViewModel viewModel = getCycleViewModel();
        super.onDestroy();
        viewModel.cycleStatus.removeObservers(this);
    }

    public <T extends View> T findViewById(@IdRes int id) {
        View view = viewStore.get(id);
        if (view == null) {
            view = getView().findViewById(id);
            viewStore.put(id, view);
        }
        return (T) view;
    }

    @Override
    public void onDestroyView() {
        viewStore.clear();
        super.onDestroyView();
    }

    protected void showLoading() {
        showLoading("", true);
    }

    protected void showLoading(String message) {
        showLoading(message, true);
    }

    protected LoadingDialog loadingDialog;

    protected void showLoading(String message, boolean isCancelable) {
        loadingDialog = new LoadingDialog.Builder(getActivity()).setMessage(message).setCancelable(isCancelable).create();
        loadingDialog.show();
    }

    protected void cancelLoading() {
        if (loadingDialog != null) loadingDialog.cancel();
    }
}
