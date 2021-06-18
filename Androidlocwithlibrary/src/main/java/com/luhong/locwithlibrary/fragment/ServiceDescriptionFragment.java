package com.luhong.locwithlibrary.fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.base.BaseFragment;
import com.luhong.locwithlibrary.base.BaseFragmentTow;

import butterknife.BindView;


public class ServiceDescriptionFragment extends BaseFragmentTow {
    private final String TAG = ServiceDescriptionFragment.class.getSimpleName();
    protected static final String dataTypeKey = "dataTypeKey";
    private int dataType;
    @BindView(R2.id.tv_photo_phoneAlarm)
    ImageView iv_photo;
    @BindView(R2.id.tv_description_phoneAlarm)
    TextView tv_description;


    public ServiceDescriptionFragment() {
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_service_description;
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            dataType = getArguments().getInt(dataTypeKey);
        }
    }


    @Override
    protected void initView(Bundle savedInstanceState) {
        if (dataType == 0) {
            iv_photo.setImageResource(R.mipmap.alarm_description);
            tv_description.setText(getText(R.string.alarm_description));
        } else if (dataType == 1) {
            iv_photo.setImageResource(R.mipmap.server_description);
            tv_description.setText(getText(R.string.efence_description));
        }
    }

    @Override
    protected void onEventListener() {

    }


    public static ServiceDescriptionFragment newInstance(int dataType) {
        ServiceDescriptionFragment fragment = new ServiceDescriptionFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(dataTypeKey, dataType);
        fragment.setArguments(arguments);
        return fragment;
    }
}
