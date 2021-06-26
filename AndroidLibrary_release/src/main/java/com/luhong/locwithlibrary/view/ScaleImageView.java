package com.luhong.locwithlibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * Author: ITMG
 * Time: 2016/8/18 10:55
 */
public class ScaleImageView extends AppCompatImageView {
    private int initWidth;
    private int initHeight;
    private boolean isSquare = true;

    public ScaleImageView(Context context) {
        this(context, null);
    }

    public ScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setInitSize(int initWidth, int initHeight) {
        this.initWidth = initWidth;
        this.initHeight = initHeight;
    }

    public void setSquare(boolean isSquare) {
        this.isSquare = isSquare;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (initWidth > 0 && initHeight > 0) {
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            int height = View.MeasureSpec.getSize(heightMeasureSpec);

            float scale = (float) initHeight / (float) initWidth;
            if (width > 0) {
                height = (int) ((float) width * scale);
            }
            setMeasuredDimension(width, height);
        } else {
            if (isSquare) {
                setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
                int childWidthSize = getMeasuredWidth();
                widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(childWidthSize, View.MeasureSpec.EXACTLY);
                heightMeasureSpec = widthMeasureSpec;// 高度和宽度一样
            }
            //设定高是宽的比例
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
