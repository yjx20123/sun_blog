package com.yang.blog.service.impl;

import com.mysql.jdbc.log.Log;
import com.yang.blog.dao.CategoryDao;
import com.yang.blog.pojo.BlogUser;
import com.yang.blog.pojo.Category;
import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.CategorySevice;
import com.yang.blog.utils.Constants;
import com.yang.blog.utils.IdWorker;
import com.yang.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategorySevice {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private UserServiceImpl userService;

    @Override
    public ResponseResult addCategory(Category category) {
        //先检查数据
        //必须的数据有:
        //分类名称，分类的拼音，顺序，描述
        if (TextUtils.isEmmpty(category.getName())) {
            return ResponseResult.FAILED("分类名称不可以为空");
        }
        if (TextUtils.isEmmpty(category.getPinyin())) {
            return ResponseResult.FAILED("分类拼音不可为空");
        }
        if (TextUtils.isEmmpty(category.getDescription())) {
            return ResponseResult.FAILED("分类描述不可为空");
        }
        category.setId(idWorker.nextId() + "");
        category.setCreateTime(new Date());
        category.setUpdateTime(new Date());
        category.setStatus("1");
        categoryDao.save(category);
        return ResponseResult.SUCCESS("添加分类成功");
    }

    @Override
    public ResponseResult getCategory(String categoryId) {
        Category category = categoryDao.findOneById(categoryId);
        if (category == null) {
            return ResponseResult.FAILED("分类不存在");
        }
//        String status = category.getStatus();
//        if (status.equals("0")) {
//
//        }
        ResponseResult success = ResponseResult.SUCCESS("获取分类成功");
        success.setData(category);
        return success;
    }

    @Override
    public ResponseResult listCategories() {

        Sort sort = new Sort(Sort.Direction.DESC, "createTime", "order");
        BlogUser blogUser = userService.checkBlogUser();
        List<Category> categories;
        if(blogUser==null||!blogUser.getRoles().equals(Constants.User.ROLE_ADMIN)){
            categories = categoryDao.listcateforyStatus("1");
        }
        else{
            categories = categoryDao.findAll(sort);
        }

        ResponseResult success = ResponseResult.SUCCESS("获取分类列表成功");
        success.setData(categories);
        return success;
    }

    @Override
    public ResponseResult updateCategory(String categoryId, Category category) {
        Category categoryFromDb = categoryDao.findOneById(categoryId);
        if (categoryFromDb == null) {
            return ResponseResult.FAILED("分类不存在");
        }
        String name = category.getName();
        if (!TextUtils.isEmmpty(name)) {
            categoryFromDb.setName(name);
        }
        String description = category.getDescription();
        if (!TextUtils.isEmmpty(description)) {
            categoryFromDb.setDescription(description);
        }
        String pinyin = category.getPinyin();
        if (!TextUtils.isEmmpty(pinyin)) {
            categoryFromDb.setPinyin(pinyin);
        }
        categoryFromDb.setOrder(category.getOrder());
        log.info("categoryFromDb ==>" + categoryFromDb);
        categoryDao.save(categoryFromDb);
        return ResponseResult.SUCCESS("修改分类成功");
    }

    @Override
    public ResponseResult delectCategory(String categoryId) {
        int result = categoryDao.deletebyIdUpdateStatus(categoryId);
        return result > 0 ? ResponseResult.SUCCESS("删除分类成功") : ResponseResult.FAILED("删除分类失败");
    }
    //前端展示

}
