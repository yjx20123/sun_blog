package com.yang.blog.service;

import com.yang.blog.response.ResponseResult;

public interface WebSizeInfoService {
    ResponseResult getWebTitle();

    ResponseResult putWebSizeTitle(String title);

    ResponseResult putSeoInfo(String keywords, String description);

    ResponseResult getSeoInfo();

    ResponseResult getWebSizeViewCount();
}
