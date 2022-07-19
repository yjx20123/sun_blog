package com.yang.blog.service.impl;

import com.yang.blog.dao.LooperDao;
import com.yang.blog.pojo.BlogUser;
import com.yang.blog.pojo.Looper;
import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.ILooperService;
import com.yang.blog.utils.Constants;
import com.yang.blog.utils.IdWorker;
import com.yang.blog.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


@Service
@Transactional
public class ILooperServiceImpl implements ILooperService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private LooperDao looperDao;
    @Autowired
    private UserServiceImpl userService;

    @Override
    public ResponseResult addLooper(Looper looper) {
        //检查数据
        String title = looper.getTitle();
        if (TextUtils.isEmmpty(title)) {
            return ResponseResult.FAILED("标题不能为空");
        }
        String url = looper.getImageUrl();
        if (TextUtils.isEmmpty(url)) {
            return ResponseResult.FAILED("图片不可以为空");
        }
        String targetUrl = looper.getTargetUrl();
        if (TextUtils.isEmmpty(targetUrl)) {
            return ResponseResult.FAILED("跳转连接不可以为空");
        }
        //补充数据
        looper.setId(idWorker.nextId() + "");
        looper.setCreateTime(new Date());
        looper.setUpdateTime(new Date());
        looper.setState("1");
        //保存到数据库
        looperDao.save(looper);
        //返回结果
        return ResponseResult.SUCCESS("轮播图添加成功");
    }

    @Override
    public ResponseResult deleteLooper(String looperId) {
        //删除轮播图
        int result = looperDao.deletelooperUpadateState(looperId);
        return result > 0 ? ResponseResult.SUCCESS("删除轮播图成功") : ResponseResult.FAILED("删除轮播图失败");
    }

    @Override
    public ResponseResult getLooper(String looperId) {
        Looper looperDB = looperDao.findOneById(looperId);
        if (looperDB != null && looperDB.getState() == "1") {
            ResponseResult success = ResponseResult.SUCCESS("轮播图获取成功");
            success.setData(looperDB);
            return success;
        }
        return ResponseResult.FAILED("轮播图获取失败");
    }

    @Override
    public ResponseResult updateLooper(String looperId, Looper looper) {
        Looper looperDB = looperDao.findOneById(looperId);
        //查询是否有looperid
        if (looperDB == null && looperDB.getState() == "1") {
            return ResponseResult.FAILED("当前轮播图不存在");
        }
        String title = looper.getTitle();
        if (!TextUtils.isEmmpty(title)) {
            looperDB.setTitle(title);
        }
        String imageUrl = looper.getImageUrl();
        if (!TextUtils.isEmmpty(imageUrl)) {
            looperDB.setImageUrl(imageUrl);
        }
        String targetUrl = looper.getTargetUrl();
        if (!TextUtils.isEmmpty(targetUrl)) {
            looperDB.setTargetUrl(targetUrl);
        }
        looperDB.setOrder(looper.getOrder());
        looperDao.save(looperDB);
        return ResponseResult.SUCCESS("轮播图修改成功");
    }

    @Override
    public ResponseResult listLooper() {
        BlogUser blogUser = userService.checkBlogUser();
        Sort order = new Sort(Sort.Direction.DESC, "order");
        List<Looper> loopers;
        if (blogUser == null || blogUser.getRoles().equals(Constants.User.ROLE_ADMIN)) {
            loopers = looperDao.listLooperbystate("1");
        } else {
            loopers = looperDao.findAll(order);
        }
        ResponseResult success = ResponseResult.SUCCESS("获取轮播图成功");
        success.setData(loopers);
        return success;
    }
}
