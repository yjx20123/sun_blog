package com.yang.blog.service;

import com.yang.blog.response.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ImageService {
    ResponseResult uploadImage(MultipartFile file);

    void getImage(HttpServletResponse response, String iamgeId) throws IOException;

    ResponseResult listImages(int page, int size);

    ResponseResult deleteImage(String imageId);
}
