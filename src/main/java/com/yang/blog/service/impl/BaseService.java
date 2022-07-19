package com.yang.blog.service.impl;

import com.yang.blog.utils.Constants;

public class BaseService {
    public int checkpage(int page) {
        if (page < Constants.Page.DEFAULT_PAGE) {
            page = 1;
        }
        return page;
    }

    public int checkSize(int size) {
        //限制size，每一页不得小于5个
        if (size < Constants.Page.MIN_SIZE) {
            size = Constants.Page.MIN_SIZE;
        }
        return size;
    }
}
