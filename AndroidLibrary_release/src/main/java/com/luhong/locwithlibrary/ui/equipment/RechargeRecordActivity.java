package com.luhong.locwithlibrary.ui.equipment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.RechargeRecordAdapter;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.entity.RechargeRecordEntity;
import com.luhong.locwithlibrary.event.PushEvent;
import com.luhong.locwithlibrary.listener.OnLoadMoreListener;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.model.RechargeRecordContract;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.ToastUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;

public class RechargeRecordActivity extends BaseMvpActivity<RechargeRecordPresenter> implements RechargeRecordContract.View, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {

    @BindView(R2.id.recyclerView_recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeRefreshLayout_recyclerView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.tv_errorLoad_recyclerView)
    TextView tv_errorLoad;

    private RechargeRecordAdapter rechargeRecordAdapter;
    private int pageSize = 10, pageNo = 1, nextPageNo = 2;
    private boolean isLoadMore;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_recharge_record;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "充值记录", "", new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
               /* Bundle bundle = new Bundle();
                bundle.putInt(RechargeActivity.dataTypeKey, RechargeActivity.TYPE_FLOW);
                startIntentActivityForResult(RechargeActivity.class, RechargeActivity.RECHARGE_SUCCESS_CODE, bundle);*/
            }
        });
        swipeRefreshLayout.setColorSchemeResources(R.color.theme_color);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        rechargeRecordAdapter = new RechargeRecordAdapter(mActivity, new ArrayList<RechargeRecordEntity>(), true);
        rechargeRecordAdapter.setLoadingView(R.layout.load_loading_layout);
        rechargeRecordAdapter.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(rechargeRecordAdapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void fetchData() {
        mPresenter.getRechargeRecord(pageSize, pageNo);
    }

    @Override
    protected void onEventListener() {

    }

    @Override
    public void onRechargeRecordSuccess(BasePageEntity<RechargeRecordEntity> pageList) {
        try {
            if (isLoadMore) {
                if (pageList == null || pageList.getRecords() == null || pageList.getRecords().size() == 0) {
                    rechargeRecordAdapter.setLoadEndView(R.layout.load_end_layout);
                    ToastUtil.show(R.string.load_end);
                    return;
                }
                rechargeRecordAdapter.setLoadMoreData(pageList.getRecords());
                if (pageList.getRecords().size() < pageSize) {
                    rechargeRecordAdapter.setLoadEndView(R.layout.load_end_layout);
                } else {
                    nextPageNo++;
                }
            } else {
                rechargeRecordAdapter.clearData();
                swipeRefreshLayout.setRefreshing(false);
                if (pageList == null || pageList.getRecords() == null || pageList.getRecords().size() == 0) {
                    tv_errorLoad.setText(R.string.load_null);
                    tv_errorLoad.setVisibility(View.VISIBLE);
                    return;
                }
                rechargeRecordAdapter.setNewData(pageList.getRecords());
                if (pageList.getTotal() <= pageSize) {
                    rechargeRecordAdapter.setLoadEndView(R.layout.load_end_layout);
                } else {
                    rechargeRecordAdapter.setLoadingView(R.layout.load_loading_layout);
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
        pageNo = 1;
        nextPageNo = 2;
        fetchData();
    }

    @Override
    public void onLoadMore(boolean isReload) {
        if (pageNo == nextPageNo && !isReload) {
            return;
        }
        isLoadMore = true;
        pageNo = nextPageNo;
        fetchData();
    }

    @Override
    public void onFailure(int errType, String errMsg) {
        if (isLoadMore) {
            rechargeRecordAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            if (rechargeRecordAdapter.getDatasSize() == 0) {
                tv_errorLoad.setText(R.string.load_failed_refresh);
                tv_errorLoad.setVisibility(View.VISIBLE);
            }
        }
    }

   /* @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribeEvent(PushEvent eventMessage)
    {//
        Logger.error("支付成功推送ID=" + eventMessage.getMsgId());
        switch (eventMessage.getMsgId())
        {
            case AppConstants.MSG_FLOW://
                showToast("支付成功");
                onRefresh();
                break;
        }
    }*/
}
