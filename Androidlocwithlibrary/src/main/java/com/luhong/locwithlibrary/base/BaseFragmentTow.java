package com.luhong.locwithlibrary.base;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luhong.locwithlibrary.dialog.LoadingDialog;
import com.luhong.locwithlibrary.utils.EventBusUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by ITMG on 2019-3-10
 */
public abstract class BaseFragmentTow extends Fragment {
    protected String TAG = BaseFragment.class.getSimpleName();
    protected BaseActivity mActivity;
    protected Unbinder unbinder;
    private View baseView;

    protected abstract int initLayoutId();//设置布局资源

    protected abstract void initData();//初始化数据

    protected abstract void initView(Bundle savedInstanceState);//初始化视图

    protected abstract void onEventListener();//事件监听

    protected LoadingDialog loadingDialog;
    protected void showLoading() {
        showLoading("", true);
    }
    protected void showLoading(String message) {
        showLoading(message, true);
    }
    protected void showLoading(String message, boolean isCancelable) {
        loadingDialog = new LoadingDialog.Builder(mActivity).setMessage(message).setCancelable(isCancelable).create();
        loadingDialog.show();
    }

    protected void cancelLoading() {
        if (loadingDialog != null) loadingDialog.cancel();
    }
    @Override
    public void onAttach(Context context) {
        // TODO Auto-generated method stub
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = getClass().getSimpleName();
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        initLayoutId();
        baseView = inflater.inflate(initLayoutId(), container, false);
        EventBusUtils.register(this);
        unbinder = ButterKnife.bind(this, baseView);
        initView(savedInstanceState);// 控件初始化
        onEventListener();
        return baseView;
    }

    public void showToast(String msg) {
        // TODO Auto-generated method stub
        ToastUtil.show(msg);
    }

    public void showToast(int msgId) {
        // TODO Auto-generated method stub
        ToastUtil.show(msgId);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int viewId) {
        return (T) baseView.findViewById(viewId);
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (unbinder != null) unbinder.unbind();
        EventBusUtils.unregister(this);
        super.onDestroy();
        if (baseView != null) {
            ViewGroup parent = (ViewGroup) baseView.getParent();
            if (parent != null) {
                parent.removeView(baseView);
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribeEvent(String eventMessage) {

    }

    public void startIntentActivity(Class<?> cla) {
        startActivity(cla, -1, null, null);
    }

    public void startIntentActivity(Class<?> cla, String key, Object value) {
        startActivity(cla, -1, key, value);
    }

    public void startIntentActivity(Class<?> cla, Bundle bundle) {
        startActivity(cla, -1, bundle);
    }

    public void startIntentActivityForResult(Class<?> cla, int requestCode) {
        startActivity(cla, requestCode, null, null);
    }

    public void startIntentActivityForResult(Class<?> cla, int requestCode, Bundle bundle) {
        startActivity(cla, requestCode, bundle);
    }

    public void startIntentActivityForResult(Class<?> cla, int requestCode, String key, Object value) {
        startActivity(cla, requestCode, key, value);
    }

    public void startActivity(Class<?> cla, int requestCode, Bundle bundle) {//多个值
        Intent intent = new Intent(mActivity, cla);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        if (requestCode == -1)
            startActivity(intent);
        else {
            startActivityForResult(intent, requestCode);
        }
    }

    public void startActivity(Class<?> cla, int requestCode, String key, Object value) {//单个值
        Intent intent = new Intent(mActivity, cla);
        if (value instanceof Integer) {
            intent.putExtra(key, (Integer) value);
        } else if (value instanceof String) {
            intent.putExtra(key, (String) value);
        } else if (value instanceof Double) {
            intent.putExtra(key, (Double) value);
        } else if (value instanceof Boolean) {
            intent.putExtra(key, (Boolean) value);
        } else {
            intent.putExtra(key, (Serializable) value);
        }
        if (requestCode == -1)
            startActivity(intent);
        else {
            startActivityForResult(intent, requestCode);
        }
    }
}
