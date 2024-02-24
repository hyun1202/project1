package com.hyun.jobty.global;

import com.hyun.jobty.util.Util;
import org.junit.jupiter.api.Test;

public class UtilTest {
    @Test
    public void uuidTest(){
        String str = Util.random(1, 32);
        System.out.println(str);
    }
}
