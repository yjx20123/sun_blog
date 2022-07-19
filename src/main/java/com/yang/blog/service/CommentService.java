package com.yang.blog.service;

import com.yang.blog.pojo.Comment;
import com.yang.blog.response.ResponseResult;

public interface CommentService {
    ResponseResult postComment(Comment comment);

    ResponseResult deleteCommentById(String commentId);

    ResponseResult listCommentByArticleId(String articleId, int page, int size);
}
