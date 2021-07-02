package com.luhong.locwithlibrary.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;


import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.utils.DensityUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 对话框
 * Created by ITMG on 2019/9/17 0017.
 */
public abstract class BaseDialog extends AlertDialog {
    protected Unbinder mUnbinder;
    private Window mWindow;
   protected Context mContext;

    protected BaseDialog(Context context) {
        super(context);
        this.mContext = context;
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        mWindow = getWindow();
        setContentView(initLayoutId());
        mUnbinder = ButterKnife.bind(this);
        setGravityAndWidth(setGravity());
        initAlertDialogView(mWindow);
        onEventListener();
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }

    @Override
    public void show() {
        super.show();
        getWindow().setBackgroundDrawable(new ColorDrawable(0));
        updateGravityCenter();
    }

    /**
     * 设置位置和宽度
     */
    private void setGravityAndWidth(int gravity) {
        mWindow.setGravity(gravity);
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        mWindow.setBackgroundDrawableResource(R.color.transparent);
        if (gravity == Gravity.CENTER) {
            mWindow.setWindowAnimations(R.style.DialogScaleAnimStyle);
        } else {
            if (gravity == Gravity.TOP) {
                mWindow.setWindowAnimations(R.style.DialogTopAnimStyle);
            } else if (gravity == Gravity.BOTTOM) {
                mWindow.setWindowAnimations(R.style.DialogBottomAnimStyle);
            }
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        }
        mWindow.setAttributes(lp);
    }

    protected void updateGravityCenter() {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        if (lp.gravity != Gravity.CENTER) return;
        WindowManager m = mWindow.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高
        // 设置宽度
        lp.width = (int) (DensityUtils.getScreenWidth(getContext()) * 0.95);
        lp.gravity = Gravity.CENTER;//设置位置
        //p.alpha = 0.8f;//设置透明度
        mWindow.setAttributes(lp);
    }

    /**
     * 加载布局资源
     *
     * @return
     */
    protected abstract int initLayoutId();

    /**
     * 设置dialog位置 Gravity.CENTER/Gravity.BOTTOM
     *
     * @return
     */
    protected abstract int setGravity();

    /**
     * 初始化视图
     *
     * @param window
     */
    protected abstract void initAlertDialogView(Window window);

    /**
     * 事件监听
     */
    protected abstract void onEventListener();

    public interface IEventListener {
        void onConfirm();

        void onCancel();
    }
}
