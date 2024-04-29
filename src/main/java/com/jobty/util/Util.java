package com.jobty.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class Util {

    public static <T> ResponseEntity<T> responseEntityOf(T body){
        return new ResponseEntity<>(body, null, HttpStatus.OK);
    }
    public static <T> ResponseEntity<T> responseEntityOf(T body, HttpHeaders headers){
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    public static <T> ResponseEntity<T> responseEntityOf(T body, HttpHeaders headers, HttpStatus status){
        return new ResponseEntity<>(body, headers, status);
    }

    public static String random(){
        return UUID.randomUUID().toString().substring(1,8) + System.currentTimeMillis();
    }

    public static String random(int start, int end){
        return UUID.randomUUID().toString().substring(start, end);
    }

    @SuppressWarnings("unchecked")
    public static <K, T> Map<K, T> toMap(Map<Object, Object> map){
        Map<K, T> newMap = new LinkedHashMap<>();
        for(Map.Entry<Object, Object> entry: map.entrySet()){
            newMap.put((K)entry.getKey(), (T)entry.getValue());
        }
        return newMap;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> Map<K, V> mapOf(Object... args){
        Map<K, V> map = new LinkedHashMap<>();
        int size = args.length >> 1;
        for (int i=0; i<=size; i+=2) {
            map.put((K) args[i], (V)args[i + 1]);
        }
        return map;
    }

    public static int findIndexArrayValue(String[] names, String name){
        int index = 0;
        for (int i=0; i<names.length; i++){
            index = i;
            if (name.equals(names[i]))
                return index;
        }
        return index;
    }
}
