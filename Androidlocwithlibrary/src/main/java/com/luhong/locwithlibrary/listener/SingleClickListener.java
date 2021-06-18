package com.luhong.locwithlibrary.listener;

import android.view.View;

public abstract class SingleClickListener implements View.OnClickListener {
    private long firstTime;

    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View v) {
        if (System.currentTimeMillis() - firstTime > 600) {
            onSingleClick(v);
            firstTime = System.currentTimeMillis();
        }
    }
}
