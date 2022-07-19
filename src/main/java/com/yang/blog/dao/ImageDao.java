package com.yang.blog.dao;

import com.yang.blog.pojo.BlogUser;
import com.yang.blog.pojo.Image;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ImageDao extends JpaSpecificationExecutor<Image>, JpaRepository<Image, String> {
    @Modifying
    @Query(nativeQuery = true,value = "UPDATE tb_images SET `state`=0 WHERE `id`=?")
    int delteImageByUpdateState(String imageId);
}
