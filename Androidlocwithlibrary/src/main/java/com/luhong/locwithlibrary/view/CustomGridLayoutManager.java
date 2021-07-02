package com.luhong.locwithlibrary.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * recyclerView 表格
 * Created by ITMG on 2020-01-02.
 */
public class CustomGridLayoutManager extends StaggeredGridLayoutManager {
    private boolean isScrollEnabled = true;


    public CustomGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes, boolean isScrollEnabled) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.isScrollEnabled = isScrollEnabled;
    }

    public CustomGridLayoutManager(int spanCount, int orientation, boolean isScrollEnabled) {
        super(spanCount, orientation);
        this.isScrollEnabled = isScrollEnabled;
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollEnabled && super.canScrollVertically();
    }
}
