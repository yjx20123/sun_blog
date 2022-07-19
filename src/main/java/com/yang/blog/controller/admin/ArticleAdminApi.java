package com.yang.blog.controller.admin;

import com.yang.blog.pojo.Article;
import com.yang.blog.pojo.Looper;
import com.yang.blog.response.ResonseState;
import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/article")
public class ArticleAdminApi {
    @Autowired
    private ArticleService articleService;

    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addArticle(@RequestBody Article article) {
        return articleService.addArticle(article);
    }

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("{articleId}")
    public ResponseResult deleteArticle(@PathVariable("articleId") String articleId) {
        return articleService.deleteArticleById(articleId);
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/{articleId}")
    public ResponseResult updateArticle(@PathVariable("articleId") String articleId, @RequestBody Article article) {
        return articleService.updateArticle(articleId, article);
    }


    @PreAuthorize("@permission.admin()")
    @GetMapping("/{articleId}")
    public ResponseResult getArticle(@PathVariable("articleId") String articleId) {
        return articleService.getArticleById(articleId);
    }


    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listArticles(@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam(value = "state", required = false) String state,
                                       @RequestParam(value = "keyword", required = false) String keyword,
                                       @RequestParam(value = "categoryId", required = false) String categoryId
    ) {

        return articleService.listArticles(page, size, keyword, categoryId, state);
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("/sate/{articleId}")
    public ResponseResult updateARticleState(@PathVariable("articleId") String articleId) {
        return articleService.updateARticleState(articleId);
    }


    @PreAuthorize("@permission.admin()")
    @PutMapping("/top/{articleId}")
    public ResponseResult topArticle(@PathVariable("articleId") String articleId) {
        return articleService.topArticle(articleId);
    }

}
