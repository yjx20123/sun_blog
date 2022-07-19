package com.yang.blog.dao;

import com.yang.blog.pojo.Looper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LooperDao extends JpaSpecificationExecutor<Looper>, JpaRepository<Looper, String> {
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE tb_looper SET `state`=0 WHERE id=?")
    int deletelooperUpadateState(String looperId);

    Looper findOneById(String id);

    @Modifying
    @Query(nativeQuery = true, value = "select * from tb_looper where `state`=? order by `order` desc")
    List<Looper> listLooperbystate(String state);
}
