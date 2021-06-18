package com.luhong.locwithlibrary.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.listener.SingleClickListener;

import butterknife.BindView;


/**
 * 导航模式
 * Created by ITMG on 2020-01-03.
 */
public class NavigateTypeDialog extends BaseDialog
{
    @BindView(R2.id.tv_drive_navigateTypeDialog)
    TextView tv_drive;
    @BindView(R2.id.tv_ride_navigateTypeDialog)
    TextView tv_ride;
    @BindView(R2.id.tv_walk_navigateTypeDialog)
    TextView tv_walk;
    @BindView(R2.id.tv_cancel_navigateTypeDialog)
    TextView tv_cancel;
    private static Activity mContext;
    private INavigateListener navigateListener;

    public static NavigateTypeDialog getInstance(Activity context)
    {
        mContext = context;
        return new NavigateTypeDialog(context);
    }

    public void showDialog(INavigateListener navigateListener)
    {
        this.navigateListener = navigateListener;
        show();
    }

    public NavigateTypeDialog(Context context)
    {
        super(context);
    }

    @Override
    protected int initLayoutId()
    {
        return R.layout.dialog_navigate_type;
    }

    @Override
    protected int setGravity()
    {
        return Gravity.BOTTOM;
    }

    @Override
    protected void initAlertDialogView(Window window)
    {
        // TODO Auto-generated method stub
    }

    @Override
    protected void onEventListener()
    {
        // TODO Auto-generated method stub
        tv_drive.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                // TODO Auto-generated method stub
                navigateListener.onNavigateType(0);
                cancel();
            }
        });
        tv_ride.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                // TODO Auto-generated method stub
                navigateListener.onNavigateType(1);
                cancel();
            }
        });
        tv_walk.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                // TODO Auto-generated method stub
                navigateListener.onNavigateType(2);
                cancel();
            }
        });
        tv_cancel.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                // TODO Auto-generated method stub
                cancel();
            }
        });
    }


    public interface INavigateListener
    {
        void onNavigateType(int type);//导航模式
    }
}
