package com.yang.blog.controller.admin;

import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.WebSizeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/web_size_info")
public class WebSizeInfoAdminApi {
    @Autowired
    private WebSizeInfoService webSizeInfoService;
    @PreAuthorize("@permission.admin()")
    @PostMapping("/title")
    public ResponseResult getWebTitle() {
        return webSizeInfoService.getWebTitle();
    }
    @PreAuthorize("@permission.admin()")
    @PutMapping("/title")
    public ResponseResult upWebSizeTitle(@RequestParam("title") String title) {
        return webSizeInfoService.putWebSizeTitle(title);
    }
    @PreAuthorize("@permission.admin()")
    @GetMapping("/seo")
    public ResponseResult getSeoInfo() {
        return webSizeInfoService.getSeoInfo();
    }
    @PreAuthorize("@permission.admin()")
    @PutMapping("/seo")
    public ResponseResult putSeoInfo(@RequestParam("keywords") String keywords, @RequestParam("description") String description) {
        return webSizeInfoService.putSeoInfo(keywords, description);
    }
    @PreAuthorize("@permission.admin()")
    @GetMapping("/view_count")
    public ResponseResult getWebSizeViewCount() {
        return webSizeInfoService.getWebSizeViewCount();
    }
}
