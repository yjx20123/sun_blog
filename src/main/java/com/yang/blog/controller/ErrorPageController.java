package com.yang.blog.controller;

import com.yang.blog.response.ResponseResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorPageController {
    @GetMapping("/404")
    public ResponseResult page404() {
        return ResponseResult.ERROR_404();
    }

    @GetMapping("/403")
    public ResponseResult page403() {
        return ResponseResult.ERROR_403();
    }

    @GetMapping("/504")
    public ResponseResult page504() {
        return ResponseResult.ERROR_504();
    }

    @GetMapping("/503")
    public ResponseResult page505() {
        return ResponseResult.ERROR_505();
    }
}
