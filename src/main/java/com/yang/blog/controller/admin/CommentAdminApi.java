package com.yang.blog.controller.admin;

import com.yang.blog.response.ResponseResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/comment")
public class CommentAdminApi {

    @DeleteMapping("{commentId}")
    public ResponseResult deleteComment(@PathVariable("commentId") String commentId) {
        return ResponseResult.SUCCESS();
    }

    @GetMapping("{commentId}")
    public ResponseResult getComment(@PathVariable("commentId") String commentId) {
        return ResponseResult.SUCCESS();
    }

    @GetMapping("/list")
    public ResponseResult listComments(@RequestParam("page") int page, @RequestParam("size") int size) {
        return ResponseResult.SUCCESS();
    }
    @PutMapping("/top/{commentId}")
    public ResponseResult topComment(@PathVariable("commentId") String commentId){
    return ResponseResult.SUCCESS();
    }
}
