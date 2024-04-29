package com.jobty;

import org.junit.jupiter.api.*;

public class JUnitCycleTest {
    // 전체 테스트를 시작하기 전 1회 실행 => static
    @BeforeAll
    static void beforeAll(){
        System.out.println("@BeforeAll");
    }

    // 테스트 케이스를 시작하기 전마다 실행
    @BeforeEach
    public void beforeEach(){
        System.out.println("@BeforeEach");
    }

    @Test
    public void Test1(){
        System.out.println("@Test1");
    }

    @Test
    public void Test2(){
        System.out.println("@Test2");
    }

    @Test
    public void Test3(){
        System.out.println("@Test3");
    }

    // 전체 테스트를 마치고 종료 전 1회 실행 => static
    @AfterAll
    static public void AfterAll(){
        System.out.println("@AfterAll");
    }

    // 테스트 케이스 종료하기 전마다 실행
    @AfterEach
    public void AfterEach(){
        System.out.println("@AfterEach");
    }
}
