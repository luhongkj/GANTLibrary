package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.BaseUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;
import com.luhong.locwithlibrary.view.ClearEditText;

import butterknife.BindView;


public class UpdateTrackDialog extends BaseDialog
{
    @BindView(R2.id.et_trackName_updateTrackDialog)
    ClearEditText et_trackName;
    @BindView(R2.id.tv_confirm_updateTrackDialog)
    TextView tv_confirm;
    @BindView(R2.id.iv_close_updateTrackDialog)
    ImageView iv_close;
    private String trackName;
    private IUpdateTrackListener updateTrackListener;
    private static UpdateTrackDialog updateTrackDialog;

    public static UpdateTrackDialog getInstance(Context context)
    {
        updateTrackDialog = new UpdateTrackDialog(context);
        return updateTrackDialog;
    }

    public void showDialog(String trackName, IUpdateTrackListener updateTrackListener)
    {
        this.trackName = trackName;
        this.updateTrackListener = updateTrackListener;
        if (updateTrackDialog != null) show();
    }

    public UpdateTrackDialog(Context context)
    {
        super(context);
    }

    @Override
    protected int initLayoutId()
    {
        return R.layout.dialog_update_track;
    }

    @Override
    protected int setGravity()
    {
        return Gravity.CENTER;
    }

    @Override
    protected void initAlertDialogView(Window window)
    {
        setCancelable(false);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        et_trackName.setText(trackName);
    }

    @Override
    protected void onEventListener()
    {
        tv_confirm.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                trackName = et_trackName.getText().toString().trim();
                if (TextUtils.isEmpty(trackName))
                {
                    ToastUtil.show("请输入轨迹名称");
                    return;
                }
                updateTrackListener.onConfirm(trackName);
                cancel();
            }
        });
        iv_close.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                cancel();
            }
        });
    }

    @Override
    public void cancel()
    {
        BaseUtils.closeInPut(et_trackName);
        super.cancel();
    }

    public interface IUpdateTrackListener
    {
        void onConfirm(String content);

        void onCancel();
    }
}
