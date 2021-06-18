package com.luhong.locwithlibrary.contract.home;

import android.content.Context;

import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.PdfEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.List;

public interface DeviceManageContract {

    interface View extends BaseMvpView {
        void onDeviceListSuccess(List<DeviceEntity> dataList);

        void onDeviceUpdateSuccess(DeviceEntity deviceEntity);

        void onDeviceDeleteSuccess(boolean isCurrentDevice, DeviceEntity deviceEntity);

        void onProductDescriptionSuccess(PdfEntity resultEntity);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void getDeviceList(Context context);

        public abstract void updateDevice(DeviceEntity deviceEntity);

        public abstract void deleteDevice(DeviceEntity deviceEntity);

        public abstract void getProductDescription(Context context, String sn);
    }
}
