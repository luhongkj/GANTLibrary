package com.luhong.locwithlibrary.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.Size;

import com.zyq.easypermission.EasyPermission;
import com.zyq.easypermission.EasyPermissionHelper;
import com.zyq.easypermission.EasyPermissionResult;

import java.security.acl.Permission;

import static android.Manifest.permission.MANAGE_EXTERNAL_STORAGE;

/**
 * Created by ITMG on 2019-12-16.
 */
public class PermissionsUtils {
    @SuppressLint("InlinedApi")
    protected static String[] needPermissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.CAMERA,
    };


    private PermissionsUtils() {
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean hasPermissions(Activity mActivity, @Size(min = 1) @NonNull String... permissions) {
        return EasyPermission.build().hasPermission(mActivity, permissions);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean shouldShowRequestPermission(Activity mActivity, String permissions) {
        return EasyPermission.build().shouldShowRequestPermissionRationale(mActivity, permissions);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestNeedPermissions(Activity mActivity, @Size(min = 1) @NonNull String[] permissions) {
        EasyPermission.build().requestPermission(mActivity, permissions);
    }


    @TargetApi(Build.VERSION_CODES.M)
    public static void requestNeedPermissions(Activity mActivity, @Size(min = 1) @NonNull String[] permissions, EasyPermissionResult permissionResult) {
        EasyPermission.build().mContext(mActivity).mPerms(permissions).mResult(permissionResult).requestPermission();
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void requestNeedPermissions(Activity mActivity, int requestCode, @Size(min = 1) @NonNull String[] permissions, EasyPermissionResult permissionResult) {
        EasyPermission.build().mRequestCode(requestCode).mContext(mActivity).mPerms(permissions).mResult(permissionResult).requestPermission();
    }

    @RequiresApi(api = 30)
    public static void requestStoragePermissions(@NonNull Activity mActivity, EasyPermissionResult permissionResult) {
        requestNeedPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, permissionResult);
    }
    @RequiresApi(api = 30)
    public static void requestManage_external_storagepermissions(@NonNull Activity mActivity, EasyPermissionResult permissionResult) {
        requestNeedPermissions(mActivity, new String[]{Manifest.permission.MANAGE_EXTERNAL_STORAGE}, permissionResult);
    }


    public static void requestRecordPermissions(@NonNull Activity mActivity, EasyPermissionResult permissionResult) {
        requestNeedPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO}, permissionResult);
    }


    public static void requestStorageAndCameraPermissions(@NonNull Activity mActivity, EasyPermissionResult permissionResult) {
        requestNeedPermissions(mActivity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, permissionResult);
    }

    public static void requestCameraPermissions(@NonNull Activity mActivity, EasyPermissionResult permissionResult) {
        requestNeedPermissions(mActivity, new String[]{Manifest.permission.CAMERA}, permissionResult);
    }

    public static void requestAudioPermissions(@NonNull Activity mActivity, EasyPermissionResult permissionResult) {
        requestNeedPermissions(mActivity, new String[]{Manifest.permission.RECORD_AUDIO}, permissionResult);
    }

    public static void requestCallPermissions(@NonNull Activity mActivity, EasyPermissionResult permissionResult) {
        requestNeedPermissions(mActivity, new String[]{Manifest.permission.CALL_PHONE}, permissionResult);
    }

    public static void requestLocationPermissions(@NonNull Activity mActivity, EasyPermissionResult permissionResult) {
        requestNeedPermissions(mActivity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, permissionResult);
    }

    public static void requestContactsPermissions(@NonNull Activity mActivity, EasyPermissionResult permissionResult) {
        requestNeedPermissions(mActivity, new String[]{Manifest.permission.READ_CONTACTS}, permissionResult);
    }

    public static void requestPhoneStatePermissions(@NonNull Activity mActivity, EasyPermissionResult permissionResult) {
        requestNeedPermissions(mActivity, new String[]{Manifest.permission.READ_PHONE_STATE}, permissionResult);
    }

    @RequiresApi(api = 30)
    @TargetApi(Build.VERSION_CODES.M)
    public static void requestAllPermissions(Activity mActivity, EasyPermissionResult permissionResult) {
        if (mActivity == null) return;
        PackageInfo packageInfo = null;
        try {
            packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            Logger.error("A problem occurred when retrieving permissions: " + e);
        }
        if (packageInfo != null) {
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions != null) {
                if (permissionResult != null) {
                    requestNeedPermissions(mActivity, permissions, permissionResult);
                } else {
                    requestNeedPermissions(mActivity, permissions);
                }
            }
        }
    }

    @RequiresApi(api = 30)
    public static void requestNeedAllPermissions(@NonNull Activity mActivity, EasyPermissionResult permissionResult) {
        if (permissionResult != null) {
            requestNeedPermissions(mActivity, needPermissions, permissionResult);
        } else {
            requestNeedPermissions(mActivity, needPermissions);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void openAppDetails(Activity context, String... permissionShow) {
        EasyPermission.build().openAppDetails(context, permissionShow);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void openAppDetailsForEn(Activity context, String... permissionShow) {
        EasyPermission.build().openAppDetailsForEn(context, permissionShow);
    }

    public static void onRequestPermissionsResult(@NonNull Activity context, int requestCode, @NonNull String[] permissions, int[] grantResults) {
        EasyPermissionHelper.getInstance().onRequestPermissionsResult(requestCode, permissions, grantResults, context);
    }
}
