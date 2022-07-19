package com.yang.blog.dao;

import com.yang.blog.pojo.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RefreshTokenDao extends JpaRepository<RefreshToken,String>, JpaSpecificationExecutor<RefreshToken> {
    RefreshToken findByTokenKey(String tokenKey);
    int deleteAllByUserId(String userid);
    int deleteByTokenKey(String tokenKey);
}
