package com.yang.blog.dao;

import com.yang.blog.pojo.BlogUserNoPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BlogNoPasswordDao extends JpaRepository<BlogUserNoPassword,String>, JpaSpecificationExecutor<BlogUserNoPassword> {
}
