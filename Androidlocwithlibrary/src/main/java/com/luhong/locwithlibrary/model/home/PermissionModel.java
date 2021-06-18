package com.luhong.locwithlibrary.model.home;

import androidx.annotation.NonNull;

import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.dialog.BaseDialog;
import com.luhong.locwithlibrary.dialog.PromptDialog;
import com.luhong.locwithlibrary.listener.IRequestListener;
import com.luhong.locwithlibrary.model.IPermissionModel;
import com.luhong.locwithlibrary.utils.BaseUtils;
import com.zyq.easypermission.EasyPermissionResult;

import java.util.List;

/**
 * Created by ITMG on 2019-12-04.
 */
public class PermissionModel implements IPermissionModel {

    @Override
    public void checkLocationInit(final BaseActivity mActivity, final IRequestListener<Object> requestListener) {
        mActivity.requestLocationPermissions(new EasyPermissionResult() {
            @Override
            public void onPermissionsAccess(int requestCode) {
                super.onPermissionsAccess(requestCode);
                if (!BaseUtils.isGpsEnabled(mActivity)) {
                    PromptDialog.getInstance(mActivity).showDialog("请打开定位服务", new BaseDialog.IEventListener() {
                        @Override
                        public void onConfirm() {
                            requestListener.onFailure(BaseConstants.TYPE_NOT_OPEN_GPS, "请打开定位服务");
                        }

                        @Override
                        public void onCancel() {
                            //                        requestListener.onFailure(CodeTableActivity.TYPE_NOT_OPEN_GPS, "请打开位置服务");
                        }
                    });
                } else {
                    requestListener.onSuccess(1);
                }
            }

            @Override
            public void onPermissionsDismiss(int requestCode, @NonNull List<String> permissions) {
                super.onPermissionsDismiss(requestCode, permissions);
                PromptDialog.getInstance(mActivity).showDialog("请打开定位权限", new BaseDialog.IEventListener() {
                    @Override
                    public void onConfirm() {
                        requestListener.onFailure(BaseConstants.TYPE_NOT_FINE_LOCATION, "请打开定位权限");
                    }

                    @Override
                    public void onCancel() {
                        //                            requestListener.onFailure(CodeTableActivity.TYPE_NOT_FINE_LOCATION, "请打开定位权限");
                    }
                });
            }
        });
    }


}
