package com.luhong.locwithlibrary.presenter.home;

import android.content.Context;
import android.text.TextUtils;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.contract.home.DeviceManageContract;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.PdfEntity;
import com.luhong.locwithlibrary.model.home.DeviceManageModel;
import com.luhong.locwithlibrary.model.home.IDeviceManageModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.utils.SPUtils;

import java.util.List;

/**
 * {"code":1,"msg":"请求成功","data":[{"id":"10000000000070","vin":"186883373783538","brandName":"捷安特","vehicleType":"1","vehicleTypeCn":"自行车","vehicleModel":"test","unitModelCn":"V3_3.0  TD1030","originalSn":"867967024285496","encryptSn":"922926C3489601AB4E7824CADAF413D0","sn":"967024285496","unitId":"24","nickName":"我的爱车","userId":"10000000000003","oweFee":5.0,"unitStatus":1,"freeInsuredAmount":3000.0,"oweFeeType":2}]}
 */
public class DeviceManagePresenter extends DeviceManageContract.Presenter {
    private IDeviceManageModel deviceManageModel;

    public DeviceManagePresenter() {
    }

    @Override
    public void getDeviceList(Context context) {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        ApiClient.getInstance().doSubscribe(deviceManageModel.getDeviceList(), new BaseObserver<List<DeviceEntity>>() {
            @Override
            protected void onSuccess(List<DeviceEntity> resultEntity, String msg) {
                SPUtils.putObject(context, AppConstants.DEVICE_MANAGE_KEY, resultEntity);
                if (isViewAttached()) baseMvpView.onDeviceListSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });

    }

    @Override
    public void updateDevice(DeviceEntity deviceEntity) {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        ApiClient.getInstance().doSubscribe(deviceManageModel.updateVehicle(deviceEntity), new BaseObserver<DeviceEntity>() {
            @Override
            protected void onSuccess(DeviceEntity resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onDeviceUpdateSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });
    }

    @Override
    public void deleteDevice(DeviceEntity deviceEntity) {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        ApiClient.getInstance().doSubscribe(deviceManageModel.deleteVehicle(deviceEntity), new BaseObserver<DeviceEntity>() {
            @Override
            protected void onSuccess(DeviceEntity resultEntity, String msg) {
                boolean isCurrentDevice = deviceEntity != null && !TextUtils.isEmpty(deviceEntity.getSn()) && !TextUtils.isEmpty(AppVariable.currentDeviceId) && deviceEntity.getSn().equals(AppVariable.currentDeviceId);
                if (isViewAttached())
                    baseMvpView.onDeviceDeleteSuccess(isCurrentDevice, deviceEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(2, errResult);
            }
        });
    }

    @Override
    public void getProductDescription(Context context, String sn) {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        ApiClient.getInstance().doSubscribe(deviceManageModel.productDescription(sn), new BaseObserver<PdfEntity>() {
            @Override
            protected void onSuccess(PdfEntity resultEntity, String msg) {
                if (isViewAttached()) {
                    baseMvpView.onProductDescriptionSuccess(resultEntity);
                    SPUtils.putObject(context, AppConstants.MAIN_SERVER_KEY, resultEntity);
                }
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });
    }
}
