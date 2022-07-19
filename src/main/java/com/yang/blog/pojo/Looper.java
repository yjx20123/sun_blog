package com.yang.blog.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tb_looper")
public class Looper implements Serializable, Cloneable {
    /**
     * ID
     */
    @Id
    private String id;
    /**
     * 轮播图的Url
     */
    @Column(name = "`title`")
    private String title;
    @Column(name = "`order`")
    private int order=0;
    @Column(name = "`state`")
    private String state="1";
    @Column(name = "`target_url`")
    private String targetUrl;
    @Column(name = "`image_url`")
    private String imageUrl;
    @Column(name = "`create_time`")
    private Date createTime;
    @Column(name = "`update_time`")
    private Date updateTime;
}

