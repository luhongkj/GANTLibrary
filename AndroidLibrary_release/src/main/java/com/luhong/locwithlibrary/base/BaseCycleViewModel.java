package com.luhong.locwithlibrary.base;



import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseCycleViewModel extends ViewModel
{
    public MutableLiveData<Status> cycleStatus = new MutableLiveData<>();

    public static enum Status
    {
        INIT_OBJ,
        INIT_DATA,
        INIT_VIEW,
        INIT_EVENT,
        LOAD_DATA,
    }
}
