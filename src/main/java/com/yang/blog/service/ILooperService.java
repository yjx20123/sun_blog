package com.yang.blog.service;

import com.yang.blog.pojo.Looper;
import com.yang.blog.response.ResponseResult;

public interface  ILooperService  {
    ResponseResult addLooper(Looper looper);

    ResponseResult deleteLooper(String looperId);

    ResponseResult getLooper(String looperId);

    ResponseResult updateLooper(String looperId, Looper looper);

    ResponseResult listLooper();
}
