package com.hyun.jobty;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class JUnitTest {
    @DisplayName("Test Name")
    @Test
    public void junitTest(){
        int a = 1, b = 2;
        int sum = 3;

        // param 1: 기대 값
        // param 2: 검증 값
        org.junit.jupiter.api.Assertions.assertEquals(sum, a+b);
        Assertions.assertThat(a+b).isEqualTo(2);
    }
}
