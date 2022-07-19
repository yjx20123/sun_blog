package com.yang.blog.service.impl;

import com.yang.blog.pojo.BlogUser;
import com.yang.blog.service.IUserService;
import com.yang.blog.utils.Constants;
import com.yang.blog.utils.CookieUtils;
import com.yang.blog.utils.TextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

@Service("permission")
@Transactional
public class PermissionService {
    @Autowired
    private IUserService userService;

    //判断是不是管理员
    public boolean admin() {
        //拿到request和response
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        String tokenKey = CookieUtils.getCookie(request, Constants.User.COOKIE_TOKEN_KEY);
        //没有令牌的key,没有登录，不用往下执行了
        if (TextUtils.isEmmpty(tokenKey)) {
            return false;
        }
        BlogUser blogUser = userService.checkBlogUser();
        if (blogUser == null) {
            return false;
        }
        if (blogUser.getRoles().equals(Constants.User.ROLE_ADMIN)) {
            //管理员身份确认
            return true;
        }
        return false;
    }
}
