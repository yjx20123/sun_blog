package com.yang.blog;

import java.util.Calendar;
import java.util.Date;

public class TestDate {
    public static void main(String[] args) {
        long currentTimeMillis = System.currentTimeMillis();
        Calendar instance = Calendar.getInstance();
        instance.set(2999,11,1);

        System.out.println(currentTimeMillis);
    }
}
