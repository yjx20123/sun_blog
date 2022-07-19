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

@Entity
@Table(name="tb_images")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Image implements Serializable,Cloneable {
    @Id
    private String id;
    @Column(name = "`user_id`")
    private String user_id;
    @Column(name = "`url`")
    private String url;
    @Column(name = "`name`")
    private  String name;
    @Column(name = "`path`")
    private String path;
    @Column(name = "`content_type`")
    private String contentType;
    @Column(name = "`state`")
    private String state;
    @Column(name = "`create_time`")
    private Date createTime;
    @Column(name = "`update_time`")
    private Date updateTime;
}
