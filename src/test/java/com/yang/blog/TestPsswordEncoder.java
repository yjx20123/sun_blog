package com.yang.blog;

import ch.qos.logback.core.pattern.color.BoldCyanCompositeConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPsswordEncoder  {
    public static void main(String[] args) {
        BCryptPasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
        String encode = passwordEncoder.encode("123456");
        //数据库密文
        //$2a$10$6HxEuGtQSZQvdev0ZZFA3etu.MSaR0H6Dw7vsp6Mq1RioCEKW7tqq
        System.out.println("encode ==> "+encode);
        //验证登录
        String originalPassword="123456";
        boolean matches = passwordEncoder.matches(originalPassword, "$2a$10$6HxEuGtQSZQvdev0ZZFA3etu.MSaR0H6Dw7vsp6Mq1RioCEKW7tqq");
        System.out.println("密码是否正确"+matches);
    }
}
