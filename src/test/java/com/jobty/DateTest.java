package com.jobty;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class DateTest {
    @Test
    public void DateTimeTest(){
        LocalDateTime date = LocalDateTime.now();
        LocalDateTime after6Month = date.plusMonths(6);
        LocalDateTime after6MonthPlusOneWeek = after6Month.plusWeeks(1);
        LocalDateTime after6MonthMinusOneWeek = after6Month.minusWeeks(1);
        System.out.println("오늘 날짜: " + date);
        System.out.println("6개월 후: " + after6Month);
        System.out.println("6개월 + 1주일 후: " + after6MonthPlusOneWeek);
        System.out.println(date.isAfter(after6MonthPlusOneWeek));
        System.out.println(after6MonthMinusOneWeek.isAfter(after6Month));
    }
}
