package com.yang.blog.service;

import com.yang.blog.pojo.FriendLink;
import com.yang.blog.response.ResponseResult;

public interface FriendLinkService {
    ResponseResult addFriendLink(FriendLink friendLink);

    ResponseResult getFriendLink(String friendLinkId);

    ResponseResult listFriendLinks();

    ResponseResult updateFriendLink(String friendLinkId, FriendLink friendLink);

    ResponseResult deleteFriendLink(String friendLinkId);
}
