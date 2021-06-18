package com.luhong.locwithlibrary.ui.equipment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.FlowBillAdapter;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.FlowAccountContract;
import com.luhong.locwithlibrary.dialog.BaseDialog;
import com.luhong.locwithlibrary.dialog.DevicePromptDialog;
import com.luhong.locwithlibrary.entity.FlowAccountEntity;
import com.luhong.locwithlibrary.entity.FlowBillEntity;
import com.luhong.locwithlibrary.event.PushEvent;
import com.luhong.locwithlibrary.listener.OnLoadMoreListener;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.FlowAccountPresenter;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

public class FlowAccountActivity extends BaseMvpActivity<FlowAccountPresenter> implements FlowAccountContract.View, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    @BindView(R2.id.recyclerView_flow_account)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeRefreshLayout_flow_account)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.tv_errorLoad_flow_account)
    TextView tv_errorLoad;
    @BindView(R2.id.tv_record_flow_account)
    TextView tv_record;
    @BindView(R2.id.tv_balance_flow_account)
    TextView tv_balance;
    @BindView(R2.id.tv_recharge_flow_account)
    TextView tv_recharge;

    private FlowBillAdapter flowBillAdapter;
    private int pageSize = 10, pageNo = 1, nextPageNo = 2;
    private boolean isLoadMore;

    @Override
    protected boolean isLightStatusBar() {
        return false;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_flow_account;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "流量账户");
        swipeRefreshLayout.setColorSchemeResources(R.color.theme_color);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        flowBillAdapter = new FlowBillAdapter(mActivity, new ArrayList<FlowBillEntity>(), true);
        flowBillAdapter.setLoadingView(R.layout.load_loading_layout);
        flowBillAdapter.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(flowBillAdapter);
    }

    @Override
    protected void initData() {

    }

    boolean isPay;

    @Override
    protected void fetchData() {
        mPresenter.getFlowAccountInfo();
        pageNo = 1;
        nextPageNo = 2;
        mPresenter.getFlowBill(pageSize, pageNo);
    }

    @Override
    protected void onEventListener() {
        tv_record.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startIntentActivity(RechargeRecordActivity.class);
            }
        });
        tv_recharge.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                isbalance = Integer.parseInt(tv_balance.getText().toString().trim());
                startIntentActivityForResult(FlowAccountPayActivity.class, 100);
            }
        });
    }

    int isbalance;//支付前的月份_用于判断支付完成后的一个支付状态

    @Override
    public void onFlowAccountInfoSuccess(FlowAccountEntity resultEntity) {
        cancelLoading();
        if (resultEntity != null || resultEntity.getMoney() < resultEntity.getFlowFee()) {
            float moneys = resultEntity.getMoney();
            float flowFee = (float) resultEntity.getFlowFee();
            tv_balance.setText((int) (moneys / flowFee) + "");
            if (isPay) {
                isPay = false;
                float yue = (moneys / flowFee);
                if (yue > isbalance)
                    paySuccess(DevicePromptDialog.TYPE_PAY_FLOW_SUCCESS, (int) yue + "");
            }
        }
    }

    @Override
    public void onBillSuccess(BasePageEntity<FlowBillEntity> pageList) {
        try {
            if (isLoadMore) {
                if (pageList == null || pageList.getRecords() == null || pageList.getRecords().size() == 0) {
                    flowBillAdapter.setLoadEndView(R.layout.load_end_layout);
                    ToastUtil.show(R.string.load_end);
                    return;
                }
                flowBillAdapter.setLoadMoreData(pageList.getRecords());
                if (pageList.getRecords().size() < pageSize) {
                    flowBillAdapter.setLoadEndView(R.layout.load_end_layout);
                } else {
                    nextPageNo++;
                }
            } else {
                flowBillAdapter.clearData();
                swipeRefreshLayout.setRefreshing(false);
                if (pageList == null || pageList.getRecords() == null || pageList.getRecords().size() == 0) {
                    tv_errorLoad.setText(R.string.load_null);
                    tv_errorLoad.setVisibility(View.VISIBLE);
                    return;
                }
                flowBillAdapter.setNewData(pageList.getRecords());
                if (pageList.getTotal() <= pageSize) {
                    flowBillAdapter.setLoadEndView(R.layout.load_end_layout);
                } else {
                    flowBillAdapter.setLoadingView(R.layout.load_loading_layout);
                }
                tv_errorLoad.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Logger.error("数据解析出错=" + e);
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onRefresh() {
        isLoadMore = false;
        fetchData();
    }

    @Override
    public void onLoadMore(boolean isReload) {
        if (pageNo == nextPageNo && !isReload) {
            return;
        }
        isLoadMore = true;
        pageNo = nextPageNo;
        mPresenter.getFlowBill(pageSize, pageNo);
    }

    @Override
    public void onFailure(int errType, String errMsg) {
        if (isLoadMore) {
            flowBillAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            if (flowBillAdapter.getDatasSize() == 0) {
                tv_errorLoad.setText(R.string.load_failed_refresh);
                tv_errorLoad.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == FlowAccountPayActivity.FLOWACCOUNTPAY) {
            showLoading();
            isPay = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    fetchData();
                }
            }, 1000);
        }
    }

    private void paySuccess(int type, String amount) {
        DevicePromptDialog.getInstance(mActivity).showDialog(type, "", amount, new BaseDialog.IEventListener() {
            @Override
            public void onConfirm() {

            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
