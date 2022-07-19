package com.yang.blog.controller.portal;

import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.ArticleService;
import com.yang.blog.service.CategorySevice;
import com.yang.blog.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/portal/article")
public class ArticlePortalApi {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategorySevice categorySevice;
    @GetMapping("/list/{page}/{size}")
    public ResponseResult ListArticle(@PathVariable("page") int page, @PathVariable("size") int size) {
        return articleService.listArticles(page, size, null, null, Constants.Article.STATE_PUBLISH);
    }

    @GetMapping("list/{categoryId}/{page}/{size}")
    public ResponseResult listAractile(@PathVariable("categoryId") String categoryId, @PathVariable("page") int page, @PathVariable("size") int size) {
        return articleService.listArticles(page, size, null, categoryId, Constants.Article.STATE_PUBLISH);
    }

    /**
     * 获取文章详情
     *
     * @param articleId
     * @return
     */
    @GetMapping("{articleId}")
    public ResponseResult getArticleDetail(@PathVariable("articleId") String articleId) {
        return articleService.getArticleById(articleId);
    }
    /**
     * 通过标签来计算这个匹配度
     * 标签：有一个，或者多个（5个以内，包含5个）
     * 从里面随机拿一个标签出来--->每一次获取的推荐文章，不那么雷同，种一样就雷同了
     * 通过标签去查询类似的文章，所包含此标签的文章
     * 如果没有相关文章，则从数据中获取最新的文章的
     *
     * @param articleId
     * @return
     */
    @GetMapping("/recommend/{articleId}/{size}")
    public ResponseResult getRecommendArticles(@PathVariable("articleId") String articleId, @PathVariable("size") int size) {
        return articleService.listRecommendArticle(articleId, size);
    }

    @GetMapping("/list/label/{label}/{page}/{size}")
    public ResponseResult listArticleByLabel(@PathVariable("label") String label,
                                             @PathVariable("page") int page, @PathVariable("size") int size) {
        return articleService.listArticlesByLabel(page, size, label);
    }
    @GetMapping("/categories")
    public ResponseResult getCategories() {
        return categorySevice.listCategories();
    }
    /**
     * 获取标签云，用户点击标签，就会通过标签获取相关的文章列表
     * 任意用户
     *
     * @param size
     * @return
     */
    @GetMapping("/label/{size}")
    public ResponseResult getLabels(@PathVariable("size") int size) {
        return articleService.listLabels(size);
    }

    @GetMapping("/top")
    public ResponseResult getTopArticle() {
        return articleService.listTopArticle();
    }

}
