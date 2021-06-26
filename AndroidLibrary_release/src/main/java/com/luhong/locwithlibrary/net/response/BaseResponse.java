package com.luhong.locwithlibrary.net.response;

import java.io.Serializable;

/**
 * Created by ITMG on 2018-12-15.
 */
public class BaseResponse<T> extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    private T data;

    public BaseResponse()
    {
        super();
    }

    public BaseResponse(T data)
    {
        super();
        this.data = data;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public boolean isSuccess()
    {
        return getCode() == 1;
    }
}
