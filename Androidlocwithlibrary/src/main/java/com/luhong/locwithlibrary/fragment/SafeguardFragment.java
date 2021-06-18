package com.luhong.locwithlibrary.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.RecyclerViewHolder;
import com.luhong.locwithlibrary.apadter.SafeguardAdapter;
import com.luhong.locwithlibrary.base.BaseMvpFragment;
import com.luhong.locwithlibrary.contract.SafeguardContract;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.listener.OnLoadMoreListener;
import com.luhong.locwithlibrary.listener.OnRecyclerClickListener;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.SafeguardPresenter;
import com.luhong.locwithlibrary.ui.insurance.SafeguardDetailActivity;
import com.luhong.locwithlibrary.ui.insurance.SafeguardEditActivity;
import com.luhong.locwithlibrary.ui.insurance.SafeguardIntroduceActivity;
import com.luhong.locwithlibrary.ui.insurance.SafeguardPayActivity;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class SafeguardFragment extends BaseMvpFragment<SafeguardPresenter> implements SafeguardContract.View, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    protected static final String dataTypeKey = "dataTypeKey";
    public static final int dataType_home = 0;
    public static final int dataType_min = 1;
    @BindView(R2.id.recyclerView_recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeRefreshLayout_recyclerView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.tv_errorLoad_recyclerView)
    TextView tv_errorLoad;

    private SafeguardAdapter safeguardAdapter;
    private int pageSize = 100, pageNo = 1, nextPageNo = 2;
    private int dataType;


    public SafeguardFragment() {
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_safeguard;
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            dataType = getArguments().getInt(dataTypeKey);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        swipeRefreshLayout.setColorSchemeResources(R.color.theme_color);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        safeguardAdapter = new SafeguardAdapter(mActivity, new ArrayList<SafeguardEntity>(), false/*dataType == dataType_min*/, dataType);
        safeguardAdapter.setLoadingView(R.layout.load_loading_layout);
        safeguardAdapter.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(safeguardAdapter);
    }

    @Override
    protected void onEventListener() {
        safeguardAdapter.setOnClickListener(new OnRecyclerClickListener<SafeguardEntity>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, SafeguardEntity data, int position) {
                if (dataType == dataType_home) {
                    Bundle bundle = new Bundle();
                    bundle.putString("protocol", data.getUrl());
                    bundle.putString("insureSetId", data.getId());
                    startIntentActivity(SafeguardIntroduceActivity.class, bundle);
                } else if (dataType == dataType_min) {//1待初审,2待终审,3审核未过,4待支付,5已保障,6已过期,7已理赔,8过期未支付
                    /*
                    购买保险：
http://gis.luhongkj.com:8802/buyInsurance.html
需要拼接参数：token：token；insureCost：保险费用；cost：应收保费/年；valuation：车辆估值；insureFee：赠送保费；orderNo：保单号
                     */
                    Bundle bundle = new Bundle();
                    if (data.getStatus() == SafeguardEntity.TYPE_PAY) {
                        bundle.putSerializable(SafeguardPayActivity.DATA_PAY, data);
                        startIntentActivity(SafeguardPayActivity.class, bundle);
                        return;
                    } else if (data.getStatus() == SafeguardEntity.TYPE_GUARANTEED || data.getStatus() == SafeguardEntity.TYPE_EXPIRED || data.getStatus() == SafeguardEntity.TYPE_CLAIMED) {
                        bundle.putSerializable(SafeguardDetailActivity.dataTypeKey, data);
                        startIntentActivity(SafeguardDetailActivity.class, bundle);
                        return;
                    }
                    bundle.putInt(SafeguardEditActivity.dataTypeKey, SafeguardEditActivity.dataType_info);
                    bundle.putString(SafeguardEditActivity.safeguardIdKey, data.getId());
                    bundle.putString(SafeguardEditActivity.insureSetIdKey, data.getInsureSetId());
                    startIntentActivityForResult(SafeguardEditActivity.class, SafeguardEditActivity.RESULT_CODE, bundle);
                }
            }

            @Override
            public void onItemLongClick(RecyclerViewHolder viewHolder, SafeguardEntity data, int position) {

            }
        });
    }

    @Override
    protected void fetchData() {
        if (dataType == dataType_home) {
            mPresenter.getSafeguardHome();
        } else if (dataType == dataType_min) {
            mPresenter.getSafeguardMine();
        }
    }

    @Override
    public void onSafeguardHomeSuccess(List<SafeguardEntity> dataList) {
        updateView(dataList);
    }

    @Override
    public void onSafeguardMineSuccess(BasePageEntity<SafeguardEntity> pageList) {
        if (pageList == null) return;
        updateView(pageList.getRecords());
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
            safeguardAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            if (safeguardAdapter.getDatasSize() == 0) {
                tv_errorLoad.setText(R.string.load_failed_refresh);
                tv_errorLoad.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (dataType == dataType_min) {
            /*if (resultCode == SafeguardEditActivity.RESULT_CODE) {
                Logger.error("刷新");
                //                onRefresh();
            }*/
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUserVisibleHint(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && dataType == dataType_min) {
            if (mPresenter != null) onRefresh();
        }
    }

    private void updateView(List<SafeguardEntity> dataList) {
        try {
            if (pageNo > 1) {
                if (dataList == null || dataList.size() == 0) {
                    safeguardAdapter.setLoadEndView(R.layout.load_end_layout);
                    ToastUtil.show(R.string.load_end);
                    return;
                }
                safeguardAdapter.setLoadMoreData(dataList);
                if (dataList.size() < pageSize) {
                    safeguardAdapter.setLoadEndView(R.layout.load_end_layout);
                } else {
                    nextPageNo++;
                }
            } else {
                safeguardAdapter.clearData();
                swipeRefreshLayout.setRefreshing(false);
                if (dataList == null || dataList.size() == 0) {
                    tv_errorLoad.setText(R.string.load_null);
                    tv_errorLoad.setVisibility(View.VISIBLE);
                    return;
                }
                safeguardAdapter.setNewData(dataList);
                if (dataList.size() < pageSize) {
                    safeguardAdapter.setLoadEndView(R.layout.load_end_layout);
                } else {
                    safeguardAdapter.setLoadingView(R.layout.load_loading_layout);
                }
                tv_errorLoad.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Logger.error("数据解析出错=" + e);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public static SafeguardFragment newInstance(int dataType) {
        SafeguardFragment fragment = new SafeguardFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(dataTypeKey, dataType);
        fragment.setArguments(arguments);
        return fragment;
    }
}
