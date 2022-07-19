package com.yang.blog.service.impl;

import com.yang.blog.dao.ArticleNoConnentDao;
import com.yang.blog.dao.CommentDao;
import com.yang.blog.pojo.ArticleNoContent;
import com.yang.blog.pojo.BlogUser;
import com.yang.blog.pojo.Comment;
import com.yang.blog.response.ResponseResult;
import com.yang.blog.service.CommentService;
import com.yang.blog.utils.Constants;
import com.yang.blog.utils.IdWorker;
import com.yang.blog.utils.TextUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;


@Service
@Slf4j
@Transactional
public class CommentServiceImpl extends BaseService implements CommentService {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ArticleNoConnentDao articleNoConnentDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private CommentDao commentDao;

    /**
     * 发表评论
     *
     * @param comment 评论
     * @return
     */
    @Override
    public ResponseResult postComment(Comment comment) {
        //检查用户是否有登录
        BlogUser sobUser = userService.checkBlogUser();
        if (sobUser == null) {
            return ResponseResult.FAILED("用户未登录");
        }
        //检查内容
        String articleId = comment.getArticleId();
        if (TextUtils.isEmmpty(articleId)) {
            return ResponseResult.FAILED("文章ID不可以为空.");
        }
        ArticleNoContent article = articleNoConnentDao.findOneById(articleId);
        if (article == null) {
            return ResponseResult.FAILED("文章不存在.");
        }
        String content = comment.getContent();
        if (TextUtils.isEmmpty(content)) {
            return ResponseResult.FAILED("评论内容不可以为空.");
        }
        //补全内容
        comment.setId(idWorker.nextId() + "");
        comment.setUpdateTime(new Date());
        comment.setCreateTime(new Date());
        comment.setState("1");
        comment.setUserAvatar(sobUser.getAvatar());
        comment.setUserName(sobUser.getUsername());
        comment.setUserId(sobUser.getId());
        //保存入库
        commentDao.save(comment);
        //返回结果
        return ResponseResult.SUCCESS("评论成功");
    }

    @Override
    public ResponseResult deleteCommentById(String commentId) {
        //检查用户角色
        BlogUser sobUser = userService.checkBlogUser();
        if (sobUser == null) {
            return ResponseResult.FAILED("用户未登录");
        }
        //把评论找出来，比对用户权限
        Comment comment = commentDao.findOneById(commentId);
        if (comment == null) {
            return ResponseResult.FAILED("评论不存在.");
        }
        if (sobUser.getId().equals(comment.getUserId()) ||
                //登录要判断角色
                Constants.User.ROLE_ADMIN.equals(sobUser.getRoles())) {
            commentDao.deleteById(commentId);
            return ResponseResult.SUCCESS("评论删除成功.");
        } else {
            return ResponseResult.FAILED("评论无法删除");
        }
    }


    /**
     * 获取文章的评论
     * 评论的排序策略：
     * 最基本的就按时间排序-->升序和降序-->先发表的在前面或者后发表的在前面
     * <p>
     * 置顶的：一定在前最前面
     * <p>
     * 后发表的：前单位时间内会排在前面，过了此单位时间，会按点赞量和发表时间进行排序
     *
     * @param articleId
     * @param page
     * @param size
     * @return
     */
    @Override
    public ResponseResult listCommentByArticleId(String articleId, int page, int size) {
        page = checkpage(page);
        size = checkSize(size);
        Sort sort = new Sort(Sort.Direction.DESC, "state", "createTime");
        Pageable pageable = PageRequest.of(page - 1, size, sort);
        Page<Comment> all = commentDao.findAll(pageable);
        ResponseResult success = ResponseResult.SUCCESS("评论列表获取成功.");
        success.setData(all);
        return success;
    }


}
