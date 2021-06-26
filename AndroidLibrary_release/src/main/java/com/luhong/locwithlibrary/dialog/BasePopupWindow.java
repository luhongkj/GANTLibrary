package com.luhong.locwithlibrary.dialog;


import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.utils.DensityUtils;


public abstract class BasePopupWindow extends PopupWindow {
    private Activity context;
    private View rootView, parentView;
    public static int CENTER_TYPE = 1, RIGHT_TYPE = 2;
    private boolean isAlpha;

    public BasePopupWindow(Activity context, View parentView, boolean isAlpha, int width, int height) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.parentView = parentView;
        this.isAlpha = isAlpha;
        LayoutInflater mLayoutInflater = LayoutInflater.from(context);
        rootView = mLayoutInflater.inflate(initLayoutId(), null);
        setContentView(rootView);
        if (isAlpha) setBackgroundAlpha(0.7f);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setAnimationStyle(setAnimStyle());
        setWidth(width);
        setHeight(height);
        showAsDropDown(parentView, 0, 0);

        initView();
        onEventListener();
    }

    protected int setAnimStyle() {
        return R.style.DialogTopAnimStyle;
    }

    public int setWidths() {
        return LayoutParams.MATCH_PARENT;
    }

    public int setHeight() {
        return DensityUtils.getScreenHeight(context) / 4;
    }

    public void setUpdateView(int xOff, int yOff, int width) {
        // TODO Auto-generated method stub
        if (xOff == CENTER_TYPE) {
            xOff = (DensityUtils.getScreenWidth(context) - width) / 2;
        } else if (xOff == RIGHT_TYPE) {
            xOff = DensityUtils.getScreenWidth(context) - width;
        }
        update(parentView, xOff, yOff, width, LayoutParams.WRAP_CONTENT);
    }

    public void setBackgroundAlpha(float alpha) {
        if (alpha < 0 || alpha > 1) return;
        Window window = context.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = alpha;
        window.setAttributes(lp);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (isAlpha) setBackgroundAlpha(1);
    }

    protected Activity getContext() {
        return context;
    }

    protected final <T extends View> T findView(int viewId) {
        return (T) rootView.findViewById(viewId);
    }

    public abstract int initLayoutId();

    public abstract void initView();

    public abstract void onEventListener();


}
