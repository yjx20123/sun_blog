package com.yang.blog.config;

import com.google.gson.Gson;
import com.yang.blog.utils.IdWorker;
import com.yang.blog.utils.JwtUtil;
import com.yang.blog.utils.RedisUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class BeanConfig {
    @Bean
    public IdWorker createId() {
        return new IdWorker(0, 0);
    }

    @Bean
    public BCryptPasswordEncoder cryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RedisUtil createRedis() {
        return new RedisUtil();
    }

    @Bean
    public Random createRandom() {
        return new Random();
    }

    @Bean
    public JwtUtil jwtUtil() {
        return new JwtUtil();
    }

    @Bean
    public Gson createGson() {
        return new Gson();
    }
}
