package com.yang.blog.dao;

import com.yang.blog.pojo.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;

public interface CommentDao extends JpaRepository<Comment,String>, JpaSpecificationExecutor<Comment> {
    int deleteAllByarticleId(String Id);

    Comment findOneById(String commentId);
}
