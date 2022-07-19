package com.yang.blog.controller.portal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/protal/search")
public class SearchPortalApi {
    @GetMapping
    public RestController doSearch(@RequestParam("keyword") String keyword,@RequestParam("page") int page){
        return null;
    }
}
