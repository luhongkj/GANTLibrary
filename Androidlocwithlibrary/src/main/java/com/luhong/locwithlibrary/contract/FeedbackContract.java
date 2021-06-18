package com.luhong.locwithlibrary.contract;

import com.luhong.locwithlibrary.entity.FeedbackEntity;
import com.luhong.locwithlibrary.entity.FeedbackRecordEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * 问题反馈
 * Created by ITMG on 2020-01-06.
 */
public interface FeedbackContract {

    interface View extends BaseMvpView {
        void onGetFeedbackTypeSuccess(List<FeedbackEntity> dataList);

        void onGetQuestionsSuccess(BasePageEntity<FeedbackRecordEntity> dataList);

        void onSaveQuestionsSuccess(Object resultEntity);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void getFeedbackType();

        public abstract void getQuestions(int pageSize, int pageNo);

        public abstract void saveQuestions(Map<String, Object> bodyParams);
    }

    interface IModel {
        Observable<BaseResponse<List<FeedbackEntity>>> getFeedbackType();

        Observable<BaseResponse<BasePageEntity<FeedbackRecordEntity>>> getQuestions(int pageSize, int pageNo);

        Observable<BaseResponse<Object>> saveQuestions(Map<String, Object> bodyParams);
    }
}
