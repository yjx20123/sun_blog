package com.yang.blog.controller.admin;

import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.ImageService;
import com.yang.blog.service.impl.ImageServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController

@RequestMapping("/admin/image")
public class ImageAdminApi {
    @Autowired
    private ImageService imageService;

    @PostMapping
    /**
     * 关于图片（文件）上传
     * 一般来说，现在比较常用的对象存储
     * 使用nginx+fastdfs
     */
    @PreAuthorize("@permission.admin()")
    public ResponseResult uploadImage(@RequestParam("file") MultipartFile file) {
        return imageService.uploadImage(file);
    }

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("{imageId}")
    public ResponseResult deleteImage(@PathVariable("imageId") String imageId) {
        return imageService.deleteImage(imageId);
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("{imageId}")
    public ResponseResult updateImage(@PathVariable("imageId") String iamgeId) {
        return ResponseResult.SUCCESS();
    }

    @GetMapping("{imageId}")
    public void getImage(HttpServletResponse response, @PathVariable("imageId") String iamgeId) throws IOException {
        try {
            imageService.getImage(response, iamgeId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/list/{page}/{size}")
    public ResponseResult listImages(@PathVariable("page") int page, @PathVariable("size") int size) {
        return imageService.listImages(page, size);
    }
}
