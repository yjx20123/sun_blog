package com.yang.blog.controller.portal;

import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.CategorySevice;
import com.yang.blog.service.FriendLinkService;
import com.yang.blog.service.ILooperService;
import com.yang.blog.service.WebSizeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/portal/web")
public class WebSizeApi {
    @Autowired
    private CategorySevice categorySevice;
    @Autowired
    private FriendLinkService friendLinkService;
    @Autowired
    private ILooperService looperService;
    @Autowired
    private WebSizeInfoService webSizeInfoService;
    @GetMapping("/categories")
    public ResponseResult getCategories(){
        return categorySevice.listCategories() ;
    }
    @GetMapping("/title")
    public ResponseResult getWebSizeTitle(){
        return webSizeInfoService.getWebTitle();
    }
    @GetMapping("/view_count")
    public ResponseResult getWebsizeViewCount(){
        return webSizeInfoService.getWebSizeViewCount();
    }
    @GetMapping("/seo")
    public ResponseResult getWebSizeSeoInfo(){
        return webSizeInfoService.getSeoInfo();
    }
    @GetMapping("/loop")
    public ResponseResult getLoop(){
        return looperService.listLooper();
    }
    @GetMapping("/friendLink")
    public ResponseResult getLinks(){
        return friendLinkService.listFriendLinks();
    }
}
