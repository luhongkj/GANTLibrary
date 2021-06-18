package com.luhong.locwithlibrary.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.CertificateTypeSelectAdapter;
import com.luhong.locwithlibrary.listener.SingleClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * 证件类型选择
 * Created by ITMG on 2019-12-28.
 */
public class TypeOfCertificateDialog extends BaseDialog implements CertificateTypeSelectAdapter.IEventListener {
    @BindView(R2.id.recyclerView_vehicleDialog)
    RecyclerView recyclerView;
    @BindView(R2.id.tv_cancel_vehicleDialog)
    TextView tv_cancel;
    @BindView(R2.id.tv_title_vehicleDialog)
    TextView tv_title_vehicleDialog;

    private static Activity mActivity;
    private IEventListener photoListener;
    private CertificateTypeSelectAdapter deviceSelectAdapter;

    public static TypeOfCertificateDialog getInstance(Activity context) {
        mActivity = context;
        return new TypeOfCertificateDialog(context);
    }

    public void showDialog(IEventListener eventListener) {
        this.photoListener = eventListener;
        show();
    }

    public TypeOfCertificateDialog(Context context) {
        super(context);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.dialog_vehicle;
    }

    @Override
    protected int setGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected void initAlertDialogView(Window window) {

        tv_title_vehicleDialog.setText("证件类型");

        // TODO Auto-generated method stub
        deviceSelectAdapter = new CertificateTypeSelectAdapter(mActivity, new ArrayList<String>(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(deviceSelectAdapter);

        List<String> list = new ArrayList<>();
        list.add("大陆身份证");
        list.add("港澳台身份证");
        list.add("护照");

        deviceSelectAdapter.setNewData(list);
    }

    @Override
    protected void onEventListener() {
        tv_cancel.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                cancel();
            }
        });
    }

    @Override
    public void onCheck(String data, int position) {
        if (data == null) return;
        photoListener.onSelectCallback(data);
        cancel();
    }

    public interface IEventListener {
        void onSelectCallback(String data);
    }
}
