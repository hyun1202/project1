package com.hyun.jobty.global.response;

import java.util.List;

public class ListResult<T> extends CommonResult{
    private List<T> data;

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getData() {
        return data;
    }
}
