package com.yang.blog.utils;

import com.yang.blog.pojo.BlogUser;
import io.jsonwebtoken.Claims;

import java.util.HashMap;
import java.util.Map;

public class ClaimsUtils {
    private static final String ID="id";
    private static final String USERNAME="username";
    private static final String ROLES="roles";
    private static final String AVATAR="avatar";
    private static final String EMAIL="email";
    private static final String SIGN="sign";
    public static Map<String,Object> blogUserClaims(BlogUser blogUser){

        Map<String,Object> claims=new HashMap<>();
        claims.put(ID,blogUser.getId());
        claims.put(USERNAME,blogUser.getUsername());
        claims.put(ROLES,blogUser.getRoles());
        claims.put(AVATAR,blogUser.getAvatar());
        claims.put(EMAIL,blogUser.getEmail());
        claims.put(SIGN,blogUser.getSign());
        return claims;
    }
    public static  BlogUser claimBlogUser(Claims claims){
        BlogUser blogUser = new BlogUser();
        String id = (String) claims.get(ID);
        blogUser.setId(id);
        String username = (String) claims.get(USERNAME);
        blogUser.setUsername(username);
        String roles = (String) claims.get(ROLES);
        blogUser.setRoles(roles);
        String avatar = (String) claims.get(AVATAR);
        blogUser.setAvatar(avatar);
        String email = (String) claims.get(EMAIL);
        blogUser.setEmail(email);
        String sign = (String) claims.get(SIGN);
        blogUser.setSign(sign);
        return blogUser;
    }

}
