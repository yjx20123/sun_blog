package com.yang.blog.service;

import com.yang.blog.pojo.Category;
import com.yang.blog.response.ResponseResult;

public interface CategorySevice {
    ResponseResult addCategory(Category category);

    ResponseResult getCategory(String categoryId);

    ResponseResult listCategories();

    ResponseResult updateCategory(String categoryId, Category category);

    ResponseResult delectCategory(String categoryId);
}
