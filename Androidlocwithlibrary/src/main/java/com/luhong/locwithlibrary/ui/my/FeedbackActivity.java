package com.luhong.locwithlibrary.ui.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.FeedbackAdapter;
import com.luhong.locwithlibrary.api.ApiServer;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.contract.FeedbackContract;
import com.luhong.locwithlibrary.entity.FeedbackEntity;
import com.luhong.locwithlibrary.entity.FeedbackRecordEntity;
import com.luhong.locwithlibrary.imagepicker.view.APPTittle;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.FeedbackPresenter;
import com.luhong.locwithlibrary.ui.BasePdfActivity;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.view.CustomGridLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class FeedbackActivity extends BaseMvpActivity<FeedbackPresenter> implements FeedbackContract.View, /*SwipeRefreshLayout.OnRefreshListener, */BaseRecyclerAdapter.IEventListener<FeedbackEntity> {
    @BindView(R2.id.recyclerView_feedback)
    RecyclerView recyclerView;
    @BindView(R2.id.et_content_feedback)
    EditText et_content;
    @BindView(R2.id.btn_confirm_feedback)
    TextView btn_confirm;
    @BindView(R2.id.tv_commonProblem_feedback)
    TextView tv_commonProblem;
    @BindView(R2.id.app_title)
    APPTittle app_title;

    private FeedbackAdapter feedbackAdapter;
    private String feedbackType;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_feedback;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        app_title.setTitleText("问题反馈");
        app_title.setRightText("反馈记录");
        app_title.rightTextView.setVisibility(View.VISIBLE);
        app_title.onLeftImgClick(v -> finish());
        app_title.onRightTextClick(v -> startIntentActivity(FeedbackRecordActivity.class));
        feedbackAdapter = new FeedbackAdapter(mActivity, new ArrayList<FeedbackEntity>(), this);
        recyclerView.setLayoutManager(new CustomGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(feedbackAdapter);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void fetchData() {
        mPresenter.getFeedbackType();
    }

    @Override
    public void onGetFeedbackTypeSuccess(List<FeedbackEntity> dataList) {
        feedbackAdapter.setNewData(dataList);
    }

    @Override
    public void onGetQuestionsSuccess(BasePageEntity<FeedbackRecordEntity> dataList) {

    }

    @Override
    public void onSaveQuestionsSuccess(Object resultEntity) {
        finish();
    }

    @Override
    public void onFailure(int errType, String errMsg) {
    }

    @Override
    protected void onEventListener() {
        btn_confirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (TextUtils.isEmpty(feedbackType)) {
                    showToast("请选择问题类型");
                    return;
                }
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    showToast(et_content.getHint().toString());
                    return;
                } else if (content.length() < 5) {
                    showToast("问题描述不能少于5个字");
                    return;
                }
                Logger.error("意见反馈输入长度=" + content.length());
                Map<String, Object> bodyParams = new HashMap<>();
                bodyParams.put("content", et_content.getText().toString());
                bodyParams.put("type", feedbackType);
                mPresenter.saveQuestions(bodyParams);
            }
        });
        tv_commonProblem.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(BaseConstants.WEB_TITLE_KEY, "常见问题");
                bundle.putString(BaseConstants.WEB_URL_KEY, ApiServer.BASE_WEB_URL + "Common-problem.pdf");
                startIntentActivity(BasePdfActivity.class, bundle);
            }
        });
    }

    @Override
    public void onCheck(FeedbackEntity data, int position) {
        feedbackType = data.getValue();
        feedbackAdapter.releaseData();
        data.setCheck(true);
        feedbackAdapter.notifyDataSetChanged();
    }
}
