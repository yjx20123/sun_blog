package com.yang.blog.service.impl;

import com.google.gson.Gson;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.ChineseCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import com.yang.blog.dao.BlogNoPasswordDao;
import com.yang.blog.dao.RefreshTokenDao;
import com.yang.blog.dao.SettingsDao;
import com.yang.blog.dao.UserDao;
import com.yang.blog.pojo.BlogUser;

import com.yang.blog.pojo.BlogUserNoPassword;
import com.yang.blog.pojo.RefreshToken;
import com.yang.blog.pojo.Setting;

import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.IUserService;
import com.yang.blog.utils.*;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@Transactional
public class UserServiceImpl extends BaseService implements IUserService {
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private SettingsDao settingsDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private BCryptPasswordEncoder cryptPasswordEncoder;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private Random createRandom;
    @Autowired
    private Random random;
    @Autowired
    private RefreshTokenDao refreshTokenDao;
    @Autowired
    private Gson gson;
    @Autowired
    private BlogNoPasswordDao blogNoPasswordDao;

    @Override
    public ResponseResult initManagerAccount(BlogUser blogUser, HttpServletRequest request) {
        //检查是否有初始化

        if ((settingsDao.findOneBykey(Constants.Settings.MANAGER_ACCOUNT_INIT_STATE)) != null) {
            return ResponseResult.FAILED("管理员账号已经初始化");
        }
        //检查数据
        if (TextUtils.isEmmpty(blogUser.getUsername())) {
            return ResponseResult.FAILED("用户名不能为空");
        }
        if (TextUtils.isEmmpty(blogUser.getPassword())) {
            return ResponseResult.FAILED("密码不能为空");
        }
        if (TextUtils.isEmmpty(blogUser.getEmail())) {
            return ResponseResult.FAILED("邮箱不能为空");
        }
        //补充数据
        blogUser.setId(String.valueOf(idWorker.nextId()));
        blogUser.setRoles(Constants.User.ROLE_ADMIN);
        blogUser.setAvatar(Constants.User.DEFAULT_AVATAR);
        blogUser.setState(Constants.User.DEFAULT_STATE);
        String remoteAddr = request.getRemoteAddr();
        blogUser.setLoginip(remoteAddr);
        blogUser.setReg_ip(remoteAddr);
        blogUser.setCreatetime(new Date());
        blogUser.setUpdatetime(new Date());
        String password = blogUser.getPassword();

        String encode = cryptPasswordEncoder.encode(password);
        blogUser.setPassword(encode);
        userDao.save(blogUser);
        //更新已经添加的标记
        Setting setting = new Setting();
        setting.setKey(Constants.Settings.MANAGER_ACCOUNT_INIT_STATE);
        setting.setId(idWorker.nextId() + "");
        setting.setCreateTime(new Date());
        setting.setValue("1");
        setting.setUpdateTime(new Date());
        settingsDao.save(setting);
        return ResponseResult.SUCCESS("管理员初始化成功");
    }

    public static final int[] captcha_Font_types = {
            Captcha.FONT_1,
            Captcha.FONT_2,
            Captcha.FONT_3,
            Captcha.FONT_4,
            Captcha.FONT_5,
            Captcha.FONT_6,
            Captcha.FONT_7,
            Captcha.FONT_8,
            Captcha.FONT_9,
            Captcha.FONT_10
    };

    @Override
    public void createCaptcha(HttpServletResponse response, String captchakey) throws Exception {
        if (TextUtils.isEmmpty(captchakey) || captchakey.length() < 13) {
            return;
        }
        long key;

        try {
            key = Long.parseLong(captchakey);
        } catch (Exception e) {
            return;
        }
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        int captchaType = createRandom.nextInt(3);
        Captcha captcha = null;
        if (captchaType == 1) {
            captcha = new GifCaptcha(200, 60);
        } else if (captchaType == 2) {
            captcha = new ChineseCaptcha(200, 60);
        } else {
            captcha = new ArithmeticCaptcha(200, 60);
            captcha.setLen(3);  // 几位数运算，默认是两位
            captcha.text();  // 获取运算的结果：5
        }
        // 三个参数分别为宽、高、位数

        int index = createRandom.nextInt(captcha_Font_types.length);
        log.info("captcha_Font_types.length index ==>" + index);
        captcha.setFont(captcha_Font_types[index]);
        String content = captcha.text().toLowerCase();
        //保存到redis中
        redisUtil.set(Constants.User.KEY_CAPTCHA_CONTENT + key, content, 10 * 60);
        // 输出图片流
        captcha.out(response.getOutputStream());
    }

