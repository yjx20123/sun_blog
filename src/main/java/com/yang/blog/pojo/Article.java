package com.yang.blog.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;

@Entity
@Table(name = "tb_article")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Article implements Serializable, Cloneable {
    /**
     * ID
     */
    @Id
    private String id;
    /**
     * 用户ID
     */
    @Column(name = "`title`")
    private String title;
    @Column(name = "`user_id`")
    private String userId;
    @Column(name = "`user_avatar`")
    private String userAvatar;
    @Column(name = "`user_name`")
    private String userName;
    @Column(name = "`category_id`")
    private String CategoryId;
    @Column(name = "`content`")
    private String content;
    @Column(name = "`type`")
    private String type;
    //0表示删除，1,表示已经发布2 表示草稿，3表示置顶
    @Column(name = "`state`")
    private String state;
    @Column(name = "`summary`")
    private String summary;



    @Column(name = "`labels`")
    private String labels;
    @Column(name = "`cover`")
    private String cover;

    @Column(name = "`view_count`")
    private int viewCount;
    @Column(name = "`create_time`")
    private Date createTime;
    @Column(name = "`update_time`")
    private Date updateTime;
    @OneToOne(targetEntity = BlogUserNoPassword.class)
    @JoinColumn(name = "`user_id`",referencedColumnName = "`id`",insertable = false,updatable = false)
    private BlogUserNoPassword blogUserNoPassword;
    @Transient
    private List<String> labelall=new ArrayList<>();

    public String getLabels() {
        //打散到集合
        this.labelall.clear();
        if(!this.labels.contains("-")){
            this.labelall.add(this.labels);
        }
        else{
            String[] split = this.labels.split("-");
            List<String> list = Arrays.asList(split);
            this.labelall.addAll(list);
        }
        return labels;
    }
}


