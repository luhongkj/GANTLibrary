package com.luhong.locwithlibrary.entity;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class VehicleViewModel extends ViewModel
{
    // 当前选中的车辆ID
    public MutableLiveData<DeviceEntity> device = new MutableLiveData<>();
}