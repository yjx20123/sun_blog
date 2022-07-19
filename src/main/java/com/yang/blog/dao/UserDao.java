package com.yang.blog.dao;

import com.yang.blog.pojo.BlogUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface UserDao extends JpaSpecificationExecutor<BlogUser>, JpaRepository<BlogUser, String> {
    BlogUser findByUsername(String userName);

    BlogUser findByEmail(String email);

    BlogUser findOneById(String id);

    //通过修改用户的状态进行伪删除
    @Modifying
    @Query(nativeQuery = true, value = "UPDATE `tb_user` set `state` ='0' where  `id`=?")
    int deleteBlogUserByState(String userid);

//    @Query(value = "select new BlogUser(u.username,u.id,u.roles,u.avatar,u.email,u.state,u.createtime) from BlogUser as u")
//    Page<BlogUser> listallUserNoPassword(Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "update `tb_user` set `password`=? where `email`=?")
    int updatePasswordByEmail(String encode, String Email);

    @Modifying
    @Query(nativeQuery = true, value = "update `tb_user` set `email`=? where `id`=?")
    int updateEmailById(String email, String id);

}


