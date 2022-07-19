package com.yang.blog;

import com.yang.blog.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;


public class TestCreateToken {
    public static void main(String[] args) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("id", "722250648279580673");
//        claims.put("userName", "测试用户");
//        claims.put("role", "role_normal");
//        claims.put("avatar", "https://cdn.sunofbeaches.com/images/default_avatar.png");
//        claims.put("email", "test@sunofbeach.net");
//        String token = JwtUtil.createToken(
//                claims);//有效期为1分钟
//        System.out.println(token);

        Claims claims = JwtUtil.parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoicm9sZV9ub3JtYWwiLCJpZCI6IjcyMjI1MDY0ODI3OTU4MDY3MyIsImF2YXRhciI6Imh0dHBzOi8vY2RuLnN1bm9mYmVhY2hlcy5jb20vaW1hZ2VzL2RlZmF1bHRfYXZhdGFyLnBuZyIsInVzZXJOYW1lIjoi5rWL6K-V55So5oi3IiwiZXhwIjoxNjQ0NTU2MzUxLCJlbWFpbCI6InRlc3RAc3Vub2ZiZWFjaC5uZXQifQ.7zzQVL7PAp_cCSqNdKkW-r34UWVptOqNWKOxHhO1qdk");                //==============================================//
        Object id = claims.get("id");
        Object name = claims.get("userName");
        Object role = claims.get("role");
        Object avatar = claims.get("avatar");
        Object email = claims.get("email");

        System.out.println("id == > " + id);
        System.out.println("name == > " + name);
        System.out.println("role == > " + role);
        System.out.println("avatar == > " + avatar);
        System.out.println("email == > " + email);
    }
}




