package com.luhong.locwithlibrary.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.DeviceSelectAdapter;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.app.BaseVariable;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 车辆选择
 * Created by ITMG on 2019-12-28.
 */
public class VehicleDialog extends BaseDialog implements DeviceSelectAdapter.IEventListener
{
    @BindView(R2.id.recyclerView_vehicleDialog)
    RecyclerView recyclerView;
    @BindView(R2.id.tv_cancel_vehicleDialog)
    TextView tv_cancel;
    private static Activity mActivity;
    private IEventListener photoListener;
    private DeviceSelectAdapter deviceSelectAdapter;

    public static VehicleDialog getInstance(Activity context)
    {
        mActivity = context;
        return new VehicleDialog(context);
    }

    public void showDialog(IEventListener eventListener)
    {
        this.photoListener = eventListener;
        show();
    }

    public VehicleDialog(Context context)
    {
        super(context);
    }

    @Override
    protected int initLayoutId()
    {
        return R.layout.dialog_vehicle;
    }

    @Override
    protected int setGravity()
    {
        return Gravity.BOTTOM;
    }

    @Override
    protected void initAlertDialogView(Window window)
    {
        // TODO Auto-generated method stub
        deviceSelectAdapter = new DeviceSelectAdapter(mActivity, new ArrayList<UserEntity.VehicleList>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(deviceSelectAdapter);

        List<UserEntity.VehicleList> vehicleList = SPUtils.getObject(mActivity,
                AppConstants.safeguardVehicleKey + BaseVariable.phone,
                new TypeToken<List<UserEntity.VehicleList>>(){}.getType());
        if (vehicleList != null && vehicleList.size() != 0)
        {
            deviceSelectAdapter.setNewData(vehicleList);
        }
        //        }
    }

    @Override
    protected void onEventListener()
    {
        // TODO Auto-generated method stub
        tv_cancel.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                // TODO Auto-generated method stub
                cancel();
            }
        });
    }

    @Override
    public void onCheck(UserEntity.VehicleList data, int position)
    {
        if (data == null) return;
        photoListener.onSelectCallback(data);
        cancel();
    }

    public interface IEventListener
    {
        void onSelectCallback(UserEntity.VehicleList data);
    }
}
