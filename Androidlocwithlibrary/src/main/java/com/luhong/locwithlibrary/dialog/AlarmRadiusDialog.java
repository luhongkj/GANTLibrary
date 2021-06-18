package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.apadter.AlarmRadiusAdapter;
import com.luhong.locwithlibrary.apadter.RecyclerViewHolder;
import com.luhong.locwithlibrary.entity.AlarmRadiusEntity;
import com.luhong.locwithlibrary.listener.OnRecyclerClickListener;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 围栏半径设置
 */
public class AlarmRadiusDialog extends BaseDialog {
    @BindView(R2.id.recyclerView_alarmRadiusDialog)
    RecyclerView recyclerView;
    @BindView(R2.id.tv_confirm_alarmRadiusDialog)
    TextView tv_confirm;
    @BindView(R2.id.iv_close_alarmRadiusDialog)
    ImageView iv_close;
    private AlarmRadiusAdapter alarmRadiusAdapter;
    private List<AlarmRadiusEntity> dataList = new ArrayList<>();
    //    private AlarmRadiusEntity selectEntity;
    private String initRadius = "";

    private IAlarmRadiusListener updateTrackListener;
    private static AlarmRadiusDialog alarmRadiusDialog;

    public static AlarmRadiusDialog getInstance(Context context) {
        alarmRadiusDialog = new AlarmRadiusDialog(context);
        return alarmRadiusDialog;
    }

    public void showDialog(String initRadius, IAlarmRadiusListener updateTrackListener) {
        this.updateTrackListener = updateTrackListener;
        this.initRadius = initRadius;
        show();
    }

    public AlarmRadiusDialog(Context context) {
        super(context);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.dialog_alarm_radius;
    }

    @Override
    protected int setGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initAlertDialogView(Window window) {
        setCancelable(false);
        alarmRadiusAdapter = new AlarmRadiusAdapter(getContext(), new ArrayList<AlarmRadiusEntity>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(alarmRadiusAdapter);

        dataList.add(new AlarmRadiusEntity("300", !TextUtils.isEmpty(initRadius) && initRadius.equals("300")));
        dataList.add(new AlarmRadiusEntity("500", !TextUtils.isEmpty(initRadius) && initRadius.equals("500")));
        dataList.add(new AlarmRadiusEntity("1000", !TextUtils.isEmpty(initRadius) && initRadius.equals("1000")));
        dataList.add(new AlarmRadiusEntity("3000", !TextUtils.isEmpty(initRadius) && initRadius.equals("3000")));
        dataList.add(new AlarmRadiusEntity("5000", !TextUtils.isEmpty(initRadius) && initRadius.equals("5000")));
        alarmRadiusAdapter.setNewData(dataList);
    }

    @Override
    protected void onEventListener() {
        alarmRadiusAdapter.setOnClickListener(new OnRecyclerClickListener<AlarmRadiusEntity>() {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, AlarmRadiusEntity data, int position) {
                initRadius = data.getContent();
                alarmRadiusAdapter.releaseData();
                data.setCheck(true);
                alarmRadiusAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemLongClick(RecyclerViewHolder viewHolder, AlarmRadiusEntity data, int position) {

            }
        });
        tv_confirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (TextUtils.isEmpty(initRadius)) {
                    ToastUtil.show("请选择围栏半径");
                    return;
                }
                updateTrackListener.onConfirm(initRadius);
            }
        });
        iv_close.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                cancel();
            }
        });
    }

    public interface IAlarmRadiusListener {
        void onConfirm(String content);
    }
}
