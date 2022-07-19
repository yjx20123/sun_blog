package com.yang.blog.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_comment")
public class Comment implements Serializable, Cloneable {
    @Id
    private String id;
    @Column(name = "parent_content")
    private String parentContent;
    @Column(name = "`article_id`")
    private String articleId;
    @Column(name = "`content`")
    private String content;
    @Column(name = "`user_id`")
    private String userId;
    @Column(name = "`user_avatar`")
    private String userAvatar;
    @Column(name = "`user_name`")
    private String userName;
    @Column(name = "`state`")
    private String state;
    @Column(name = "`create_time`")
    private Date createTime;
    @Column(name = "`update_time`")
    private Date updateTime;
}
