package com.luhong.locwithlibrary.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.PhoneAlarmAdapter;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.base.BaseMvpFragment;
import com.luhong.locwithlibrary.contract.AlarmRecordContract;
import com.luhong.locwithlibrary.entity.PhoneAlarmEntity;
import com.luhong.locwithlibrary.listener.OnLoadMoreListener;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.AlarmRecordPresenter;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;


public class AlarmRecordFragment extends BaseMvpFragment<AlarmRecordPresenter> implements AlarmRecordContract.View, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    protected static final String dataTypeKey = "dataTypeKey";
    @BindView(R2.id.ll_content_alarmRecord_item)
    LinearLayout ll_content;
    @BindView(R2.id.tv_frequency_alarmRecord_item)
    TextView tv_frequency;
    @BindView(R2.id.tv_phoneNo_alarmRecord_item)
    TextView tv_phoneNo;
    @BindView(R2.id.recyclerView_recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeRefreshLayout_recyclerView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.tv_errorLoad_recyclerView)
    TextView tv_errorLoad;

    private PhoneAlarmAdapter phoneAlarmAdapter;
    private int pageSize = 10, pageNo = 1, nextPageNo = 2;
    private int dataType;


    public AlarmRecordFragment() {
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_alarm_record;
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
            tv_phoneNo.setText("接听手机号");
        } else if (dataType == 1) {
            tv_phoneNo.setText("接收短信手机号");
        }
        swipeRefreshLayout.setColorSchemeResources(R.color.theme_color);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        phoneAlarmAdapter = new PhoneAlarmAdapter(mActivity, new ArrayList<PhoneAlarmEntity>(), true);
        phoneAlarmAdapter.setLoadingView(R.layout.load_loading_layout);
        phoneAlarmAdapter.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(phoneAlarmAdapter);
    }

    @Override
    protected void fetchData() {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("current", pageNo);
        bodyParams.put("size", pageSize);
        bodyParams.put("vehicleId", AppVariable.currentVehicleId);
        mPresenter.getPhoneAlarm(dataType, bodyParams);
    }

    @Override
    protected void onEventListener() {

    }

    @Override
    public void onPhoneAlarmSuccess(BasePageEntity<PhoneAlarmEntity> dataList) {
        try {
            if (pageNo > 1) {
                if (dataList == null || dataList.getRecords().size() == 0) {
                    phoneAlarmAdapter.setLoadEndView(R.layout.load_end_layout);
                    ToastUtil.show(R.string.load_end);
                    return;
                }
                phoneAlarmAdapter.setLoadMoreData(dataList.getRecords());
                if (dataList.getRecords().size() < pageSize) {
                    phoneAlarmAdapter.setLoadEndView(R.layout.load_end_layout);
                } else {
                    nextPageNo++;
                }
            } else {
                phoneAlarmAdapter.clearData();
                swipeRefreshLayout.setRefreshing(false);
                //                ll_content.setVisibility(View.VISIBLE);
                if (dataList == null || dataList.getRecords().size() == 0) {
                    tv_errorLoad.setText(R.string.load_null);
                    tv_errorLoad.setVisibility(View.VISIBLE);
                    ll_content.setVisibility(View.GONE);
                    return;
                }
                phoneAlarmAdapter.setNewData(dataList.getRecords());
                if (dataList.getTotal() <= pageSize) {
                    phoneAlarmAdapter.setLoadEndView(R.layout.load_end_layout);
                } else {
                    phoneAlarmAdapter.setLoadingView(R.layout.load_loading_layout);
                }
                tv_errorLoad.setVisibility(View.GONE);
                ll_content.setVisibility(View.VISIBLE);
            }
            tv_frequency.setText("累计告警：" + dataList.getTotal() + "次");
        } catch (Exception e) {
            Logger.error("数据解析出错=" + e);
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onRefresh() {
        pageNo = 1;
        nextPageNo = 2;
        fetchData();
    }

    @Override
    public void onLoadMore(boolean isReload) {
        if (pageNo == nextPageNo && !isReload) {
            return;
        }
        pageNo = nextPageNo;
        fetchData();
    }

    @Override
    public void onFailure(int errType, String errMsg) {
        if (pageNo > 1) {
            phoneAlarmAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            if (phoneAlarmAdapter.getDatasSize() == 0) {
                tv_frequency.setText("累计告警：0次");
                tv_errorLoad.setText(R.string.load_failed_refresh);
                tv_errorLoad.setVisibility(View.VISIBLE);
            }
        }
    }

    public static AlarmRecordFragment newInstance(int dataType) {
        AlarmRecordFragment fragment = new AlarmRecordFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(dataTypeKey, dataType);
        fragment.setArguments(arguments);
        return fragment;
    }
}
