package com.luhong.locwithlibrary.base;

import android.annotation.TargetApi;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.appcompat.app.AppCompatActivity;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.PermissionsUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;
import com.zyq.easypermission.EasyPermissionResult;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * 继承了Activity，实现Android6.0的运行时权限检测
 * 需要进行运行时权限检测的Activity可以继承这个类
 * @文件名称：PermissionsChecker.java
 * @类型名称：PermissionsChecker
 * @since 2.5.0
 */
public class CheckPermissionsActivity extends AppCompatActivity {

	protected LinearLayout ll_titleBar;
	protected TextView tv_title_left;
	protected TextView tv_title_center;
	protected TextView tv_title_right;

	protected void showToast(String strText) {
		ToastUtil.show(strText);
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
	private static final int PERMISSON_REQUESTCODE = 0;
	

	@Override
	protected void onResume() {
		super.onResume();
	}
	@TargetApi(Build.VERSION_CODES.KITKAT)
	protected void setTranslucentStatus(boolean isLight) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			updateStatusColor(isLight);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			getWindow().setStatusBarColor(Color.TRANSPARENT);
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

	/**
	 * 获取权限集中需要申请权限的列表
	 * @param permissions
	 * @return
	 * @since 2.5.0
	 *
	 */
	private List<String> findDeniedPermissions(String[] permissions) {
		List<String> needRequestPermissonList = new ArrayList<String>();
		if (Build.VERSION.SDK_INT >= 23
				&& getApplicationInfo().targetSdkVersion >= 23){
			try {
				for (String perm : permissions) {
					Method checkSelfMethod = getClass().getMethod("checkSelfPermission", String.class);
					Method shouldShowRequestPermissionRationaleMethod = getClass().getMethod("shouldShowRequestPermissionRationale",
							String.class);
					if ((Integer)checkSelfMethod.invoke(this, perm) != PackageManager.PERMISSION_GRANTED
							|| (Boolean)shouldShowRequestPermissionRationaleMethod.invoke(this, perm)) {
						needRequestPermissonList.add(perm);
					}
				}
			} catch (Throwable e) {

			}
		}
		return needRequestPermissonList;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean hasPermissions(@Size(min = 1) @NonNull String... permissions) {
		return PermissionsUtils.hasPermissions(this, permissions);
	}

	public boolean shouldShowRequestPermission(String permissions) {
		return PermissionsUtils.shouldShowRequestPermission(this, permissions);
	}

	public void requestAllPermissions(EasyPermissionResult permissionResult) {
		PermissionsUtils.requestAllPermissions(this, permissionResult);
	}

	public void requestAllPermissions() {
		PermissionsUtils.requestNeedAllPermissions(this, null);
	}

	public void requestNeedPermissions(@Size(min = 1) @NonNull String... permissions) {
		PermissionsUtils.requestNeedPermissions(this, permissions);
	}

	public void requestNeedPermissions(@Size(min = 1) @NonNull String[] permissions, EasyPermissionResult permissionResult) {
		PermissionsUtils.requestNeedPermissions(this, permissions, permissionResult);
	}

	public void requestNeedPermissions(int requestCode, @Size(min = 1) @NonNull String[] permissions, EasyPermissionResult permissionResult) {
		PermissionsUtils.requestNeedPermissions(this, requestCode, permissions, permissionResult);
	}

	public void requestStoragePermissions(EasyPermissionResult permissionResult) {
		PermissionsUtils.requestStoragePermissions(this, permissionResult);
	}
	public void requestRecordPermissions(EasyPermissionResult permissionResult) {
		PermissionsUtils.requestRecordPermissions(this, permissionResult);
	}

	public void requestStorageAndCameraPermissions(EasyPermissionResult permissionResult) {
		PermissionsUtils.requestStorageAndCameraPermissions(this, permissionResult);
	}

	public void requestCameraPermissions(EasyPermissionResult permissionResult) {
		PermissionsUtils.requestCameraPermissions(this, permissionResult);
	}

	public void requestAudioPermissions(EasyPermissionResult permissionResult) {
		PermissionsUtils.requestAudioPermissions(this, permissionResult);
	}

	public void requestCallPermissions(EasyPermissionResult permissionResult) {
		PermissionsUtils.requestCallPermissions(this, permissionResult);
	}

	public void requestLocationPermissions(EasyPermissionResult permissionResult) {
		PermissionsUtils.requestLocationPermissions(this, permissionResult);
	}

	public void requestContactsPermissions(EasyPermissionResult permissionResult) {
		PermissionsUtils.requestContactsPermissions(this, permissionResult);
	}

	public void requestPhoneStatePermissions(EasyPermissionResult permissionResult) {
		PermissionsUtils.requestPhoneStatePermissions(this, permissionResult);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		PermissionsUtils.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
	}
}