    /**
     * 发送邮件
     * 注册（resigter) 如果邮箱不为空，表示已注册
     * 修改邮箱（update) 如果邮箱不为空，表示已注册
     * 找回密码（forget） 如果邮箱为空，表示未注册
     *
     * @param request
     * @param emailAddress
     * @return
     */
    @Override
    public ResponseResult sendEmail(HttpServletRequest request, String type, String emailAddress) {
        if (emailAddress == null) {
            return ResponseResult.FAILED("邮箱地址不可为空");
        }
        BlogUser byEmail = userDao.findByEmail(emailAddress);
        if ("resigter".equals(type) || "update".equals(type)) {

            if (byEmail != null) {
                return ResponseResult.FAILED("该邮箱已经被注册了");
            }
        } else if ("forget".equals(type)) {
            if (byEmail == null) {
                return ResponseResult.FAILED("该邮箱未被被注册了");
            }
        }

        //防止暴力发送
        String remoteAddr = request.getRemoteAddr();
        log.info("email==>ip" + remoteAddr);
        if (remoteAddr != null) {
            remoteAddr = remoteAddr.replaceAll(":", "_");
        }
        log.info("constants.user.key_email_send_ip ==>" + Constants.User.KEY_EMAIL_SEND_IP + remoteAddr);
        Integer ipSendTime = (Integer) redisUtil.get(Constants.User.KEY_EMAIL_SEND_IP + remoteAddr);
        if (ipSendTime != null && ipSendTime > 10) {
            return ResponseResult.FAILED("您的发送次数已达到上限10次,请一个小时后再来注册!!!");
        }
        Object hasEmailSend = redisUtil.get(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress);
        if (hasEmailSend != null) {
            return ResponseResult.FAILED("你发送验证码也太频繁了吧!!!");
        }
        //检查邮箱地址是否正确
        boolean emailAddressOk = TextUtils.isEmailAddressOk(emailAddress);
        if (!emailAddressOk) {
            return ResponseResult.FAILED("您的邮箱格式错误，请检查你的邮箱格式");
        }
        int code = random.nextInt(999999);
        if (code < 100000) {
            code += 100000;
        }
        log.info("sendEmail ==> code ==>" + code);
        try {
            EmailSender.SenRegisterVerifyCode(String.valueOf(code), emailAddress);
        } catch (Exception e) {
            return ResponseResult.FAILED("验证码发送失败,请重新尝试");
        }
        //做记录
        //发送记录，code
        if (ipSendTime == null) {
            ipSendTime = 0;
        }
        ipSendTime++;
        //一小时有效期
        redisUtil.set(Constants.User.KEY_EMAIL_SEND_IP + remoteAddr, ipSendTime, 60 * 60);
        redisUtil.set(Constants.User.KEY_EMAIL_SEND_ADDRESS + emailAddress, true, 30);
        redisUtil.set(Constants.User.KEY_CAPTCHA_CONTENT + emailAddress, String.valueOf(code), 60 * 10);
        //发送验证码
        return ResponseResult.SUCCESS("验证码发送成功");
    }

