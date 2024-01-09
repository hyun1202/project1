package com.hyun.jobty.global.response;

import com.hyun.jobty.global.util.Util;
import java.util.Map;

public class ListResult<T> extends CommonResult{
    private Map<String, T> data;

    public void setData(Object ...data){
        this.data = Util.toMap(Util.mapOf(data));
    }

    public Map<String, T> getData() {
        return data;
    }
}
