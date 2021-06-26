package com.luhong.locwithlibrary.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.Size;
import androidx.appcompat.app.AppCompatActivity;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.dialog.LoadingDialog;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.EventBusUtils;
import com.luhong.locwithlibrary.utils.PermissionsUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;
import com.zyq.easypermission.EasyPermissionResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by ITMG on 2019/3/3.
 */
public abstract class BaseActivity extends AppCompatActivity {
    public String TAG = BaseActivity.class.getSimpleName();
    protected BaseActivity mActivity;
    private Handler handler;
    private static final List<BaseActivity> mActivityList = new LinkedList<>();
    protected Unbinder mUnbinder;
    protected LoadingDialog loadingDialog;
    protected LinearLayout ll_titleBar;
    protected TextView tv_title_left;
    protected TextView tv_title_center;
    protected TextView tv_title_right;
    protected ImageView iv_icon_title;

    @LayoutRes
    protected abstract int initLayoutId();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initData();

    protected abstract void onEventListener();

    protected boolean isLightStatusBar() {
        return false;
    }

    protected boolean isRequestPermissions() {
        return false;
    }

    protected boolean isRequestedOrientation() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isTaskRoot()) {
            final Intent intent = getIntent();
            final String intentAction = intent.getAction();
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && intentAction != null && intentAction.equals(Intent.ACTION_MAIN)) {
                finish();
                return;
            }
        }
        initLayoutId();
        super.onCreate(savedInstanceState);
        if (isRequestedOrientation()) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setContentView(initLayoutId());
        setTranslucentStatus(true);
        mActivityList.add(this);
        mActivity = this;
        TAG = this.getLocalClassName();
        EventBusUtils.register(this);
        mUnbinder = ButterKnife.bind(this);
        if (isRequestPermissions()) {
            requestAllPermissions();
        }

        initView(savedInstanceState);
        initData();
        onEventListener();
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setTranslucentStatus(boolean isLight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            updateStatusColor(isLight);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);//状态栏为透明

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    protected void updateStatusColor(boolean isLight) {
        View decorView = getWindow().getDecorView();
        if (isLight) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    public void initTitleView(boolean isShowLeft, String centerText) {
        initTitleView(isShowLeft, centerText, -1, null);
    }

    public void initTitleView(boolean isShowLeft, String centerText, @DrawableRes int srcId, View.OnClickListener onClickListener) {
        // TODO Auto-generated method stub
        ll_titleBar = findViewById(R.id.ll_titleBar_title);
        tv_title_left = findViewById(R.id.tv_left_title);
        tv_title_center = findViewById(R.id.tv_center_title);

        if (isShowLeft) {
            tv_title_left.setVisibility(View.VISIBLE);
            tv_title_left.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    finish();
                }
            });
        } else
            tv_title_left.setVisibility(View.GONE);

        if (TextUtils.isEmpty(centerText))
            tv_title_center.setVisibility(View.GONE);
        else {
            tv_title_center.setText(centerText);
            tv_title_center.setVisibility(View.VISIBLE);
        }

        if (srcId != -1 && onClickListener != null) {
            iv_icon_title = findViewById(R.id.iv_icon_title);
            iv_icon_title.setImageResource(srcId);
            iv_icon_title.setVisibility(View.VISIBLE);
            iv_icon_title.setOnClickListener(onClickListener);
        }
    }

    public void initTitleView(boolean isShowLeft, String centerText, String rightText, View.OnClickListener onClickListener) {
        // TODO Auto-generated method stub
        ll_titleBar = findViewById(R.id.ll_titleBar_title);
        tv_title_left = findViewById(R.id.tv_left_title);
        tv_title_center = findViewById(R.id.tv_center_title);

        if (isShowLeft) {
            tv_title_left.setVisibility(View.VISIBLE);
            tv_title_left.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View view) {
                    finish();
                }
            });
        } else
            tv_title_left.setVisibility(View.GONE);

        if (TextUtils.isEmpty(centerText))
            tv_title_center.setVisibility(View.GONE);
        else {
            tv_title_center.setText(centerText);
            tv_title_center.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(rightText) && onClickListener != null) {
            tv_title_right = findViewById(R.id.tv_right_title);
            tv_title_right.setText(rightText);
            tv_title_right.setVisibility(View.VISIBLE);
            tv_title_right.setOnClickListener(onClickListener);
        }
    }

    public void updateTitle(String titleText) {
        if (tv_title_center != null) tv_title_center.setText(titleText);
    }

    public void updateTitle(int titleTextId) {
        if (tv_title_center != null) tv_title_center.setText(titleTextId);
    }

    public void updateTitleRight(int titleRightTextId) {
        if (tv_title_right != null) tv_title_right.setText(titleRightTextId);
    }

    public void updateTitleRight(String titleRightText) {
        if (tv_title_right != null) tv_title_right.setText(titleRightText);
    }

    public void updateTitleBarColor(@ColorInt int titleBarColor) {
        if (ll_titleBar != null && titleBarColor != 0)
            ll_titleBar.setBackgroundColor(titleBarColor);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribeEvent(String eventMessage) {

    }

    protected void showLoading(String message) {
        showLoading(message, true);
    }

    protected void showLoading() {
        showLoading("", true);
    }

    protected void showLoading(String message, boolean isCancelable) {
        if (mActivity.isDestroyed()) {
            return;
        }
        loadingDialog = new LoadingDialog.Builder(mActivity).setMessage(message).setCancelable(isCancelable).create();
        loadingDialog.show();
    }

    protected void cancelLoading() {
        if (loadingDialog != null) loadingDialog.cancel();
    }

    protected void showToast(int strId) {
        ToastUtil.show(strId);
    }

    protected void showToast(String strText) {
        ToastUtil.show(strText);
    }

    public void openKeyboard(EditText editText) {
        if (editText == null || editText.getContext() == null) return;
        if (handler == null) handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        editText.setFocusable(true);
                        editText.setFocusableInTouchMode(true);
                        editText.requestFocus();
                        InputMethodManager inputManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputManager.showSoftInput(editText, 0);
                    }
                });
            }
        }, 200);
    }

    public void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public static void closeKeyboard(EditText edit) {
        if (edit != null && edit.getContext() != null) {
            InputMethodManager imm = (InputMethodManager) edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }


    public static void finishAll() {
        List<BaseActivity> activityList;
        synchronized (BaseActivity.mActivityList) {
            activityList = new ArrayList<>(BaseActivity.mActivityList);
        }
        for (BaseActivity activity : activityList) {
            activity.finish();
        }
    }

    public static void finishAll(BaseActivity except) {
        List<BaseActivity> activityList;
        synchronized (BaseActivity.mActivityList) {
            activityList = new ArrayList<>(BaseActivity.mActivityList);
        }
        for (BaseActivity activity : activityList) {
            if (activity != except)
                activity.finish();
        }
    }

    public static void finishSpecifyActivity(Class<? extends BaseActivity> specifyActivity) {
        List<BaseActivity> activityList;
        synchronized (BaseActivity.mActivityList) {
            activityList = new ArrayList<>(BaseActivity.mActivityList);
        }
        for (BaseActivity activity : activityList) {
            if (activity.getClass().getSimpleName().equals(specifyActivity.getSimpleName())) {
                activity.finish();
                break;
            }
        }
    }

    protected void backHome(Activity activity) {
        moveTaskToBack(true);
    }

    public static void exitApp() {
        finishAll();
        System.gc();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
        }
        if (mUnbinder != null) mUnbinder.unbind();
        mActivityList.remove(this);
        super.onDestroy();
        EventBusUtils.unregister(this);
    }

    @Override
    public void finish() {
        closeKeyboard();
        super.finish();
    }


    @RequiresApi(api = 30)
    public boolean hasPermissions(@Size(min = 1) @NonNull String... permissions) {
        return PermissionsUtils.hasPermissions(this, permissions);
    }

    @RequiresApi(api = 30)
    public boolean shouldShowRequestPermission(String permissions) {
        return PermissionsUtils.shouldShowRequestPermission(this, permissions);
    }

    @RequiresApi(api = 30)
    public void requestAllPermissions(EasyPermissionResult permissionResult) {
        PermissionsUtils.requestAllPermissions(this, permissionResult);
    }

    @RequiresApi(api = 30)
    public void requestAllPermissions() {
        PermissionsUtils.requestNeedAllPermissions(this, null);
    }

    @RequiresApi(api = 30)
    public void requestNeedPermissions(@Size(min = 1) @NonNull String[] permissions) {
        PermissionsUtils.requestNeedPermissions(this, permissions);
    }

    @RequiresApi(api = 30)
    public void requestNeedPermissions(@Size(min = 1) @NonNull String[] permissions, EasyPermissionResult permissionResult) {
        PermissionsUtils.requestNeedPermissions(this, permissions, permissionResult);
    }

    @RequiresApi(api = 30)
    public void requestNeedPermissions(int requestCode, @Size(min = 1) @NonNull String[] permissions, EasyPermissionResult permissionResult) {
        PermissionsUtils.requestNeedPermissions(this, requestCode, permissions, permissionResult);
    }

    @RequiresApi(api = 30)
    public void requestStoragePermissions(EasyPermissionResult permissionResult) {
        PermissionsUtils.requestStoragePermissions(this, permissionResult);
    }

    @RequiresApi(api = 30)
    public void requestStorageAndCameraPermissions(EasyPermissionResult permissionResult) {
        PermissionsUtils.requestStorageAndCameraPermissions(this, permissionResult);
    }

    @RequiresApi(api = 30)
    public void requestCameraPermissions(EasyPermissionResult permissionResult) {
        PermissionsUtils.requestCameraPermissions(this, permissionResult);
    }

    @RequiresApi(api = 30)
    public void requestAudioPermissions(EasyPermissionResult permissionResult) {
        PermissionsUtils.requestAudioPermissions(this, permissionResult);
    }

    @RequiresApi(api = 30)
    public void requestCallPermissions(EasyPermissionResult permissionResult) {
        PermissionsUtils.requestCallPermissions(this, permissionResult);
    }

    @RequiresApi(api = 30)
    public void requestLocationPermissions(EasyPermissionResult permissionResult) {
        PermissionsUtils.requestLocationPermissions(this, permissionResult);
    }

    @RequiresApi(api = 30)
    public void requestContactsPermissions(EasyPermissionResult permissionResult) {
        PermissionsUtils.requestContactsPermissions(this, permissionResult);
    }

    @RequiresApi(api = 30)
    public void requestPhoneStatePermissions(EasyPermissionResult permissionResult) {
        PermissionsUtils.requestPhoneStatePermissions(this, permissionResult);
    }

    @RequiresApi(api = 30)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    public void showDialog(String message, DialogInterface.OnClickListener onClickListener) {
        new AlertDialog.Builder(mActivity).setMessage(message).setCancelable(false).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).setPositiveButton("确定", onClickListener).show();
    }

    public void startIntentActivity(Class<?> cla) {
        startActivity(cla, -1, null);
    }

    public void startIntentActivity(Class<?> cla, String Key, Object value) {
        startActivity(cla, -1, Key, value);
    }

    public void startIntentActivity(Class<?> cla, Bundle bundle) {
        startActivity(cla, -1, bundle);
    }

    public void startIntentActivityForResult(Class<?> cla, int requestCode) {
        startActivity(cla, requestCode, null);
    }

    public void startIntentActivityForResult(Class<?> cla, int requestCode, Bundle bundle) {
        startActivity(cla, requestCode, bundle);
    }

    public void startIntentActivityForResult(Class<?> cla, int requestCode, String key, Object value) {
        startActivity(cla, requestCode, key, value);
    }

    public void startActivity(Class<?> cla, int requestCode, Bundle bundle) {
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

    public void startActivity(Class<?> cla, int requestCode, String key, Object value) {
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
