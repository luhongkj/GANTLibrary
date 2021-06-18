package com.luhong.locwithlibrary.net.response;

import java.io.Serializable;
import java.util.List;

/**
 * 分页
 * Created by ITMG on 2018-12-15.
 */
public class BasePageEntity<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int total;
    private int size;//页容量
    private int pages;
    private int current;//页码
    private List<T> records;


    public BasePageEntity() {
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }

}
