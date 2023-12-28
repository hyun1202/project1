package com.hyun.jobty.global.response;

import java.util.HashMap;
import java.util.List;

public class ListResult<T> extends CommonResult{
    private HashMap<String, T> data;

    public void setData(Object ...data){
        this.data = setHashMapData(data);
    }

    public void setData(String n1, T d1){
        this.data = setHashMapData(n1, d1);
    }

    public void setData(String n1, T d1, String n2, T d2) {
        this.data = setHashMapData(n1, d1, n2, d2);
    }

    private HashMap<String, T> setHashMapData(Object... input) {
        HashMap<String, T> map = new HashMap<>();
        int size = input.length >> 1;
        for (int i=0; i <= size; i+=2) {
            map.put((String) input[i], (T)input[i + 1]);
        }
        return map;
    }

    public HashMap<String, T> getdata(){
        return data;
    }
}
