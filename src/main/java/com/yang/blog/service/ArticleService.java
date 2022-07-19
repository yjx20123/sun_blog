package com.yang.blog.service;

import com.yang.blog.pojo.Article;
import com.yang.blog.response.ResponseResult;

public interface ArticleService {
    ResponseResult addArticle(Article article);

    ResponseResult updateArticle(String articleId, Article article);

    ResponseResult deleteArticleById(String articleId);

    ResponseResult listArticles(int page, int size, String keyword, String categoryId, String state);

    ResponseResult topArticle(String articleId);

    ResponseResult getArticleById(String articleId);

    ResponseResult updateARticleState(String articleId);

    ResponseResult listTopArticle();

    ResponseResult listLabels(int size);

    ResponseResult listArticlesByLabel(int page, int size, String label);

    ResponseResult listRecommendArticle(String articleId, int size);
}
