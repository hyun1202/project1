package com.hyun.jobty;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

public class ParamTest {

    public String test(String name){
        return name;
    }

    public HashMap<String, Object> setData(String name, Object data) {
        return setHashMapData(name, data);
    }

    public HashMap<String, Object> setData(String name, Object data, String name2, Object data2) {
        return setHashMapData(name, data, name2, data2);
    }

    private HashMap<String, Object> setHashMapData(Object... input) {
        HashMap<String, Object> map = new HashMap<>();
        int size = input.length >> 1;
        for (int i=0; i <= size; i+=2) {
            map.put((String) input[i], input[i + 1]);
        }
        return map;
    }

    @Test
    void Test(){
        String name1 = "name1", data1 = "data1";
        String name2 = "name2", data2 = "data2";
        ParamTest p = new ParamTest();
        HashMap<String, Object> m1 = p.setData(name1, data1);
        Assertions.assertThat(m1.get(name1)).isEqualTo(data1);
        HashMap<String, Object> m2 = p.setData(name1, data1, name2, data2);
        Assertions.assertThat(m2.get(name2)).isEqualTo(data2);
    }

    @Test
    void Test2(){
        System.getenv("CONF_PATH");
    }

    @Test
    void Test3() throws NoSuchMethodException {
        Method method = ParamTest.class.getDeclaredMethod("test", String.class);
        Parameter[] parameters = method.getParameters();
        System.out.println(parameters[0].getName());
    }
}
