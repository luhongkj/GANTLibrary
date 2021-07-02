package com.luhong.locwithlibrary.model;

import com.luhong.locwithlibrary.entity.CodeTableParams;
import com.luhong.locwithlibrary.entity.SportEntity;
import com.luhong.locwithlibrary.entity.SportLocationEntity;
import com.luhong.locwithlibrary.entity.SportLocationPageEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public interface ISportModel {
    Observable<BaseResponse<SportEntity>> getSportList(String queryMonth, int pageSize, int pageNo);

    Observable<BaseResponse<List<SportLocationEntity>>> getSportLocations(String sportId);

    Observable<BaseResponse<SportLocationPageEntity<SportLocationEntity>>> getSportLocationsNew(String sportId);

    Observable<BaseResponse<Object>> updateSport(String sportId, String sportName);

    Observable<BaseResponse<Object>> uploadSport(CodeTableParams codeTableParams);
}
