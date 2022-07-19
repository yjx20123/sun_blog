package com.yang.blog.dao;

import com.yang.blog.pojo.ArticleNoContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleNoConnentDao extends JpaRepository<ArticleNoContent, String>, JpaSpecificationExecutor<ArticleNoContent> {
    //测试查询相关文章
    @Query(nativeQuery = true, value = "select * from tb_article where labels like ? and `id`!=? limit ?")
    List<ArticleNoContent> listArticleByLikeLabel(String label, String articleId, int size);
    //获取最新的文章
    @Query(nativeQuery = true, value = "select * from tb_article where `id`!=? order by `create_time` desc limit ?")
    List<ArticleNoContent> listLastedArticleBySize(String articleId, int size);
    ArticleNoContent findOneById(String articleId);
}
