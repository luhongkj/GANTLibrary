package com.luhong.locwithlibrary.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.RecyclerViewHolder;
import com.luhong.locwithlibrary.apadter.SportRecordAdapter;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.dialog.DateDIYDialog;
import com.luhong.locwithlibrary.dialog.UpdateTrackDialog;
import com.luhong.locwithlibrary.entity.SportEntity;
import com.luhong.locwithlibrary.listener.OnLoadMoreListener;
import com.luhong.locwithlibrary.listener.OnRecyclerClickListener;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.model.SportRecordContract;
import com.luhong.locwithlibrary.presenter.SportRecordPresenter;
import com.luhong.locwithlibrary.utils.DateUtils;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 运动记录
 */
public class LSportRecordActivity extends BaseMvpActivity<SportRecordPresenter> implements SportRecordContract.View, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, SportRecordAdapter.ISportRecordListener {
    @BindView(R2.id.rl_date_sportRecord)
    RelativeLayout rl_date;
    @BindView(R2.id.tv_date_sportRecord)
    TextView tv_date;
    @BindView(R2.id.recyclerView_recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeRefreshLayout_recyclerView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.tv_errorLoad_recyclerView)
    TextView tv_errorLoad;

    private SportRecordAdapter sportRecordAdapter;
    private int pageSize = 10, pageNo = 1, nextPageNo = 2;
    private boolean isLoadMore;
    private String queryMonth;

    @Override
    protected int initLayoutId() {
        // TODO Auto-generated method stub
        return R.layout.activity_sport_record;
    }

    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "运动记录");
        swipeRefreshLayout.setColorSchemeResources(R.color.theme_color);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);

        sportRecordAdapter = new SportRecordAdapter(mActivity, new ArrayList<SportEntity.TrackList>(), this);
        sportRecordAdapter.setLoadingView(R.layout.load_loading_layout);
        sportRecordAdapter.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(sportRecordAdapter);

        queryMonth = DateUtils.formatCurrentDateTime("yyyy-MM");
    }

    @Override
    protected void initData() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void fetchData() {
        mPresenter.getSportRecord(queryMonth, pageSize, pageNo);
    }

    @Override
    protected void onEventListener() {
        // TODO Auto-generated method stub
        rl_date.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                DateDIYDialog.getInstance(mActivity).showDialog(true, new DateDIYDialog.IResultListener() {
                    @Override
                    public void onConfirm(int year, int month, int day) {
                        tv_date.setText(year + "年" + month + "月");

                        String selectMonth = year + "-" + month;
                        if (!queryMonth.equals(selectMonth)) {
                            queryMonth = selectMonth;
                            swipeRefreshLayout.setRefreshing(true);
                            onRefresh();
                        }
                    }
                });
            }
        });
        sportRecordAdapter.setOnClickListener(new OnRecyclerClickListener<SportEntity.TrackList>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, SportEntity.TrackList data, int position) {
              Bundle bundle = new Bundle();
                bundle.putSerializable(MapTrackActivity.dataTypeKey, data);
                startIntentActivity(MapTrackActivity.class, bundle);
            }

            @Override
            public void onItemLongClick(RecyclerViewHolder viewHolder, SportEntity.TrackList data, int position) {

            }
        });
    }

    @Override
    public void onEdit(final SportEntity.TrackList data, final int position) {
        UpdateTrackDialog.getInstance(mActivity).showDialog(data.getName(), new UpdateTrackDialog.IUpdateTrackListener() {
            @Override
            public void onConfirm(String content) {
                mPresenter.updateSport(position, data.getId(), content);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onSportRecordSuccess(List<SportEntity.TrackList> dataList) {
        try {
            if (isLoadMore) {
                if (dataList == null || dataList.size() == 0) {
                    sportRecordAdapter.setLoadEndView(R.layout.load_end_layout);
                    ToastUtil.show(R.string.load_end);
                    return;
                }
                sportRecordAdapter.setLoadMoreData(dataList);
                if (dataList.size() < pageSize) {
                    sportRecordAdapter.setLoadEndView(R.layout.load_end_layout);
                } else {
                    nextPageNo++;
                }
            } else {
                swipeRefreshLayout.setRefreshing(false);
                sportRecordAdapter.clearData();
                if (dataList == null || dataList.size() == 0) {
                    tv_errorLoad.setText(R.string.load_null);
                    tv_errorLoad.setVisibility(View.VISIBLE);
                    return;
                }
                sportRecordAdapter.setNewData(dataList);
                if (dataList.size() < pageSize) {
                    sportRecordAdapter.setLoadEndView(R.layout.load_end_layout);
                } else {
                    sportRecordAdapter.setLoadingView(R.layout.load_loading_layout);
                }
                recyclerView.scrollToPosition(0);
                tv_errorLoad.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Logger.error("数据解析出错=" + e);
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onUpdateSportSuccess(int position, String sportName) {
        sportRecordAdapter.getItem(position).setName(sportName);
        sportRecordAdapter.notifyItemChanged(position);
        //        onRefresh();
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
        if (errType == 0)
            if (isLoadMore) {
                sportRecordAdapter.setLoadFailedView(R.layout.load_failed_layout);
            } else {
                sportRecordAdapter.clearData();
                swipeRefreshLayout.setRefreshing(false);
                if (sportRecordAdapter.getDatasSize() == 0) {
                    tv_errorLoad.setText(R.string.load_failed_refresh);
                    tv_errorLoad.setVisibility(View.VISIBLE);
                }
            }
    }

}
