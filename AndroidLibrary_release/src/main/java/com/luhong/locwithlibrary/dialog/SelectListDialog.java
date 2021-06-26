package com.luhong.locwithlibrary.dialog;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.apadter.RecyclerViewHolder;
import com.luhong.locwithlibrary.apadter.SelectListAdapter;
import com.luhong.locwithlibrary.entity.SelectListEntity;
import com.luhong.locwithlibrary.listener.OnRecyclerClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ITMG on 2019/9/17 0017.
 */
public class SelectListDialog extends BasePopupWindow
{
    private RecyclerView recyclerView;
    private ISelectListener selectListener;
    private SelectListAdapter selectListAdapter;
    private List<SelectListEntity> dataList;

    public SelectListDialog(Activity context, View parentView, List<SelectListEntity> dataList, ISelectListener selectListener)
    {
        super(context, parentView, true, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.selectListener = selectListener;
        this.dataList = dataList;
        if (selectListAdapter != null) selectListAdapter.setNewData(dataList);
    }

    @Override
    public int initLayoutId()
    {
        return R.layout.dialog_select_list;
    }

    @Override
    public void initView()
    {
        setOutsideTouchable(true);
        recyclerView = findView(R.id.recyclerView_selectList);
        selectListAdapter = new SelectListAdapter(getContext(), new ArrayList<SelectListEntity>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(selectListAdapter);

    }

    @Override
    public void onEventListener()
    {
        selectListAdapter.setOnClickListener(new OnRecyclerClickListener<SelectListEntity>()
        {
            @Override
            public void onItemClick(RecyclerViewHolder viewHolder, SelectListEntity data, int position)
            {
                selectListener.onDialogClick(data);
                dismiss();
            }

            @Override
            public void onItemLongClick(RecyclerViewHolder viewHolder, SelectListEntity data, int position)
            {

            }
        });
    }

    public interface ISelectListener
    {
        void onDialogClick(SelectListEntity data);
    }

}
