package com.yang.blog.service.impl;

import com.yang.blog.dao.FriendLinkDao;
import com.yang.blog.pojo.BlogUser;
import com.yang.blog.pojo.FriendLink;
import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.FriendLinkService;
import com.yang.blog.utils.Constants;
import com.yang.blog.utils.IdWorker;
import com.yang.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class FriendLinkServiceimpl implements FriendLinkService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private FriendLinkDao friendLinkDao;
    @Autowired
    private UserServiceImpl userService;
    @Override
    public ResponseResult addFriendLink(FriendLink friendLink) {
        String url = friendLink.getUrl();
        if (TextUtils.isEmmpty(url)) {
            return ResponseResult.FAILED("链接Url不可以为空!!!");
        }
        String name = friendLink.getName();
        if (TextUtils.isEmmpty(name)) {
            return  ResponseResult.FAILED("用户名不可为空");
        }
        String logo = friendLink.getLogo();
        if (TextUtils.isEmmpty(logo)) {
            return  ResponseResult.FAILED("用户名不可为空");
        }
        friendLink.setId(idWorker.nextId()+"");
        friendLink.setCreateTime(new Date());
        friendLink.setUpdateTime(new Date());
        friendLink.setState("1");
        friendLinkDao.save(friendLink);
        return ResponseResult.SUCCESS("友链添加成功");
    }

    @Override
    public ResponseResult getFriendLink(String friendLinkId) {
        Optional<FriendLink> id = friendLinkDao.findById(friendLinkId);
        if(id==null){
            return  ResponseResult.FAILED("友链不存在");
        }
        ResponseResult success = ResponseResult.SUCCESS("获取友链成功");
        success.setData(id);
        return success;
    }

    @Override
    public ResponseResult listFriendLinks() {
        BlogUser blogUser = userService.checkBlogUser();
        List<FriendLink> friendLinks;
        if(blogUser==null||blogUser.getRoles().equals(Constants.User.ROLE_ADMIN)){
            friendLinks = friendLinkDao.listStatus("1");
        }else {
            Sort order = new Sort(Sort.Direction.DESC, "order");
            friendLinks= friendLinkDao.findAll(order);
        }

        ResponseResult success = ResponseResult.SUCCESS("获取友链链表成功!!!");
        success.setData(friendLinks);
        return success;
    }

    @Override
    public ResponseResult updateFriendLink(String friendLinkId, FriendLink friendLink) {
        FriendLink friendLinkDB = friendLinkDao.findOneById(friendLinkId);
        if(friendLinkDB==null){
            return ResponseResult.FAILED("友链不存在");
        }
        String name = friendLink.getName();
        if(!TextUtils.isEmmpty(name)){
            friendLinkDB.setName(name);
        }
        String logo = friendLink.getLogo();
        if(!TextUtils.isEmmpty(logo)){
            friendLinkDB.setLogo(logo);
        }
        String url = friendLink.getUrl();
        if(!TextUtils.isEmmpty(url)){
            friendLinkDB.setName(url);
        }
        friendLinkDB.setOrder(friendLink.getOrder());
        friendLinkDao.save(friendLinkDB);


        return ResponseResult.SUCCESS("修改友链成功");
    }

    @Override
    public ResponseResult deleteFriendLink(String friendLinkId) {
        FriendLink friendLinkDB = friendLinkDao.findOneById(friendLinkId);
        if(friendLinkDB==null){
            return ResponseResult.FAILED("友链不存在");
        }
        friendLinkDao.deleteById(friendLinkId);
        return ResponseResult.SUCCESS("删除友链成功");
    }
}
