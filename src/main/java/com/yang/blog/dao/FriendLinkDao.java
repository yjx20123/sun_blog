package com.yang.blog.dao;


import com.yang.blog.pojo.FriendLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendLinkDao extends JpaSpecificationExecutor<FriendLink>, JpaRepository<FriendLink, String>{
    FriendLink findOneById(String id);
    @Modifying
    @Query(nativeQuery = true,value = "select * from tb_friends where `state`=? order by `order` desc " )
    List<FriendLink> listStatus(String status);
}
