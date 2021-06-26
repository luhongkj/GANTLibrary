package com.luhong.locwithlibrary.presenter;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.FeedbackContract;
import com.luhong.locwithlibrary.entity.FeedbackEntity;
import com.luhong.locwithlibrary.entity.FeedbackRecordEntity;
import com.luhong.locwithlibrary.model.FeedbackModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by ITMG on 2020-01-06.
 */
public class FeedbackPresenter extends FeedbackContract.Presenter {
    private FeedbackContract.IModel feedbackModel;

    public FeedbackPresenter() {
    }

    @Override
    public void getFeedbackType() {
        //        List<FeedbackEntity> dataList = new ArrayList<>();
        //        dataList.add(new FeedbackEntity("硬件相关", false));
        //        dataList.add(new FeedbackEntity("告警相关", false));
        //        dataList.add(new FeedbackEntity("定位相关", false));
        //        dataList.add(new FeedbackEntity("数据相关", false));
        //        dataList.add(new FeedbackEntity("流量充值", false));
        //        dataList.add(new FeedbackEntity("保险服务", false));
        //        dataList.add(new FeedbackEntity("产品改进", false));
        //        dataList.add(new FeedbackEntity("售后服务", false));
        //        dataList.add(new FeedbackEntity("其他", false));
        //        if (isViewAttached()) baseMvpView.onGetFeedbackTypeSuccess(dataList);

        if (feedbackModel == null) feedbackModel = new FeedbackModel();
        ApiClient.getInstance().doSubscribe(feedbackModel.getFeedbackType(), new BaseObserver<List<FeedbackEntity>>() {
            @Override
            protected void onSuccess(List<FeedbackEntity> dataList, String msg) {
                if (isViewAttached()) baseMvpView.onGetFeedbackTypeSuccess(dataList);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });
    }

    @Override
    public void getQuestions(int pageSize, int pageNo) {
        if (feedbackModel == null) feedbackModel = new FeedbackModel();
        ApiClient.getInstance().doSubscribe(feedbackModel.getQuestions(pageSize, pageNo), new BaseObserver<BasePageEntity<FeedbackRecordEntity>>() {
            @Override
            protected void onSuccess(BasePageEntity<FeedbackRecordEntity> resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onGetQuestionsSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });
    }

    @Override
    public void saveQuestions(Map<String, Object> bodyParams) {
        if (feedbackModel == null) feedbackModel = new FeedbackModel();
        ApiClient.getInstance().doSubscribe(feedbackModel.saveQuestions(bodyParams), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                ToastUtil.show("提交成功");
                if (isViewAttached()) baseMvpView.onSaveQuestionsSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(2, errResult);
            }
        });
    }
}
