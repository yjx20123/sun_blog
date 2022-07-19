package com.yang.blog.controller.admin;

import com.yang.blog.pojo.Looper;
import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.ILooperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/loop")
public class LooperAdminApi {
    @Autowired
    private ILooperService looperService;

    @PreAuthorize("@permission.admin()")
    @PostMapping
    public ResponseResult addLooper(@RequestBody Looper looper) {
        return looperService.addLooper(looper);
    }

    @PreAuthorize("@permission.admin()")
    @DeleteMapping("{looperId}")
    public ResponseResult deleteLooper(@PathVariable("looperId") String looperId) {
        return looperService.deleteLooper(looperId);
    }

    @PreAuthorize("@permission.admin()")
    @PutMapping("{looperId}")
    public ResponseResult updateLooper(@PathVariable("looperId") String looperId,@RequestBody Looper looper) {
        return looperService.updateLooper(looperId,looper);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("{looperId}")
    public ResponseResult getLooper(@PathVariable("looperId") String looperId) {
        return looperService.getLooper(looperId);
    }

    @PreAuthorize("@permission.admin()")
    @GetMapping("/list")
    public ResponseResult listLooper() {
        return looperService.listLooper();
    }
}
