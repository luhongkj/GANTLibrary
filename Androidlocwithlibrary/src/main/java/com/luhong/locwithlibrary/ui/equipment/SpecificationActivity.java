package com.luhong.locwithlibrary.ui.equipment;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.SpecificationAdapter;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.home.DeviceManageContract;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.PdfEntity;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.home.DeviceManagePresenter;
import com.luhong.locwithlibrary.ui.BasePdfActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 产品说明书列表
 */
public class SpecificationActivity extends BaseMvpActivity<DeviceManagePresenter> implements DeviceManageContract.View {

    @BindView(R2.id.tv_left_title)
    TextView tvLeftTitle;
    @BindView(R2.id.tv_center_title)
    TextView tvCenterTitle;
    @BindView(R2.id.iv_icon_title)
    ImageView ivIconTitle;
    @BindView(R2.id.tv_right_title)
    TextView tvRightTitle;
    @BindView(R2.id.ll_titleBar_title)
    LinearLayout llTitleBarTitle;
    @BindView(R2.id.rv_list)
    RecyclerView rvList;
    SpecificationAdapter adapter;

    PdfEntity resultEntitysss;

    @Override
    protected void fetchData() {
        mPresenter.getDeviceList(this);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_specification;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "产品说明书");
        rvList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onEventListener() {

    }

    @Override
    public void onDeviceListSuccess(List<DeviceEntity> dataList) {
        adapter = new SpecificationAdapter(this, dataList, new SpecificationAdapter.ISportRecordListener() {
            @Override
            public void onEdit(DeviceEntity data, int position) {
                mPresenter.getProductDescription(mActivity, data.getSn());//获取PDF文档
            }
        });
        rvList.setAdapter(adapter);
    }

    @Override
    public void onDeviceUpdateSuccess(DeviceEntity deviceEntity) {

    }

    @Override
    public void onDeviceDeleteSuccess(boolean isCurrentDevice, DeviceEntity deviceEntity) {

    }

    @Override
    public void onFailure(int errType, String errMsg) {

    }


    @Override
    public void onProductDescriptionSuccess(PdfEntity resultEntity) {
        this.resultEntitysss = resultEntity;
        if (null == resultEntitysss || TextUtils.isEmpty(resultEntitysss.getUrl())) {
            showToast("未获取到产品说明书");
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(BaseConstants.WEB_TITLE_KEY, "产品说明书");
        bundle.putString(BaseConstants.WEB_URL_KEY, resultEntitysss.getUrl());
        startIntentActivity(BasePdfActivity.class, bundle);
    }

    @Override
    public void onSafeguardMineSuccess(BasePageEntity<SafeguardEntity> pageList) {

    }

    @Override
    public void onVehicleConfirmPaySuccess(Object resultEntity) {

    }
}
