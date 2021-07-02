package com.luhong.locwithlibrary.imagepicker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.luhong.locwithlibrary.utils.Ext;


/**
 * 沉浸式，兼容凹口屏的控件
 */
public class ImmersiveView extends LinearLayout
{
    private int stateBarHeight;
    private TextView stateBarView;
    protected ConstraintLayout contentView;
    private int contentHeight = 0;

    public ImmersiveView(Context context)
    {
        this(context, null);
    }

    public ImmersiveView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public ImmersiveView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context)
    {
        stateBarHeight = Ext.getStatbarHeight(context);
        setOrientation(VERTICAL);
        stateBarView = new TextView(context);
        contentView = new ConstraintLayout(context);
        LayoutParams stateBarParams = new LayoutParams(LayoutParams.MATCH_PARENT, 0);
        stateBarView.setLayoutParams(stateBarParams);
        LayoutParams contentBarParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        contentView.setLayoutParams(contentBarParams);
        super.addView(stateBarView);
        super.addView(contentView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        int h = Ext.measureHeight(heightMeasureSpec);
        if (contentHeight == 0 && h > 0)
        {
            contentHeight = h;
            ViewGroup.LayoutParams stateBarParams = stateBarView.getLayoutParams();
            stateBarParams.height = stateBarHeight;

            ViewGroup.LayoutParams contentBarParams = contentView.getLayoutParams();
            contentBarParams.height = contentHeight;

            this.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }
        super.onMeasure(widthMeasureSpec, stateBarHeight + contentHeight);
    }

    @Override
    public void addView(View child)
    {
        contentView.addView(child);
    }

    
}
