package com.luhong.locwithlibrary.model;

import com.luhong.locwithlibrary.entity.SportLocationEntity;
import com.luhong.locwithlibrary.entity.SportLocationPageEntity;

import java.util.List;


public interface IMainView extends IView {
    void onTrackDetailSuccess(List<SportLocationEntity> dataList);
    void onTrackDetailNewSuccess(SportLocationPageEntity<SportLocationEntity> dataList);
}
