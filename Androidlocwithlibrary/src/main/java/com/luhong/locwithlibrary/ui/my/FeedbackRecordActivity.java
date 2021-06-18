package com.luhong.locwithlibrary.ui.my;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.FeedbackRecordAdapter;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.FeedbackContract;
import com.luhong.locwithlibrary.entity.FeedbackEntity;
import com.luhong.locwithlibrary.entity.FeedbackRecordEntity;
import com.luhong.locwithlibrary.listener.OnLoadMoreListener;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.FeedbackPresenter;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class FeedbackRecordActivity extends BaseMvpActivity<FeedbackPresenter> implements FeedbackContract.View, SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener, FeedbackRecordAdapter.IEventListener {
    @BindView(R2.id.recyclerView_recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.swipeRefreshLayout_recyclerView)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R2.id.tv_errorLoad_recyclerView)
    TextView tv_errorLoad;
    private FeedbackRecordAdapter feedbackRecordAdapter;
    private int pageSize = 20, pageNo = 1, nextPageNo = 2;
    private boolean isLoadMore;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_feedback_record;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "问题反馈");
        swipeRefreshLayout.setColorSchemeResources(R.color.theme_color);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        feedbackRecordAdapter = new FeedbackRecordAdapter(mActivity, new ArrayList<FeedbackRecordEntity>(), this);
        feedbackRecordAdapter.setLoadingView(R.layout.load_loading_layout);
        feedbackRecordAdapter.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(feedbackRecordAdapter);
    }

    @Override
    protected void initData() {

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
    protected void fetchData() {
        mPresenter.getQuestions(pageSize, pageNo);
    }

    @Override
    public void onGetFeedbackTypeSuccess(List<FeedbackEntity> dataList) {

    }

    @Override
    public void onGetQuestionsSuccess(BasePageEntity<FeedbackRecordEntity> pageList) {
        try {
            if (isLoadMore) {
                if (pageList == null || pageList.getRecords() == null || pageList.getRecords().size() == 0) {
                    feedbackRecordAdapter.setLoadEndView(R.layout.load_end_layout);
                    ToastUtil.show(R.string.load_end);
                    return;
                }
                List<FeedbackRecordEntity> dataList = pageList.getRecords();
                feedbackRecordAdapter.setLoadMoreData(dataList);
                if (dataList.size() < pageSize) {
                    feedbackRecordAdapter.setLoadEndView(R.layout.load_end_layout);
                } else {
                    nextPageNo++;
                }
            } else {
                swipeRefreshLayout.setRefreshing(false);
                feedbackRecordAdapter.clearData();
                if (pageList == null || pageList.getRecords() == null || pageList.getRecords().size() == 0) {
                    tv_errorLoad.setText(R.string.load_null);
                    tv_errorLoad.setVisibility(View.VISIBLE);
                    return;
                }
                List<FeedbackRecordEntity> dataList = pageList.getRecords();
                feedbackRecordAdapter.setNewData(dataList);
                if (pageList.getTotal() <= pageSize) {
                    feedbackRecordAdapter.setLoadEndView(R.layout.load_end_layout);
                } else {
                    feedbackRecordAdapter.setLoadingView(R.layout.load_loading_layout);
                }
                tv_errorLoad.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            Logger.error("数据解析出错=" + e);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onSaveQuestionsSuccess(Object resultEntity) {

    }

    @Override
    public void onFailure(int errType, String errMsg) {
        if (isLoadMore) {
            feedbackRecordAdapter.setLoadFailedView(R.layout.load_failed_layout);
        } else {
            swipeRefreshLayout.setRefreshing(false);
            if (feedbackRecordAdapter.getDatasSize() == 0) {
                tv_errorLoad.setText(R.string.load_failed_refresh);
                tv_errorLoad.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onEventListener() {

    }

    @Override
    public void onStatusClick(FeedbackRecordEntity data, int position) {
        data.setOpen(!data.isOpen());
        feedbackRecordAdapter.notifyDataSetChanged();
    }
}
