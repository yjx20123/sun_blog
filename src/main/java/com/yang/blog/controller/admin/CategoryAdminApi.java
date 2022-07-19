package com.yang.blog.controller.admin;

import com.yang.blog.pojo.Category;
import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.CategorySevice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 分类api
 */
@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryAdminApi {
    @Autowired
    private CategorySevice categorySevice;

    /**
     * 添加分类
     * 需要管理员权限
     *
     * @param category
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addCategory(@RequestBody Category category) {
        return categorySevice.addCategory(category);
    }

    /**
     * 删除分类
     *
     * @param categoryId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @DeleteMapping("{categoryId}")
    public ResponseResult delectCategory(@PathVariable("categoryId") String categoryId) {
        return categorySevice.delectCategory(categoryId);
    }

    /**
     * 修改分类
     *
     * @param categoryId
     * @param category
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @PutMapping("{categoryId}")
    public ResponseResult updateCategory(@PathVariable("categoryId") String categoryId, @RequestBody Category category) {
        return categorySevice.updateCategory(categoryId, category);
    }
    /**
     * 获取单个分类
     * 需要管理员权限
     * 不读取也是可以的
     *
     * @param categoryId
     * @return
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/{categoryId}")
    public ResponseResult getCategory(@PathVariable("categoryId") String categoryId) {
        return categorySevice.getCategory(categoryId);
    }

    /**
     * 获取全部分类
     */
    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listCategories() {
        return categorySevice.listCategories();
    }
}