    @Override
    public ResponseResult registerUser(BlogUser blogUser, String code, String captchaCode, String captchaKey, HttpServletRequest request) {
        //第一步：检查当前用户名是否已经注册
        String username = blogUser.getUsername();
        if (TextUtils.isEmmpty(username)) {
            return ResponseResult.FAILED("注册用户名不可为空");
        }
        BlogUser byUsername = userDao.findByUsername(username);
        if (byUsername != null) {
            return ResponseResult.FAILED("当前用户名已存在,换一个名字吧!");
        }
        //第二步：检查邮箱格式是否正确
        String email = blogUser.getEmail();
        if (TextUtils.isEmmpty(email)) {
            return ResponseResult.FAILED("邮箱不可为空");
        }
        if (!TextUtils.isEmailAddressOk(email)) {
            return ResponseResult.FAILED("邮箱格式不正确,请重新填写");
        }
        //第三步：检查该邮箱是否已经注册
        BlogUser byEmail = userDao.findByEmail(email);
        if (byEmail != null) {
            return ResponseResult.FAILED("该邮箱已经被注册过了");
        }
        //第四部：检查邮箱验证码正确
        String emailVerifyCode = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + email);
        log.info("emailVerifyCode==>" + emailVerifyCode);
        if (TextUtils.isEmmpty(emailVerifyCode)) {
            return ResponseResult.FAILED("邮箱验证码已过期");
        } else if (!emailVerifyCode.equals(code)) {
            return ResponseResult.FAILED("邮箱验证码错误!!!");
        } else {
            redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + email);
        }
        //第五步： 检查图灵验证码是否正确
        String captchaVerifyCode = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        if (TextUtils.isEmmpty(captchaCode)) {
            return ResponseResult.FAILED("验证码已过期");
        }
        if (!captchaVerifyCode.equals(captchaCode)) {
            return ResponseResult.FAILED("验证码不正确");
        } else {
            redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + captchaKey);
        }
        //达到可以注册的条件
        //第六步：对密码进行加密
        String password = blogUser.getPassword();
        if (TextUtils.isEmmpty(password)) {
            return ResponseResult.FAILED("密码不可以为空!!!");
        }
        String passwordEncode = cryptPasswordEncoder.encode(password);
        blogUser.setPassword(passwordEncode);
        //第七步：补全数据
        String ipAddress = request.getRemoteAddr();
        blogUser.setId(idWorker.nextId() + "");
        blogUser.setLoginip(ipAddress);
        blogUser.setReg_ip(ipAddress);
        blogUser.setUpdatetime(new Date());
        blogUser.setCreatetime(new Date());
        blogUser.setAvatar(Constants.User.DEFAULT_AVATAR);
        blogUser.setRoles(Constants.User.ROLE_NORMAL);
        blogUser.setState("1");
        //第八步：保存到数据库
        userDao.save(blogUser);
        return ResponseResult.SUCCESS("注册成功");
    }

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public ResponseResult doLogin(String captcha,
                                  String captcha_key,
                                  BlogUser blogUser
    ) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpServletResponse response = requestAttributes.getResponse();
        Object captchaKey = redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + captcha_key);
        if (!captcha.equals(captchaKey)) {
            return ResponseResult.FAILED("验证码不正确");
        }
        //验证成功
        redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + captcha_key);
        String username = blogUser.getUsername();
        if (TextUtils.isEmmpty(username)) {
            return ResponseResult.FAILED("账号不可为空");
        }
        String password = blogUser.getPassword();
        if (TextUtils.isEmmpty(password)) {
            return ResponseResult.FAILED("密码不可为空");
        }
        BlogUser byUsername = userDao.findByUsername(username);
        if (byUsername == null) {

            byUsername = userDao.findByEmail(username);
        }
        if (byUsername == null) {
            return ResponseResult.FAILED("用户名或者密码错误");
        }
        //用户存在
        // 对比密码
        boolean matches = cryptPasswordEncoder.matches(password, byUsername.getPassword());
        if (!matches) {
            return ResponseResult.FAILED("用户名或者密码错误");
        }
        //判断用户状态
        if (!"1".equals(byUsername.getState())) {
            return ResponseResult.FAILED("当前账号已被禁用");
        }
        createToken(response, byUsername);
        return ResponseResult.SUCCESS("登录成功");
    }

    /**
     * @return token_key
     */
    private String createToken(HttpServletResponse response, BlogUser userFromDb) {
        int deleteResult = refreshTokenDao.deleteAllByUserId(userFromDb.getId());
        log.info("deleteResult of refresh token .. " + deleteResult);
        //生成token
        Map<String, Object> claims = ClaimsUtils.blogUserClaims(userFromDb);
        //token默认有效为2个小时
        String token = JwtUtil.createToken(claims);
        //返回token的md5值，token会保存到redis里
        //前端访问的时候，携带token的md5key，从redis中获取即可
        String tokenKey = DigestUtils.md5DigestAsHex(token.getBytes());
        //保存token到redis里，有效期为2个小时，key是tokenKey
        redisUtil.set(Constants.User.TOKEN_KEY + tokenKey, token, Constants.TimeValue.HOUR_2);
        //把tokenKey写到cookies里
        //这个要动态获取，可以从request里获取，
        CookieUtils.setUpCookie(response, Constants.User.COOKIE_TOKEN_KEY, tokenKey);
        //生成refreshToken
        String refreshTokenValue = JwtUtil.createRefreshToken(userFromDb.getId(), Constants.TimeValue.MONTH * 1000);
        log.info("refreshTokenValue==>" + refreshTokenValue);
        //保存到数据库里
        //refreshToken，tokenKey，用户ID，创建时间，更新时间
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(idWorker.nextId() + "");
        refreshToken.setRefreahToken(refreshTokenValue);
        refreshToken.setUserId(userFromDb.getId());
        refreshToken.setTokenKey(tokenKey);
        refreshToken.setCreatetime(new Date());
        refreshToken.setUpdatetime(new Date());
        refreshTokenDao.save(refreshToken);
        return tokenKey;
    }


    /**
     * 本质检查用户是否有登录
     *
     * @return
     */
    @Override
    public BlogUser checkBlogUser() {
        String tokenKey = CookieUtils.getCookie(getRequests(), Constants.User.COOKIE_TOKEN_KEY);
        BlogUser blogUser = parseByTokenKey(tokenKey);
        if (blogUser == null) {

            //说明解析出错，过期了
            //去mysql查询refreshToken
            RefreshToken refreshToken = refreshTokenDao.findByTokenKey(tokenKey);

            //如果不存在，就是当前访问没有登录
            if (refreshToken == null) {
                return null;
            }
            //如果存在，就解析reFreshToken
            try {

//                JwtUtil.parseJWT(refreshToken.getRefreahToken());
                //如果refreshToken有效，创建新的token和新的refreshToken
                BlogUser userFromDb = userDao.findOneById(refreshToken.getUserId());
                //删掉refreshToken的记录
                String newtokenKey = createToken(getResponse(), userFromDb);
                //返回token
                parseByTokenKey(newtokenKey);
            } catch (Exception e1) {
                //如果refreshToken过期了,就当前访问没有登录,提示用户登录
            }
        }
        return blogUser;
    }

    private BlogUser parseByTokenKey(String tokenKey) {
        String token = (String) redisUtil.get(Constants.User.TOKEN_KEY + tokenKey);
        if (token != null) {
            try {
                Claims claims = JwtUtil.parseJWT(token);
                return ClaimsUtils.claimBlogUser(claims);
            } catch (Exception e) {
                return null;
            }

        }
        return null;
    }

    @Override
    public ResponseResult getUserInfo(String userid) {
        BlogUser userInfo = userDao.findOneById(userid);
        if (userInfo == null) {
            return ResponseResult.FAILED("您所查询的用户不存在");
        }
        String userJson = gson.toJson(userInfo);
        BlogUser newBlogUser = gson.fromJson(userJson, BlogUser.class);
        newBlogUser.setPassword("");
        newBlogUser.setEmail("");
        newBlogUser.setReg_ip("");
        newBlogUser.setLoginip("");
        ResponseResult success = ResponseResult.SUCCESS("获取成功");
        success.setData(newBlogUser);
        return success;
    }

    @Override
    public ResponseResult checkEmail(String email) {
        BlogUser byEmail = userDao.findByEmail(email);
        return byEmail == null ? ResponseResult.FAILED("当前邮箱未被注册") : ResponseResult.SUCCESS("该邮箱已被注册!!!");
    }

    @Override
    public ResponseResult checkUsername(String username) {
        BlogUser byUsername = userDao.findByUsername(username);
        return byUsername == null ? ResponseResult.FAILED("当前用户名未被使用") : ResponseResult.SUCCESS("该用户名已被使用!!!");
    }

    @Override
    public ResponseResult updateUserInfo(String userId, BlogUser blogUser) {

        BlogUser userFromKey = checkBlogUser();

        if (userFromKey == null) {
            ResponseResult.FAILED("账号未登录");
        }//判断用户的id是否一致，如果一致才可以修改
        BlogUser userFromDb = userDao.findOneById(userFromKey.getId());
        if (!userFromDb.getId().equals(userId)) {
            return ResponseResult.FAILED("您无权修改");
        }
        //可以进行修改
        //可修改的项
        //签名
        //用户名
        //头像
        if (!TextUtils.isEmmpty(blogUser.getAvatar())) {
            userFromDb.setAvatar(blogUser.getAvatar());
        }
        userFromDb.setSign(blogUser.getSign());
        String username = blogUser.getUsername();
        if (!TextUtils.isEmmpty(username)) {
            BlogUser byUsername = userDao.findByUsername(username);
            if (byUsername != null) {
                return ResponseResult.FAILED("该用户名已经被注册了");
            }
            userFromDb.setUsername(username);
        }
        userDao.save(userFromDb);
        //干掉redis里的token，下一次请求，需要解析token的，就会根据refreshToken重新创建一个
        String cookie = CookieUtils.getCookie(getRequests(), Constants.User.COOKIE_TOKEN_KEY);
        redisUtil.del(cookie);
        return ResponseResult.SUCCESS("修改成功");
    }

    @Override
    public ResponseResult deleteUser(String userId) {
        //满足条件进行修改
        int result = userDao.deleteBlogUserByState(userId);
        if (result > 0) {
            return ResponseResult.SUCCESS("删除成功");
        }
        return ResponseResult.FAILED("用户不存在");
    }

    @Override
    public ResponseResult listUsers(int page, int size) {
        page = this.checkpage(page);
        size = this.checkSize(size);
        //根据注册日期排序
        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
        PageRequest pagelabel = PageRequest.of(page - 1, size, sort);
        Page<BlogUserNoPassword> all = blogNoPasswordDao.findAll(pagelabel);

        ResponseResult success = ResponseResult.SUCCESS("获取用户信息成功");
        success.setData(all);
        return success;
    }

    @Override
    public ResponseResult updatePassword(String verify_code, BlogUser blogUser) {
        //检查邮箱是否有填写
        String email = blogUser.getEmail();
        if (TextUtils.isEmmpty(email)) {
            return ResponseResult.FAILED("邮箱不可为空");
        }
        //之后去邮箱获取Email_code
        String EmailCode = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + email);
        if (EmailCode == null || !EmailCode.equals(verify_code)) {
            return ResponseResult.FAILED("验证码错误或超时");
        }

        int result = userDao.updatePasswordByEmail(cryptPasswordEncoder.encode(blogUser.getPassword()), email);
        redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + email);
        return result > 0 ? ResponseResult.SUCCESS("修改密码成功") : ResponseResult.FAILED("修改密码失败");
    }

    @Override
    public ResponseResult updateEmail(String email, String verify_code) {
        BlogUser blogUser = this.checkBlogUser();
        //没有登录
        if (blogUser == null) {
            ResponseResult.FAILED("账号未登录,无法修改");
        }
        //对比验证码

        String EmailCode = (String) redisUtil.get(Constants.User.KEY_CAPTCHA_CONTENT + email);
        if (TextUtils.isEmmpty(EmailCode) || !verify_code.equals(EmailCode)) {
            return ResponseResult.FAILED("验证码错误或者已过期");
        }
        log.info("我执行到了这里");
        //可以修改邮箱
        int result = userDao.updateEmailById(email, blogUser.getId());
        redisUtil.del(Constants.User.KEY_CAPTCHA_CONTENT + email);
        return result > 0 ? ResponseResult.SUCCESS("修改邮箱成功") : ResponseResult.FAILED("修改邮箱失败");
    }

    public static HttpServletRequest getRequests() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return servletRequestAttributes.getResponse();
    }

    @Override
    public ResponseResult logout() {
        String cookieKey = CookieUtils.getCookie(getRequests(), Constants.User.COOKIE_TOKEN_KEY);
        if (TextUtils.isEmmpty(cookieKey)) {
            return ResponseResult.FAILED("登录状态已失效");

        }
        //删除redis中的tokenkey
        redisUtil.del(Constants.User.TOKEN_KEY + cookieKey);
        //删除cookie中的key
        CookieUtils.deleteCookie(getResponse(), Constants.User.COOKIE_TOKEN_KEY);
        //删除mysql中的refreshToken
        int result = refreshTokenDao.deleteByTokenKey(cookieKey);
        return ResponseResult.SUCCESS("退出登录成功");
    }


}
