package com.yang.blog.controller.portal;

import com.yang.blog.pojo.Comment;
import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/portal/comment")
public class CommentPortalApi {
    @Autowired
    private CommentService commentService;
    @PostMapping
    public ResponseResult postComment(@RequestBody Comment comment) {
        return commentService.postComment(comment);
    }


    @DeleteMapping("/{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId") String commentId) {
        return commentService.deleteCommentById(commentId);
    }


    @GetMapping("/list/{articleId}/{page}/{size}")
    public ResponseResult listComments(@PathVariable("articleId") String articleId, @PathVariable("page") int page,@PathVariable("size") int size) {
        return commentService.listCommentByArticleId(articleId, page, size);
    }

}
