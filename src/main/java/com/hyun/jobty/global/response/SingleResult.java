package com.hyun.jobty.global.response;

public class SingleResult<T> extends CommonResult{
    private T data;
    public void setData(T data){
        this.data = data;
    }
    public T getData() { return this.data; }
}
